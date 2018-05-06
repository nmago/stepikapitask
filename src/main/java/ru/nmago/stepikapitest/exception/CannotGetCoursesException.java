package ru.nmago.stepikapitest.exception;

public class CannotGetCoursesException extends Throwable {
    private String message;

    public CannotGetCoursesException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
