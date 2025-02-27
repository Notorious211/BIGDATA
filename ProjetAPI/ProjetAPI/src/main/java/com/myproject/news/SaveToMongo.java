package com.myproject.news;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SaveToMongo {

    public static void main(String[] args) {
        String apiKey = "4a19912d182145d894ac1affb668931f"; // Your NewsAPI key
        String query = "technology"; // Topic to search
        String urlString = "https://newsapi.org/v2/everything?q=" + query + "&apiKey=" + apiKey;

        try {
            // Step 1: Fetch data from NewsAPI
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Step 2: Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");

            // Step 3: Connect to MongoDB
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("trend_analysis");
            MongoCollection<Document> collection = database.getCollection("news");

            // Step 4: Save articles to MongoDB
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                Document doc = new Document("title", article.getString("title"))
                        .append("description", article.getString("description"))
                        .append("publishedAt", article.getString("publishedAt"))
                        .append("source", article.getJSONObject("source").getString("name"));
                collection.insertOne(doc);
            }

            System.out.println("Articles successfully saved to MongoDB!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
