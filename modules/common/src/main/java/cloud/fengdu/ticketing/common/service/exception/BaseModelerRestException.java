package cloud.fengdu.ticketing.common.service.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Base exception for all exceptions in the REST layer.
 * 
 * @author Feng du
 */
public class BaseModelerRestException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    protected String messageKey;
    protected Map<String, String> errors;

    public BaseModelerRestException() {
        super();
    }

    public BaseModelerRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseModelerRestException(String message) {
        super(message);
    }

    public BaseModelerRestException(Throwable cause) {
        super(cause);
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public void addErrors(String key, String message) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        errors.put(key, message);
    }
}
