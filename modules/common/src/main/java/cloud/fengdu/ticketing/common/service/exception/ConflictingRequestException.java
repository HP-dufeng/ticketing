package cloud.fengdu.ticketing.common.service.exception;


import java.util.Map;


public class ConflictingRequestException extends BaseModelerRestException {

    private static final long serialVersionUID = 1L;

    public ConflictingRequestException(String s) {
        super(s);
    }

    public ConflictingRequestException(String message, String messageKey) {
        this(message);
        setMessageKey(messageKey);
    }

    public ConflictingRequestException(String message, String messageKey, Map<String, String> errors) {
        this(message, messageKey);
        this.errors = errors;
    }

}