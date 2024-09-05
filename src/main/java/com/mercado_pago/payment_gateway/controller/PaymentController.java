package com.mercado_pago.payment_gateway.controller;

import com.google.gson.Gson;
import com.mercado_pago.payment_gateway.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/mercadopago/checkout")
public class PaymentController {

    @PostMapping
    public ResponseEntity<PaymentIntention> createCheckoutPreference() throws URISyntaxException, IOException, InterruptedException {

        Item item = new Item();
        item.setId("bike-teste");
        item.setTitle("Aluguel de bike");
        item.setQuantity(1);
        item.setUnit_price(10);

        List<Item> items = new ArrayList<>();
        items.add(item);

        Gson gson = new Gson();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setItems(items);
        String jsonBody = gson.toJson(paymentRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.mercadopago.com/checkout/preferences"))
                .header("Authorization", "Bearer " + Constants.APY_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {

            PaymentIntention paymentIntention = gson.fromJson(response.body(), PaymentIntention.class);
            return ResponseEntity.ok(paymentIntention);

        } else {

            System.err.println("Erro na requisição: " + response.body());
            return ResponseEntity.status(response.statusCode()).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boolean> getConfirmationPayment(@PathVariable String id) throws URISyntaxException, IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.mercadopago.com/checkout/preferences/"+id))
                .header("Authorization", "Bearer " + Constants.APY_KEY)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        Confirmation confirmation = gson.fromJson(response.body(), Confirmation.class);

        String autoReturn = confirmation.getAuto_return();

        if (response.statusCode() != 200 || !Objects.equals(autoReturn, "approved")){
            ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }

}
