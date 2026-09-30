package cole;

public class Cole {

    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private static final String ERROR_EVENT_TIME =
            "OOPS!!! Please specify the event time using /from and /to, e.g. \"event meeting /from Mon 2pm /to 4pm\".";
    private static final String ERROR_EVENT_DESCRIPTION =
            "OOPS!!! Umm... what's the event? You didn't give me a description.";
    private static final String ERROR_TASK_NOT_FOUND =
            "OOPS!!! That task number doesn't exist.";

    private static TaskList tasks;

    private static final Storage storage = new Storage("./data/cole.txt");
    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        tasks = new TaskList(storage.load());

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
            } else if (isCommand(input, COMMAND_TODO)) {
                addTodo(input);
            } else if (isCommand(input, COMMAND_DEADLINE)) {
                addDeadline(input);
            } else if (isCommand(input, COMMAND_EVENT)) {
                addEvent(input);
            } else {
                ui.showError("OOPS !!! I have no idea what that command means, sorry !");
            }
        }
        ui.close();
    }

    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    private static String argumentsOf(String input, String command) {
        return input.length() > command.length() ? input.substring(command.length()) : "";
    }

    private static void markTask(String input, boolean isDone) {
        String command = isDone ? "mark" : "unmark";
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
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

    private static void deleteTask(String input) {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
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

    private static void addTodo(String input) {
        try {
            String description = argumentsOf(input, COMMAND_TODO).trim();
            requireNonEmpty(description, "OOPS!!! You forgot to tell me what the todo is about!");
            addTask(new ToDo(description));
        } catch (ColeException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void addDeadline(String input) {
        try {
            String[] deadlineParts = argumentsOf(input, COMMAND_DEADLINE).split(" /by ", 2);
            if (deadlineParts.length < 2) {
                throw new ColeException("OOPS!!! Please specify the deadline using /by, "
                        + "e.g. \"deadline return book /by Sunday\".");
            }

            String description = deadlineParts[0].trim();
            String by = deadlineParts[1];

            requireNonEmpty(description, "OOPS!!! What's the deadline for? Please add a description.");
            requireNonEmpty(by, "OOPS!!! What's the time for the deadline? Please add a specific time.");

            addTask(new Deadline(description, by));
        } catch (ColeException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void addEvent(String input) {
        try {
            String content = argumentsOf(input, COMMAND_EVENT);
            requireNonEmpty(content, ERROR_EVENT_DESCRIPTION);

            String[] eventParts = content.split(" /from ", 2);
            if (eventParts.length < 2) {
                throw new ColeException(ERROR_EVENT_TIME);
            }

            String description = eventParts[0].trim();
            requireNonEmpty(description, ERROR_EVENT_DESCRIPTION);

            String[] fromTo = eventParts[1].split(" /to ", 2);
            if (fromTo.length < 2) {
                throw new ColeException(ERROR_EVENT_TIME);
            }

            String from = fromTo[0].trim();
            String to = fromTo[1].trim();
            requireNonEmpty(from, ERROR_EVENT_TIME);
            requireNonEmpty(to, ERROR_EVENT_TIME);

            addTask(new Event(description, from, to));
        } catch (ColeException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void addTask(Task newTask) {
        tasks.add(newTask);
        storage.save(tasks.getAll());

        ui.showMessages("Got it. I've added this task:",
                " " + newTask,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    private static String invalidTaskNumberMessage(String command) {
        return "OOPS!!! Please provide a valid task number, e.g. \"" + command + " 2\".";
    }


    private static void requireNonEmpty(String value, String errorMessage) throws ColeException {
        if (value.trim().isEmpty()) {
            throw new ColeException(errorMessage);
        }
    }
}
