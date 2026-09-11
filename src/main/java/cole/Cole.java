package cole;

import java.util.Scanner;

public class Cole {

    public static final String DIVIDER = "_____________________________________________________________\n";

    private static final int MAX_TASKS = 100;
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    public static Task[] actions = new Task[MAX_TASKS];
    public static int taskCounts = 0;
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        printGreetings();

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println(DIVIDER);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.println(DIVIDER);
                System.out.println("Here are the tasks in your list:");

                if (taskCounts == 0) {
                    System.out.println("There is no task now!");
                } else {
                    for (int i = 0; i < taskCounts; i++) {
                        System.out.println((i + 1) + ". " + actions[i]);
                    }
                }

                System.out.println(DIVIDER);
            } else if (input.startsWith("mark ")) {
                System.out.println(DIVIDER);
                try {
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    actions[index].markAction();
                    System.out.println(DIVIDER);
                } catch (NumberFormatException e) {
                    System.out.println("OOPS!!! Please provide a valid task number, e.g. \"mark 2\".");
                    System.out.println(DIVIDER);
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    System.out.println("OOPS!!! That task number doesn't exist.");
                    System.out.println(DIVIDER);
                }
            } else if (input.startsWith("unmark ")) {
                System.out.println(DIVIDER);

                try {
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    actions[index].unmarkAction();
                    System.out.println(DIVIDER);
                } catch (NumberFormatException e) {
                    System.out.println("OOPS!!! Please provide a valid task number, e.g. \"unmark 2\".");
                    System.out.println(DIVIDER);
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    System.out.println("OOPS!!! That task number doesn't exist.");
                    System.out.println(DIVIDER);
                }
            } else if (input.startsWith(COMMAND_TODO + " ") || input.equals(COMMAND_TODO)) {
                try {
                    String description = input.length() > COMMAND_TODO.length()
                            ? input.substring(COMMAND_TODO.length()).trim() : "";
                    requireNonEmpty(description, "OOPS!!! You forgot to tell me what the todo is about!");
                    addTask(new ToDo(description));
                } catch (ColeException e) {
                    printError(e);
                }
            } else if (input.startsWith(COMMAND_DEADLINE + " ") || input.equals(COMMAND_DEADLINE)) {

                try {
                    String content = input.length() > COMMAND_DEADLINE.length()
                            ? input.substring(COMMAND_DEADLINE.length()) : "";
                    String[] deadlineParts = content.split(" /by ", 2);

                    if (deadlineParts.length < 2) {
                        throw new ColeException("OOPS!!! Please specify the deadline using /by, e.g. \"deadline return book /by Sunday\".");
                    }

                    String description = deadlineParts[0].trim();
                    String by = deadlineParts[1];

                    requireNonEmpty(description, "OOPS!!! What's the deadline for? Please add a description.");
                    requireNonEmpty(by, "OOPS!!! What's the time for the deadline? Please add a specific time.");

                    addTask(new Deadline(description, by));
                } catch (ColeException e) {
                    printError(e);
                }

            } else if (input.startsWith(COMMAND_EVENT + " ") || input.equals(COMMAND_EVENT)) {

                try {
                    String content = input.length() > COMMAND_EVENT.length()
                            ? input.substring(COMMAND_EVENT.length()) : "";
                    requireNonEmpty(content, "OOPS!!! Umm... what's the event? You didn't give me a description.");
                    String[] eventParts = content.split(" /from ", 2);

                    if (eventParts.length < 2) {
                        throw new ColeException("OOPS!!! Please specify the event time using /from and /to, e.g. \"event meeting /from Mon 2pm /to 4pm\".");
                    }

                    String description = eventParts[0].trim();
                    requireNonEmpty(description, "OOPS!!! Umm... what's the event? You didn't give me a description.");

                    String[] fromTo = eventParts[1].split(" /to ", 2);
                    if (fromTo.length < 2) {
                        throw new ColeException("OOPS!!! Please specify the event time using /from and /to, e.g. \"event meeting /from Mon 2pm /to 4pm\".");
                    }

                    String from = fromTo[0].trim();
                    String to = fromTo[1].trim();
                    requireNonEmpty(from, "OOPS!!! Please specify the event time using /from and /to, e.g. \"event meeting /from Mon 2pm /to 4pm\".");
                    requireNonEmpty(to, "OOPS!!! Please specify the event time using /from and /to, e.g. \"event meeting /from Mon 2pm /to 4pm\".");

                    addTask(new Event(description, from, to));

                } catch (ColeException e) {
                    printError(e);
                }
            } else {
                printError(new ColeException("OOPS !!! I have no idea what that command means, sorry !"));
            }
        }
        scanner.close();

    }

    public static void printGreetings() {
        System.out.println(DIVIDER);

        System.out.println("  ____      _      \n"
                + " / ___|___ | | ___ \n"
                + "| |   / _ \\| |/ _ \\\n"
                + "| |__| (_) | |  __/\n"
                + " \\____\\___/|_|\\___|\n");

        System.out.println("Hello! I'm Cole.\n");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    public static void addTask(Task newTask) {

        try {
            actions[taskCounts] = newTask;
            taskCounts++;

            System.out.println(DIVIDER);
            System.out.println("Got it. I've added this task:");
            System.out.println(" " + newTask);
            System.out.println("Now you have " + taskCounts + " tasks in the list.");
            System.out.println(DIVIDER);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(DIVIDER);
            System.out.println("OOPS!!! The task list is full, I can't add any more tasks.");
            System.out.println(DIVIDER);
        }

    }

    public static void printError(ColeException e) {
        System.out.println(DIVIDER);
        System.out.println(e.getMessage());
        System.out.println(DIVIDER);
    }

    private static void requireNonEmpty(String value, String errorMessage) throws ColeException {
        if (value.trim().isEmpty()) {
            throw new ColeException(errorMessage);
        }
    }


}
