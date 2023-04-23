CREATE TABLE IF NOT EXISTS users
(
    id   SERIAL PRIMARY KEY,
    first_name VARCHAR (30) NOT NULL,
    last_name VARCHAR (30) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR (30) UNIQUE NOT NULL,
    password VARCHAR (150) NOT NULL,
    role VARCHAR (10) NOT NULL
    );

CREATE TABLE IF NOT EXISTS ads
(
    id  SERIAL PRIMARY KEY,
    author_id BIGINT REFERENCES users (id),
    image VARCHAR (250),
    title VARCHAR (100) NOT NULL,
    description TEXT,
    price INTEGER NOT NULL
    );

CREATE TABLE IF NOT EXISTS comment
(
    id SERIAL PRIMARY KEY,
    author_id BIGINT REFERENCES users (user_id),
    ads_id BIGINT REFERENCES ads (ads_id),
    created_at TIMESTAMP NOT NULL,
    text TEXT NOT NULL
    );

/**
  Это SQL-скрипт для создания трех таблиц в базе данных:

users: таблица для хранения информации о пользователях. Она содержит следующие столбцы:
id: автоинкрементирующийся идентификатор пользователя (тип данных SERIAL), является первичным ключом.
first_name: имя пользователя (тип данных VARCHAR), не может быть NULL.
last_name: фамилия пользователя (тип данных VARCHAR), не может быть NULL.
phone: номер телефона пользователя (тип данных VARCHAR), не может быть NULL.
email: электронная почта пользователя (тип данных VARCHAR), уникальное значение, не может быть NULL.
password: пароль пользователя (тип данных VARCHAR), не может быть NULL.
role: роль пользователя (тип данных VARCHAR), не может быть NULL.

ads: таблица для хранения информации о объявлениях. Она содержит следующие столбцы:
id: автоинкрементирующийся идентификатор объявления (тип данных SERIAL), является первичным ключом.
author_id: идентификатор автора объявления, является внешним ключом, связанным с полем id таблицы users.
image: путь к изображению объявления (тип данных VARCHAR).
title: заголовок объявления (тип данных VARCHAR), не может быть NULL.
description: описание объявления (тип данных TEXT).
price: цена объявления (тип данных INTEGER), не может быть NULL.

comment: таблица для хранения информации о комментариях к объявлениям. Она содержит следующие столбцы:
id: автоинкрементирующийся идентификатор комментария (тип данных SERIAL), является первичным ключом.
author_id: идентификатор автора комментария, является внешним ключом, связанным с полем id таблицы users.
ads_id: идентификатор объявления, к которому оставлен комментарий, является внешним ключом, связанным с полем id таблицы ads.
created_at: дата и время создания комментария (тип данных TIMESTAMP), не может быть NULL.
text: текст комментария (тип данных TEXT), не может быть NULL.
 */