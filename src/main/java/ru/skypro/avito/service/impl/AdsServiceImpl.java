package ru.skypro.avito.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.avito.dto.AdsDto;
import ru.skypro.avito.dto.CreateAds;
import ru.skypro.avito.dto.FullAds;
import ru.skypro.avito.exception.AdsNotFoundException;
import ru.skypro.avito.exception.IncorrectArgumentException;
import ru.skypro.avito.mapper.AdsMapper;
import ru.skypro.avito.model.Ads;
import ru.skypro.avito.model.Image;
import ru.skypro.avito.model.User;
import ru.skypro.avito.repository.AdsRepository;
import ru.skypro.avito.service.AdsService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final UserServiceImpl userService;
    private final AdsRepository adsRepository;
    private final ImageServiceImpl imageService;

    /**
     * Метод getAllAds() переопределяет метод из интерфейса или родительского класса и предполагает получение списка всех объявлений.
     * @return
     */
    @Override
    public List<AdsDto> getAllAds() {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug".
        log.debug("Getting all ads");

        // Используется репозиторий adsRepository для получения всех объявлений из базы данных.
        return adsRepository.findAll()
                // Результат из репозитория преобразуется в поток (Stream) объектов.
                .stream()

                // Каждый объект объявления (Ads) маппится в объект DTO (AdsDto) с использованием маппера AdsMapper.INSTANCE::toDto.
                .map(AdsMapper.INSTANCE::toDto)

                // Результаты маппинга собираются в список (List) с использованием Collectors.toList().
                .collect(Collectors.toList());

        // Список с маппированными объявлениями в виде объектов DTO возвращается как результат выполнения метода.
    }

    /**
     * Метод addAds() переопределяет метод из интерфейса или родительского класса и предполагает добавление нового объявления.
     * @param imageFile
     * @param createAds
     * @param authentication
     * @return
     * @throws IOException
     */
    @Override
    public AdsDto addAds(MultipartFile imageFile, CreateAds createAds, Authentication authentication) throws IOException {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug".
        log.debug("Adding ads");

        // Проверяется наличие обязательных полей (title, description, price) в объекте createAds.
        // Если какое-либо из полей отсутствует или пустое, выбрасывается исключение IncorrectArgumentException.
        if (createAds.getTitle() == null || createAds.getTitle().isBlank()
            || createAds.getDescription() == null || createAds.getDescription().isBlank()
            || createAds.getPrice() == null) throw new IncorrectArgumentException();

        // Используется маппер AdsMapper для преобразования объекта createAds в объект Ads (сущность объявления).
        Ads ads = AdsMapper.INSTANCE.toEntity(createAds);

        // Используется сервис userService для получения информации о текущем пользователе на основе данных аутентификации.
        User user = userService.getUserByUsername(authentication.getName());

        // Полученный пользователь устанавливается как автор для объявления.
        ads.setAuthor(user);

        // Используется сервис imageService для загрузки изображения объявления и получения объекта Image.
        Image image = imageService.uploadImage(imageFile);

        // Объект Image устанавливается в свойство image для объявления.
        ads.setImage(image);

        // Объявление сохраняется в базу данных с использованием репозитория adsRepository.
        return AdsMapper.INSTANCE.toDto(adsRepository.save(ads));

        // Результат сохранения, объект Ads, маппится в объект DTO (AdsDto) с использованием маппера AdsMapper.INSTANCE::toDto
    }

    /**
     * Метод getAdsById() предполагает получение объявления по его идентификатору.
     * @param id
     * @return
     */
    public FullAds getAdsById(Integer id) {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug", с указанием переданного идентификатора.
        log.debug("Getting ads by id: {}", id);

        // Используется маппер AdsMapper для преобразования найденного объявления, полученного с помощью метода findAdsById(id),
        // в объект FullAds (DTO с полной информацией об объявлении).
        return AdsMapper.INSTANCE.toFullAds(findAdsById(id));

        // Преобразованный объект FullAds возвращается как результат выполнения метода.
    }

    /**
     * Метод removeAdsById() предполагает удаление объявления по его идентификатору.
     * @param id
     */
    @Override
    public void removeAdsById(Integer id) {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug", с указанием переданного идентификатора.
        log.debug("Removing ads by id: {}", id);

        // Используется метод findAdsById(id) для поиска объявления по указанному идентификатору.
        Ads ads = findAdsById(id);

        // Полученное объявление удаляется из репозитория adsRepository с помощью метода delete(ads).
        adsRepository.delete(ads);

        // Выводится информационное сообщение в лог, используя уровень логирования "info", о том, что объявление успешно удалено.
        log.info("Ads removed successfully");
    }

    /**
     * Метод updateAds() предполагает обновление деталей объявления по его идентификатору
     * @param id
     * @param createAds
     * @return
     */
    @Override
    public AdsDto updateAds(Integer id, CreateAds createAds) {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug", с указанием переданного идентификатора.
        log.debug("Updating ads by id: {}", id);

        // Проверяется наличие обязательных полей (заголовок, описание, цена) в объекте createAds.
        // Если хотя бы одно из полей отсутствует или пустое, выбрасывается исключение IncorrectArgumentException.
        if (createAds.getTitle() == null || createAds.getTitle().isBlank()
                || createAds.getDescription() == null || createAds.getDescription().isBlank()
                || createAds.getPrice() == null) throw new IncorrectArgumentException();

        // Используется метод findAdsById(id) для поиска объявления по указанному идентификатору.
        Ads ads = findAdsById(id);

        // Обновляются детали объявления (заголовок, описание, цена) на основе данных из объекта createAds.
        ads.setTitle(createAds.getTitle());
        ads.setDescription(createAds.getDescription());
        ads.setPrice(createAds.getPrice());

        // Обновленное объявление сохраняется в репозитории adsRepository с помощью метода save(ads).
        adsRepository.save(ads);

        // Выводится информационное сообщение в лог, используя уровень логирования "info", о том, что детали объявления успешно обновлены.
        log.info("Ads details updated for ads: {}", ads.getTitle());

        // Возвращается объект AdsDto, созданный на основе обновленного объявления с помощью маппера AdsMapper.
        return AdsMapper.INSTANCE.toDto(ads);
    }

    /**
     * Метод getAdsMe() предполагает получение списка объявлений, созданных авторизованным пользователем.
     * @param authentication
     * @return
     */
    @Override
    public List<AdsDto> getAdsMe(Authentication authentication) {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug",
        // с указанием имени авторизованного пользователя из объекта authentication.
        log.debug("Getting ads by author {}", authentication.getName());
        return adsRepository.

                // Используется метод userService.getUserByUsername(authentication.getName())
                // для получения объекта пользователя на основе имени пользователя из объекта authentication.
                // Используется метод adsRepository.findAllByAuthorId() для поиска всех объявлений,
                // созданных указанным пользователем на основе его идентификатора.
                findAllByAuthorId(userService.getUserByUsername(authentication.getName()).getId())

                // Результат поиска объявлений преобразуется в список объектов AdsDto с помощью методов map() и collect() из Java Stream API.
                .stream()
                .map(AdsMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        // Возвращается список объявлений, представленных в форме AdsDto, созданных указанным пользователем.
    }

    /**
     * Метод updateAdsImage() предполагает обновление изображения для объявления с указанным идентификатором.
     * @param id
     * @param imageFile
     * @throws IOException
     */
    @Override
    public void updateAdsImage(Integer id, MultipartFile imageFile) throws IOException {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug",
        // с указанием идентификатора объявления, для которого выполняется обновление изображения.
        log.debug("Updating ads image by id: {}", id);

        // Используется метод findAdsById(id) для поиска объявления по его идентификатору.
        Ads ads = findAdsById(id);

        // Если у найденного объявления уже есть изображение (ads.getImage() != null),
        // то вызывается метод imageService.remove() для удаления старого изображения.
        if (ads.getImage() != null) {
            imageService.remove(ads.getImage());
        }

        // Загружается новое изображение с помощью метода imageService.uploadImage(imageFile)
        // и устанавливается для объявления с помощью ads.setImage().
        ads.setImage(imageService.uploadImage(imageFile));

        // Обновленное объявление сохраняется в репозитории с помощью метода adsRepository.save(ads).
        adsRepository.save(ads);

        // Выводится отладочное сообщение в лог, указывая, что изображение объявления было успешно обновлено.
        log.debug("Avatar updated for ads: {}", ads.getTitle());
    }

    /**
     * Метод findAdsById() предполагает поиск объявления по его идентификатору.
     * @param id
     * @return
     */
    public Ads findAdsById(Integer id) {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug",
        // с указанием идентификатора объявления, для которого выполняется поиск.
        log.debug("Finding ads by id: {}", id);

        // Используется метод adsRepository.findById(id) для поиска объявления в репозитории по его идентификатору.
        // Если объявление не найдено, то выбрасывается исключение AdsNotFoundException,
        // которое является пользовательским исключением, указанном в методе orElseThrow().
        // Это исключение сигнализирует о том, что
        // объявление с указанным идентификатором не было найдено и может быть обработано соответствующим образом в вышестоящем коде.
        return adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
    }
}

