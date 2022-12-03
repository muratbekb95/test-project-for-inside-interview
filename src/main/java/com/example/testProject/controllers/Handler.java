package com.example.testProject.controllers;

import com.example.testProject.models.MessageRequest;
import com.example.testProject.models.UserDataRequest;
import com.example.testProject.models.AuthenticationResponse;
import com.example.testProject.services.DataControllerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.StringTokenizer;

@RestController
@RequestMapping("api/v1/application")
public class Handler {
    private final DataControllerService service;
    public Handler(DataControllerService service) {
        this.service = service;
    }
    @PostMapping("/checkPassAndCreateToken")
    public AuthenticationResponse checkPassAndCreateToken(@RequestBody UserDataRequest data) {
        return service.checkPassAndCreateToken(data);
    }

    // Здесь я получаю header в виде Bearer {ID токена}, извлекаю ID токена через tokenizer
    @PostMapping("/history10")
    public List<MessageRequest> saveMessageOrGetLastTenMessages(@RequestHeader(value="Authorization") String token, @RequestBody MessageRequest data) throws Exception {
        StringTokenizer tokenizer = new StringTokenizer(token, " ");
        String auth = "";
        while (tokenizer.hasMoreTokens()) {
            String tk = tokenizer.nextToken();
            if(!tk.equals("Bearer")) {
                auth = tk;
            }
        }

        return service.saveMessageOrGetLastTenMessages(auth, data);
    }

    @PostMapping("/createUser")
    public void createUser(@RequestBody UserDataRequest user) {
        service.createUser(user);
    }

    @PostMapping("/createNewMessage")
    public void createNewMessage(@RequestBody MessageRequest messageRequest) {
        service.createNewMessage(messageRequest);
    }
}
