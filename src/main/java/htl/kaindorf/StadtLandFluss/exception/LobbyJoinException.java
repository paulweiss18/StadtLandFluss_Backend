package htl.kaindorf.StadtLandFluss.exception;


/**
 * Custo Exception Handling for Players Joining a Lobby
 */
public class LobbyJoinException extends RuntimeException{
    private String message;

    public LobbyJoinException(String message){
        super(message);
    }

    public LobbyJoinException(String message, Throwable ex){
        super(message, ex);
    }

}
