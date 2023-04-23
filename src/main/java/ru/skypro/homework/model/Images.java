package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

/**
 * Класс представляет модель изображения, связанного с объявлением.
 */

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String mediaType;
    @NonNull
    private String filePath;
    @NonNull
    private Long fileSize;

    /**
     * Аннотация @OneToOne указывает на связь "один к одному" между таблицами Images и Ads в базе данных,
     * что означает, что одно изображение связано с одним объявлением.
     */
    @OneToOne
    @JoinColumn(name = "ads_id")
    private Ads ads;
}
