import java.util.Scanner;

public class Cole {

    public static final String divider = "_____________________________________________________________\n";
    public static Task[] actions = new Task[100];
    public static int taskCounts = 0;
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        printGreetings();

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println(divider);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            else if (input.equalsIgnoreCase("list")) {
                System.out.println(divider);
                System.out.println("Here are the tasks in your list:");

                if (taskCounts == 0) {
                    System.out.println("There is no task now!");
                } else {
                    for (int i = 0; i < taskCounts; i++) {
                        System.out.println((i + 1) + ". " + actions[i]);
                    }
                }

                System.out.println(divider);
            }

            else if (input.startsWith("mark ")) {
                System.out.println(divider);
                try {
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    actions[index].markAction();
                    System.out.println(divider);
                } catch (NumberFormatException e) {
                    System.out.println("OOPS!!! Please provide a valid task number, e.g. \"mark 2\".");
                    System.out.println(divider);
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    System.out.println("OOPS!!! That task number doesn't exist.");
                    System.out.println(divider);
                }
            }

            else if (input.startsWith("unmark ")) {
                System.out.println(divider);

                try {
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    actions[index].unmarkAction();
                    System.out.println(divider);
                } catch (NumberFormatException e) {
                    System.out.println("OOPS!!! Please provide a valid task number, e.g. \"unmark 2\".");
                    System.out.println(divider);
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    System.out.println("OOPS!!! That task number doesn't exist.");
                    System.out.println(divider);
                }
            }

            else if (input.startsWith("todo ")) {
                addTask(new ToDo(input.substring(5)));

            }

            else if (input.startsWith("deadline ")){
                String[] deadlineParts = input.substring(9).split(" /by ", 2);
                String description = deadlineParts[0];
                String by = deadlineParts[1];

                addTask(new Deadline(description, by));

            }

            else if (input.startsWith("event ")){
                String[] eventParts = input.substring(6).split(" /from ", 2);
                String description = eventParts[0];

                String[] fromTo = eventParts[1].split(" /to ", 2);
                String from = fromTo[0];
                String to = fromTo[1];

                addTask(new Event(description, from, to));
            }
        }
        scanner.close();

    }

    public static void printGreetings() {
        System.out.println(divider);

        System.out.println("  ____      _      \n"
                + " / ___|___ | | ___ \n"
                + "| |   / _ \\| |/ _ \\\n"
                + "| |__| (_) | |  __/\n"
                + " \\____\\___/|_|\\___|\n");

        System.out.println("Hello! I'm Cole.\n");
        System.out.println("What can I do for you?");
        System.out.println(divider);
    }

    public static void addTask(Task newTask){

        try {
            actions[taskCounts] = newTask;
            taskCounts++;

            System.out.println(divider);
            System.out.println("Got it. I've added this task:");
            System.out.println(" " + newTask);
            System.out.println("Now you have " + taskCounts + " tasks in the list.");
            System.out.println(divider);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(divider);
            System.out.println("OOPS!!! The task list is full, I can't add any more tasks.");
            System.out.println(divider);
        }


    }

}
