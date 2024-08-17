package com.ai.chatbot.datasethelper;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class Movie {

    private int id;
    private String name;
    private Set<String> tags;
    private double totalRating;
    private int ratingCount;

    public Movie(int id, String name) {
        this.id = id;
        this.name = name;
        this.tags = new HashSet<>();
        this.totalRating = 0.0;
        this.ratingCount = 0;
    }

    public void addRating(double rating) {
        this.totalRating += rating;
        this.ratingCount++;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("name", this.name);
        jsonObject.put("average_rating", this.ratingCount == 0 ? 0.0 : this.totalRating / this.ratingCount);
        jsonObject.put("tags", this.tags);
        return jsonObject;
    }
}
