package guid.net.asimutjava;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestConnexion {
    public static void main(String[] args) {
        // 1. On crée un client HTTP
        HttpClient client = HttpClient.newHttpClient();

        // 2. On prépare la requête vers ton API Node.js
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/projets/api/json"))
                .build();

        System.out.println("Tentative de connexion à l'API...");

        // 3. On envoie la requête et on affiche la réponse
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    System.out.println("Réponse reçue de l'API :");
                    System.out.println(json);
                })
                .join();
    }
}