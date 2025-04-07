package dev.algorise.geojobsearch.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JobTest {
    private Job job;

    @BeforeEach
    public void setUp() {
        job = new Job();
        job.setJobID(1L);
        job.setTitle("Software Engineer");
        job.setCompany("Algorise");
        job.setLocation("Remote");
        job.setCity("Any");
        job.setProvince("Any");
        job.setDescription("Description of the job");
        job.setSalary(BigDecimal.valueOf(100000));
        job.setJobType("Full-time");
        job.setDate(new Date());
        job.setJobURL("https://example.com");
    }

    @Test
    public void testJobConstructorAndGetters() {
        assertEquals(1L, job.getJobID());
        assertEquals("Software Engineer", job.getTitle());
        assertEquals("Algorise", job.getCompany());
        assertEquals("Remote", job.getLocation());
        assertEquals("Any", job.getCity());
        assertEquals("Any", job.getProvince());
        assertEquals("Description of the job", job.getDescription());
        assertEquals(BigDecimal.valueOf(100000), job.getSalary());
        assertEquals("Full-time", job.getJobType());
        assertNotNull(job.getDate());
        assertEquals("https://example.com", job.getJobURL());
    }

    @Test
    public void testSetAndGetDate() {
        Date date = new Date();
        job.setDate(date);
        assertEquals(date, job.getDate());
    }

    @Test
    public void testSetAndGetDateWithNull() {
        job.setDate(null);
        assertNull(job.getDate());
    }

    // Additional tests for setters
    @Test
    public void testSetAndGetJobID() {
        job.setJobID(1L);
        assertEquals(1L, job.getJobID());
    }

    @Test
    public void testSetAndGetTitle() {
        job.setTitle("Software Engineer");
        assertEquals("Software Engineer", job.getTitle());
    }

    @Test
    public void testSetAndGetCompany() {
        job.setCompany("Algorise");
        assertEquals("Algorise", job.getCompany());
    }

    @Test
    public void testSetAndGetLocation() {
        job.setLocation("Remote");
        assertEquals("Remote", job.getLocation());
    }

    @Test
    public void testSetAndGetCity() {
        job.setCity("Any");
        assertEquals("Any", job.getCity());
    }

    @Test
    public void testSetAndGetProvince() {
        job.setProvince("Any");
        assertEquals("Any", job.getProvince());
    }

    @Test
    public void testSetAndGetDescription() {
        job.setDescription("Description of the job");
        assertEquals("Description of the job", job.getDescription());
    }

    @Test
    public void testSetAndGetSalary() {
        job.setSalary(BigDecimal.valueOf(100000));
        assertEquals(BigDecimal.valueOf(100000), job.getSalary());
    }

    @Test
    public void testSetAndGetJobType() {
        job.setJobType("Full-time");
        assertEquals("Full-time", job.getJobType());
    }

    @Test
    public void testSetAndGetJobURL() {
        job.setJobURL("https://example.com");
        assertEquals("https://example.com", job.getJobURL());
    }
}
