package com.maxim.tbank.database.exception;

/**
 * Класс проверяемого исключения показывающий ошибку базы данных
 */
public class DatabaseException extends Exception{

    public DatabaseException() {
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public DatabaseException(String message) {
        super(message);
    }
}
