package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exceptions.EmptyFileException;
import ru.skypro.homework.exceptions.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {
    @Mock
    private ImageRepository imageRepository;
    @InjectMocks
    private ImageServiceImpl out;
    private Image testImage;
    private MultipartFile mockedFile;

    @BeforeEach
    void init() {
        testImage = new Image();
        testImage.setId(1L);
        testImage.setData(new byte[0]);
        testImage.setMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        mockedFile = mock(MultipartFile.class);
    }

    @Test
    void shouldThrowImageNotFoundException_whenGetAdsImageWithWrongId() {
        when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ImageNotFoundException.class)
                .isThrownBy(() -> out.getAdsImage(testImage.getId()));
    }

    @Test
    void shouldReturnBytesArray_whenGetAdsImage() {
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testImage));

        byte[] result = out.getAdsImage(testImage.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void shouldReturnImageInstance_whenCreateImage() {
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        Image result = out.createImage(mockedFile, new Ads());

        assertThat(result).isNotNull();
        verify(imageRepository, atMostOnce()).save(testImage);
    }

    @Test
    void shouldReturnBytesArray_whenUpdateAdsImage() throws IOException {
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testImage));
        when(mockedFile.getBytes()).thenReturn(new byte[0]);
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        byte[] result = out.updateAdsImage(anyLong(), mockedFile);

        assertThat(result).isNotNull();
        verify(imageRepository, atMostOnce()).save(testImage);
    }

    @Test
    void shouldThrowEmptyFileException_whenUseEmpty() {
        when(mockedFile.isEmpty()).thenReturn(true);

        assertThatExceptionOfType(EmptyFileException.class)
                .isThrownBy(() -> out.createImage(mockedFile, new Ads()));
    }

    @Test
    void shouldThrowIOException_whenUseBadFile() throws IOException {
        when(mockedFile.getBytes()).thenThrow(IOException.class);

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> out.createImage(mockedFile, new Ads()));
    }
}
