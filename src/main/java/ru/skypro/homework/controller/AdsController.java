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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.ImageService;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/ads")
public class AdsController {

    private final AdsService adsService;
    private final CommentService commentService;
    private final ImageService imageService;

    @Operation(summary = "getAds",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseWrapperAds.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(@RequestPart("properties") CreateAdsDto createAds,
                                         @RequestPart("image") MultipartFile image,
                                         Authentication authentication
                                         ) {
        log.info("Was invoked add ad method");
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.createAds(createAds, image, authentication));
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{ad_pk}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable(name = "ad_pk") long adPk) {
        log.info("Was invoked get all comments for ad = {} method", adPk);
        return ResponseEntity.ok(commentService.getAllCommentsForAdsWithId(adPk));
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{ad_pk}/comments")
    public ResponseEntity<CommentDto> addComments(@PathVariable(name = "ad_pk") long adPk,
                                                  @RequestBody CommentDto commentDto,
                                                  Authentication authentication) {
        log.info("Was invoked add comment for ad = {} method", adPk);
        CommentDto newComment = commentService.createNewComment(adPk, commentDto, authentication);
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAds(@PathVariable int id,
                                          Authentication authentication) {
        log.info("Was invoked delete ad by id = {} method", id);
        adsService.removeAds(id, authentication);
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
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(@PathVariable int id,
                                            @RequestBody CreateAdsDto createAdsDto,
                                            Authentication authentication) {
        log.info("Was invoked update ad by id = {} method", id);
        return ResponseEntity.ok(adsService.updateAdsById(id, createAdsDto, authentication));
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> getComments(@PathVariable("ad_pk") long adPk,
                                                  @PathVariable int id) {
        log.info("Was invoked get ad's comment by id = {} method", id);
        return ResponseEntity.ok(commentService.getComments(adPk, id));
    }

    @Operation(summary = "deleteComments",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<Void> deleteComments(@PathVariable("ad_pk") long adPk,
                                               @PathVariable int id,
                                               Authentication authentication) {
        log.info("Was invoked delete ad's comment by id = {} method", id);
        commentService.deleteComments(adPk, id, authentication);
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
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> updateComments(@PathVariable("ad_pk") long adPk,
                                                     @PathVariable int id,
                                                     @RequestBody CommentDto commentDto,
                                                     Authentication authentication) {
        log.info("Was invoked update ad's = {} comment by id = {} method", adPk, id);
        return ResponseEntity.ok(commentService.updateComments(adPk, id, commentDto, authentication));
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe(Authentication authentication) {
        log.info("Was invoked get all ads for current user = {} method", authentication.getName());
        log.info("{}", authentication.getAuthorities());
        return ResponseEntity.ok(adsService.getAllAdsForUser(authentication.getName()));
    }

    @Operation(summary = "updateAdsImage",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    schema = @Schema(implementation = byte[].class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", content = @Content)
            })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping (value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateAdsImage(@PathVariable Long id,
                                                 @RequestParam MultipartFile image,
                                                 Authentication authentication){
        log.info("Was invoked updateAdsImage method from {}", ImageController.class.getSimpleName());

        byte[] imageBytes = imageService.updateAdsImage(id, image, authentication);
        return ResponseEntity.ok(imageBytes);
    }
}
