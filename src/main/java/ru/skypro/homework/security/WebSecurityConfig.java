package ru.skypro.homework.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.service.UserService;

import javax.sql.DataSource;

/**
 * Это класс конфигурации Spring Security для приложения. Он настраивает правила доступа для различных конечных точек, используя HTTP-методы и URL-адреса.
 *
 * Также он настраивает пользовательскую службу Spring Security, которая используется для аутентификации пользователей и предоставления информации о них,
 * а также вызывает метод setAdminInUsers из userService, который устанавливает права администратора для некоторых пользователей.
 */

@Configuration
public class WebSecurityConfig {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private UserService userService;

  private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

  /**
   * Метод userDetailsService(), который объявлен как бин в Spring-контексте с использованием аннотации @Bean и с атрибутами @Primary.
   *
   * Он создает экземпляр класса JdbcUserDetailsManager, который является реализацией интерфейса UserDetailsService,
   * предоставляемого Spring Security. JdbcUserDetailsManager используется для хранения информации о пользователях в базе данных.
   *
   * Метод userDetailsService() принимает в качестве аргумента объект DataSource,
   * который используется для настройки и установки источника данных (базы данных) для JdbcUserDetailsManager.
   *
   * Аннотация @Primary указывает, что данный бин является основным при необходимости разрешить конфликты между несколькими бинами с одним и тем же типом.
   *
   * После настройки JdbcUserDetailsManager с использованием переданного DataSource, метод возвращает экземпляр JdbcUserDetailsManager,
   * который будет использоваться в приложении для выполнения операций связанных с пользователями, таких как аутентификация и авторизация.
   * @param dataSource
   * @return
   */
  @Bean
  @Primary
  public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
    JdbcUserDetailsManager users = new JdbcUserDetailsManager();
    users.setDataSource(dataSource);
    return users;
  }

  /**
   * Метод filterChain() принимает в качестве аргумента объект HttpSecurity, который предоставляет API для настройки конфигурации безопасности HTTP.
   *
   * Внутри метода производятся следующие настройки:
   *
   * Включение поддержки Cross-Origin Resource Sharing (CORS) с помощью метода cors().
   * Отключение защиты от межсайтовой подделки запроса (CSRF) с помощью метода csrf().disable().
   * Настройка правил авторизации с использованием метода authorizeHttpRequests().
   * Разрешение доступа для HTTP-запросов с методом POST на URL-ы "/login" и "/register" без авторизации с помощью метода
   * antMatchers(HttpMethod.POST, "/login", "/register").permitAll().
   * Разрешение доступа для HTTP-запросов с методом GET на URL "/ads" без авторизации с помощью метода antMatchers(HttpMethod.GET, "/ads").permitAll().
   * Разрешение доступа для HTTP-запросов с методом GET на URL-ы,
   * начинающиеся с "/getImage/" и "/getAvatar/" без авторизации с помощью метода antMatchers(HttpMethod.GET, "/getImage/", "/getAvatar/").permitAll().
   * Определение правил авторизации для URL-ов, начинающихся с "/ads/" и "/users/",
   * требующих аутентификации с помощью метода mvcMatchers("/ads/", "/users/").authenticated().
   * Включение HTTP-авторизации с использованием базовой аутентификации с помощью метода httpBasic(withDefaults()).
   * Наконец, метод возвращает настроенный объект HttpSecurity, который будет использоваться в приложении для обработки HTTP-запросов
   * с учетом настроенных правил безопасности.
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().authorizeHttpRequests()
            .antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
            .antMatchers(HttpMethod.GET, "/ads").permitAll()
            .antMatchers(HttpMethod.GET, "/getImage/**", "/getAvatar/**").permitAll()
            .mvcMatchers("/ads/**", "/users/**").authenticated()
            .and().httpBasic(withDefaults());
    return http.build();
  }

  /**
   * Метод setAdmin() выполняет следующие действия:
   *
   * Записывает сообщение "Was invoked method for set ADMIN." в лог используя объект LOGGER. LOGGER - это объект логгера,
   * настроенного с использованием библиотеки SLF4J/Logback или другой аналогичной библиотеки логгирования.
   * Вызывает метод setAdminInUsers() на объекте userService. userService - это экземпляр класса UserService,
   * который должен быть автоматически внедрен (инжектирован) в данном классе с использованием аннотации @Autowired.
   * Метод setAdminInUsers() выполняет какую-то логику, связанную с установкой админских прав или свойств на объектах, связанных с пользователем в системе.
   * Таким образом, данный метод setAdmin() может быть использован для выполнения определенной логики,
   * связанной с установкой админских прав или свойств на объектах, связанных с пользователем, и может быть вызван при инициализации Spring-контекста.
   */
  @Bean
  public void setAdmin() {
    LOGGER.info("Was invoked method for set ADMIN.");
    userService.setAdminInUsers();
  }
}