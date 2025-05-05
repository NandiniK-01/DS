import java.util.Scanner;

public class Ring {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int num;

		System.out.print("Enter the number of processes: ");
		num = in.nextInt();

		Process[] proc = new Process[num];

		for (int i = 0; i < num; i++) {
			proc[i] = new Process();
			proc[i].index = i;
			System.out.print("Enter the ID of process " + i + ": ");
			proc[i].id = in.nextInt();
			proc[i].state = "active";
		}

		int coordinatorId = findMaxId(proc);
		System.out.println("Initial coordinator is Process with ID: " + coordinatorId);

		while (true) {
			System.out.println("\n1. Start Election");
			System.out.println("2. Crash a process");
			System.out.println("3. Recover a process");
			System.out.println("4. Quit");
			System.out.print("Enter your choice: ");
			int ch = in.nextInt();

			switch (ch) {
			case 1:
				System.out.print("Enter process number that initiates the election: ");
				int initiator = in.nextInt();

				if (!proc[initiator].state.equals("active")) {
					System.out.println("This process is down. Cannot initiate election.");
					break;
				}

				int[] electionIds = new int[num];
				int count = 0;
				int curr = initiator;

				System.out.println("Election messages being passed in the ring:");

				do {
					if (proc[curr].state.equals("active")) {
						System.out.println("Process " + proc[curr].id + " passes message...");
						electionIds[count++] = proc[curr].id;
					}
					curr = (curr + 1) % num;
				} while (curr != initiator);

				// Find new coordinator
				int newCoordinatorId = electionIds[0];
				for (int i = 1; i < count; i++) {
					if (electionIds[i] > newCoordinatorId) {
						newCoordinatorId = electionIds[i];
					}
				}

				coordinatorId = newCoordinatorId;
				System.out.println("New coordinator is Process with ID: " + coordinatorId);
				break;

			case 2:
				System.out.print("Enter process number to crash: ");
				int crash = in.nextInt();
				if (crash >= 0 && crash < num) {
					proc[crash].state = "down";
					System.out.println("Process " + proc[crash].id + " is now down.");
				} else {
					System.out.println("Invalid process number.");
				}
				break;

			case 3:
				System.out.print("Enter process number to recover: ");
				int recover = in.nextInt();
				if (recover >= 0 && recover < num) {
					if (proc[recover].state.equals("down")) {
						proc[recover].state = "active";
						System.out.println("Process " + proc[recover].id + " has recovered and is now active.");
					} else {
						System.out.println("Process " + proc[recover].id + " is already active.");
					}

				} else {
					System.out.println("Invalid process number.");
				}
				break;

			case 4:
				System.out.println("Program terminated.");
				return;

			default:
				System.out.println("Invalid choice.");
			}
		}
	}

	static int findMaxId(Process[] proc) {
		int maxId = -1;
		for (Process p : proc) {
			if (p.state.equals("active") && p.id > maxId) {
				maxId = p.id;
			}
		}
		return maxId;
	}
}

class Process {
	int index;
	int id;
	String state;
}
