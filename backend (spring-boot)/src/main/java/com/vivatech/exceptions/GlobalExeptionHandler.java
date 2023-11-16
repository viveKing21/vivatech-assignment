package com.vivatech.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExeptionHandler {
	@ExceptionHandler(CrewConnectException.class)
	public ResponseEntity<ErrorDetails> appException(CrewConnectException cce, WebRequest req){
		ErrorDetails errorDetails = new ErrorDetails();
		
		errorDetails.setMessage(cce.getMessage());
		errorDetails.setDescription(req.getDescription(false));
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetails> NoHandlerFound(NoHandlerFoundException se , WebRequest req){

        ErrorDetails med = new ErrorDetails();
        med.setMessage(se.getMessage());
        med.setDescription(req.getDescription(false));

        return new ResponseEntity<>(med, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> myException(MethodArgumentNotValidException se , WebRequest req){
    	String errorMessage = se.getBindingResult().getFieldError().getDefaultMessage();
    	
        ErrorDetails med = new ErrorDetails();
        med.setMessage(errorMessage);
        med.setDescription(req.getDescription(false));

        return new ResponseEntity<>(med, HttpStatus.BAD_GATEWAY) ;
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> myException(ConstraintViolationException e , WebRequest req){
    	String errorMessage = String.join(", ", e.getConstraintViolations().stream().map(violation -> violation.getMessageTemplate()).toList());
        
        ErrorDetails med = new ErrorDetails();
        med.setMessage(errorMessage);
        med.setDescription(req.getDescription(false));

        return new ResponseEntity<>(med, HttpStatus.BAD_GATEWAY) ;
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(
            DataIntegrityViolationException e, WebRequest req) {

        String errorMessage = "Duplicate data entry.";

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(errorMessage);
        errorDetails.setDescription(req.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> myException(Exception e , WebRequest req){
    	
        ErrorDetails med = new ErrorDetails();
        med.setMessage(e.getMessage());
        med.setDescription(req.getDescription(false));

        return new ResponseEntity<>(med, HttpStatus.BAD_GATEWAY) ;
    }
    // production use:
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
//        String errorMessage = "Internal Server Error: " + ex.getMessage();
//        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}