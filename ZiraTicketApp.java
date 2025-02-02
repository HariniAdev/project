import java.util.*;

class Ticket {
    private int ticketId;
    private String userName;
    private String route;
    private int seatNumber;
    private double price;

    public Ticket(int ticketId, String userName, String route, int seatNumber, double price) {
        this.ticketId = ticketId;
        this.userName = userName;
        this.route = route;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoute() {
        return route;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketId +
                ", Name: " + userName +
                ", Route: " + route +
                ", Seat Number: " + seatNumber +
                ", Price: $" + price;
    }
}

class BookingSystem {
    private Map<Integer, Boolean> seats = new HashMap<>();
    private List<String> routes = new ArrayList<>(List.of("City A to City B", "City C to City D", "City E to City F"));
    private ArrayList<Ticket> tickets = new ArrayList<>();
    private Map<String, List<Ticket>> bookingHistory = new HashMap<>();
    private Queue<String> waitlist = new LinkedList<>();
    private Map<String, String> users = new HashMap<>();
    private double basePrice = 500.0;
    private int ticketCounter = 1;
    private int availableSeats = 10;

    public BookingSystem() {
        for (int i = 1; i <= 10; i++) {
            seats.put(i, false); // Initialize all seats as available
        }
    }

    public void addUser(String username, String password) {
        users.put(username, password);
    }

    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public void displayRoutes() {
        System.out.println("Available Routes:");
        for (int i = 0; i < routes.size(); i++) {
            System.out.println((i + 1) + ". " + routes.get(i));
        }
    }

    public double calculateDynamicPricing() {
        if (availableSeats <= 2) {
            return basePrice * 1.10; // Increase price by 10% if 2 or fewer seats are available
        }
        return basePrice;
    }

    public double applySeasonalDiscount(double price) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH); // 0 = January, 11 = December
        if (month == Calendar.DECEMBER) {
            return price * 0.90; // 10% discount in December
        }
        return price;
    }

    public boolean bookTicket(String username, int routeOption) {
        if (availableSeats <= 0) {
            System.out.println("No seats available. Adding to waitlist...");
            waitlist.add(username);
            return false;
        }

        if (routeOption < 1 || routeOption > routes.size()) {
            System.out.println("Invalid route selection.");
            return false;
        }

        int seatNumber = 11 - availableSeats;
        String route = routes.get(routeOption - 1);
        double price = calculateDynamicPricing();
        price = applySeasonalDiscount(price);

        Ticket ticket = new Ticket(ticketCounter++, username, route, seatNumber, price);
        tickets.add(ticket);
        availableSeats--;
        seats.put(seatNumber, true);
        addToBookingHistory(username, ticket);
        System.out.println("Ticket booked successfully! Your ticket details:");
        System.out.println(ticket);
        sendEmailConfirmation(ticket);
        return true;
    }

    public void sendEmailConfirmation(Ticket ticket) {
        System.out.println("Sending email confirmation to " + ticket.getUserName());
        System.out.println("Email content: Your ticket details - " + ticket);
    }

    public void cancelTicket(String username, int ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId() == ticketId && ticket.getUserName().equals(username)) {
                tickets.remove(ticket);
                availableSeats++;
                seats.put(ticket.getSeatNumber(), false);
                System.out.println("Ticket with ID " + ticketId + " canceled successfully.");

                if (!waitlist.isEmpty()) {
                    String nextInLine = waitlist.poll();
                    System.out.println("Notifying waitlisted user: " + nextInLine);
                    bookTicket(nextInLine, routes.indexOf(ticket.getRoute()) + 1);
                }
                return;
            }
        }
        System.out.println("Ticket not found or not owned by the user.");
    }

    public void displayAvailableSeats() {
        System.out.println("Available Seats:");
        for (Map.Entry<Integer, Boolean> seat : seats.entrySet()) {
            if (!seat.getValue()) {
                System.out.print(seat.getKey() + " ");
            }
        }
        System.out.println();
    }

    public void addToBookingHistory(String username, Ticket ticket) {
        bookingHistory.putIfAbsent(username, new ArrayList<>());
        bookingHistory.get(username).add(ticket);
    }

    public void displayBookingHistory(String username) {
        if (bookingHistory.containsKey(username)) {
            System.out.println("Booking History for " + username + ":");
            for (Ticket ticket : bookingHistory.get(username)) {
                System.out.println(ticket);
            }
        } else {
            System.out.println("No booking history found for " + username);
        }
    }

    public void displayAdminStats() {
        System.out.println("Total Tickets Sold: " + (10 - availableSeats));
        System.out.println("Total Revenue: $" + tickets.stream().mapToDouble(Ticket::getPrice).sum());
    }
}

public class ZiraTicketApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookingSystem bookingSystem = new BookingSystem();

        // Preload admin and some users
        bookingSystem.addUser("admin", "admin123");
        bookingSystem.addUser("user1", "password1");
        bookingSystem.addUser("user2", "password2");

        System.out.println("=== Welcome to Zira Ticket Booking System ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!bookingSystem.login(username, password)) {
            System.out.println("Invalid login credentials!");
            return;
        }

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Display Routes");
            System.out.println("2. Book a Ticket");
            System.out.println("3. Cancel a Ticket");
            System.out.println("4. View Booking History");
            System.out.println("5. View Available Seats");
            System.out.println("6. Admin Stats (Admin Only)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> bookingSystem.displayRoutes();
                case 2 -> {
                    bookingSystem.displayRoutes();
                    System.out.print("Select a route (1-3): ");
                    int routeOption = scanner.nextInt();
                    bookingSystem.bookTicket(username, routeOption);
                }
                case 3 -> {
                    System.out.print("Enter the Ticket ID to cancel: ");
                    int ticketId = scanner.nextInt();
                    bookingSystem.cancelTicket(username, ticketId);
                }
                case 4 -> bookingSystem.displayBookingHistory(username);
                case 5 -> bookingSystem.displayAvailableSeats();
                case 6 -> {
                    if (username.equals("admin")) {
                        bookingSystem.displayAdminStats();
                    } else {
                        System.out.println("Access denied!");
                    }
                }
                case 7 -> {
                    System.out.println("Thank you for using Zira Ticket Booking System!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}


