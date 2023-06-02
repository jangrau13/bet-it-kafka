package ch.unisg.ics.edpo.gamemaster.streaming.serialization.json;

public class CustomDeserilizationError extends RuntimeException{
    public CustomDeserilizationError(String message, Throwable cause) {
        super(message, cause);
    }
}
