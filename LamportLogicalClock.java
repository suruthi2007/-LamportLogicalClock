
import java.util.*;

class Event {
    String process;
    String eventType;
    String description;
    int timestamp;

    Event(String process, String eventType,
          String description, int timestamp) {
        this.process = process;
        this.eventType = eventType;
        this.description = description;
        this.timestamp = timestamp;
    }
}

public class LamportLogicalClock {

    static Map<String, Integer> clocks = new LinkedHashMap<>();
    static List<Event> events = new ArrayList<>();

    // Initialize distributed processes
    static void initializeProcesses() {
        clocks.put("P1", 0);
        clocks.put("P2", 0);
        clocks.put("P3", 0);
    }

    // Local event
    static void localEvent(String process, String description) {
        int time = clocks.get(process) + 1;
        clocks.put(process, time);

        events.add(new Event(
            process, "LOCAL", description, time
        ));
    }

    // Send message event
    static int sendMessage(String sender, String message) {
        int time = clocks.get(sender) + 1;
        clocks.put(sender, time);

        events.add(new Event(
            sender, "SEND", "Sends " + message, time
        ));

        return time;
    }

    // Receive message event
    static void receiveMessage(String receiver,
                               String message,
                               int receivedTimestamp) {

        int time = Math.max(
            clocks.get(receiver), receivedTimestamp
        ) + 1;

        clocks.put(receiver, time);

        events.add(new Event(
            receiver, "RECEIVE",
            "Receives " + message, time
        ));
    }

    // Display all events
    static void displayEvents() {

        System.out.println("\n===== LAMPORT CLOCK EVENT LOG =====");

        System.out.printf("%-10s %-12s %-20s %-10s%n",
                "Process", "Event", "Description", "Clock");

        System.out.println(
            "-------------------------------------------------------"
        );

        for (Event e : events) {
            System.out.printf("%-10s %-12s %-20s %-10d%n",
                    e.process, e.eventType,
                    e.description, e.timestamp);
        }
    }

    // Display final logical clocks
    static void displayClocks() {

        System.out.println("\n===== FINAL PROCESS CLOCKS =====");

        for (String process : clocks.keySet()) {
            System.out.println(
                process + " : " + clocks.get(process)
            );
        }
    }

    // Display events in timestamp order
    static void displayEventOrdering() {

        List<Event> sortedEvents = new ArrayList<>(events);

        sortedEvents.sort(
            Comparator.comparingInt(e -> e.timestamp)
        );

        System.out.println("\n===== EVENT ORDERING =====");

        for (Event e : sortedEvents) {
            System.out.println(
                e.process + " -> " +
                e.eventType + " -> Clock: " +
                e.timestamp
            );
        }
    }

    public static void main(String[] args) {

        initializeProcesses();

        System.out.println("LAMPORT LOGICAL CLOCK SIMULATOR");
        System.out.println("--------------------------------");

        // Sample simulation

        localEvent("P1", "Event A");
        localEvent("P2", "Event B");

        int messageTime = sendMessage("P1", "M1");

        receiveMessage("P2", "M1", messageTime);

        localEvent("P3", "Event C");

        // Display results

        displayEvents();
        displayClocks();
        displayEventOrdering();

        System.out.println(
            "\nSimulation completed successfully!"
        );
    }
}
