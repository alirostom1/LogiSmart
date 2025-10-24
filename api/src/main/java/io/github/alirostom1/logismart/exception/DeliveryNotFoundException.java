package io.github.alirostom1.logismart.exception;

import java.util.UUID;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(UUID id) {
        super("Delivery with id: " + id + " not found!");
    }
}
