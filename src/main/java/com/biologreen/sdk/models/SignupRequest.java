package com.biologreen.sdk.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Represents the JSON payload for a face signup request.
 * This object will be converted to JSON and sent to the BioLogreen API.
 */
public class SignupRequest {

    // The 'transient' keyword tells Gson to ignore this field when converting to JSON.
    // We only need the imageBase64 field in the final JSON payload.
    @SerializedName("image_base64")
    private final String imageBase64;

    @SerializedName("custom_fields")
    private final Map<String, Object> customFields;

    /**
     * Constructor for the signup request.
     * @param imageBase64 The Base64 encoded string of the user's image.
     * @param customFields A map of optional custom data to associate with the user.
     */
    public SignupRequest(String imageBase64, Map<String, Object> customFields) {
        this.imageBase64 = imageBase64;
        this.customFields = customFields;
    }
}
