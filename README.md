Проект собрал по Java версии 11 и Spring Boot версии 2.7.6
Автор: Муратбек Бауыржан

Установил библиотеки
    spring-boot-starter-web,
    spring-boot-starter-data-jpa,
    h2 (для хранения данных)
    lombok (для автоматической подставки setter + getter методов для Entity)
    io.jsonwebtoken:jjwt (библиотека для генерации/валидации токена)

В коде я поделил структуру 
    на controllers (классы, которые слушают и отправляют ответы путем отправки запросов на эндпоинты),
    на models (модели, которые содержат Entity для пользователя и сообщения)
    на repositories (классы, через которые выполняются манипуляции по данным)
    на services (классы, которые исполняют бизнес логику)

Controllers:
    Handler - основной класс, который слушает запросы и отправляет пользователю статус код или другие ответы
Models:
    AuthenticationResponse - класс, который возвращает токен
    MessageRequest - класс, который возвращает содержания сообщения, либо выступает как параметр при отправке запросов (name - имя пользователя, password - пароль, created_date - дата создания, updated_date - дата изменения, user - данные пользователя)
    UserDataRequest - класс, который выступает как параметр при отправке запросов (name - имя пользователя, password - пароль)
Repositores:
    MessagesRepository - репозиторий, которые манипулирует данными MessageRequest
    UsersRepository - репозиторий, которые манипулирует данными UserDataRequest
Services:
    DataControllerService - сервис, который позволяет выполнять бизнес логику данных, например создния пользователя, создание сообщения
        
        Самые основные и нужные методы:

        // данный метод позволяет создавать пользователя по передаче имени и пароля
        public void createUser(UserDataRequest user)

        // Проверяет на наличие пароля в БД, и позже создает токен для пользователя
        public AuthenticationResponse checkPassAndCreateToken(UserDataRequest data)

        // создает новое сообщение путем передачи имени (здесь оно привязано к классу UserDataRequest) и самого содержания сообщения
        public void createNewMessage(MessageRequest messageRequest)

        // Проводит валидацию токена, и сохранения сообшения в случае успеха, или вывода 10 последних сообщении данного пользователя
        public List<MessageRequest> saveMessageOrGetLastTenMessages(String token, MessageRequest data) throws Exception

    TokenGeneratorUtility - сервис, который отвечает за генерацию, валидацию токена

Тесты:
    DataControllerServiceTests - сервис, которые включает в себя Mock тесты... я уже расписал внутри все подробнее, здесь в детали не буду вдаваться
    я создал тесты только для этого класса, так как по мне это самый важный и необходимый из них


Обязательно прочтите файл requests.txt, чтобы запустить curl запросы через терминал!