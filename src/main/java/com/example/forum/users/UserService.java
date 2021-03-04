package com.example.forum.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers() {

        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/user")
    public ResponseEntity getUser(@RequestParam String user) {

        Optional<User> userFromDb = userRepository.findByUser(user);

        if (!userFromDb.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(userFromDb);
    }

    @GetMapping("/login")
    public ResponseEntity getUser(@RequestParam String user, @RequestParam String password) {

        Optional<User> userFromDb = userRepository.findByUser(user);

        if (!userFromDb.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (userFromDb.get().getPassword().equals(password)) return ResponseEntity.ok(userFromDb);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        Optional<User> userFromDb = userRepository.findByUser(user.getUser());

        if (userFromDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok("The user has been saved to the database.");
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {

        Optional<User> userFromDb = userRepository.findById(id);


        if (!userFromDb.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        userRepository.deleteById(id);

        return ResponseEntity.ok("The user has been deleted.");
    }


}
