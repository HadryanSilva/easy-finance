package br.com.hadryan.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyRegisteredException extends ResponseStatusException {

    public EmailAlreadyRegisteredException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
