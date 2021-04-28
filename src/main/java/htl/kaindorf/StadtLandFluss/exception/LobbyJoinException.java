package htl.kaindorf.StadtLandFluss.exception;

public class LobbyJoinException extends RuntimeException{
    private String message;

    public LobbyJoinException(String message){
        super(message);
    }

    public LobbyJoinException(String message, Throwable ex){
        super(message, ex);
    }

}
