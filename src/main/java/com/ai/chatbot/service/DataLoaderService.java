package com.ai.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    @Value("classpath:/datasets/test.json")
    private Resource movieDataset;

    private final VectorStore vectorStore;

    public void load() {
        DocumentReader reader = new JsonReader(movieDataset);

        var movies = reader.read();

        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();

        var tokenizedDocs = tokenTextSplitter.apply(movies);

        vectorStore.accept(tokenizedDocs);
    }
}
