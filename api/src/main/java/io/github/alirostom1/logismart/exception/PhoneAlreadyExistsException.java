package io.github.alirostom1.logismart.exception;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException(String phone) {
        super("Courier with phone: " + phone + " already exists!");
    }
}
