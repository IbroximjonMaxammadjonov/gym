package com.ibroximjon.trainingsummaryservice.listener;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TrainingSummaryListener {

    @JmsListener(destination = "training-summary-queue")
    public void receive(TrainingEventRequest event) {
        System.out.println("Received training event: " + event.getTrainerUsername());
        // davomiylikni hisoblash logikasi shu yerda bo‘ladi
    }
}
