package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {


    @PostMapping("/set_password")
    public ResponseEntity<NewPasswordDto> setPassword(@RequestParam(value = "currentPassword", required = false) String currentPassword,
                                                      @RequestParam(value = "newPassword", required = false) String newPassword ) {
        log.info("Was invoked set password for user method");
        return ResponseEntity.ok(new NewPasswordDto());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUsers() {
        log.info("Was invoked get all users method");
        return ResponseEntity.ok(new UserDto());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto) {
        log.info("Was invoked update user method");
        return ResponseEntity.ok(new UserDto());
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestBody MultipartFile image) {
        log.info("Was invoked update user image method");
        return ResponseEntity.ok().build();
    }

}
