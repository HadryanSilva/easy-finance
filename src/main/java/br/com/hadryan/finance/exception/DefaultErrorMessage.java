package br.com.hadryan.finance.exception;

import org.springframework.http.HttpStatus;

public record DefaultErrorMessage(HttpStatus status, String message) {
}
