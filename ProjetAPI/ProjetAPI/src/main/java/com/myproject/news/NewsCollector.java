package com.myproject.news;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewsCollector {

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

            // Step 3: Print articles to the console
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                System.out.println("Title: " + article.getString("title"));
                System.out.println("Description: " + article.getString("description"));
                System.out.println("Published At: " + article.getString("publishedAt"));
                System.out.println("-----------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
