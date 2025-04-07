package dev.algorise.geojobsearch.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;





public class JobRepositoryTest {

    @Test
    public void testJobRepositoryClassExists() {
        try {
            Class.forName("dev.algorise.geojobsearch.repository.JobRepository");
        } catch (ClassNotFoundException e) {
            fail("JobRepository class not found");
        }
    }
}
