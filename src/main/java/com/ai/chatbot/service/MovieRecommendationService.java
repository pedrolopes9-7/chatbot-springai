package com.ai.chatbot.service;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieRecommendationService {

    private final OllamaChatModel ollamaChatClient;

    private final VectorStore  vectorStore;

    private static final String INSTRUCTIONS_PROMPT_MESSAGE = """
        You're a movie recommendation system. Recommend 5 movies on `movie_genre`=%s.
        Write the final recommendation using the following template:
        Movie Name:
        Synopsis:
        Cast:
        """;

    private static final String EXAMPLES_PROMPT_MESSAGE = """
        Use movies tags and ratings and `movie_genre` to recommend the best movies based on `movie_list`.

        `movies_list`:
        %s
        """;

    private static final String SIMILARITY_PROMPT = """
        Use the `documents` below to provide accurate answers, but act as you knew this information innately.
        
        `documents`
        {documents}
        """;

    public String recommend(String genre) {
        var generalInstructions = new UserMessage(String.format(INSTRUCTIONS_PROMPT_MESSAGE, genre));

        var similarDocuments = vectorStore.similaritySearch(generalInstructions.getContent());
        var documentsMessage = similarDocuments.stream().map(Document::getContent).collect(joining(","));
        var similaritySystemMessage = new SystemPromptTemplate(SIMILARITY_PROMPT).createMessage(Map.of("documents", documentsMessage));

        var prompt = new Prompt(List.of(similaritySystemMessage, generalInstructions));
        return ollamaChatClient.call(prompt)
            .getResult()
            .getOutput()
            .getContent();
    }

    public String recommend(String genre, List<String> movies) {
        var moviesCollected = movies.stream()
            .collect(joining("\n`movie_name`=", "\n", ""));

        var generalInstructions = new UserMessage(String.format(INSTRUCTIONS_PROMPT_MESSAGE, genre));
        var examplesSystemMessage = new SystemMessage(String.format(EXAMPLES_PROMPT_MESSAGE, moviesCollected));

        var similarDocuments = vectorStore.similaritySearch(generalInstructions.getContent());
        var documentsMessage = similarDocuments.stream().map(Document::getContent).collect(joining(","));
        var similaritySystemMessage = new SystemPromptTemplate(SIMILARITY_PROMPT).createMessage(Map.of("documents", documentsMessage));

        var prompt = new Prompt(List.of(generalInstructions, examplesSystemMessage, similaritySystemMessage));
        return ollamaChatClient.call(prompt)
            .getResult()
            .getOutput()
            .getContent();
    }
}
