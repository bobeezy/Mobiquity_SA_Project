package main.java.exceptions;

/**
 * @author lionel.mangoua
 * date: 06/08/22
 */

public class ReadPropertyFileException extends RuntimeException {

    public ReadPropertyFileException(String message) {
        super(message);
    }
}
