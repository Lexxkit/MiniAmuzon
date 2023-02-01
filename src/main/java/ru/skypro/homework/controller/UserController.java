package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
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
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        log.info("Was invoked get user by Email method");
        return ResponseEntity.ok(userService.getUserByEmail(authentication.getName()));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto,
                                              Authentication authentication) {
        log.info("Was invoked update user method");
        return ResponseEntity.ok(userService.updateUser(dto, authentication.getName()));
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestBody MultipartFile image,
                                                Authentication authentication) {
        log.info("Was invoked update user image method");
        userService.updateUserAvatar(authentication.getName(), image);
        return ResponseEntity.ok().build();
    }
}
