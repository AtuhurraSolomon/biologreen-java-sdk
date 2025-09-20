package com.biologreen.sdk;

import com.biologreen.sdk.exceptions.BioLogreenApiException;
import com.biologreen.sdk.models.FaceAuthResponse;
import com.biologreen.sdk.models.LoginRequest;
import com.biologreen.sdk.models.SignupRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The main client for interacting with the BioLogreen Facial Authentication API.
 */
public class BioLogreenClient {
    private static final String DEFAULT_BASE_URL = "https://api.biologreen.com/v1";
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    private final String baseUrl;

    private BioLogreenClient(Builder builder) {
        this.apiKey = Objects.requireNonNull(builder.apiKey, "API key cannot be null.");
        this.baseUrl = builder.baseUrl;
        this.gson = new Gson();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Registers a new user by their face.
     *
     * @param imageFile The image file of the user's face.
     * @param customFields A map of optional custom data to associate with the user.
     * @return A {@link FaceAuthResponse} object with the new user's details.
     * @throws IOException If there is an error reading the image file or a network issue.
     * @throws BioLogreenApiException If the API returns an error.
     */
    public FaceAuthResponse signupWithFace(File imageFile, Map<String, Object> customFields) throws IOException, BioLogreenApiException {
        String imageBase64 = imageFileToBase64(imageFile);
        SignupRequest requestPayload = new SignupRequest(imageBase64, customFields);
        return performPost("/auth/signup-face", requestPayload, FaceAuthResponse.class);
    }

    /**
     * Authenticates an existing user by their face.
     *
     * @param imageFile The image file of the user's face.
     * @return A {@link FaceAuthResponse} object with the matched user's details.
     * @throws IOException If there is an error reading the image file or a network issue.
     * @throws BioLogreenApiException If the API returns an error.
     */
    public FaceAuthResponse loginWithFace(File imageFile) throws IOException, BioLogreenApiException {
        String imageBase64 = imageFileToBase64(imageFile);
        LoginRequest requestPayload = new LoginRequest(imageBase64);
        return performPost("/auth/login-face", requestPayload, FaceAuthResponse.class);
    }

    /**
     * A helper utility to convert an image file into a Base64 encoded string.
     *
     * @param imageFile The file to encode.
     * @return A Base64 string representation of the image.
     * @throws IOException If the file cannot be read.
     */
    public static String imageFileToBase64(File imageFile) throws IOException {
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private <T> T performPost(String endpoint, Object payload, Class<T> responseClass) throws IOException, BioLogreenApiException {
        String jsonPayload = gson.toJson(payload);
        RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(this.baseUrl + endpoint)
                .header("X-API-KEY", this.apiKey)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBodyString = Objects.requireNonNull(response.body()).string();

            if (!response.isSuccessful()) {
                // Try to parse a structured error
                try {
                    Type mapType = new TypeToken<Map<String, String>>() {}.getType();
                    Map<String, String> errorBody = gson.fromJson(responseBodyString, mapType);
                    String detail = errorBody.getOrDefault("detail", "An unknown API error occurred.");
                    throw new BioLogreenApiException(detail, response.code());
                } catch (JsonSyntaxException e) {
                    // Fallback for non-JSON error responses
                    throw new BioLogreenApiException(responseBodyString, response.code());
                }
            }

            return gson.fromJson(responseBodyString, responseClass);
        }
    }


    /**
     * A builder for creating an instance of the {@link BioLogreenClient}.
     */
    public static class Builder {
        private final String apiKey;
        private String baseUrl = DEFAULT_BASE_URL;

        /**
         * The required constructor for the Builder.
         * @param apiKey Your secret API key from the BioLogreen developer dashboard.
         */
        public Builder(String apiKey) {
            this.apiKey = apiKey;
        }

        /**
         * Optional: Sets a custom base URL for the API. Useful for testing or proxies.
         * If not set, it defaults to the production BioLogreen API URL.
         * @param baseUrl The custom base URL (e.g., "http://localhost:8000/v1").
         * @return The builder instance for chaining.
         */
        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * Builds and returns the configured {@link BioLogreenClient}.
         * @return A new instance of the client.
         */
        public BioLogreenClient build() {
            return new BioLogreenClient(this);
        }
    }
}

