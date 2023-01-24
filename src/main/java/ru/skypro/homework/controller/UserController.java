package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/set_password")
    public ResponseEntity<NewPasswordDto> setPassword(@RequestParam(value = "currentPassword", required = false) String currentPassword,
                                                      @RequestParam(value = "newPassword", required = false) String newPassword ) {
        log.info("Was invoked set password for user method");
        return ResponseEntity.ok(new NewPasswordDto());
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperUserDto> getUsers() {
        log.info("Was invoked get all users method");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto) {
        log.info("Was invoked update user method");
        return ResponseEntity.ok(userService.updateUser(dto));
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestBody MultipartFile image) {
        log.info("Was invoked update user image method");
        String username = null; //Change this with username from authorization
        userService.updateUserImage(username, image);
        return ResponseEntity.ok().build();
    }

}
