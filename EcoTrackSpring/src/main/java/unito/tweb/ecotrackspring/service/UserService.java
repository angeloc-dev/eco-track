package unito.tweb.ecotrackspring.service;

import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.UserRepo;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepository;

    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public void updateUser(Integer userId, User user){
        Optional<User> existingUser = getUserById(userId);
        user = validateUser(user);
        if (existingUser.isPresent()) {
            existingUser.get().setUsername(user.getUsername());
            existingUser.get().setName(user.getName());
            existingUser.get().setSurname(user.getSurname());
            updateUserEmail(existingUser.get().getIduser(), user.getEmail());
            existingUser.get().setPassword(user.getPassword());
            existingUser.get().setCover(user.getCover());
            existingUser.get().setAge(user.getAge());
            existingUser.get().setLevel(user.getLevel());
            existingUser.get().setXp(user.getXp());
            userRepository.save(existingUser.get());
        }
    }

    // Metodo per validare un User
    public User validateUser(User user){
        // Validazione della lunghezza del username
        if (user.getUsername() == null || user.getUsername().length() > 15) {
            throw new IllegalArgumentException("User Username must not exceed 15 characters");
        }

        // Validazione della lunghezza del nome
        if (user.getName() == null || user.getName().length() > 30) {
            throw new IllegalArgumentException("User Name must not exceed 30 characters");
        }

        // Validazione della lunghezza del cognome
        if (user.getSurname() == null || user.getSurname().length() > 30) {
            throw new IllegalArgumentException("User Surname must not exceed 30 characters");
        }

        // Validazione dell'età
        if (user.getAge() == null || user.getAge() < 14 || user.getAge() > 100 ) {
            throw new IllegalArgumentException("User Age must be between 14 and 100");
        }

        // Validazione della mail
        if (user.getEmail() == null || user.getEmail().length() > 50) {
            throw new IllegalArgumentException("User Description must not exceed 50 characters");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validazione della password
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new IllegalArgumentException("User Password must contain at least 8 characters");
        } else if (user.getPassword().length() > 30) {
            throw new IllegalArgumentException("User Password must not exceed 30 characters");
        }

        // Validazione del valore del livello
        if (user.getLevel() == null || user.getLevel() < 0 || user.getLevel() > 50) {
            throw new IllegalArgumentException("User Level must be between 0 and 50");
        }

        // Validazione del valore di XP
        if (user.getXp() < 5 || user.getXp() > 10000) {
            throw new IllegalArgumentException("User XP must be between 0 and 10000");
        }

        return user;
    }

    public void updateUserCover(Integer userId, String coverImageData) throws IOException {
        Optional<User> user = getUserById(userId);
        if (user.isEmpty()) {
            return;
        }
        // Salvare l'immagine nella directory del server
        String filePath = "/src/main/resources/static/images/users/" + user.get().getIduser() + ".png";
        Files.write(Paths.get(filePath), Base64.getDecoder().decode(coverImageData));
        // Aggiornare l'attributo cover
        user.get().setCover(filePath);
        userRepository.save(user.get());
    }

    public void updateUserEmail(Integer userId, String email) {
        Optional<User> user = getUserById(userId);
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (user.isEmpty()){
            return;
        }
        user.get().setEmail(email);
        userRepository.save(user.get());
    }

    public boolean checkCredentials(String username, String password) {
        return this.userRepository.existsByUsernameAndPassword(username, password);
    }

    public void deleteUser(Integer userId){
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public Optional<User> getUserById(Integer userId){
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User findByUsername(String username){
        if (username == null){
            throw new IllegalArgumentException("Username is null.");
        }
        return userRepository.findByUsername(username);
    }

}
