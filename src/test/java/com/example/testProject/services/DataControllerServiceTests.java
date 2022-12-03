package com.example.testProject.services;

import com.example.testProject.models.AuthenticationResponse;
import com.example.testProject.models.MessageRequest;
import com.example.testProject.models.UserDataRequest;
import com.example.testProject.repositories.MessagesRepository;
import com.example.testProject.repositories.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class DataControllerServiceTests {
    private UsersRepository usersRepository;
    private MessagesRepository messagesRepository;
    private TokenGeneratorUtility tokenGeneratorUtility;
    private DataControllerService dataControllerService;

    @BeforeEach
    void setupService() {
        usersRepository = Mockito.mock(UsersRepository.class);
        messagesRepository = Mockito.mock(MessagesRepository.class);
        tokenGeneratorUtility = Mockito.mock(TokenGeneratorUtility.class);
        dataControllerService = new DataControllerService(usersRepository, messagesRepository, tokenGeneratorUtility);
    }

    @Test
    void createUser() {
        UserDataRequest userDataRequest = new UserDataRequest("Andrey", "123");
        Mockito.when(usersRepository.findByName("Andrey")).thenReturn(Optional.of(userDataRequest));
        // Проверка на сохранение пользователя, и возвращение значения с БД самого последнего пользователя
        Mockito.when(usersRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        UserDataRequest data = new UserDataRequest("Andrey", "123");
        // Проверка на то, что у пользователя имя Andrey
        Assertions.assertThat(data.getName()).isEqualTo("Andrey");
        // Проверка на то, что у пользователя пароль 123
        Assertions.assertThat(data.getPassword()).isEqualTo("123");

        // Проверка на то, что существует ли пользователь с именем Rabat, здесь тест должен намеренно провалится
        Assertions.assertThat(usersRepository.findByName("Rabat")).isPresent();
    }

    @Test
    public void checkPassAndCreateToken() {
        UserDataRequest data = new UserDataRequest("Andrey", "123");
        // Проверка на то, что пользователь с именем Andrey существует
        Mockito.when(usersRepository.findByName("Andrey")).thenReturn(Optional.of(data));
        String token = tokenGeneratorUtility.generateToken(data);
        // Проверка на наличие токена
        Mockito.when(tokenGeneratorUtility.generateToken(data)).thenReturn(String.valueOf(token));

        Assertions.assertThat(data.getName()).isEqualTo("Andrey");
        Assertions.assertThat(tokenGeneratorUtility.generateToken(data)).isNotEmpty();
    }

    @Test
    public void createNewMessage() {
        MessageRequest messageRequest = new MessageRequest("Andrey", "How are you?");

        UserDataRequest userData = new UserDataRequest("Andrey", "123");
        Mockito.when(usersRepository.save(userData)).then(AdditionalAnswers.returnsFirstArg());

        Optional<UserDataRequest> data = usersRepository.findByName("Andrey");
        Mockito.when(usersRepository.findByName("Andrey")).thenReturn(data);
        Mockito.when(usersRepository.findByName(messageRequest.getName())).thenReturn(data);
        Mockito.when(messagesRepository.save(messageRequest)).then(AdditionalAnswers.returnsFirstArg());

        Assertions.assertThat(data).isPresent();
        Assertions.assertThat(messageRequest.getMessage()).isEqualTo("How are you?");
    }

    @Test
    public void saveMessageOrGetLastTenMessages() throws Exception {
        UserDataRequest data = new UserDataRequest("Andrey", "123");
        Mockito.when(usersRepository.save(data)).then(AdditionalAnswers.returnsFirstArg());
        String token = tokenGeneratorUtility.generateToken(data);
        boolean isValid = tokenGeneratorUtility.validateToken(token);
        Mockito.when(isValid).thenReturn(isValid);

        MessageRequest messageRequest = new MessageRequest("Andrey", "history 10");
        Mockito.when(messagesRepository.save(new MessageRequest("Andrey", "How are you?"))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(messagesRepository.save(new MessageRequest("Andrey", "The key to success is hard work"))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(messagesRepository.save(new MessageRequest("Andrey", "My name is Andrey"))).then(AdditionalAnswers.returnsFirstArg());

        Optional<UserDataRequest> userData = usersRepository.findByName(messageRequest.getName());
        // Проверка сообщения, которое содержит текст history 10
        Assertions.assertThat(messageRequest.getMessage()).isEqualTo("history 10");
        // Проверяет есть ли у пользователя созданные сообщения
        Assertions.assertThat(messagesRepository.getLastTen(data.getName())).isNotEmpty();
    }
}
