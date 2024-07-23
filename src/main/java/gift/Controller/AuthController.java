package gift.Controller;

import gift.Entity.UserEntity;
import gift.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Page<UserEntity> getAllUsers(Pageable pageable) {
        return userService.findAllUsers((org.springframework.data.domain.Pageable) pageable);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return userService.findUserByIdResponse(id);
    }

    @PostMapping("/users")
    public UserEntity createUser(@RequestBody UserEntity userEntity) {
        return userService.saveUser(userEntity);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
