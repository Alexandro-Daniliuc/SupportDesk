# SupportDesk

Progetto sviluppato per l'esame di **Ingegneria del Software (ISPW)** — Uniroma2, DICII, ultimo anno della triennale.

È un'applicazione desktop in **JavaFX** per la gestione di un help desk: gli utenti aprono ticket di assistenza, i tecnici li lavorano, i manager tengono d'occhio SLA e notifiche. L'ho usata soprattutto come palestra per mettere in pratica quello che avevamo studiato a lezione (pattern GoF, architettura a livelli, persistenza astratta) su un progetto vero, non su un esercizio giocattolo.

In questo README provo a spiegare la teoria che c'è dietro alle scelte fatte, più che limitarmi a descrivere cosa fa l'app.

---

## Architettura: Boundary–Control–Entity

Per strutturare il progetto ho seguito lo schema BCE visto a lezione (una variante del MVC), con l'obiettivo di tenere separata la UI dalla logica di dominio:

| Livello | Package | Cosa fa |
|---|---|---|
| **Boundary** | `boundary/javafx` + file `.fxml` | I controller grafici JavaFX: leggono l'input dell'utente e basta, non contengono logica applicativa |
| **Control** | `controller/applicativo` | Un controller per ogni caso d'uso (login, apertura ticket, assegnazione, SLA, correlazione, commenti, knowledge base, registrazione) |
| **Entity** | `model` | Le classi di dominio (`Ticket`, `User`, `Comment`, `KnowledgeEntry`, `Notification`), senza alcuna dipendenza da JavaFX o dal database |
| **Bean / Record** | `bean`, `record` | DTO immutabili usati per far passare i dati tra i livelli senza esporre direttamente le entity |

La regola che ho cercato di rispettare sempre è che boundary e control non si parlano mai direttamente: in mezzo c'è sempre una **Facade** (`utility/facade`, una per caso d'uso). All'inizio mi sembrava una complicazione inutile, ma con il crescere del progetto è stata la cosa che mi ha evitato più casini quando dovevo modificare una schermata senza rompere la logica sotto.

---

## Design pattern usati (e perché li ho scelti)

### Singleton
Mi serviva un'unica istanza condivisa per cose come la sessione utente o il motore di correlazione, evitando però `synchronized` ovunque. Ho usato l'**initialization-on-demand holder idiom**: una classe statica interna caricata dalla JVM solo al primo accesso, che garantisce thread-safety senza lock espliciti. L'ho applicato a 13 classi in totale (`UserSession`, `ApplicationModeManager`, `CorrelationEngine`, `ConfigLoader`, `ConnectionManager`, `PersistenceLayerFactory` e tutte le Facade).

### Abstract Factory + DAO
Volevo poter far girare l'app con tre modalità di persistenza diverse (in memoria, su file, su database) senza riempire il codice di `if` sparsi ovunque. `DAOAbstractFactory` definisce l'interfaccia comune, e tre factory concrete (`DAOFactoryDB`, `DAOFactoryFile`, `DAOFactoryDemo`) la implementano. La scelta di quale usare viene fatta una sola volta all'avvio, in base alla modalità configurata.

### Decorator
Dovevo aggiungere delle "etichette" opzionali a un ticket (SLA scaduto, criticità) senza creare una sottoclasse per ogni combinazione possibile. `TicketDecorator` avvolge un `TicketComponent` e delega tutto tranne quello che deve modificare; `TicketWithSLA` e `TicketCritical` sono impilabili tra loro.

### Strategy
Il motore di correlazione tra ticket doveva poter cambiare criterio di decisione senza toccare il calcolo delle metriche. `CorrelationStrategy` valuta un `CorrelationContext` (le metriche già calcolate) e decide se due ticket sono correlati; `CategoryAwareStrategy` è l'implementazione che uso di default.

### Observer
Quando un ticket cambia stato, ruoli diversi devono essere avvisati di cose diverse (il manager degli SLA violati, il tecnico delle assegnazioni, l'utente della risoluzione). `Subject` tiene la lista degli `Observer` e notifica un `EventType` tipizzato; ogni observer (`ManagerNotificationObserver`, `TechnicianNotificationObserver`, `UserNotificationObserver`) reagisce solo a quello che gli interessa.

### State
Volevo evitare transizioni di stato non valide per un ticket (tipo passare da aperto direttamente a chiuso). Ho incapsulato la macchina a stati direttamente nell'enum `TicketStatus`, dove ogni costante implementa il proprio metodo `nextStates()`:

```
OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED
                                     ↳ REOPENED → ASSIGNED
```

Così la logica delle transizioni sta in un unico punto e non deve essere ricontrollata da ogni controller.

---

## Implementazione di algoritmi: il motore di correlazione

Non volevo un semplice confronto per parole chiave, quindi ho implementato una piccola pipeline di *information retrieval*, roba vista più su algoritmi/basi di dati che a ISPW, ma che mi sembrava perfetta per questo caso d'uso:

1. normalizzo il testo di titolo + descrizione;
2. costruisco un vettore **TF-IDF** per ogni ticket (peso alto ai termini frequenti nel documento ma rari nel resto);
3. calcolo la **cosine similarity** tra il ticket nuovo e ogni candidato;
4. calcolo anche l'**indice di Jaccard** sulle keyword più significative, come metrica complementare;
5. tutte queste metriche finiscono in un `CorrelationContext` che passo alla `CorrelationStrategy` — così il calcolo delle feature resta separato dalla regola che decide se sono "correlati" o no.

---

## Persistenza multi-modalità

L'enum `ApplicationMode` permette di far girare l'app in tre modi diversi, senza toccare i controller:

- **DEMO** — tutto in memoria, comodo per provare l'app velocemente;
- **FULL_FILE** — persistenza su CSV;
- **FULL_DB** — persistenza su MySQL via JDBC.

---

## Dominio

- Ruoli: `USER`, `TECHNICIAN`, `MANAGER`
- Priorità con SLA incluso nell'enum stesso (non in un file di configurazione): `LOW` 72h, `MEDIUM` 24h, `HIGH` 8h, `CRITICAL` 4h
- Categorie: `HARDWARE`, `SOFTWARE`, `NETWORK`, `EMAIL`, `SECURITY`, `OTHER`
- Knowledge base interrogabile dal motore di correlazione

---

## Test e qualità del codice

Ho scritto test JUnit 5 sulle parti che mi sembravano più a rischio: la state machine dei ticket, la strategy di correlazione, la factory di persistenza, la modalità file, il login e il flusso completo di apertura di un ticket.

Ho anche collegato **SonarCloud** al progetto e sistemato via via i warning che segnalava (campi statici mutabili, import inutilizzati, if annidati, letterali duplicati), il che mi ha fatto capire meglio cosa vuol dire davvero "codice pulito" oltre alla teoria vista a lezione.

---

## Stack

Java 21 · JavaFX 21 · MySQL · SLF4J/Logback · JUnit 5 · Maven · SonarCloud · Git

---

## Cosa mi porto a casa da questo progetto

È stato il primo progetto abbastanza grande in cui ho dovuto pensare all'architettura *prima* di scrivere codice, invece di improvvisare man mano. Mi sono scontrato parecchio con la differenza tra "conoscere un pattern" e "capire quando ha davvero senso usarlo" — un paio di volte ho aggiunto pattern che poi ho tolto perché complicavano le cose senza motivo. Alla fine credo di aver capito meglio concetti come separazione delle responsabilità, basso accoppiamento e testabilità non tanto dalla teoria, quanto dal doverli applicare e vedere cosa succedeva quando li rispettavo (o non li rispettavo).
