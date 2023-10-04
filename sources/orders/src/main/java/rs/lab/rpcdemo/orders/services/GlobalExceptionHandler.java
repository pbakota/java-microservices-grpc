package rs.lab.rpcdemo.orders.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import rs.lab.rpcdemo.common.utils.ErrorData;
import rs.lab.rpcdemo.common.utils.RpcException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RpcException.class)
    @ResponseBody
    public ResponseEntity<ErrorData> rpcExceptionHandler(RpcException e) {
        var errorData = new ErrorData(e.getMessage());
        return new ResponseEntity(errorData, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorData> exceptionHandler(Exception e) {
        var errorData = new ErrorData(e.getMessage());
        return new ResponseEntity(errorData, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}