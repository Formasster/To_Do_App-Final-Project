package es.educastur.todo_app.exception;

public class EmptyTaskListException extends RuntimeException {

    public EmptyTaskListException() {
        super("The task list is empty.");
    }

    public EmptyTaskListException(String message) {
        super(message);
    }
}