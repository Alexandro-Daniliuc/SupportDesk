package it.uniroma2.dicii.ispw.supportdesk.boundary.cli;

import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;

import java.util.Scanner;


@SuppressWarnings("java:S106")
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== SupportDesk - CLI ===");
        boolean running = true;
        while (running) {
            System.out.println("\n1) Login");
            System.out.println("2) Registrati");
            System.out.println("3) Esci");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> handleLogin(sc);
                case "2" -> RegistrationControllerCli.register(sc);
                case "3" -> running = false;
                default -> System.out.println(CliFormatter.MSG_SCELTA_NON_VALIDA);
            }
        }
        System.out.println("Arrivederci.");
        // Lo scheduler SLA condiviso (SubmitTicketController) usa un thread non-daemon:
        // senza uscita esplicita il processo CLI resterebbe vivo dopo il logout.
        System.exit(0);
    }

    private static void handleLogin(Scanner sc) {
        LoginRecord user = LoginControllerCli.login(sc);
        if (user == null) {
            return;
        }
        switch (user.role()) {
            case USER -> new UserDashboardControllerCli().run(sc);
            case TECHNICIAN -> new TechDashboardControllerCli().run(sc);
            case MANAGER -> new ManagerDashboardControllerCli().run(sc);
        }
    }
}
