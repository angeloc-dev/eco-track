package unito.tweb.ecotrackspring.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unito.tweb.ecotrackspring.persistence.User;
import unito.tweb.ecotrackspring.service.UserInGroupService;
import unito.tweb.ecotrackspring.service.UserService;

@RestController
@RequestMapping("/session")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LoginController {
    private final UserService userService;
    private final UserInGroupService userInGroupService;

    LoginController(UserService userService, UserInGroupService userInGroupService) {
        this.userService = userService;
        this.userInGroupService = userInGroupService;
    }

    @GetMapping("/login")
    public ResponseEntity<SessionData> isLogged(HttpSession session,
                                                @RequestParam(required = true) String username) {
        String existingUser = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.badRequest().body(new SessionData("",
                    "Bad request: missing username attribute."));
        }
        if (existingUser != null) {
            if (username.equals(existingUser)) {
                return ResponseEntity.ok(new SessionData(username,
                        "User actually authenticated."));
            }
            return ResponseEntity.badRequest().body(new SessionData("",
                    "Another user authenticated."));
        }
        return ResponseEntity.status(401).body(
                new SessionData("",
                        "Invalid credentials."));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLogin> login(
            HttpSession session,
            @RequestParam(required = false) String role,
            @RequestBody User user) {
        String existingUserUsername = (String) session.getAttribute("username");

        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(new UserLogin(new SessionData("",
                    "Bad credentials."), null));
        }
        if (existingUserUsername != null) {
            if (user.getUsername().equals(existingUserUsername)) {
                User existingUser = userService.findByUsername(existingUserUsername);
                if (existingUser != null) {
                    return ResponseEntity.ok(new UserLogin(new SessionData(user.getUsername(),
                            "User already authenticated."), existingUser));
                }
                return ResponseEntity.badRequest().body(new UserLogin(new SessionData("",
                        "User already authenticated & Bad credentials."), null));

            }
            return ResponseEntity.badRequest().body(new UserLogin(new SessionData("",
                    "Another user authenticated."), null));
        }
        boolean auth = userService.checkCredentials(user.getUsername(), user.getPassword());
        if (auth) {
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser != null) {
                if (role != null && role.equals("admin")) {
                    boolean authGroup = userInGroupService.isUserAdmin(existingUser);
                    if (!authGroup) {
                        return ResponseEntity.status(401).body(new UserLogin(
                                new SessionData("",
                                        "Invalid credentials: this user is not an admin of a group"),
                                null));
                    }
                }
                session.setAttribute("username", user.getUsername());
                return ResponseEntity.ok(new UserLogin(new SessionData(
                        user.getUsername(),
                        "Log in successful."),existingUser));
            }
        }
        return ResponseEntity.status(401).body(new UserLogin(
                new SessionData("",
                        "Invalid credentials."), null));
    }

    @GetMapping("/logout")
    public ResponseEntity<SessionData> invalidate(HttpSession session) {
        String existingUser = (String) session.getAttribute("username");
        if (existingUser == null) {
            return ResponseEntity.ok(new SessionData("",
                    "No user to log out."));
        }
        session.invalidate();
        return ResponseEntity.ok(new SessionData("",
                "User successfully logged out."));
    }
}
