package cloud.fengdu.ticketing.common.rest.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cloud.fengdu.ticketing.common.service.exception.BaseModelerRestException;
import cloud.fengdu.ticketing.common.service.exception.ConflictingRequestException;
import cloud.fengdu.ticketing.common.service.exception.ErrorInfo;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

@ControllerAdvice
public class RestExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorInfo errorInfo = new ErrorInfo("Argument not valid");
        errorInfo.setErrors(errors);

        return errorInfo;
    }

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ExceptionHandler(ConflictingRequestException.class)
    @ResponseBody
    public ErrorInfo handleConflict(ConflictingRequestException e) {
        return createInfoFromException(e, e.getMessageKey());
    }

    protected ErrorInfo createInfoFromException(BaseModelerRestException exception, String defaultMessageKey) {
        ErrorInfo result = null;
        result = new ErrorInfo(exception.getMessage());
        if (exception.getErrors() != null) {
            result.setErrors(exception.getErrors());
        }
        if (exception.getMessageKey() != null) {
            result.setMessageKey(exception.getMessageKey());
        } else {
            result.setMessageKey(defaultMessageKey);
        }
        return result;
    }
}
