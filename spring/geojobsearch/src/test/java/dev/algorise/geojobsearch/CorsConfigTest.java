package dev.algorise.geojobsearch;

// Tests the configuration of CORS (Cross-Origin Resource Sharing) in CorsConfig class


import static org.mockito.Mockito.verify;

import org.junit.Before;


import dev.algorise.geojobsearch.config.CorsConfig;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

public class CorsConfigTest {

    @Mock
    private CorsRegistry corsRegistry;

    private CorsConfig corsConfig;

    @Before
    public void setUp() {
        // Initialize Mockito annotations
        // MockitoAnnotations.initMocks(this);
        MockitoAnnotations.openMocks(this);
        corsConfig = new CorsConfig();
    }

    @Test
    public void testCorsConfiguration() {
        // Call the addCorsMappings method of CorsConfig
        corsConfig.addCorsMappings(corsRegistry);

        // Verify that addMapping was called with the expected parameters
        verify(corsRegistry).addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}


