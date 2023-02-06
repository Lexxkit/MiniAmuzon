package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.CommentMapperImpl;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    @Mock
    private AdsService adsService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Spy
    private CommentMapper commentMapper = new CommentMapperImpl();
    @InjectMocks
    private CommentServiceImpl out;

    private Ads testAds;
    private Comment testComment;
    private CommentDto testCommentDto;
    private User testUser;
    private Authentication auth;

    @BeforeEach
    void init() {
        testUser = new User();
        testUser.setId(42L);
        testUser.setUsername("test@test.com");
        auth = new UsernamePasswordAuthenticationToken(testUser, null);

        testAds = new Ads();
        testAds.setId(1L);
        testAds.setPrice(new BigDecimal(10));
        testAds.setTitle("Test ads");

        testComment = new Comment();
        testComment.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1, 1));
        testComment.setText("Test comment");
        testComment.setId(1L);

        testAds.setComments(List.of(testComment));

        testCommentDto = commentMapper.commentToCommentDto(testComment);
    }

    @Test
    void shouldReturnResponseWrapperCommentWithAllCommentsForAd_whenGetAllCommentsForAdsWithId() {
        when(adsService.getAdsById(anyLong())).thenReturn(testAds);

        ResponseWrapperComment result = out.getAllCommentsForAdsWithId(testAds.getId());

        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(testAds.getComments().size());
        assertThat(result.getResults().contains(commentMapper.commentToCommentDto(testComment))).isTrue();
    }

    @Test
    void shouldReturnCommentDto_WhenCreateNewComment() {
        when(adsService.getAdsById(anyLong())).thenReturn(testAds);
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(userService.getUser(any(String.class))).thenReturn(null);

        CommentDto result = out.createNewComment(testAds.getId(), testCommentDto, auth);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo(testCommentDto.getText());
        assertThat(result.getCreatedAt()).isEqualTo(testCommentDto.getCreatedAt());
        assertThat(result.getPk()).isEqualTo(testCommentDto.getPk());
    }

    @Test
    void shouldReturnCommentDto_WhenGetCommentsWithIdAndAuthorId() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        CommentDto result = out.getComments(testComment.getId(), testUser.getId());

        assertThat(result).isNotNull();
        assertThat(result.getPk()).isEqualTo(testCommentDto.getPk());
        assertThat(result.getCreatedAt()).isEqualTo(testCommentDto.getCreatedAt());
    }

    @Test
    void shouldThrowCommentNotFoundException_WhenGetCommentsWithWrongIdAndAuthorId() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(CommentNotFoundException.class)
                .isThrownBy(() -> out.getComments(testComment.getId(), testUser.getId()));
    }
}
