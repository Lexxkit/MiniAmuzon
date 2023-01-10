package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {


    @PostMapping("/set_password")
    public ResponseEntity<NewPassword> setPassword(@RequestBody String currentPassword,
                                                                String newPassword ) {
        return ResponseEntity.ok(new NewPassword());
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUsers() {
        return ResponseEntity.ok(new User());
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody User dto) {
        return ResponseEntity.ok(new User());
    }

    @PatchMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestBody MultipartFile image) {
        return ResponseEntity.ok().build();
    }

}
