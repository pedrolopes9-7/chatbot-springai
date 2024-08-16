package com.ai.chatbot.datasethelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DatasetTransformer {

    public static void main(String[] args) {
        String moviesFile = "C:\\Users\\55319\\workroot\\chatbot\\src\\main\\java\\com\\ai\\chatbot\\datasethelper\\movies.csv";
        String ratingsFile = "C:\\Users\\55319\\workroot\\chatbot\\src\\main\\java\\com\\ai\\chatbot\\datasethelper\\ratings.csv";
        String tagsFile = "C:\\Users\\55319\\workroot\\chatbot\\src\\main\\java\\com\\ai\\chatbot\\datasethelper\\tags.csv";
        String outputFile = "C:\\Users\\55319\\workroot\\chatbot\\src\\main\\java\\com\\ai\\chatbot\\datasethelper\\movies.json";

        Map<Integer, Movie> movieMap = new HashMap<>();

        System.out.println("Start processing movies.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(moviesFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int movieId = Integer.parseInt(fields[0]);
                String title = fields[1];
                movieMap.put(movieId, new Movie(movieId, title));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done processing movies.csv");

        System.out.println("Start processing ratings.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(ratingsFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int movieId = Integer.parseInt(fields[1]);
                double rating = Double.parseDouble(fields[2]);

                if (movieMap.containsKey(movieId)) {
                    Movie movie = movieMap.get(movieId);
                    movie.addRating(rating);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done processing ratings.csv");

        System.out.println("Start processing tags.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(tagsFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int movieId = Integer.parseInt(fields[1]);
                String tag = fields[2];

                if (movieMap.containsKey(movieId)) {
                    Movie movie = movieMap.get(movieId);
                    movie.addTag(tag);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done processing tags.csv");

        System.out.println("Writing final result to movies.json");

        try (FileWriter file = new FileWriter(outputFile)) {
            JSONArray moviesArray = new JSONArray();

            for (Movie movie : movieMap.values()) {
                moviesArray.put(movie.toJson());
            }

            JSONObject result = new JSONObject();
            result.put("movies", moviesArray);

            file.write(result.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished all tasks");
    }
}
