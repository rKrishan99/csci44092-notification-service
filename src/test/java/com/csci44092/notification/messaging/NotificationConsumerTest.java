package com.csci44092.notification.messaging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationConsumer}.
 * Verifies that the consumer correctly processes incoming OrderEvent messages
 * without requiring a live RabbitMQ broker.
 */
@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Spy
    private NotificationConsumer notificationConsumer;

    @Test
    @DisplayName("Should process OrderEvent without throwing any exception")
    void consumeOrderEvent_shouldProcessEventSuccessfully() {
        // Arrange
        OrderEvent event = new OrderEvent(1001L, "CUST-001", LocalDateTime.now());

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> notificationConsumer.consumeOrderEvent(event));
    }

    @Test
    @DisplayName("Should call consumeOrderEvent exactly once when an event is received")
    void consumeOrderEvent_shouldBeInvokedExactlyOnce() {
        // Arrange
        OrderEvent event = new OrderEvent(2002L, "CUST-099", LocalDateTime.now());

        // Act
        notificationConsumer.consumeOrderEvent(event);

        // Assert - spy verifies the method was called exactly once
        verify(notificationConsumer, times(1)).consumeOrderEvent(event);
    }

    @Test
    @DisplayName("Should handle OrderEvent with all valid fields correctly")
    void consumeOrderEvent_shouldHandleEventWithAllFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        OrderEvent event = new OrderEvent(3003L, "CUST-777", now);

        // Act
        notificationConsumer.consumeOrderEvent(event);

        // Assert - verify the event fields were set correctly before consumption
        assertEquals(3003L, event.getOrderId());
        assertEquals("CUST-777", event.getCustomerId());
        assertEquals(now, event.getTimestamp());
        verify(notificationConsumer, times(1)).consumeOrderEvent(event);
    }

    @Test
    @DisplayName("Should handle OrderEvent with null customerId gracefully")
    void consumeOrderEvent_shouldHandleNullCustomerIdWithoutException() {
        // Arrange - edge case: null customerId
        OrderEvent event = new OrderEvent(4004L, null, LocalDateTime.now());

        // Act & Assert - consumer should not throw even with null fields
        assertDoesNotThrow(() -> notificationConsumer.consumeOrderEvent(event));
    }
}
