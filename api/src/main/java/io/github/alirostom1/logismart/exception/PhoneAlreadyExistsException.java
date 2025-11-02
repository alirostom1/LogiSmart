package io.github.alirostom1.logismart.exception;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException(String phone) {
        super("User with phone: " + phone + " already exists!");
    }
}
