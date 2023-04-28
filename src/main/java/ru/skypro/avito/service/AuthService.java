package ru.skypro.avito.service;


import ru.skypro.avito.dto.RegisterReq;

public interface AuthService {

    boolean login(String userName, String password);

    boolean register(RegisterReq registerReq);

}
