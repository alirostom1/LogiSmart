package io.github.alirostom1.logismart.exception;

public class ProductNameAlreadyExistsException extends RuntimeException {
  public ProductNameAlreadyExistsException(String name) {
      super("Product with name '" + name + "' already exists");
  }
}
