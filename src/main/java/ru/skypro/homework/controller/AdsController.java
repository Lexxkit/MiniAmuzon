package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

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
    public ResponseEntity<Ads> addAds(@RequestBody CreateAds createAds) { /// TODO: 10.01.2023 дополнить сохранением изображения!!!
        return ResponseEntity.status(HttpStatus.CREATED).body(new Ads());
    }

    @GetMapping("/{ad_pk}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable(name = "ad_pk") String adPk) {
        return ResponseEntity.ok(new ResponseWrapperComment());
    }

    @PostMapping("/{ad_pk}/comments")
    public ResponseEntity<Comment> addComments(@PathVariable(name = "ad_pk") String adPk, @RequestBody Comment comment) {
        return ResponseEntity.ok(new Comment());
    }

    // TODO: 10.01.2023 GET getFullAd /ads/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAds(@PathVariable int id) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ads> updateAds(@PathVariable int id,
                                         @RequestBody CreateAds createAds) {
        return ResponseEntity.ok(new Ads());
    }

    // TODO: 10.01.2023 GET getComments /ads/{ad_pk}/comments/{id}
    // TODO: 10.01.2023 DELETE deleteComments /ads/{ad_pk}/comments/{id}
    // TODO: 10.01.2023 PATCH updateComments /ads/{ad_pk}/comments/{id}


    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe(@RequestParam(value = "authenticated", required = false) Boolean authenticated,
                                                       @RequestParam(value = "authorities[0].authority", required = false) String authority,
                                                       @RequestParam(value = "credentials", required = false) Object credentials,
                                                       @RequestParam(value = "details", required = false) Object details,
                                                       @RequestParam(value = "principal", required = false) Object principal) {

        return ResponseEntity.ok(new ResponseWrapperAds());
    }
}
