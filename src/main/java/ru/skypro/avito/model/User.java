package ru.skypro.avito.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.avito.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phone;
    @NotNull
    private String password;
    private Boolean enabled;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Ads> ads;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments;
    @OneToOne(cascade = CascadeType.ALL)
    private Avatar avatar;

}