package ru.skypro.avito.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.avito.dto.AdsDto;
import ru.skypro.avito.dto.CreateAds;
import ru.skypro.avito.dto.FullAds;

import java.io.IOException;
import java.util.List;

public interface AdsService {

    List<AdsDto> getAllAds();

    AdsDto addAds(MultipartFile imageFiles, CreateAds createAds, Authentication authentication) throws IOException;

    FullAds getAdsById(Integer id);

    void removeAdsById(Integer id);

    AdsDto updateAds(Integer id, CreateAds createAds);

    void updateAdsImage(Integer id, MultipartFile imageFile) throws IOException;

    List<AdsDto> getAdsMe(Authentication authentication);

}

