package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.skypro.homework.enums.Role;

import javax.persistence.*;
import java.util.List;

/**
 * Класс представляет модель пользователя.
 */

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    @NonNull
    private Role role;
    @NonNull
    private String password;
    private Boolean enabled;

    /**
     * Аннотация @OneToMany указывает на связь "один ко многим" между таблицами Users и Ads и Comment в базе данных,
     * что означает, что один пользователь может иметь множество объявлений и комментариев
     */
    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Ads> ads;

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Comment> comments;

    /**
     * Аннотация @OneToOne указывает на связь "один к одному" между таблицами Users и Avatar в базе данных,
     * что означает, что один пользователь имеет одну аватарку.
     */
    @OneToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    private String image;
}
