package com.ai.chatbot.datasethelper;

import org.json.JSONArray;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DatasetTransformer {

    public static void main(String[] args) throws IOException, URISyntaxException {

        ClassLoader classLoader = DatasetTransformer.class.getClassLoader();

        File moviesFile = new File(classLoader.getResource("datasets/movies.csv").getFile());
        File ratingsFile = new File(classLoader.getResource("datasets/ratings.csv").getFile());
        File tagsFile = new File(classLoader.getResource("datasets/tags.csv").getFile());

        Path source = Paths.get("src/main/resources/datasets/movies.json");
        File outputFile = source.toFile();

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

            file.write(moviesArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished all tasks");
    }
}
