package com.biologreen.sdk.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Represents the successful JSON response from the BioLogreen face authentication API.
 */
public class FaceAuthResponse {

    // The @SerializedName annotation tells the Gson library to map the JSON key
    // "user_id" to this Java field. This is useful for handling snake_case in JSON.
    @SerializedName("user_id")
    private int userId;

    @SerializedName("is_new_user")
    private boolean isNewUser;

    @SerializedName("custom_fields")
    private Map<String, Object> customFields;

    // --- Getters ---
    // Standard Java practice is to make fields private and provide public "getter" methods.

    public int getUserId() {
        return userId;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public Map<String, Object> getCustomFields() {
        return customFields;
    }

    // --- toString() for easy debugging ---

    @Override
    public String toString() {
        return "FaceAuthResponse{" +
                "userId=" + userId +
                ", isNewUser=" + isNewUser +
                ", customFields=" + customFields +
                '}';
    }
}
