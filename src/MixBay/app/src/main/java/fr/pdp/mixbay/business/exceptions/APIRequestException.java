package fr.pdp.mixbay.business.exceptions;

import java.util.concurrent.ExecutionException;

public class APIRequestException extends ExecutionException {

    public APIRequestException(String message) {
        super(message);
    }
}
