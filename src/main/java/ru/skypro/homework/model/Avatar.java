package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

/**
 * Класс представляет модель аватарки (изображения профиля) пользователя.
 */

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "avatars")
public class Avatar {
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
     * Аннотация @Lob указывает, что поле data представляет собой большой объект,
     * который будет храниться в базе данных как BLOB или CLOB в зависимости от используемой СУБД.
     */
    @Lob
    private byte[] data;

    /**
     * Аннотация @OneToOne указывает на связь "один к одному" между таблицами Avatar и Users в базе данных.
     */
    @OneToOne
    @JoinColumn(name = "users_id")
    private Users users;
}
