package ru.skypro.avito.dto;

import lombok.Data;

@Data
public class CreateAds {

    private String title;
    private String description;
    private Integer price;

}
