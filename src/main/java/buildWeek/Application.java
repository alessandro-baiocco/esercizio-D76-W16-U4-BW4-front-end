package buildWeek;

import buildWeek.dao.*;
import buildWeek.entities.*;
import buildWeek.enums.SellerType;
import buildWeek.enums.TicketDuration;
import buildWeek.enums.TransportType;
import net.datafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;

public class Application {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("cosotransport");

    private static final EntityManager em = emf.createEntityManager();
    private static final SubscriptionDAO subDao = new SubscriptionDAO(em);
    private static final SellerDao sellDao = new SellerDao(em);
    private static final UserBadgeDao userDAO = new UserBadgeDao(em);
    private static final TransportDao transDao = new TransportDao(em);
    private static final ServiceDao servDao = new ServiceDao(em);
    private static final RouteDAO rouDao = new RouteDAO(em);
    private static final Random rnd = new Random();
    private static final LocalDate now = LocalDate.now();
    private static final Faker faker = new Faker();
    private final TravelDao travDao = new TravelDao(em);
    private final TicketsDAO tickDao = new TicketsDAO(em);
    private final ValidationDao valDao = new ValidationDao(em);
    private final LocalDateTime nowTime = LocalDateTime.now();


    public static void main(String[] args) {
        menu();
        em.close();
        emf.close();
    }

    public static void menu() {
        int menu01 = -1;
        while (menu01 < 0) {
            System.out.println();
            System.out.println("Benvenuto nel sistema di coso trasporti");
            System.out.println();
            System.out.println("seleziona un'opzione");
            System.out.println("1 - Area utenti");
            System.out.println("2 - Area amministrativa");
            System.out.println("0 - Esci");
            System.out.println();

            menu01 = scanInt();

            switch (menu01) {
                case 1:
                    userMenuLogin();
                    menu01 = -1;
                    break;
                case 2:
                    adminMenu();
                    menu01 = -1;
                    break;
                case 0:
                    System.out.println("Arrivederci");
                    break;
                default:
                    System.out.println("Opzione non valida");
                    menu();
            }
        }
    }

    private static void adminMenu() {
        int menu01 = -1;
        while (menu01 < 0) {
            System.out.println();
            System.out.println("Area amministrativa");
            System.out.println();
            System.out.println("1 - gestisci mezzi");
            System.out.println("2 - gestisci tratte");
            System.out.println("3 - visualizza statistiche viaggi");
            System.out.println("0 - indietro");
            menu01 = scanInt();
            switch (menu01) {
                case 1:
                    manageTransports();
                    menu01 = -1;
                    break;
                case 2:
                    manageRoutes();
                    menu01 = -1;
                    break;
                case 3:
                    statisctic();
                    menu01 = -1;
                case 0:
                    System.out.println("Arrivederci");
                    break;
                default:
                    System.out.println("Opzione non valida");
                    menu01 = -1;
            }
        }
    }

    private static void statisctic() {
    }

    private static void manageRoutes() {
    }

    private static void manageTransports() {
        int menu01 = -1;
        while (menu01 < 0) {
            System.out.println();
            System.out.println("Gestione trasporti");
            System.out.println();
            System.out.println("1 - visualizza tutti i trasporti");
            System.out.println("2 - visualizza manutenzioni trasporto");
            System.out.println("3 - metti mezzo in manutenzione");
            System.out.println("4 - metti mezzo in servizio");
            System.out.println("5 - visualizza tratta trasporto");
            System.out.println("0 - indietro");
            menu01 = scanInt();
            switch (menu01) {

                case 1:
                    transDao.getAll().forEach(System.out::println);
                    menu01 = -1;
                    break;
                case 2:
                    servDao.getAllServices(getTransport()).forEach(System.out::println);
                    menu01 = -1;
                    break;
                case 0:
                    System.out.println("Arrivederci");
                    break;
                default:
                    System.out.println("Opzione non valida");
                    menu01 = -1;
            }
        }
    }

    private static Transport getTransport() {
        Transport transport = null;
        while (true) {
            System.out.println("inserisci l'id del trasporto");
            System.out.println("0 - indietro");
            transport = transDao.getById(scanInt());
            if (transport != null) {
                return transport;
            } else {
                System.out.println("trasporto non trovato");
            }
        }
    }

    private static void userMenuLogin() {
        int menu01 = -1;
        while (menu01 < 0) {
            System.out.println();
            System.out.println("Area utenti");
            System.out.println();
            System.out.println("inserisci il tuo id");
            System.out.println("0 - indietro");
            System.out.println();
            menu01 = scanInt();
            if (menu01 == 0) {
                System.out.println("indietro");
            } else {
                if (userDAO.getById(menu01) != null) {
                    userMenu(userDAO.getById(menu01));
                } else {
                    System.out.println("utente non trovato");
                }
                menu01 = -1;
            }
        }

    }

    private static void userMenu(UserBadge userBadge) {
        int menu01 = -1;
        while (menu01 < 0) {
            System.out.println("benvenuto " + userBadge.getName());
            System.out.println();
            System.out.println("seleziona un'opzione");
            System.out.println("1 - Acquista biglietto");
            System.out.println("2 - gestisti tessera personale");
            System.out.println("3 - gestisci abbonamento");
            System.out.println("4 - prendi un mezzo");
            System.out.println("0 - indietro");
            menu01 = scanInt();
            switch (menu01) {
                case 1:
                    buyTicket(userBadge);
                    menu01 = -1;
                    break;
                case 2:
                    userBadgeMenu(userBadge);
                    menu01 = -1;
                    break;
                case 3:
                    subscriptionMenu(userBadge);
                    menu01 = -1;
                    break;
                case 4:
                    takeTransport(userBadge);
                    menu01 = -1;
                    break;
                case 0:
                    System.out.println("indietro");
                    break;
                default:
                    System.out.println("Opzione non valida");
                    menu01 = -1;
            }
        }
    }

    private static void takeTransport(UserBadge userBadge) {
    }

    private static void subscriptionMenu(UserBadge userBadge) {
    }

    private static void userBadgeMenu(UserBadge userBadge) {
    }

    private static void buyTicket(UserBadge userBadge) {
    }

    public static int scanInt() {
        int num = -1;
        Scanner scanner = new Scanner(System.in);
        while (num < 0) {
            try {
                num = scanner.nextInt();
            } catch (Exception e) {
                System.err.println("Inserisci un numero");
                scanner.nextLine();
            }
        }
        return num;
    }

    public static void createUsers() {
        Supplier<UserBadge> userSupplier = () -> {
            return new UserBadge(faker.name().firstName(), faker.name().lastName(), now.minusYears(rnd.nextInt(20, 50)), now.minusYears(rnd.nextInt(2, 5)));
        };
        for (int d = 0; d < 10; d++) {
            userDAO.save(userSupplier.get());
        }
    }

    private static void createTransport() {
        for (int e = 0; e < 20; e++) {
            if (e <= 5) {
                transDao.save(new Transport(faker.name().title(), TransportType.BUS, 80, true));
            } else if (e <= 10) {
                transDao.save(new Transport(faker.name().title(), TransportType.TRAM, 80, true));
            } else if (e <= 15) {
                transDao.save(new Transport(faker.name().title(), TransportType.BUS, 80, false));
            } else {
                transDao.save(new Transport(faker.name().title(), TransportType.TRAM, 80, false));

            }
        }
    }

    private static void createServiceAndRoutes() {
        for (int o = 0; o < 10; o++) {
            servDao.save(new Service(transDao.getById(rnd.nextInt(96, 115)), now.minusDays(rnd.nextInt(20, 30))));
        }
        for (int u = 0; u < 10; u++) {
            rouDao.save(new Route(faker.futurama().location(), faker.futurama().location(), rnd.nextInt(20, 40)));
        }
        for (int u = 0; u < 10; u++) {
            rouDao.save(new Route(faker.futurama().location(), faker.futurama().location(), rnd.nextInt(20, 40)));
        }
        for (int e = 0; e < 5; e++) {
            transDao.save(new Transport(faker.name().title(), TransportType.TRAM, 80, true));
        }
    }

    public void createSellers() {
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                sellDao.save(new Seller(true, SellerType.AUTO));

            } else {
                sellDao.save(new Seller(false, SellerType.REAL));
            }
        }
    }

    private void createSubscriptions() {
        List<TicketDuration> lista = new ArrayList<>();
        lista.add(TicketDuration.WEEKLY);
        lista.add(TicketDuration.MONTHLY);
        for (int a = 0; a < 10; a++) {
            subDao.save(new Subscription(now.minusMonths(rnd.nextInt(5, 10)),
                    sellDao.getRandomSeller(),
                    lista.get(rnd.nextInt(0, 2)), now.minusMonths(rnd.nextInt(2, 5))
                    , userDAO.getRandomUserBadge()));
        }
    }

    private void createTravelAndSetRouTransAndTravel() {
        int randomMinutes = 20 + rnd.nextInt(21);
        for (int c = 0; c < 10; c++) {
            LocalDateTime coso = nowTime.minusDays(rnd.nextInt(20, 30));
            travDao.save(new Travel(coso,
                    coso.plus(randomMinutes, ChronoUnit.valueOf("MINUTES")),
                    rouDao.getById(rnd.nextInt(72, 82)),
                    transDao.getById(rnd.nextInt(57, 102))));
        }

        for (int w = 0; w < 10; w++) {
            rouDao.setTrans();
            rouDao.setTravel();
        }
    }


    private void createTicket() {
        for (int i = 0; i <= 30; i++) {
            tickDao.save(new Ticket(now.minusDays(rnd.nextInt(1, 30)), sellDao.getRandomSeller()));
        }
    }

    private void createValidation() {
        for (int i = 0; i < 10; i++) {
            valDao.save(new Validation(now.minusDays(rnd.nextInt(1, 50)),
                    transDao.getRandomTransport(),
                    travDao.getRandomTravel(),
                    tickDao.getRandomTicket()));
        }
        for (int i = 0; i < 10; i++) {
            tickDao.save(new Ticket(now.minusDays(rnd.nextInt(1, 20)), sellDao.getRandomSeller(), valDao.getRandomValidation()));
        }
    }

}










