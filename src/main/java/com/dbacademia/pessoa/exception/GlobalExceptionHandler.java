package com.dbacademia.pessoa.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {

        // Lógica para definir 404 se o ID não for encontrado, senão 400
        int statusValue = ex.getMessage().contains("ID não encontrado") ? 404 : 400;

        List<ErrorResponse.FieldErrorItem> details = List.of(
                new ErrorResponse.FieldErrorItem(
                        (ex.getField() != null ? ex.getField() : "business"),
                        ex.getMessage()
                )
        );

        ErrorResponse body = ErrorResponse.of(
                "BusinessRuleViolation",
                ex.getMessage(),
                statusValue, // Usa o status dinâmico (400 ou 404)
                request.getRequestURI(),
                details
        );

        return ResponseEntity.status(statusValue).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErrorResponse.FieldErrorItem> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldErrorItem)
                .toList();

        ErrorResponse body = ErrorResponse.of(
                "ValidationFailed",
                "Existem campos inválidos",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                details);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        List<ErrorResponse.FieldErrorItem> details = ex.getConstraintViolations()
                .stream()
                .map(cv -> new ErrorResponse.FieldErrorItem(
                        extractProperty(cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : ""),
                        cv.getMessage()))
                .toList();

        ErrorResponse body = ErrorResponse.of(
                "ValidationFailed",
                "Existem campos inválidos",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                details);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(
            org.springframework.web.server.ResponseStatusException ex,
            HttpServletRequest request) {

        List<ErrorResponse.FieldErrorItem> details = List.of(
                new ErrorResponse.FieldErrorItem("business",
                        ex.getReason() != null ? ex.getReason() : "Regra de negócio violada")
        );

        ErrorResponse body = ErrorResponse.of(
                "BusinessRuleViolation",
                ex.getReason() != null ? ex.getReason() : "Regra de negócio violada",
                ex.getStatusCode().value(),
                request.getRequestURI(),
                details
        );

        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    private ErrorResponse.FieldErrorItem toFieldErrorItem(FieldError fe) {
        String msg = org.springframework.util.StringUtils.hasText(fe.getDefaultMessage()) ? fe.getDefaultMessage() : fe.getCode();
        return new ErrorResponse.FieldErrorItem(fe.getField(), msg);
    }

    private String extractProperty(String path) {
        return (path != null && path.contains(".")) ? path.substring(path.lastIndexOf('.') + 1) : path;
    }

}
