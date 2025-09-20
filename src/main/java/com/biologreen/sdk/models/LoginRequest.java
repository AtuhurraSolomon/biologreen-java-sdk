package com.biologreen.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the JSON payload for a face login request.
 * This object will be converted to JSON and sent to the BioLogreen API.
 */
public class LoginRequest {

    @SerializedName("image_base64")
    private final String imageBase64;

    /**
     * Constructor for the login request.
     * @param imageBase64 The Base64 encoded string of the user's image.
     */
    public LoginRequest(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
