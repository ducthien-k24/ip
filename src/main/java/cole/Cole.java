package cole;

/**
 * Entry point of the Cole chatbot. Owns the Ui, Storage and TaskList,
 * and runs the loop that reads commands and executes them.
 */
public class Cole {

    private static final String ERROR_TASK_NOT_FOUND =
            "OOPS!!! That task number doesn't exist.";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Cole chatbot that stores its tasks in the given file.
     *
     * @param filePath path of the data file used to load and save tasks
     */
    public Cole(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Greets the user, then reads and executes commands until the user types "bye".
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            if (input.equalsIgnoreCase("bye")) {
                ui.showMessages("Bye. Hope to see you again soon!");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                ui.showTaskList(tasks.getAll());
            } else if (input.startsWith("mark ")) {
                markTask(input, true);
            } else if (input.startsWith("unmark ")) {
                markTask(input, false);
            } else if (input.startsWith("delete ")) {
                deleteTask(input);
            } else if (Parser.isAddTaskCommand(input)) {
                addTask(input);
            } else {
                ui.showError("OOPS !!! I have no idea what that command means, sorry !");
            }
        }
        ui.close();
    }

    public static void main(String[] args) {
        new Cole("./data/cole.txt").run();
    }

    private void markTask(String input, boolean isDone) {
        String command = isDone ? "mark" : "unmark";
        try {
            int index = Parser.parseTaskIndex(input);
            Task task = tasks.get(index);
            if (isDone) {
                task.markAsDone();
                storage.save(tasks.getAll());
                ui.showMessages("Nice! I've marked this task as done:", " " + task);
            } else {
                task.markAsNotDone();
                storage.save(tasks.getAll());
                ui.showMessages("OK, I've marked this task as not done yet:", " " + task);
            }
        } catch (NumberFormatException e) {
            ui.showMessages(invalidTaskNumberMessage(command));
        } catch (IndexOutOfBoundsException e) {
            ui.showMessages(ERROR_TASK_NOT_FOUND);
        }
    }

    private void deleteTask(String input) {
        try {
            int index = Parser.parseTaskIndex(input);
            Task removedTask = tasks.delete(index);
            storage.save(tasks.getAll());
            ui.showMessages("Noted. I've removed this task:",
                    " " + removedTask,
                    "Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            ui.showMessages(invalidTaskNumberMessage("delete"));
        } catch (IndexOutOfBoundsException e) {
            ui.showMessages(ERROR_TASK_NOT_FOUND);
        }
    }

    private void addTask(String input) {
        try {
            Task newTask = Parser.parseTask(input);
            tasks.add(newTask);
            storage.save(tasks.getAll());
            ui.showMessages("Got it. I've added this task:",
                    " " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list.");
        } catch (ColeException e) {
            ui.showError(e.getMessage());
        }
    }

    private String invalidTaskNumberMessage(String command) {
        return "OOPS!!! Please provide a valid task number, e.g. \"" + command + " 2\".";
    }
}
