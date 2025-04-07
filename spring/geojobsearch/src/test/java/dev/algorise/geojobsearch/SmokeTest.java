// The purpose of this test is to verify that the JobController 
//class can be successfully loaded as part of the Spring application context

package dev.algorise.geojobsearch;

import static org.assertj.core.api.Assertions.assertThat;

import dev.algorise.geojobsearch.api.JobController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmokeTest {

    @Autowired
    private JobController controller;

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}
