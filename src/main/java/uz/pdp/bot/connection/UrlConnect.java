package uz.pdp.bot.connection;

import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import uz.pdp.bot.model.OrderProduct;
import uz.pdp.bot.model.dto.UserLoginDto;
import uz.pdp.bot.model.dto.UserRegisterDto;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UrlConnect {

    private static HttpPost createRequestForUrl(String url) {
        String urlConnection = "http://localhost:8080" + url;
        HttpPost request = new HttpPost(urlConnection);
        request.addHeader("content-type", "application/json");
        request.addHeader("Accept", "application/json");
        return request;
    }

    private static StringBuilder getResponse(HttpResponse response) throws IOException {
        StringBuilder response0 = new StringBuilder();
        if (response != null) {
            InputStream in = response.getEntity().getContent();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(in))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response0.append(responseLine.trim());
                }
            }
        }
        return response0;
    }


    public static StringBuilder registerUser(UserRegisterDto userRegisterDto) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = createRequestForUrl("/api/register");
        JsonObjectBuilder userBuilder = Json.createObjectBuilder();

        userBuilder.add("fullName", userRegisterDto.getFullName());
        userBuilder.add("username", userRegisterDto.getUsername());
        userBuilder.add("password", userRegisterDto.getPassword());

        JsonObject userObject = userBuilder.build();
        String jsonInputString = String.valueOf(userObject);
        StringEntity stringEntity = new StringEntity(jsonInputString);
        request.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(request);

        return getResponse(response);
    }

    @SneakyThrows
    public static StringBuilder loginUser(UserLoginDto userLoginDto) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = createRequestForUrl("/api/login");
        JsonObjectBuilder userBuilder = Json.createObjectBuilder();

        userBuilder.add("username", userLoginDto.getUsername());
        userBuilder.add("password", userLoginDto.getPassword());

        return getStringBuilder(httpClient, request, userBuilder);
    }

    @SneakyThrows
    public static StringBuilder findByAllOrElseNull(String name, String url) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = createRequestForUrl("/api" + url);
        JsonObjectBuilder userBuilder = Json.createObjectBuilder();

        userBuilder.add("name", name);

        System.out.println("name = " + name);
        System.out.println("url = " + url);

        return getStringBuilder(httpClient, request, userBuilder);
    }

    @SneakyThrows
    public static StringBuilder findByAllOrElseNull(String name, String username, String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = createRequestForUrl("/api" + url);
        JsonObjectBuilder userBuilder = Json.createObjectBuilder();

        userBuilder.add("id", name);
        userBuilder.add("name", username);

        System.out.println("name = " + name);
        System.out.println("url = " + url);

        return getStringBuilder(httpClient, request, userBuilder);
    }


    @SneakyThrows
    public static StringBuilder addOrderToCart(OrderProduct orderProduct, String url) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = createRequestForUrl("/api" + url);
        JsonObjectBuilder userBuilder = Json.createObjectBuilder();

        userBuilder.add("customerId", orderProduct.getCustomerId());
        userBuilder.add("productId", orderProduct.getProduct().getId().toString());
        userBuilder.add("username", orderProduct.getUsername());
        userBuilder.add("count", orderProduct.getCount());

        System.out.println("url = " + url);

        return getStringBuilder(httpClient, request, userBuilder);
    }

    @SneakyThrows
    private static StringBuilder getStringBuilder(HttpClient httpClient, HttpPost request, JsonObjectBuilder userBuilder) {

        JsonObject userObject = userBuilder.build();
        String jsonInputString = String.valueOf(userObject);
        StringEntity stringEntity = new StringEntity(jsonInputString);
        request.setEntity(stringEntity);

        HttpResponse response = httpClient.execute(request);
        System.out.println("response.getStatusLine() = " + response.getStatusLine().getStatusCode());

        return getResponse(response);
    }

}
