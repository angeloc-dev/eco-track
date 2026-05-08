package unito.tweb.ecotrackspring.session;

public class SessionData {
    private final String username;
    private final String message;

    public SessionData(String cookieId, String message) {
        this.username = cookieId;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
