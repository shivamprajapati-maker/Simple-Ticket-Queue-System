import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Simple Ticket Queue System
 * Console application: raise tickets, serve the next ticket (FIFO),
 * view the waiting queue and search a ticket by ID.
 * Data structure: Queue implemented with a linked list (Node class).
 * Data is loaded from tickets.txt at start and saved to it on exit.
 *
 * Author : Shivam Prajapati (ERP ID: RU-25-11374)
 * Course : Data Structures and Algorithms (DSA), PS No. 34
 */

// One support ticket (a record stored as an object)
class Ticket {
    private final String id;
    private final String customer;
    private final String issue;

    public Ticket(String id, String customer, String issue) {
        this.id = id;
        this.customer = customer;
        this.issue = issue;
    }

    public String getId() { return id; }
    public String getCustomer() { return customer; }
    public String getIssue() { return issue; }

    @Override
    public String toString() {
        return id + " | " + customer + " | " + issue;
    }
}

// One link of the linked list
class Node {
    Ticket data;
    Node next;

    Node(Ticket data) {
        this.data = data;
        this.next = null;
    }
}

// FIFO queue implemented with a linked list (head = front, tail = rear)
class TicketQueue {
    private Node head;
    private Node tail;
    private int size;

    public TicketQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty() { return head == null; }

    public int getSize() { return size; }

    // Add a ticket at the tail. O(1)
    public void enqueue(Ticket t) {
        Node n = new Node(t);
        if (tail == null) {
            head = n;
            tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    // Remove and return the ticket at the head (FIFO). O(1)
    public Ticket dequeue() {
        if (head == null) {
            return null;
        }
        Ticket t = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return t;
    }

    // Print all waiting tickets from head to tail. O(n)
    public void viewQueue() {
        System.out.println("Waiting: " + size);
        Node cur = head;
        int pos = 1;
        while (cur != null) {
            System.out.println("    " + pos + ". " + cur.data);
            cur = cur.next;
            pos++;
        }
    }

    // Manual linear search by ID (no built-in search methods). O(n)
    // Returns the position (1-based) or -1 when not found.
    public int searchById(String id) {
        Node cur = head;
        int pos = 1;
        while (cur != null) {
            if (cur.data.getId().equalsIgnoreCase(id)) {
                return pos;
            }
            cur = cur.next;
            pos++;
        }
        return -1;
    }

    // Return the ticket at a given position (1-based), or null.
    public Ticket getAt(int position) {
        Node cur = head;
        int pos = 1;
        while (cur != null) {
            if (pos == position) {
                return cur.data;
            }
            cur = cur.next;
            pos++;
        }
        return null;
    }
}

public class TicketQueueSystem {

    private static final String FILE_NAME = "tickets.txt";
    private static final int FIRST_ID = 101;

    private static TicketQueue queue = new TicketQueue();
    private static int nextId = FIRST_ID;

    // Load tickets from tickets.txt (format: ID|Customer|Issue)
    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", 3);
                if (parts.length < 3) {
                    continue; // skip a damaged line
                }
                queue.enqueue(new Ticket(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                int number = extractNumber(parts[0].trim());
                if (number >= nextId) {
                    nextId = number + 1;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + FILE_NAME + ": " + e.getMessage());
        }
    }

    // Save all waiting tickets to tickets.txt, in queue order
    private static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 1; i <= queue.getSize(); i++) {
                Ticket t = queue.getAt(i);
                bw.write(t.getId() + "|" + t.getCustomer() + "|" + t.getIssue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save " + FILE_NAME + ": " + e.getMessage());
        }
    }

    // "T105" -> 105 (returns -1 when the ID is not in the T<number> form)
    private static int extractNumber(String id) {
        if (id.length() < 2 || (id.charAt(0) != 'T' && id.charAt(0) != 't')) {
            return -1;
        }
        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String generateId() {
        String id = "T" + nextId;
        nextId++;
        return id;
    }

    // The file uses '|' as separator, so it is not allowed inside the text
    private static String clean(String s) {
        return s.replace('|', '/').trim();
    }

    private static void raiseTicket(Scanner sc) {
        System.out.print("Customer: ");
        String customer = clean(sc.hasNextLine() ? sc.nextLine() : "");
        System.out.print("Issue: ");
        String issue = clean(sc.hasNextLine() ? sc.nextLine() : "");
        if (customer.isEmpty() || issue.isEmpty()) {
            System.out.println("Customer name and issue cannot be empty. Ticket not raised.");
            return;
        }
        Ticket t = new Ticket(generateId(), customer, issue);
        queue.enqueue(t);
        System.out.println("Ticket raised. ID: " + t + " | Position: " + queue.getSize());
    }

    private static void serveNext() {
        if (queue.isEmpty()) {
            System.out.println("No tickets in the queue.");
            return;
        }
        Ticket t = queue.dequeue();
        System.out.println("Ticket " + t.getId() + " served. Customer: " + t.getCustomer());
        System.out.println("Tickets waiting: " + queue.getSize());
    }

    private static void viewQueue() {
        if (queue.isEmpty()) {
            System.out.println("No tickets in the queue.");
            return;
        }
        queue.viewQueue();
    }

    private static void searchTicket(Scanner sc) {
        System.out.print("Enter ticket ID: ");
        String id = (sc.hasNextLine() ? sc.nextLine() : "").trim();
        if (id.isEmpty()) {
            System.out.println("Ticket ID cannot be empty.");
            return;
        }
        int pos = queue.searchById(id);
        if (pos == -1) {
            System.out.println("Ticket not found.");
        } else {
            System.out.println("Ticket found: " + queue.getAt(pos) + " | Position: " + pos);
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("===== Simple Ticket Queue System =====");
        System.out.println("1. Raise Ticket");
        System.out.println("2. Serve Next Ticket");
        System.out.println("3. View Queue");
        System.out.println("4. Search Ticket by ID");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) {
        loadFromFile();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showMenu();
            if (!sc.hasNextLine()) {
                break; // input ended: save and exit
            }
            String input = sc.nextLine().trim();
            switch (input) {
                case "1": raiseTicket(sc); break;
                case "2": serveNext(); break;
                case "3": viewQueue(); break;
                case "4": searchTicket(sc); break;
                case "5": running = false; break;
                default: System.out.println("Invalid choice. Please enter 1 to 5.");
            }
        }

        saveToFile();
        System.out.println("Tickets saved to " + FILE_NAME + ". Goodbye!");
        sc.close();
    }
}
