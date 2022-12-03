package com.example.testProject.services;

import com.example.testProject.models.MessageRequest;
import com.example.testProject.models.UserDataRequest;
import com.example.testProject.models.AuthenticationResponse;
import com.example.testProject.repositories.MessagesRepository;
import com.example.testProject.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataControllerService {
    // передаю в конструктор репозитории пользователя, сообщении, где происходят манипуляция всех данных
    private final UsersRepository usersRepository;
    private final MessagesRepository messagesRepository;
    private final TokenGeneratorUtility tokenGeneratorUtility;
    public DataControllerService(UsersRepository usersRepository, MessagesRepository messagesRepository, TokenGeneratorUtility tokenGeneratorUtility) {
        this.usersRepository = usersRepository;
        this.messagesRepository = messagesRepository;
        this.tokenGeneratorUtility = tokenGeneratorUtility;
    }

    // создаю пользователя по передаче имени и пароля
    public void createUser(UserDataRequest user) {
        usersRepository.save(user);
    }

    // Проверка на наличие пароля в БД, и дальнейшее создание токена для пользователя
    public AuthenticationResponse checkPassAndCreateToken(UserDataRequest data) {
        Optional<UserDataRequest> user = usersRepository.findByName(data.getName());
        if (user.isPresent()) {
            String token = tokenGeneratorUtility.generateToken(data);
            return new AuthenticationResponse(token);
        }
        return null;
    }

    // создаю новое сообщение путем передачи имени (здесь оно привязано к классу UserDataRequest) и самого содержания сообщения
    public void createNewMessage(MessageRequest messageRequest) {
        Optional<UserDataRequest> user = usersRepository.findByName(messageRequest.getName());
        if (user.isPresent()) {
            messageRequest.setUser(user.get());
            messagesRepository.save(messageRequest);
        }
    }

    // Код по проверке валидации токена, сохранения сообшения в случае успеха, или вывода 10 последних сообщении данного пользователя
    public List<MessageRequest> saveMessageOrGetLastTenMessages(String token, MessageRequest data) throws Exception {
        if (tokenGeneratorUtility.validateToken(token)) {
            String message = data.getMessage();
            String name = data.getName();
            if (message.equals("history 10")) {
                List<MessageRequest> messages = messagesRepository.getLastTen(name);
                for (int i = 0; i < messages.size(); i++) {
                    String nameOfUser = messages.get(i).getName();
                    Optional<UserDataRequest> user = usersRepository.findByName(nameOfUser);
                     if (user.isPresent()) {
                         messages.get(i).setUser(user.get());
                     } else {
                         messages.remove(i);
                         i--;
                     }
                }
                return messages;
            } else {
                Optional<UserDataRequest> user = usersRepository.findByName(name);
                if (user.isPresent()) {
                    data.setUser(user.get());
                    messagesRepository.save(data);
                } else {
                    throw new Exception("Пользователя не существует!");
                }
            }
        }
        return new ArrayList<>();
    }
}
