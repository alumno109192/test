package com.es.prosacyte.web.demo.exception;

/**
 * Thrown when attempting to release more inventory than was reserved
 */
public class InvalidReleaseQuantityException extends DomainException {

    public InvalidReleaseQuantityException() {
        super("Cannot release more quantity than what's reserved");
    }

    public InvalidReleaseQuantityException(int reserved, int requested) {
        super(String.format(
                "Invalid release quantity. Reserved: %d, Attempted release: %d",
                reserved,
                requested
        ));
    }
}
