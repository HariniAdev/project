import java.io.*;
import java.util.*;

class Bug implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    String title;
    String description;
    String status;

    public Bug(int id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bug ID: " + id + ", Title: " + title + ", Status: " + status;
    }
}

public class BugTracker {
    static List<Bug> bugList = new ArrayList<>();
    static final String FILE_NAME = "bugs.dat";

    public static void main(String[] args) {
        loadBugs();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Report Bug\n2. View Bugs\n3. Update Status\n4. Exit");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter Bug Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();
                    bugList.add(new Bug(bugList.size() + 1, title, desc, "Open"));
                    saveBugs();
                    break;
                case 2:
                    bugList.forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("Enter Bug ID to Update: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter New Status (Open/In Progress/Fixed): ");
                    String status = sc.nextLine();
                    for (Bug b : bugList) {
                        if (b.id == id) {
                            b.status = status;
                            break;
                        }
                    }
                    saveBugs();
                    break;
                case 4:
                    System.exit(0);
            }
        }
    }

    static void saveBugs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(bugList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadBugs() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            bugList = (List<Bug>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            bugList = new ArrayList<>();
        }
    }
}
