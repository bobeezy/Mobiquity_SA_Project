package main.java.exceptions;

/**
 * @author lionel.mangoua
 * date: 06/08/22
 */

public class SheetNameException extends RuntimeException {

    public SheetNameException(String message) {
        super(message);
    }
}
