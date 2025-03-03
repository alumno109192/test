package com.es.prosacyte.web.demo.exception;

import com.es.prosacyte.web.demo.exceptions.DomainException;

public class InsufficientStockException extends DomainException {
    public InsufficientStockException() {
        super("Requested quantity exceeds available stock");
    }

    public InsufficientStockException(int available, int requested) {
        super(String.format("Insufficient stock. Available: %d, Requested: %d", available, requested));
    }
}