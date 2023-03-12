package com.skd.exception;

public class OrderItemNotInStockException extends RuntimeException {
    public OrderItemNotInStockException(String message) {
        super(message);
    }
}
