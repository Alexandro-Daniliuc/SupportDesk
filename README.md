
# SupportDesk

🇬🇧 [English](#english) · 🇮🇹 [Italiano](#italiano)

---

<a id="english"></a>
## 🇬🇧 English

Project developed for the **Software Engineering (ISPW)** exam — Uniroma2, DICII, final year of the bachelor's degree.

It's a **JavaFX** desktop application for managing a help desk: users open support tickets, technicians work on them, managers keep an eye on SLAs and notifications. I used it mainly as a way to put into practice what we studied in class (GoF patterns, layered architecture, abstract persistence) on a real project, rather than a toy exercise.

In this README I try to explain the theory behind the choices I made, rather than just describing what the app does.

---

### Architecture: Boundary–Control–Entity

To structure the project I followed the BCE scheme seen in class (a variant of MVC), with the goal of keeping the UI separate from the domain logic:

| Layer | Package | What it does |
|---|---|---|
| **Boundary** | `boundary/javafx` + `.fxml` files | The JavaFX graphical controllers: they just read user input, they contain no application logic |
| **Control** | `controller/applicativo` | One controller per use case (login, ticket opening, assignment, SLA, correlation, comments, knowledge base, registration) |
| **Entity** | `model` | The domain classes (`Ticket`, `User`, `Comment`, `KnowledgeEntry`, `Notification`), with no dependency on JavaFX or the database |
| **Bean / Record** | `bean`, `record` | Immutable DTOs used to pass data between layers without directly exposing the entities |

The rule I always tried to respect is that boundary and control never talk to each other directly: in between there's always a **Facade** (`utility/facade`, one per use case). At first it seemed like an unnecessary complication, but as the project grew it turned out to be the thing that saved me the most trouble when I had to modify a screen without breaking the logic underneath.

---

### Design patterns used (and why I chose them)

#### Singleton
I needed a single shared instance for things like the user session or the correlation engine, without `synchronized` everywhere. I used the **initialization-on-demand holder idiom**: an inner static class loaded by the JVM only on first access, which guarantees thread-safety without explicit locks. I applied it to 13 classes in total (`UserSession`, `ApplicationModeManager`, `CorrelationEngine`, `ConfigLoader`, `ConnectionManager`, `PersistenceLayerFactory` and all the Facades).

#### Abstract Factory + DAO
I wanted to be able to run the app with three different persistence modes (in-memory, file, database) without filling the code with scattered `if`s. `DAOAbstractFactory` defines the common interface, and three concrete factories (`DAOFactoryDB`, `DAOFactoryFile`, `DAOFactoryDemo`) implement it. The choice of which one to use is made only once at startup, based on the configured mode.

#### Decorator
I needed to add optional "labels" to a ticket (expired SLA, criticality) without creating a subclass for every possible combination. `TicketDecorator` wraps a `TicketComponent` and delegates everything except what it needs to modify; `TicketWithSLA` and `TicketCritical` can be stacked on top of each other.

#### Strategy
The ticket correlation engine needed to be able to change its decision criterion without touching the metric computation. `CorrelationStrategy` evaluates a `CorrelationContext` (the already-computed metrics) and decides whether two tickets are correlated; `CategoryAwareStrategy` is the implementation I use by default.

#### Observer
When a ticket changes status, different roles need to be notified about different things (the manager about violated SLAs, the technician about assignments, the user about the resolution). `Subject` holds the list of `Observer`s and notifies a typed `EventType`; each observer (`ManagerNotificationObserver`, `TechnicianNotificationObserver`, `UserNotificationObserver`) only reacts to what it cares about.

#### State
I wanted to avoid invalid state transitions for a ticket (like going straight from open to closed). I encapsulated the state machine directly inside the `TicketStatus` enum, where each constant implements its own `nextStates()` method:

```
OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED
                                     ↳ REOPENED → ASSIGNED
```

This way the transition logic lives in a single place and doesn't need to be re-checked by every controller.

---

### Algorithm implementation: the correlation engine

I didn't want a simple keyword comparison, so I implemented a small *information retrieval* pipeline — stuff seen more in algorithms/databases courses than in ISPW, but that seemed like a perfect fit for this use case:

1. normalize the title + description text;
2. build a **TF-IDF** vector for each ticket (high weight for terms frequent in the document but rare elsewhere);
3. compute the **cosine similarity** between the new ticket and each candidate;
4. also compute the **Jaccard index** on the most significant keywords, as a complementary metric;
5. all these metrics end up in a `CorrelationContext` that I pass to the `CorrelationStrategy` — this keeps feature computation separate from the rule that decides whether they're "correlated" or not.

---

### Multi-mode persistence

The `ApplicationMode` enum allows the app to run in three different modes, without touching the controllers:

- **DEMO** — everything in memory, handy for trying out the app quickly;
- **FULL_FILE** — CSV persistence;
- **FULL_DB** — MySQL persistence via JDBC.

---

### Domain

- Roles: `USER`, `TECHNICIAN`, `MANAGER`
- Priority with SLA included in the enum itself (not in a config file): `LOW` 72h, `MEDIUM` 24h, `HIGH` 8h, `CRITICAL` 4h
- Categories: `HARDWARE`, `SOFTWARE`, `NETWORK`, `EMAIL`, `SECURITY`, `OTHER`
- Knowledge base queryable by the correlation engine

---

### Tests and code quality

I wrote JUnit 5 tests on the parts that seemed riskiest to me: the ticket state machine, the correlation strategy, the persistence factory, file mode, login, and the full ticket-opening flow.

I also hooked up **SonarCloud** to the project and gradually fixed the warnings it reported (mutable static fields, unused imports, nested ifs, duplicated literals), which helped me understand what "clean code" really means beyond the theory seen in class.

---

### Stack

Java 21 · JavaFX 21 · MySQL · SLF4J/Logback · JUnit 5 · Maven · SonarCloud · Git

---

### What I'm taking away from this project

This was the first sufficiently large project where I had to think about the architecture *before* writing code, instead of improvising as I went. I clashed quite a bit with the difference between "knowing a pattern" and "understanding when it actually makes sense to use it" — a couple of times I added patterns that I later removed because they complicated things for no reason. In the end I think I understood concepts like separation of concerns, low coupling and testability better not so much from the theory, but from having to apply them and see what happened when I respected them (or didn't).

---

<a id="italiano"></a>
## 🇮🇹 Italiano

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
