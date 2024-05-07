package com.ai.chatbot.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotRequest {

    @JsonProperty("prompt_message")
    private String promptMessage;
}
