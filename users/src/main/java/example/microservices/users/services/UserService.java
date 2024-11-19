package example.microservices.users.services;

import example.microservices.users.entities.User;
import example.microservices.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String register(User user){
        Optional<User> storedUser = userRepository.findByEmail(user.getEmail());
        if (storedUser.isPresent()){
            return "User already registered with that email.";
        }
        else{
            userRepository.save(user);
            return "User registered successfully.";
        }
    }

    public List<User> findAll(){
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> login(String email, String password){
        Optional<User> storedUser = userRepository.findByEmail(email);
        if (storedUser.isPresent()){
            if(storedUser.get().getPassword().equals(password)){
                return storedUser;
            }
            else{
                return Optional.empty();
            }
        }

        return Optional.empty();

    }
}
