package com.ibroximjon.gym.producer;


import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrainingSummaryProducer {

    private final JmsTemplate jmsTemplate;

    public TrainingSummaryProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendTrainingEvent(TrainingEventRequest request) {
        jmsTemplate.convertAndSend("training-summary-queue", request);
    }
}

