BioLogreen Java SDK

The official Java SDK for the Bio-Logreen Facial Authentication API.

This SDK provides a convenient and idiomatic way for Java developers to integrate face-based signup and login into their backend applications.

Features

Simple, fluent builder for client configuration.

Methods for facial signup and login.

Automatic conversion of image files to Base64.

Handles API communication, JSON serialization, and error handling.

Built on modern libraries like OkHttp and Gson.

Installation

This SDK is published to Maven Central.

For Maven projects

To include it in your project, add the following dependency to your pom.xml:

<dependency>
    <groupId>io.github.atuhurrasolomon</groupId>
    <artifactId>biologreen-java-sdk</artifactId>
    <version>0.1.0</version> <!-- Use the latest version available -->
</dependency>


For Gradle projects

Add the following to your build.gradle file:

implementation 'io.github.atuhurrasolomon:biologreen-java-sdk:0.1.0' // Use the latest version available


Usage

Here is a basic example of how to use the client to register and authenticate a user.

import com.biologreen.sdk.BioLogreenClient;
import com.biologreen.sdk.models.FaceAuthResponse;
import com.biologreen.sdk.exceptions.BioLogreenApiException;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        // 1. Create a client using the elegant Builder pattern.
        // It's recommended to load your API key from environment variables.
        BioLogreenClient client = new BioLogreenClient.Builder("YOUR_SECRET_API_KEY")
                // When in production, you can omit setBaseUrl() to use the default live URL.
                .setBaseUrl("http://localhost:8000/v1") // Optional: for local testing
                .build();

        try {
            // --- Example: Signing up a new user ---
            System.out.println("Attempting to sign up a new user...");
            File signupImage = new File("path/to/user_signup_image.jpg");
            Map<String, Object> customData = new HashMap<>();
            customData.put("plan", "premium");
            customData.put("userId", "user-123");

            FaceAuthResponse signupResponse = client.signupWithFace(signupImage, customData);

            System.out.println("Signup Successful!");
            System.out.println("New User ID: " + signupResponse.getUserId());
            System.out.println("Is New User: " + signupResponse.isNewUser());
            System.out.println("Custom Fields: " + signupResponse.getCustomFields());

            // --- Example: Logging in an existing user ---
            System.out.println("\nAttempting to log in the user...");
            File loginImage = new File("path/to/user_login_image.jpg");

            FaceAuthResponse loginResponse = client.loginWithFace(loginImage);

            System.out.println("Login Successful!");
            System.out.println("User ID: " + loginResponse.getUserId());

        } catch (BioLogreenApiException e) {
            System.err.println("API Error: " + e.getMessage() + " (Status Code: " + e.getStatusCode() + ")");
        } catch (IOException e) {
            System.err.println("Network or File Error: " + e.getMessage());
        }
    }
}


Contributing

Suggestions and contributions are welcome. Please open an issue or a pull request on the GitHub repository to suggest changes.

License

This SDK is licenAsed under the MIT License. See the LICENSE file for more details.