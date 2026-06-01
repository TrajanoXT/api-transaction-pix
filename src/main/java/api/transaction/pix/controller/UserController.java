package api.transaction.pix.controller;

import api.transaction.pix.dto.CreateUserRequest;
import api.transaction.pix.dto.UserResponseDto;
import api.transaction.pix.entity.User;
import api.transaction.pix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);

        return ResponseEntity.ok().body(user);
    }
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> listUsers(){
        return ResponseEntity.ok(userService.listUsers());
    }


}
