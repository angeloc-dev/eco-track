package unito.tweb.ecotrackspring.session;

import unito.tweb.ecotrackspring.persistence.User;

public record UserLogin(SessionData sessionData, User user) {
}
