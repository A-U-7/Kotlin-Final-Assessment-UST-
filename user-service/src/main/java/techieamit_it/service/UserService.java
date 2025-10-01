package techieamit_it.service;


import techieamit_it.entity.Users;
import techieamit_it.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetAllUsers")
    public List<Users> getAllUsers() {
        kafkaTemplate.send("user-events", "Fetching all users");
        return userRepository.findAll();
    }
    
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserById")
    public Optional<Users> getUserById(Long id) {
        kafkaTemplate.send("user-events", "Fetching user with id: " + id);
        return userRepository.findById(id);
    }
    
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCreateUser")
    public Users createUser(Users user) {
        Users savedUser = userRepository.save(user);
        kafkaTemplate.send("user-events", "Created user: " + savedUser.getName());
        return savedUser;
    }
    
    public Users updateUser(Long id, Users user) {
        user.setId(id);
        Users updatedUser = userRepository.save(user);
        kafkaTemplate.send("user-events", "Updated user: " + updatedUser.getName());
        return updatedUser;
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        kafkaTemplate.send("user-events", "Deleted user with id: " + id);
    }
    
    // Fallback methods
    public List<Users> fallbackGetAllUsers(Throwable t) {
        kafkaTemplate.send("user-events", "Fallback: Error fetching users");
        return List.of();
    }
    
    public Optional<Users> fallbackGetUserById(Long id, Throwable t) {
        kafkaTemplate.send("user-events", "Fallback: Error fetching user with id: " + id);
        return Optional.empty();
    }
    
    public Users fallbackCreateUser(Users user, Throwable t) {
        kafkaTemplate.send("user-events", "Fallback: Error creating user");
        return new Users("Fallback", "fallback@example.com", "Fallback Dept");
    }
}