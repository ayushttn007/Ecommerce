package com.Ecomm.Ecommerce.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorFormat> GlobalExceptionHandler(Exception ex, WebRequest request) throws Exception{
        ErrorFormat errorFormat = new ErrorFormat(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorFormat, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // PasswordNotMatched Exception Handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<ErrorFormat> PasswordNotMatchedExceptionHandler(PasswordNotMatchedException ex, WebRequest request) throws  PasswordNotMatchedException{
        ErrorFormat errorFormat = new ErrorFormat(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorFormat, HttpStatus.BAD_REQUEST);
    }

    // UserAlreadyExists Exception Handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorFormat> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException ex, WebRequest request) throws  UserAlreadyExistsException{
        ErrorFormat errorFormat = new ErrorFormat(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorFormat, HttpStatus.BAD_REQUEST);
    }

    // ResourceNotFound Exception Handler
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorFormat> ResourceNotFoundExceptionHandler(ResourceNotFoundException ex, WebRequest request) throws ResourceNotFoundException {
        ErrorFormat errorFormat = new ErrorFormat(LocalDateTime.now(), ex.getMessage(), request.getDescription(false),HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorFormat, HttpStatus.NOT_FOUND);

    }

    //MethodArgumentNotValid Exception Handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> InvalidArgumentsHandler(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult() // Binding Result
                .getFieldErrors()     //List<FieldError>
                .forEach(fieldError -> {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());

                });
        return errors;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public final ResponseEntity<ErrorFormat> InvalidTokenException(ResourceNotFoundException ex, WebRequest request) throws InvalidTokenException {
        ErrorFormat errorFormat = new ErrorFormat(LocalDateTime.now(), ex.getMessage(), request.getDescription(false),HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorFormat, HttpStatus.NOT_FOUND);

    }
}
