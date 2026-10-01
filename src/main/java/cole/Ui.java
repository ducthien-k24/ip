package cole;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all interactions with the user: reading commands from the console
 * and printing Cole's responses in a consistent framed format.
 */
public class Ui {
    private static final String DIVIDER = "_____________________________________________________________\n";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads the next line of input typed by the user.
     *
     * @return the raw command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the Cole logo and the greeting message.
     */
    public void showWelcome() {
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

    /**
     * Prints the given lines between two dividers, one line per argument.
     *
     * @param lines the lines of the message to show
     */
    public void showMessages(String... lines) {
        System.out.println(DIVIDER);
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints an error message in the same framed format as normal messages.
     *
     * @param message the error message to show
     */
    public void showError(String message) {
        showMessages(message);
    }

    /**
     * Prints all tasks in the list with their 1-based index,
     * or a notice if the list is empty.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(ArrayList<Task> tasks) {
        showNumberedTasks("Here are the tasks in your list:", tasks, "There is no task now!");
    }

    /**
     * Prints the tasks that matched a find command, numbered from 1,
     * or a notice if nothing matched.
     *
     * @param matchingTasks the tasks that contain the keyword
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        showNumberedTasks("Here are the matching tasks in your list:", matchingTasks,
                "No matching tasks found.");
    }

    private void showNumberedTasks(String header, ArrayList<Task> tasks, String emptyMessage) {
        System.out.println(DIVIDER);
        System.out.println(header);

        if (tasks.isEmpty()) {
            System.out.println(emptyMessage);
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }

        System.out.println(DIVIDER);
    }

    /**
     * Releases the input scanner. Call once when the program exits.
     */
    public void close() {
        scanner.close();
    }
}
