package example.microservices.users.controllers;

import example.microservices.users.dto.UserDTO;
import example.microservices.users.entities.User;
import example.microservices.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO){

        if (userDTO.getUsername().isBlank() || userDTO.getEmail().isBlank() || userDTO.getPassword().isBlank()){
            return ResponseEntity.badRequest().build();
        }

        User newUser = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

        return ResponseEntity.ok(userService.register(newUser));
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<UserDTO> userList = userService.findAll()
                .stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build())
                .toList();

        if(userList.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<User> optional = userService.findById(id);
        if(optional.isPresent()){
            User user = optional.get();
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();

            return ResponseEntity.ok(userDTO);
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        if (userDTO.getEmail().isBlank() || userDTO.getPassword().isBlank()){
            return ResponseEntity.badRequest().build();
        }
        Optional<User> optional = userService.login(userDTO.getEmail(), userDTO.getPassword());
        if(optional.isPresent()){
            User user = optional.get();
            UserDTO newUserDTO = UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();

            return ResponseEntity.ok(newUserDTO);
        }

        return ResponseEntity.notFound().build();
    }

}
