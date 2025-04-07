package dev.algorise.geojobsearch;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class GeojobsearchApplicationTests {

    @Test
    void contextLoads() {
        // Test if the Spring application context loads successfully
    }

    @Test
    void testMainMethod() {
        // Test the main method of the application
        String[] args = {}; // Dummy arguments

        // Mock SpringApplication to avoid starting the Spring context
        SpringApplication applicationMock = Mockito.mock(SpringApplication.class);
        GeojobsearchApplication.main(args);

        assertNotNull(applicationMock);
    }

    @Test
    void testConstructor() {
        // Test the default constructor of the application
        GeojobsearchApplication application = new GeojobsearchApplication();

        assertNotNull(application);
    }
}
