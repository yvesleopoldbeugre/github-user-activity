package com.formation;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("github-activity ");
        fetch(scanner.nextLine());
        scanner.close();
    }

    public static void fetch(String username) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .headers("Accept", "application/vnd.github+json", "X-GitHub-Api-Version", "2026-03-10").uri(URI.create(
                        "https://api.github.com/users/"
                                + username + "/events"))
                .GET().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(
                    request, HttpResponse.BodyHandlers.ofString());
            JsonNode eventNode = objectMapper.readValue(response.body(), JsonNode.class);
            StringBuilder builder = new StringBuilder();
            eventNode.forEach(event -> {
                String typeEvent = event.get("type").asText().replace("Event", "");
                String reposName = event.get("repo") != null ? event.get("repo").get("name").asText() : "";
                builder.append("- ");
                builder.append(typeEvent);
                builder.append(" to ");
                builder.append(reposName);
                builder.append("\n");
            });
            System.out.println(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}