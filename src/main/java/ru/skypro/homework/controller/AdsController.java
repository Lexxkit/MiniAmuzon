package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exceptions.BadRequestException;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/ads")
public class AdsController {

    private final AdsService adsService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAds() {
        log.info("Was invoked get all ads method");
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @Operation(summary = "addAds",
            responses = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = MediaType.ALL_VALUE,
                            schema = @Schema(implementation = AdsDto.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403", content = @Content),
            @ApiResponse(responseCode = "404", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(@RequestPart("properties") CreateAdsDto createAds,
                                         @RequestPart("image") MultipartFile image
                                         ) {
        log.info("Was invoked add ad method");
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.createAds(createAds, image));
    }

    @Operation(summary = "getComments",
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.ALL_VALUE,
                            schema = @Schema(implementation = ResponseWrapperComment.class)
                    )
            ),
            @ApiResponse(responseCode = "404", content = @Content)
    })
    @GetMapping("/{ad_pk}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable(name = "ad_pk") String adPk) {
        log.info("Was invoked get all comments for ad = {} method", adPk);
        return ResponseEntity.ok(commentService.getAllCommentsForAdsWithId(getLongFromString(adPk)));
    }

    @Operation(summary = "addComments",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.ALL_VALUE,
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @PostMapping("/{ad_pk}/comments")
    public ResponseEntity<CommentDto> addComments(@PathVariable(name = "ad_pk") String adPk, @RequestBody CommentDto commentDto) {
        log.info("Was invoked add comment for ad = {} method", adPk);
        CommentDto newComment = commentService.createNewComment(getLongFromString(adPk), commentDto);
        return ResponseEntity.ok(newComment);
    }

    @Operation(summary = "getFullAd",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.ALL_VALUE,
                                    schema = @Schema(implementation = FullAdsDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getFullAd(@PathVariable int id) {
        log.info("Was invoked get full ad by id = {} method", id);
        return ResponseEntity.ok(adsService.getFullAdsById(id));
    }

    @Operation(summary = "removeAds",
            responses = {
                    @ApiResponse(responseCode = "204", content = @Content),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAds(@PathVariable int id) {
        log.info("Was invoked delete ad by id = {} method", id);
        adsService.removeAds(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "updateAds",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.ALL_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(@PathVariable int id,
                                            @RequestBody CreateAdsDto createAdsDto) {
        log.info("Was invoked update ad by id = {} method", id);
        return ResponseEntity.ok(adsService.updateAdsById(id, createAdsDto));
    }

    @Operation(summary = "getComments",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.ALL_VALUE,
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @GetMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> getComments(@PathVariable("ad_pk") String adPk,
                                                  @PathVariable int id) {
        log.info("Was invoked get ad's comment by id = {} method", id);
        return ResponseEntity.ok(commentService.getComments(getLongFromString(adPk), id));
    }

    @Operation(summary = "deleteComments",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @DeleteMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<Void> deleteComments(@PathVariable("ad_pk") String adPk,
                                               @PathVariable int id) {
        log.info("Was invoked delete ad's comment by id = {} method", id);
        commentService.deleteComments(getLongFromString(adPk), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "updateComments",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.ALL_VALUE,
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @PatchMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> updateComments(@PathVariable("ad_pk") String adPk,
                                                     @PathVariable int id,
                                                     @RequestBody CommentDto commentDto) {
        log.info("Was invoked update ad's = {} comment by id = {} method", adPk, id);
        return ResponseEntity.ok(commentService.updateComments(getLongFromString(adPk), id, commentDto));
    }


    @Operation(summary = "getAdsMe",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.ALL_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperAds.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe(@RequestParam(value = "authenticated", required = false) Boolean authenticated,
                                                       @RequestParam(value = "authorities[0].authority", required = false) String authority,
                                                       @RequestParam(value = "credentials", required = false) Object credentials,
                                                       @RequestParam(value = "details", required = false) Object details,
                                                       @RequestParam(value = "principal", required = false) Object principal) {
        log.info("Was invoked get all ads for current user = {} method", principal);
        // TODO: Получить инфо о авторизованном пользователе и передать в сервис вместо authority
        return ResponseEntity.ok(adsService.getAllAdsForUser(authority));
    }

    private Long getLongFromString(String adPk) {
        try {
            return Long.parseLong(adPk);
        } catch (NumberFormatException e) {
            log.warn("String '{}' couldn't be parsed to type Long", adPk);
            throw new BadRequestException();
        }
    }
}
