package com.br.fiap.skillsfast.domain.exceptions;

public class EntidadeNaoLocalizada extends RuntimeException {

    public EntidadeNaoLocalizada(String message) {
        super(message);
    }

    public EntidadeNaoLocalizada(String message, Throwable cause) {
        super(message, cause);
    }
}