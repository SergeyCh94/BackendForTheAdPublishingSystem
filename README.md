
<div>

## Дипломная работа "Профессия Java-разработчик ISA" от Skypro
</div>

___
## Описание проекта и его функциональность

Сайт является аналогом интернет-магазина Avito. 
Пользователи могут размещать объявления товаров и оставлять комментарии к объявлениям других пользователей.

### Реализованы следующие функции:

- Авторизация и аутентификация пользователей;
- CRUD-операции для объявлений на сайте;
- CRUD-операции для комментариев к объявлениям;
- Пользователи могут создавать, удалять или редактировать свои собственные объявления и комментарии. Администраторы могут удалять или редактировать все объявления и комментарии;
- Поиск объявлений по названию в шапке сайта;
- Загрузка и отображение изображений объявлений и аватаров пользователей.

## Задание:
- [Technical task](https://skyengpublic.notion.site/02df5c2390684e3da20c7a696f5d463d)

___
## Инструменты, используемые в проекте:
* **Backend**:
    - Java 11
    - Maven
    - Spring Boot
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - Stream API
    - REST
    - GIT
    - Swagger
    - Lombok
    - Mapstruck
* **SQL**:
    - PostgreSQL
    - Liquibase
* **Test**:
    - Junit
    - Mockito
* **Frontend**:
    - Docker image

___
## Запуск приложения
* Для запуска приложения Вам потребуется выполнить несколько шагов:
    - Клонировать проект и открыть его в среде разработки (например, *IntelliJ IDEA* или *VSCode*);
    - В файле **application.properties** указать путь к Вашей базе данных;
    - Запустить **Docker**;
    - В командной строке прописать ```docker pull ghcr.io/bizinmitya/front-react-avito:latest``` и скачать образ;
    - Запустить **Docker image** с помощью команды ```docker run -p 3000:3000 ghcr.io/bizinmitya/front-react-avito:latest```;
    - Запустить метод **main** в файле **MarketplaceApplication.java**.

После выполнения всех шагов, веб-приложение будет доступно по адресу: http://localhost:3000

___
### Разработчик:
- [Черноносов Сергей](https://github.com/SergeyCh94)
