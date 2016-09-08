package com.github.amznlabs.swa_sample_seller;

/**
 * This exception should be thrown whenever Sample Seller encounters an unexpected exception,
 * indicating there is a bug in Sample Seller.
 */
public class SampleSellerRuntimeException extends RuntimeException {
    public SampleSellerRuntimeException(String message) {
        super("Bug in Sample Seller code: " + message);
    }

    public SampleSellerRuntimeException(Throwable cause) {
        super(cause);
    }

    public SampleSellerRuntimeException(String message, Throwable cause) {
        super("Bug in Sample Seller code: " + message, cause);
    }
}
