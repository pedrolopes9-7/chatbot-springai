package com.ai.chatbot.service;

import static java.util.stream.Collectors.joining;

import java.util.List;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class MovieRecommendationService {

    private final OllamaChatModel ollamaChatClient;

    private static final String INSTRUCTIONS_PROMPT_MESSAGE = """
        You're a movie recommendation system. You should read the `movie_genre` and recommend exactly 5 movies.
        
        Write the final recommendation using the following format:
        Movie Name: <name of the movie>
        Synopsis: <a very short synopsis of the movie>
        Cast: <the main cast>
        """;

    public MovieRecommendationService(OllamaChatModel ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    public String recommend(String genre) {
        var userMessage = new UserMessage(String.format("`movie_genre`=%s", genre));
        var generalInstructions = new SystemMessage(INSTRUCTIONS_PROMPT_MESSAGE);

        var prompt = new Prompt(List.of(userMessage, generalInstructions));
        return ollamaChatClient.call(prompt).getResult().getOutput().getContent();
    }

    public String recommend(String genre, List<String> movies) {
        var moviesCollected = "user_movies_list: \n".concat(movies.stream()
            .collect(joining(",", "movie_name=", "\n")));

        var promptMessage = String.format("Give me 5 movie recommendations on the genre %s", genre);
        var currentPromptMessage = new UserMessage(promptMessage);

        var prompt = new Prompt(currentPromptMessage);
        return ollamaChatClient.call(prompt).getResult().getOutput().getContent();
    }
}
