package com.biologreen.sdk.exceptions;

/**
 * A custom exception that is thrown when the BioLogreen API returns an error.
 * This allows developers to specifically catch errors from this SDK.
 */
public class BioLogreenApiException extends Exception {

    private final int statusCode;

    /**
     * Constructs a new BioLogreenApiException.
     * @param message The detail message from the API.
     * @param statusCode The HTTP status code of the error response.
     */
    public BioLogreenApiException(String message, int statusCode) {
        super(message); // Pass the message to the parent Exception class
        this.statusCode = statusCode;
    }

    /**
     * Gets the HTTP status code of the error response.
     * @return The HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        // Provide a more detailed error message
        return String.format("API Error (Status %d): %s", statusCode, super.getMessage());
    }
}
