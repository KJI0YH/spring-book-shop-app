package com.example.mybookshopapp.services;

import com.example.mybookshopapp.errors.PaymentDoesNotExistsException;
import com.example.mybookshopapp.errors.PaymentInitiateException;
import com.example.mybookshopapp.errors.PaymentStatusException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GatewayPaymentService {

    private final String apiUrl = "https://api.yookassa.ru/v3/payments";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${yookassa.SECRET_KEY}")
    private String secretKey;
    @Value("${yookassa.shopId}")
    private String shopId;

    public JsonNode getPayment(String paymentId) throws PaymentStatusException, PaymentDoesNotExistsException {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl + "/" + paymentId))
                    .header("Authorization", "Basic " + getAuthString())
                    .build();
            HttpClient httpClient = HttpClient.newBuilder().build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() == 200) {
            try {
                return objectMapper.readTree(response.body());
            } catch (IOException e) {
                throw new PaymentStatusException("Can not to get payment status");
            }
        } else if (response.statusCode() == 404) {
            throw new PaymentDoesNotExistsException("Payment does not exists");
        } else {
            throw new PaymentStatusException("Can not to get payment status");
        }
    }

    public JsonNode createPayment(String idempotenceKey, Integer sumInCents, String currency, String
            returnUrl, String description) throws PaymentInitiateException {

        String jsonPayload = new JSONObject()
                .put(
                        "amount", new JSONObject()
                                .put("value", sumInCents / 100 + "." + sumInCents % 100)
                                .put("currency", currency)
                )
                .put("capture", true)
                .put(
                        "confirmation", new JSONObject()
                                .put("type", "redirect")
                                .put("return_url", returnUrl)
                )
                .put("description", description)
                .toString();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .header("Authorization", "Basic " + getAuthString())
                    .header("Idempotence-Key", idempotenceKey)
                    .header("Content-Type", "application/json")
                    .build();
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readTree(response.body());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new PaymentInitiateException("Can not initiate a payment");
        }
        throw new PaymentInitiateException("Can not initiate a payment");
    }

    private String getAuthString() {
        String credentials = shopId + ":" + secretKey;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
