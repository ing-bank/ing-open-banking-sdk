package org.openapitools.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiClientTest {
    @Test
    public void testApiClient() {
        assertEquals("my_url_here", new ApiClient().getBasePath());
    }
}
