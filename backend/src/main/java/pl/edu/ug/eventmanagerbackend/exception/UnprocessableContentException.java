package pl.edu.ug.eventmanagerbackend.exception;

public class UnprocessableContentException extends RuntimeException {

    public UnprocessableContentException(String message) {
        super(message);
    }

}
