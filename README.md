# doc-recognize-api
# сервис по распознаванию
Для запуска приложения нужно указать в переменных среды данные от яндекс api
 > YANDEX_TOKEN_API=<токен>;YANDEX_FOLDER_ID=<id каталга>
> 
<img src="readme-img/img.png" alt="img.png" width="700px">


# Запросы 
- запросы можно проверить из постмана (коллекция лежит  в папке проекта - src/main/resources)
  - 1 . Для обращения к ендпоинтам следует выполнить запрос к keycloak на получение access токена
  <img src="readme-img/keycloak-get-access-token.png" alt="keycloak-get-access-token.png" width="900px">
  - 2 .  Далее полученный токен вставляем во вкладке authorization   и делаем запрос
  <img src="readme-img/auth-img.png" alt="auth-img.png" width="800px">
  - 3 . Аналогично и с остальными методами 
- запросы из swagger-ui (доступно по http://localhost:8081/swagger-ui/index.html)
    - авторизация пользователя для запросов

      <img src="readme-img/auth-swagger.png" alt="auth-swagger.png" height="586px">
    - выполнение запроса
  
      <img src="readme-img/img_1.png" alt="img_1.png" height="692px">
