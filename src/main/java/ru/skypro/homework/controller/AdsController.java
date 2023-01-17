package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/ads")
public class AdsController {

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAds() {
        log.info("Was invoked get all ads method");
        return ResponseEntity.ok(new ResponseWrapperAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(@RequestPart(value = "properties") CreateAdsDto createAds,
                                         @RequestPart(value = "image") MultipartFile image) {
        log.info("Was invoked add ad method");
        return ResponseEntity.status(HttpStatus.CREATED).body(new AdsDto());
    }

    @GetMapping("/{ad_pk}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable(name = "ad_pk") String adPk) {
        log.info("Was invoked get all comments for ad = {} method", adPk);
        return ResponseEntity.ok(new ResponseWrapperComment());
    }

    @PostMapping("/{ad_pk}/comments")
    public ResponseEntity<CommentDto> addComments(@PathVariable(name = "ad_pk") String adPk, @RequestBody CommentDto comment) {
        log.info("Was invoked add comment for ad = {} method", adPk);
        return ResponseEntity.ok(new CommentDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getFullAd(@PathVariable int id) {
        log.info("Was invoked get full ad by id = {} method", id);
        return ResponseEntity.ok(new FullAdsDto());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAds(@PathVariable int id) {
        log.info("Was invoked delete ad by id = {} method", id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(@PathVariable int id,
                                            @RequestBody CreateAdsDto createAdsDto) {
        log.info("Was invoked update ad by id = {} method", id);
        return ResponseEntity.ok(new AdsDto());
    }

    @GetMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> getComments(@PathVariable("ad_pk") String adPk,
                                                  @PathVariable int id) {
        log.info("Was invoked get ad's comment by id = {} method", id);
        return ResponseEntity.ok(new CommentDto());
    }

    @DeleteMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<Void> deleteComments(@PathVariable("ad_pk") String adPk,
                                               @PathVariable int id) {
        log.info("Was invoked delete ad's comment by id = {} method", id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> updateComments(@PathVariable("ad_pk") String adPk,
                                                     @PathVariable int id,
                                                     @RequestBody CommentDto commentDto) {
        log.info("Was invoked update ad's = {} comment by id = {} method", adPk, id);
        return ResponseEntity.ok(commentDto);
    }


    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe(@RequestParam(value = "authenticated", required = false) Boolean authenticated,
                                                       @RequestParam(value = "authorities[0].authority", required = false) String authority,
                                                       @RequestParam(value = "credentials", required = false) Object credentials,
                                                       @RequestParam(value = "details", required = false) Object details,
                                                       @RequestParam(value = "principal", required = false) Object principal) {
        log.info("Was invoked get all ads for current user = {} method", principal);
        return ResponseEntity.ok(new ResponseWrapperAds());
    }
}
