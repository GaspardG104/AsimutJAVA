package guid.net.asimutjava;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import guid.net.asimutjava.models.Projet;
import java.net.URI;
import java.net.http.*;
import java.util.List;

public class TestConnexion {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/projets/api/json"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // LA MAGIE DE GSON :
        Gson gson = new Gson();
        List<Projet> projets = gson.fromJson(response.body(), new TypeToken<List<Projet>>(){}.getType());

        for (Projet p : projets) {
            System.out.println("Projet trouvé : " + p.getLibelle() + " (Responsable : " + p.getNom_resp() + ")");
        }
    }
}