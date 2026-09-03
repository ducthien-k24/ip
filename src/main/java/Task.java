public class Task {

    private String description;
    private boolean isDone;

    public Task(String description){
        this.description = description;
        this.isDone = false;
    }

    public String markedSymbol(){
        return isDone ? "X" : " ";
    }

    public void markAction(){
        this.isDone = true;
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(" " + this);
    }

    public void unmarkAction(){
        this.isDone = false;
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(" " + this);
    }

    @Override
    public String toString(){
        return "[" + markedSymbol() + "] " + description;
    }

}
