package com.ibroximjon.trainingsummaryservice.client;

import com.ibroximjon.trainingsummaryservice.dto.TrainingEventRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class TrainingSummaryClient {

    private final RestTemplate restTemplate;

    public TrainingSummaryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendTrainingEvent(TrainingEventRequest request, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken); // JWT tokenni headerga qo‘shamiz

        HttpEntity<TrainingEventRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
                "http://localhost:8082/api/trainings/event", // agar Eureka bo‘lsa: http://training-summary-service/...
                entity,
                Void.class
        );
    }
}

