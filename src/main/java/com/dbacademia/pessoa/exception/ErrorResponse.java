package com.dbacademia.pessoa.exception;

import java.time.OffsetDateTime;
import java.util.List;

public class ErrorResponse {
    private String error;
    private String message;
    private int status;
    private String path;
    private OffsetDateTime timestamp;
    private List<FieldErrorItem> details;

    public static class FieldErrorItem {
        private String field;
        private String message;

        public FieldErrorItem() {}
        public FieldErrorItem(String field, String message) {
            this.field = field;
            this.message = message;
        }
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static ErrorResponse of(String error, String message, int status, String path, List<FieldErrorItem> details) {
        ErrorResponse r = new ErrorResponse();
        r.error = error;
        r.message = message;
        r.status = status;
        r.path = path;
        r.timestamp = OffsetDateTime.now();
        r.details = details;
        return r;
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public String getPath() { return path; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public List<FieldErrorItem> getDetails() { return details; }

    public void setError(String error) { this.error = error; }
    public void setMessage(String message) { this.message = message; }
    public void setStatus(int status) { this.status = status; }
    public void setPath(String path) { this.path = path; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    public void setDetails(List<FieldErrorItem> details) { this.details = details; }
}