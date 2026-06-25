package com.csci44092.notification.messaging;

import java.time.LocalDateTime;

/**
 * Event model received from RabbitMQ by the Notification Service.
 * This mirrors the OrderEvent published by the Order Service.
 * Fields must match the JSON structure sent by the producer.
 */
public class OrderEvent {

    private Long orderId;
    private String customerId;
    private LocalDateTime timestamp;

    // Default constructor required for JSON deserialization by Jackson
    public OrderEvent() {
    }

    public OrderEvent(Long orderId, String customerId, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.timestamp = timestamp;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "orderId=" + orderId +
                ", customerId='" + customerId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
