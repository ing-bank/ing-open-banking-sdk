package org.openapitools.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiClientTest {
    @Test
    void testApiClient() {
        ApiClient apiClient = new ApiClient();
        assertTrue(apiClient.getBasePath().equals("https://api.ing.com") ||
                apiClient.getBasePath().equals("https://api.sandbox.ing.com")
        );
    }
}
