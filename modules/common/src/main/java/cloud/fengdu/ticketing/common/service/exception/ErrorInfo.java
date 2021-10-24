package cloud.fengdu.ticketing.common.service.exception;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ErrorInfo {

    private String message;
    private String messageKey;
    private List<Error> errors;

    class Error {
        String message;
        Optional<String> field;
        public Error(String message, Optional<String> field) {
            this.message = message;
            this.field = field;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public Optional<String> getField() {
            return field;
        }
        public void setField(Optional<String> field) {
            this.field = field;
        }
    }

    public ErrorInfo(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @JsonInclude(Include.NON_NULL)
    public String getMessageKey() {
        return messageKey;
    }

    @JsonInclude(Include.NON_EMPTY)
    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> params) {
        this.errors = new ArrayList<>();
        params.forEach((field, message)-> { errors.add(new Error(message, Optional.of(field))); });
    }

    public void addError(String name, String message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(new Error(message, Optional.of(name)));
    }
}

