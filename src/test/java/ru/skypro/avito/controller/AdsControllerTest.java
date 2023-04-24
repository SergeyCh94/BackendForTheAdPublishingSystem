package ru.skypro.avito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.avito.dto.CreateAds;
import ru.skypro.avito.enums.Role;
import ru.skypro.avito.model.Ads;
import ru.skypro.avito.model.Image;
import ru.skypro.avito.model.User;
import ru.skypro.avito.repository.AdsRepository;
import ru.skypro.avito.repository.ImageRepository;
import ru.skypro.avito.repository.UserRepository;
import ru.skypro.avito.service.CustomUserDetailsService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private ImageRepository imageRepository;

    private Authentication auth;
    private final MockPart imageFile
            = new MockPart("image", "image", "image".getBytes());
    private final User user = new User();
    private final CreateAds createAds = new CreateAds();
    private final Ads ads = new Ads();
    private final Image image = new Image();


    @BeforeEach
    void setUp() {
        user.setUsername("username@mail.ru");
        user.setFirstName("User");
        user.setLastName("Test");
        user.setPhone("+79609279284");
        user.setPassword(encoder.encode("password"));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                                                        userDetails.getPassword(),
                                                        userDetails.getAuthorities());

        ads.setTitle("Ads");
        ads.setDescription("description");
        ads.setPrice(1000);
        ads.setAuthor(user);
        adsRepository.save(ads);
    }

    @AfterEach
    void cleanUp() {
        userRepository.delete(user);
    }

    /**
     * Данный тест testGetAllAdsReturnsCorrectAdsList() представляет собой интеграционный тест для эндпоинта /ads методом HTTP GET,
     * который ожидает корректный список объявлений.
     * @throws Exception
     */
    @Test
    public void testGetAllAdsReturnsCorrectAdsList() throws Exception {
        // С помощью метода mockMvc.perform(get("/ads")) выполняется HTTP GET запрос на эндпоинт /ads через mockMvc,
        // который представляет объект для выполнения тестов на контроллерах Spring MVC.
        // С помощью метода andExpect(status().isOk()) проверяется, что HTTP-ответ имеет статус 200 OK, что означает успешное выполнение запроса.
        // С помощью метода andExpect(jsonPath("$").exists()) проверяется, что в JSON-ответе есть корневой элемент $.
        // С помощью метода andExpect(jsonPath("$.count").isNumber()) проверяется, что в JSON-ответе есть числовое значение для ключа count.
        // С помощью метода andExpect(jsonPath("$.results").isArray()) проверяется, что в JSON-ответе есть массив значений для ключа results.
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray());
    }

    /**
     * Данный тест testAddAdsReturnCorrectAddedAdsFromDatabase() представляет собой интеграционный тест для эндпоинта /ads методом HTTP POST,
     * который проверяет корректное добавление объявления в базу данных и возвращение соответствующего JSON-ответа.
     * @throws Exception
     */
    @Test
    public void testAddAdsReturnCorrectAddedAdsFromDatabase() throws Exception {

        // Создается объект createAds с заполненными полями title, description, и price, которые представляют данные для создания объявления.

        // Создается объект MockPart с именем properties, содержащим сериализованный в байты объект createAds.
        // Этот объект будет передан в теле HTTP POST запроса на эндпоинт /ads вместе с изображением (imageFile) и аутентификационными данными (auth).

        // С помощью метода mockMvc.perform(multipart("/ads").part(imageFile).part(created).with(authentication(auth)))
        // выполняется HTTP POST запрос на эндпоинт /ads через mockMvc, содержащий загружаемое изображение (imageFile),
        // объект MockPart с данными объявления (created), и аутентификационные данные (auth).

        // С помощью метода andExpect(status().isCreated()) проверяется, что HTTP-ответ имеет статус 201 Created, что означает успешное создание ресурса.
        // С помощью метода andExpect(jsonPath("$.pk").isNotEmpty()) проверяется, что в JSON-ответе есть не пустое значение для ключа pk,
        // который представляет идентификатор созданного объявления.
        // С помощью метода andExpect(jsonPath("$.pk").isNumber()) проверяется, что в JSON-ответе значение для ключа pk является числом.
        // С помощью метода andExpect(jsonPath("$.title").value(createAds.getTitle())) проверяется,
        // что в JSON-ответе значение для ключа title соответствует значению createAds.getTitle().
        // С помощью метода andExpect(jsonPath("$.description").value(createAds.getDescription())) проверяется,
        // что в JSON-ответе значение для ключа description соответствует значению createAds.getDescription().
        // С помощью метода andExpect(jsonPath("$.price").value(createAds.getPrice())) проверяется,
        // что в JSON-ответе значение для ключа price соответствует значению createAds.getPrice().
        createAds.setTitle("Ads");
        createAds.setDescription("description");
        createAds.setPrice(1000);
        MockPart created = new MockPart("properties", objectMapper.writeValueAsBytes(createAds));

        mockMvc.perform(multipart("/ads")
                        .part(imageFile)
                        .part(created)
                        .with(authentication(auth)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.title").value(createAds.getTitle()))
                .andExpect(jsonPath("$.description").value(createAds.getDescription()))
                .andExpect(jsonPath("$.price").value(createAds.getPrice()));
    }

    /**
     * Данный тест testGetFullAddReturnsCorrectAds() представляет собой интеграционный тест для эндпоинта /ads/{id} методом HTTP GET,
     * который проверяет корректное получение полного объявления по его идентификатору из базы данных и возвращение соответствующего JSON-ответа.
     * @throws Exception
     */
    @Test
    public void testGetFullAddReturnsCorrectAds() throws Exception {

        // С помощью метода mockMvc.perform(get("/ads/{id}", ads.getId()).with(authentication(auth))) выполняется HTTP GET запрос
        // на эндпоинт /ads/{id} с указанием идентификатора объявления (ads.getId()) и аутентификационных данных (auth) через mockMvc.

        // С помощью метода andExpect(status().isOk()) проверяется, что HTTP-ответ имеет статус 200 OK, что означает успешное получение ресурса.
        // С помощью метода andExpect(jsonPath("$.pk").value(ads.getId())) проверяется, что в JSON-ответе значение для ключа pk
        // соответствует идентификатору объявления (ads.getId()).
        // С помощью метода andExpect(jsonPath("$.title").value(ads.getTitle())) проверяется,
        // что в JSON-ответе значение для ключа title соответствует значению ads.getTitle().
        // С помощью метода andExpect(jsonPath("$.description").value(ads.getDescription())) проверяется, что в JSON-ответе значение для ключа description соответствует значению ads.getDescription().
        // С помощью метода andExpect(jsonPath("$.price").value(ads.getPrice())) проверяется,
        // что в JSON-ответе значение для ключа price соответствует значению ads.getPrice().
        // С помощью метода andExpect(jsonPath("$.email").value(user.getUsername())) проверяется,
        // что в JSON-ответе значение для ключа email соответствует значению user.getUsername().
        // С помощью метода andExpect(jsonPath("$.authorFirstName").value(user.getFirstName())) проверяется,
        // что в JSON-ответе значение для ключа authorFirstName соответствует значению user.getFirstName().
        // С помощью метода andExpect(jsonPath("$.authorLastName").value(user.getLastName())) проверяется,
        // что в JSON-ответе значение для ключа authorLastName соответствует значению user.getLastName().
        // С помощью метода andExpect(jsonPath("$.phone").value(user.getPhone())) проверяется,
        // что в JSON-ответе значение для ключа phone соответствует значению user.getPhone().
        mockMvc.perform(get("/ads/{id}", ads.getId())
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(ads.getId()))
                .andExpect(jsonPath("$.title").value(ads.getTitle()))
                .andExpect(jsonPath("$.description").value(ads.getDescription()))
                .andExpect(jsonPath("$.price").value(ads.getPrice()))
                .andExpect(jsonPath("$.email").value(user.getUsername()))
                .andExpect(jsonPath("$.authorFirstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(user.getLastName()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()));
    }

    /**
     * Данный тест testRemoveAdsReturnsOkWhenAdsRemoved() представляет собой интеграционный тест для эндпоинта /ads/{id} методом HTTP DELETE,
     * который проверяет корректное удаление объявления по его идентификатору из базы данных и возвращение статуса 200 OK.
     * @throws Exception
     */
    @Test
    public void testRemoveAdsReturnsOkWhenAdsRemoved() throws Exception {

        // С помощью метода mockMvc.perform(delete("/ads/{id}", ads.getId()).with(authentication(auth))) выполняется HTTP DELETE запрос
        // на эндпоинт /ads/{id} с указанием идентификатора объявления (ads.getId()) и аутентификационных данных (auth) через mockMvc.

        // С помощью метода andExpect(status().isOk()) проверяется, что HTTP-ответ имеет статус 200 OK, что означает успешное удаление ресурса.
        mockMvc.perform(delete("/ads/{id}", ads.getId())
                        .with(authentication(auth)))
                .andExpect(status().isOk());
    }

    /**
     * Данный тест testUpdateAdsReturnsUpdatedAds() представляет собой интеграционный тест для эндпоинта /ads/{id} методом HTTP PATCH,
     * который проверяет корректное обновление объявления в базе данных и возвращение обновленных данных объявления в ответе.
     * @throws Exception
     */
    @Test
    public void testUpdateAdsReturnsUpdatedAds() throws Exception {

        // Создается новое значение для полей объявления (newTitle, newDesc, newPrice).

        // С помощью метода adsRepository.save(ads) сохраняются изменения в объявлении в базе данных.

        // С помощью метода mockMvc.perform(patch("/ads/{id}", ads.getId())...) выполняется HTTP PATCH запрос
        // на эндпоинт /ads/{id} с указанием идентификатора объявления (ads.getId()) и аутентификационных данных (auth) через mockMvc,
        // а также передается обновленное значение объявления в теле запроса в формате JSON с использованием objectMapper.

        // С помощью метода andExpect(status().isOk()) проверяется, что HTTP-ответ имеет статус 200 OK, что означает успешное выполнение запроса.
        // С помощью методов jsonPath("$.title").value(newTitle), jsonPath("$.description").value(newDesc), jsonPath("$.price").value(newPrice) проверяется,
        // что значения полей title, description, price в ответе соответствуют ожидаемым значениям (newTitle, newDesc, newPrice).
        String newTitle = "New Ads";
        String newDesc = "New Description";
        Integer newPrice = 2000;
        ads.setTitle(newTitle);
        ads.setDescription(newDesc);
        ads.setPrice(newPrice);
        adsRepository.save(ads);

        mockMvc.perform(patch("/ads/{id}", ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ads))
                        .with((authentication(auth))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.description").value(newDesc))
                .andExpect(jsonPath("$.price").value(newPrice));
    }

    /**
     * Данный тест testGetAdsMeReturnsCorrectAdsList() представляет собой интеграционный тест для эндпоинта /ads/me методом HTTP GET,
     * который проверяет корректное получение списка объявлений текущего пользователя (авторизованного пользователя) из базы данных.
     * @throws Exception
     */
    @Test
    public void testGetAdsMeReturnsCorrectAdsList() throws Exception {

        // С помощью метода mockMvc.perform(get("/ads/me")...) выполняется HTTP GET запрос
        // на эндпоинт /ads/me с указанием текущего пользователя (авторизованного пользователя) в качестве аутентификационных данных (auth) через mockMvc.

        // С помощью метода andExpect(status().isOk()) проверяется, что HTTP-ответ имеет статус 200 OK, что означает успешное выполнение запроса.

        // С помощью методов jsonPath("$").exists(), jsonPath("$.count").isNumber(), jsonPath("$.results").isArray() проверяется,
        // что в ответе присутствуют необходимые поля (count, results) и их значения соответствуют ожидаемым значениям.
        mockMvc.perform(get("/ads/me")
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray());
    }

    /**
     * Данный тест testUpdateAdsImage() представляет собой интеграционный тест для эндпоинта /ads/{id}/image методом HTTP PATCH,
     * который проверяет корректное обновление изображения для объявления в базе данных.
     * @throws Exception
     */
    @Test
    public void testUpdateAdsImage() throws Exception {

        // С помощью метода mockMvc.perform(patch("/ads/{id}/image", ads.getId())...) выполняется HTTP PATCH запрос
        // на эндпоинт /ads/{id}/image с указанием идентификатора объявления (ads.getId()) и содержимого изображения (imageFile) в качестве аргументов.

        // С помощью метода contentType(MediaType.MULTIPART_FORM_DATA_VALUE) указывается тип контента как multipart/form-data,
        // так как передается файл изображения.

        // С помощью метода with(request -> {...}) добавляется содержимое изображения в запрос в виде Part объекта.
        // С помощью метода with(authentication(auth)) указываются аутентификационные данные текущего пользователя (авторизованного пользователя).
        // С помощью метода andExpect(status().isOk()) проверяется, что HTTP-ответ имеет статус 200 OK, что означает успешное выполнение запроса.
        mockMvc.perform(patch("/ads/{id}/image", ads.getId())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.addPart(imageFile);
                            return request;
                        })
                        .with(authentication(auth)))
                .andExpect(status().isOk());
    }

    /**
     * Данный тест testGetImage() представляет собой интеграционный тест для эндпоинта /ads/image/{id} методом HTTP GET,
     * который проверяет корректное получение изображения для объявления из базы данных.
     * @throws Exception
     */
    @Test
    public void testGetImage() throws Exception {

        // С помощью метода image.setData("image".getBytes()) устанавливается содержимое изображения в виде массива байтов.
        // С помощью метода image.setMediaType("image/jpeg") устанавливается MIME-тип изображения.
        // С помощью метода imageRepository.save(image) сохраняется изображение в базе данных.
        // С помощью метода ads.setImage(image) устанавливается изображение для объявления.
        // С помощью метода adsRepository.save(ads) сохраняется объявление в базе данных.
        // С помощью метода mockMvc.perform(get("/ads/image/{id}", image.getId())...) выполняется HTTP GET запрос
        // на эндпоинт /ads/image/{id} с указанием идентификатора изображения (image.getId()) в качестве аргумента.
        // С помощью метода contentType(MediaType.MULTIPART_FORM_DATA_VALUE) указывается тип контента как multipart/form-data,
        // так как в ответе ожидается изображение.
        // С помощью метода with(authentication(auth)) указываются аутентификационные данные текущего пользователя (авторизованного пользователя).
        // С помощью метода andExpect(status().isOk()) проверяется, что HTTP-ответ имеет статус 200 OK, что означает успешное выполнение запроса.
        // С помощью метода andExpect(content().bytes(image.getData())) проверяется,
        // что содержимое полученного изображения соответствует ожидаемому содержимому изображения, которое было сохранено в базе данных.
        image.setData("image".getBytes());
        image.setMediaType("image/jpeg");
        imageRepository.save(image);
        ads.setImage(image);
        adsRepository.save(ads);

        mockMvc.perform(get("/ads/image/{id}", image.getId())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(image.getData()));
    }


}
