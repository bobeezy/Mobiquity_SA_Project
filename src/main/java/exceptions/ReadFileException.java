package main.java.exceptions;

/**
 * @author lionel.mangoua
 * date: 06/08/22
 */

public class ReadFileException extends RuntimeException {

    public ReadFileException(String message) {
        super(message);
    }
}
