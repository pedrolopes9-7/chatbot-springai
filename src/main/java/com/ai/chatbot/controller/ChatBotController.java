package com.ai.chatbot.controller;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.chatbot.model.ChatBotResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class ChatBotController {

    private final OllamaChatClient chatClient;

    @PostMapping("/prompt")
    public ResponseEntity<ChatBotResponse> chat(@RequestBody ChatBotRequest request) {

        final var modelResponse = chatClient.call(request.getPromptMessage());

        return new ResponseEntity<>(new ChatBotResponse(modelResponse), HttpStatus.OK);
    }

    @PostMapping("/async-prompt")
    public Flux<String> chatAsync(@RequestBody ChatBotRequest request) {

        return chatClient.stream(request.getPromptMessage());
    }

}
