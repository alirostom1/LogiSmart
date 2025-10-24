package io.github.alirostom1.logismart.exception;

import java.util.UUID;

public class CourierNotFoundException extends RuntimeException {
    public CourierNotFoundException(UUID id) {
        super("Courier with id: " + id + " not found!");
    }
}
