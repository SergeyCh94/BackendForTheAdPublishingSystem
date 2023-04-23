package ru.skypro.homework.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.impl.AdsServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.ConstantsForTests.*;

@ExtendWith(MockitoExtension.class)
class AdsServiceImplTest {
    @Mock
    private AdsRepository adsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Spy
    private Authentication authentication;
    @Spy
    private ImageService imageService;
    @Spy
    private AdsMapper adsMapper;

    @InjectMocks
    private AdsServiceImpl out;

    /**
     * Этот тест проверяет метод saveAds() класса AdsService.
     * В нем используется Mockito для создания мок-объектов и настройки их поведения.
     * Затем вызывается метод saveAds() с определенными аргументами, и производится проверка,
     * что определенные методы были вызваны ожидаемое количество раз с определенными аргументами.
     * @throws IOException
     */
    @Test
    void saveAds() throws IOException {
        when(adsRepository.save(any())).thenReturn(ADS_1);
        when(userRepository.findByUsername(any())).thenReturn(USER_1);
        when(adsMapper.createAdsDtoToAds(any())).thenReturn(ADS_1);
        when(adsMapper.toAdsDto(any())).thenReturn(ADS_DTO_1);
        when(authentication.getName()).thenReturn(USER_NAME_1);
        when(imageService.getImagesByAds(any())).thenReturn(IMAGES);
        out.saveAds(CREATE_ADS_DTO, any(), authentication);
        verify(adsRepository, times(2)).save(any());
        verify(userRepository, times(1)).findByUsername(any());
    }

    /**
     * Этот тест проверяет метод getAllAds() класса AdsService.
     * В нем используется Mockito для создания мок-объектов и настройки их поведения.
     * Затем вызывается метод getAllAds(), и производится проверка, что возвращенные значения соответствуют ожидаемым значениям.
     */
    @Test
    void getAllAds() {
        List<Ads> adsList = new ArrayList<>(List.of(ADS_1, ADS_2));
        List<AdsDto> adsDtoList = new ArrayList<>(List.of(ADS_DTO_1, ADS_DTO_2));
        when(adsRepository.findAll()).thenReturn(adsList);
        when(adsMapper.listAdsToListAdsDto(anyList())).thenReturn(adsDtoList);
        ResponseWrapperAdsDto wrapper = out.getAllAds().getBody();
        assert wrapper != null;
        assertEquals(2, wrapper.getCount());
        assertEquals(adsDtoList, wrapper.getResults());
    }

    /**
     *  Этот тест проверяет метод removeAdsById() класса AdsService.
     *  В нем используется Mockito для создания мок-объектов и настройки их поведения.
     *  Затем вызывается метод removeAdsById() с определенными аргументами, и производится проверка,
     *  что определенные методы были вызваны ожидаемое количество раз с определенными аргументами.
     */
    @Test
    void removeAdsById() {
        when(adsRepository.findById(any())).thenReturn(Optional.of(ADS_1));
        when(commentRepository.findByAds(any())).thenReturn(null);
        when(adsMapper.toAdsDto(any())).thenReturn(ADS_DTO_1);
        out.removeAdsById(1L);
        verify(adsRepository, times(1)).findById(any());
        verify(imageService, times(1)).removeImagesByAds(any());
        verify(commentRepository, times(1)).findByAds(any());
        verify(commentRepository, times(1)).deleteAll(any());
        verify(adsRepository, times(1)).deleteById(any());
        verify(adsMapper, times(1)).toAdsDto(any());
    }

    /**
     * Этот тест проверяет метод getAdsById() класса AdsService.
     * В нем используется Mockito для создания мок-объектов и настройки их поведения.
     * Затем вызывается метод getAdsById() с определенными аргументами, и производится проверка,
     * что определенные методы были вызваны ожидаемое количество раз с определенными аргументами.
     */
    @Test
    void getAdsById() {
        when(adsRepository.findById(any())).thenReturn(Optional.of(ADS_1));
        out.getAdsById(1L);
        verify(adsRepository, times(1)).findById(any());
    }

    /**
     * Этот тест проверяет метод updateAds() класса AdsService.
     * В нем используется Mockito для создания мок-объектов и настройки их поведения.
     * Затем вызывается метод updateAds() с определенными аргументами, и производится проверка,
     * что возвращенное значение соответствует ожидаемому значению.
     */
    @Test
    void updateAds() {
        when(adsMapper.toAds(any())).thenReturn(ADS_1);
        when(adsRepository.findById(any())).thenReturn(Optional.of(ADS_2));
        when(adsRepository.save(any())).thenReturn(ADS_1);
        when(adsMapper.toAdsDto(any())).thenReturn(ADS_DTO_1);
        AdsDto adsDto = out.updateAds(2L, ADS_DTO_1).getBody();
        assert adsDto != null;
        assertEquals(ADS_1.getPrice(), adsDto.getPrice());
    }

    /**
     * Этот тест проверяет метод getAdsMe() класса AdsService.
     * В нем используется Mockito для создания мок-объектов и настройки их поведения.
     * Затем вызывается метод getAdsMe() с определенными аргументами, и производится проверка,
     * что возвращенные значения соответствуют ожидаемым значениям.
     */
    @Test
    void getAdsMe() {
        List<Ads> adsList = new ArrayList<>(List.of(ADS_1, ADS_2));
        List<AdsDto> adsDtoList = new ArrayList<>(List.of(ADS_DTO_1, ADS_DTO_2));
        when(userRepository.findByUsername(any())).thenReturn(USER_1);
        when(authentication.getName()).thenReturn(USER_NAME_1);
        when(adsRepository.findByUsers(USER_1)).thenReturn(adsList);
        when(adsMapper.listAdsToListAdsDto(anyList())).thenReturn(adsDtoList);
        ResponseWrapperAdsDto wrapper = out.getAdsMe(authentication).getBody();
        assert wrapper != null;
        assertEquals(2, wrapper.getCount());
        assertEquals(adsDtoList, wrapper.getResults());
    }

    /**
     * Этот тест проверяет метод updateAdsImage() класса AdsService.
     * В нем используется Mockito для создания мок-объектов и настройки их поведения.
     * Затем вызывается метод updateAdsImage() с определенными аргументами, и производится проверка,
     * что определенные методы были вызваны ожидаемое количество раз с определенными аргумент
     * @throws IOException
     */
    @Test
    void updateAdsImage() throws IOException {
        when(adsRepository.findById(any())).thenReturn(Optional.of(ADS_1));
        when(imageService.getImagesByAds(any())).thenReturn(IMAGES);
        out.updateAdsImage(1L, any());
        verify(adsRepository, times(1)).findById(any());
        verify(adsRepository, times(1)).save(any());
    }
}