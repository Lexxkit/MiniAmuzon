package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.ResponseWrapperAds;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/ads")
public class AdsController {

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAds() {
        return ResponseEntity.ok(new ResponseWrapperAds());
    }

    @PostMapping
    public ResponseEntity<Ads> addAds(@RequestBody CreateAds createAds) { // дополнить сохранением изображения!!!
        return ResponseEntity.status(HttpStatus.CREATED).body(new Ads());
    }
}
