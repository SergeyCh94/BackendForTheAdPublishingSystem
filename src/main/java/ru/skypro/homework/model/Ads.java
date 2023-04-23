package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Класс представляет модель объявления (ads) в системе.
 */

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ads")
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    @NonNull
    private Integer price;
    @NonNull
    private String title;
    private String description;

    /**
     * Аннотация @ManyToOne указывает на отношение "многие-к-одному" между таблицами ads и users,
     * где одно объявление (ads) может быть связано с одним пользователем (users).
     */
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    /**
     * Аннотация @OneToOne указывает на отношение "один-к-одному" между таблицами ads и images,
     * где одно объявление (ads) может быть связано с одним изображением (images).
     */
    @OneToOne
    @JoinColumn(name = "images_id")
    private Images images;

    /**
     * Аннотация @JsonIgnore указывает на то, что поле comments будет игнорироваться при сериализации и десериализации объектов класса Ads,
     * чтобы избежать циклических ссылок и бесконечных рекурсий.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "ads")
    private List<Comment> comments;
}
