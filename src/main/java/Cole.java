import java.util.Scanner;
// change to test to review pull request
public class Cole {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int taskCount = 0;

        String line = "____________________________________________________________";

        System.out.println(line);
        System.out.println("Hello! I'm Cole");
        System.out.println("What can I do for you?");
        System.out.println(line);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println(line);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break;

            } else if (input.equals("list")) {
                System.out.println(line);
                System.out.println("Here are the tasks in your list:");

                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }

                System.out.println(line);

            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                Task task = tasks[taskNumber - 1];

                task.markAsDone();

                System.out.println(line);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
                System.out.println(line);

            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                Task task = tasks[taskNumber - 1];

                task.markAsNotDone();

                System.out.println(line);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
                System.out.println(line);

            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);

                Task task = new Todo(description);
                tasks[taskCount] = task;
                taskCount++;

                System.out.println(line);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + task);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);

            } else if (input.startsWith("deadline ")) {
                String content = input.substring(9);
                String[] parts = content.split(" /by ", 2);

                String description = parts[0];
                String by = parts[1];

                Task task = new Deadline(description, by);
                tasks[taskCount] = task;
                taskCount++;

                System.out.println(line);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + task);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);

            } else if (input.startsWith("event ")) {
                String content = input.substring(6);

                String[] fromParts = content.split(" /from ", 2);
                String description = fromParts[0];

                String[] toParts = fromParts[1].split(" /to ", 2);
                String from = toParts[0];
                String to = toParts[1];

                Task task = new Event(description, from, to);
                tasks[taskCount] = task;
                taskCount++;

                System.out.println(line);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + task);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);
            }
        }

        scanner.close();
    }
}