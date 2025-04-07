package dev.algorise.geojobsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main class for the Geojobsearch application.
 */
@SpringBootApplication()
@EnableJpaRepositories("dev.algorise.geojobsearch.*")
@EntityScan("dev.algorise.geojobsearch.*")
@ComponentScan(basePackages = {"dev.algorise.geojobsearch.*"})

public class GeojobsearchApplication {
  /**
   * Main method to run the Geojobsearch application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    // Add a delay of 10 seconds (10000 milliseconds)
    try {
        Thread.sleep(10000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Now start the Spring application
    SpringApplication.run(GeojobsearchApplication.class, args);
  }
}
