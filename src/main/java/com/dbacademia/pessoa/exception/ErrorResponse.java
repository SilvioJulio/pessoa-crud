package com.dbacademia.pessoa.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.List;


public record ErrorResponse(
        String error,
        String message,
        int status,
        String path,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        OffsetDateTime timestamp,
        List<FieldErrorItem> details
) {
    public record FieldErrorItem(String field, String message) {
    }

    public static ErrorResponse of(String error, String message, int status, String path, List<FieldErrorItem> details) {
        return new ErrorResponse(error, message, status, path, OffsetDateTime.now(), details);
    }
}


