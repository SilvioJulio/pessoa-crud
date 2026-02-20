package com.dbacademia.pessoa.exception;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {

    private final String field;

    public BusinessRuleException(String message, String field) {
        super(message);
        this.field = field;
    }

}
