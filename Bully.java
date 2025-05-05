
import java.util.Scanner;

public class Bully {
    static boolean[] state = new boolean[5]; // process state: true = up, false = down
    static int coordinator = 5; // initial coordinator

    public static void up(int up) {
        if (state[up - 1]) {
            System.out.println("Process " + up + " is already up.");
        } else {
            state[up - 1] = true;
            System.out.println("Process " + up + " is brought up and initiates election.");
//            startElection(up);
        }
    }

    public static void down(int down) {
        if (!state[down - 1]) {
            System.out.println("Process " + down + " is already down.");
        } else {
            state[down - 1] = false;
            System.out.println("Process " + down + " is brought down.");
            if (down == coordinator) {
                System.out.println("Coordinator has crashed. A new election is needed.");
            }
        }
    }

    public static void mess(int mess) {
        if (!state[mess - 1]) {
            System.out.println("Process " + mess + " is down and cannot send messages.");
        } else if (state[coordinator - 1]) {
            System.out.println("Process " + mess + " sends message to Coordinator (Process " + coordinator + ") --> OK");
        } else {
            System.out.println("Coordinator is down. Process " + mess + " initiates an election.");
            startElection(mess);
        }
    }

    public static void startElection(int initiator) {
        boolean higherProcessFound = false;

        // Check for any higher-numbered process that is up
        for (int i = initiator; i < 5; i++) {
            if (state[i]) {
                System.out.println("Election message sent from Process " + initiator + " to Process " + (i + 1));
                higherProcessFound = true;
            }
        }

        if (!higherProcessFound) {
            // No higher processes up, initiator becomes coordinator
            coordinator = initiator;
            System.out.println("No higher active process found.");
            System.out.println("Process " + initiator + " becomes the new coordinator.");
        } else {
            // Elect highest-numbered active process
            for (int i = 4; i >= 0; i--) {
                if (state[i]) {
                    coordinator = i + 1;
                    System.out.println("Coordinator message sent from Process " + coordinator + " to all.");
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        // Initially all processes are up
        for (int i = 0; i < 5; ++i) {
            state[i] = true;
        }

        System.out.println("5 active processes: P1, P2, P3, P4, P5");
        System.out.println("Process 5 is the initial coordinator.");

        do {
            System.out.println("\n--- Bully Algorithm Menu ---");
            System.out.println("1. Bring up a process");
            System.out.println("2. Bring down a process");
            System.out.println("3. Send a message");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter process number to bring up (1-5): ");
                    int up = sc.nextInt();
                    if (up >= 1 && up <= 5) up(up);
                    else System.out.println("Invalid process number.");
                    break;

                case 2:
                    System.out.print("Enter process number to bring down (1-5): ");
                    int down = sc.nextInt();
                    if (down >= 1 && down <= 5) down(down);
                    else System.out.println("Invalid process number.");
                    break;

                case 3:
                    System.out.print("Enter process number to send message (1-5): ");
                    int mess = sc.nextInt();
                    if (mess >= 1 && mess <= 5) mess(mess);
                    else System.out.println("Invalid process number.");
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 4);

        sc.close();
    }
}
