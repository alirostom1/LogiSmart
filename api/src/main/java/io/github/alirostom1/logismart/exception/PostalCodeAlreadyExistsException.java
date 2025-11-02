package io.github.alirostom1.logismart.exception;

public class PostalCodeAlreadyExistsException extends RuntimeException {
    public PostalCodeAlreadyExistsException(int postalCode) {
        super("Zone with postal code: " + postalCode + " already exists!");
    }
}
