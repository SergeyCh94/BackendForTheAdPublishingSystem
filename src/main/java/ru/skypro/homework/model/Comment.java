package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

/**
 * Класс представляет модель комментария пользователя к объявлению.
 */

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private Instant createdAt;
    @NonNull
    private String text;

    /**
     * Аннотация @ManyToOne указывает на связь "многие к одному" между таблицами Comment и Ads в базе данных,
     * что означает, что много комментариев могут относиться к одному объявлению.
     */
    @ManyToOne
    @JoinColumn(name = "ads_id")
    private Ads ads;

    /**
     * Аннотация @ManyToOne также указывает на связь "многие к одному" между таблицами Comment и Users в базе данных,
     * что означает, что много комментариев могут принадлежать одному пользователю.
     */
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
}
