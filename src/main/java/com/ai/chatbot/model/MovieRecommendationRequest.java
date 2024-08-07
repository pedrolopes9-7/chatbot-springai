package com.ai.chatbot.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRecommendationRequest {

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("movies")
    private List<String> movies;
}
