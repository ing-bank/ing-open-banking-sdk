package com.ing.developer.app.common;

import com.ing.developer.common.exceptions.http.OpenBankingHttpBadRequestException;
import com.ing.developer.common.exceptions.http.OpenBankingHttpForbiddenException;
import com.ing.developer.common.exceptions.http.OpenBankingHttpNotFoundException;
import com.ing.developer.common.exceptions.http.OpenBankingHttpUnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericControllerAdvice {

    @ExceptionHandler(OpenBankingHttpBadRequestException.class)
    public ResponseEntity<String> handleOpenBankingHttpBadRequestException(OpenBankingHttpBadRequestException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OpenBankingHttpForbiddenException.class)
    public ResponseEntity<String> handleOpenBankingHttpForbiddenException(OpenBankingHttpForbiddenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(OpenBankingHttpUnauthorizedException.class)
    public ResponseEntity<String> handleOpenBankingHttpUnauthorizedException(OpenBankingHttpUnauthorizedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OpenBankingHttpNotFoundException.class)
    public ResponseEntity<String> handleOpenBankingHttpNotFoundException(OpenBankingHttpNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
