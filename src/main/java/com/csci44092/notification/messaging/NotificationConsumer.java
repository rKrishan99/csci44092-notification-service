package com.csci44092.notification.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ consumer for the Notification Service.
 * Listens on the order.queue and processes incoming {@link OrderEvent} messages.
 *
 * On receiving an event, it logs a mock notification simulating what a real
 * notification system (email/SMS/push) would send to the customer.
 */
@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    /**
     * Consumes an {@link OrderEvent} from the order queue.
     * Logs a mock notification with the order and customer details.
     *
     * @param orderEvent the deserialized event received from RabbitMQ
     */
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeOrderEvent(OrderEvent orderEvent) {
        logger.info("=== NOTIFICATION SERVICE: Order Event Received ===");
        logger.info("Order ID   : {}", orderEvent.getOrderId());
        logger.info("Customer ID: {}", orderEvent.getCustomerId());
        logger.info("Timestamp  : {}", orderEvent.getTimestamp());
        logger.info("--------------------------------------------------");
        logger.info("[MOCK NOTIFICATION] Dear Customer '{}', your order #{} has been placed successfully at {}.",
                orderEvent.getCustomerId(),
                orderEvent.getOrderId(),
                orderEvent.getTimestamp());
        logger.info("=== Notification processed successfully ===");
    }
}
