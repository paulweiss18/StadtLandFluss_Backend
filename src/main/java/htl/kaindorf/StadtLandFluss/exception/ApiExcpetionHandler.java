package htl.kaindorf.StadtLandFluss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Custo Exception Handling for Players Joining a Lobby
 */
@ControllerAdvice
public class ApiExcpetionHandler {

    @ExceptionHandler(value = {LobbyJoinException.class})
    public ResponseEntity<Object> handleRequestExcpetion(LobbyJoinException ex){
        //Create Payload
        ApiException apiException = new ApiException(
                ex.getMessage(),
                ex,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
