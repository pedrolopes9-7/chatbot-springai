package com.ai.chatbot.controller;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.ai.chatbot.service.DataLoaderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.chatbot.model.MovieRecommendationRequest;
import com.ai.chatbot.model.MovieRecommendationResponse;
import com.ai.chatbot.service.MovieRecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieRecommendationController {

    private final MovieRecommendationService movieRecommendationService;

    private final DataLoaderService dataLoaderService;

    @PostMapping("/recommend")
    public MovieRecommendationResponse recommend(@RequestBody MovieRecommendationRequest request) {
        if (request.getGenre() == null || request.getGenre().isEmpty()) {
            throw new IllegalArgumentException("Parameter genre is mandatory to recommend movies");
        }

        if (!isEmpty(request.getMovies())) {
            var message = movieRecommendationService.recommend(request.getGenre(), request.getMovies());
            return new MovieRecommendationResponse(message);
        }

        var message = movieRecommendationService.recommend(request.getGenre());
        return new MovieRecommendationResponse(message);
    }

    @PostMapping("/reload_datasets")
    public void reloadDatasets() {
        dataLoaderService.load();
    }

}
