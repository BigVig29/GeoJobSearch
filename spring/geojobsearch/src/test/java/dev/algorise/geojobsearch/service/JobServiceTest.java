// Tests the getAllJobs() method of the JobService class to ensure it retrieves all jobs correctly from the repository

package dev.algorise.geojobsearch.service;

import dev.algorise.geojobsearch.model.Job;
import dev.algorise.geojobsearch.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to set up mock behavior for finding a job by ID
    private void setupJobById(Long jobId, Job job) {
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
    }

    // Helper method to verify the result of a service method call
    private void verifyJobResult(Optional<Job> result, Job expectedJob) {
        assertEquals(expectedJob, result.orElse(null));
    }

    @Test
    public void testGetJobById() {
        // Prepare
        Long jobId = 1L;
        Job expectedJob = new Job();
        setupJobById(jobId, expectedJob);

        // Execute
        Optional<Job> result = jobService.getJobById(jobId);

        // Verify
        verifyJobResult(result, expectedJob);
    }

    @Test
    public void testGetJobTitleByJobId() {
        // Prepare
        Long jobId = 1L;
        String expectedTitle = "TestTitle";
        Job job = new Job();
        job.setTitle(expectedTitle);
        setupJobById(jobId, job);

        // Execute
        String result = jobService.getJobTitleByJobId(jobId);

        // Verify
        assertEquals(expectedTitle, result);
    }

    @Test
    public void testGetJobDescriptionByJobId() {
        // Prepare
        Long jobId = 1L;
        String expectedDescription = "TestDescription";
        Job job = new Job();
        job.setDescription(expectedDescription);
        setupJobById(jobId, job);

        // Execute
        String result = jobService.getJobDescriptionByJobId(jobId);

        // Verify
        assertEquals(expectedDescription, result);
    }

    @Test
    public void testGetJobURLByJobId() {
        // Prepare
        Long jobId = 1L;
        String expectedURL = "TestURL";
        Job job = new Job();
        job.setJobURL(expectedURL);
        setupJobById(jobId, job);

        // Execute
        String result = jobService.getJobURLByJobId(jobId);

        // Verify
        assertEquals(expectedURL, result);
    }

    @Test
    public void testGetLocationByJobId() {
        // Prepare
        Long jobId = 1L;
        String expectedLocation = "TestLocation";
        Job job = new Job();
        job.setLocation(expectedLocation);
        setupJobById(jobId, job);

        // Execute
        String result = jobService.getLocationByJobId(jobId);

        // Verify
        assertEquals(expectedLocation, result);
    }

    @Test
    public void testGetSalaryByJobId() {
        // Prepare
        Long jobId = 1L;
        int expectedSalary = 50000;
        Job job = new Job();
        job.setSalary(new BigDecimal(expectedSalary));
        setupJobById(jobId, job);

        // Execute
        int result = jobService.getSalaryByJobId(jobId);

        // Verify
        assertEquals(expectedSalary, result);
    }

    @Test
    public void testFilterJobsByCity() {
        // Prepare
        String city = "TestCity";
        List<Job> expectedJobs = new ArrayList<>();
        when(jobRepository.findByCity(city)).thenReturn(expectedJobs);

        // Execute
        List<Job> result = jobService.filterJobsByCity(city);

        // Verify
        assertEquals(expectedJobs, result);
    }

    @Test
    public void testFilterByLocation() {
        // Define a sample location and jobs
        String location = "TestLocation";
        List<Job> jobsAtLocation = Arrays.asList(new Job(), new Job());

        // Mock the behavior of jobRepository.findByLocation() to return only the jobs at the specified location
        when(jobRepository.findByLocation(location)).thenReturn(jobsAtLocation);

        // Call the service method
        List<Job> filteredJobs = jobService.filterByLocation(location);

        // Verify that the filtered jobs match the expected result
        assertEquals(jobsAtLocation, filteredJobs);
    }

    @Test
    public void testGetLocationsCount() {
        // Define a sample list of location counts
        List<Object[]> locationCounts = Arrays.asList(new Object[]{"Location 1", 5}, new Object[]{"Location 2", 3});

        // Mock the behavior of jobRepository.getLocationCounts() to return the sample list
        when(jobRepository.findLocationsCount("Location",0,0)).thenReturn(locationCounts);

        // Call the service method
        List<Object[]> returnedCounts = jobService.getLocationsCount("Location",0,0);

        // Verify that the returned counts match the expected result
        assertEquals(locationCounts, returnedCounts);
    }

    @Test
    public void testGetJobsByFilter() {
        // Prepare
        String location = "TestLocation";
        String jobType = "TestJobType";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        List<Job> expectedJobs = Arrays.asList(new Job(), new Job());
        when(jobRepository.findJobByFilters(location, jobType, minSalary, maxSalary)).thenReturn(expectedJobs);

        // Execute
        List<Job> result = jobService.getJobsByFilter(location, jobType, minSalary, maxSalary);

        // Verify
        assertEquals(expectedJobs, result);
    }

    @Test
    public void testGetJobTypeCount() {
        // Prepare
        String location = "TestLocation";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        List<Object[]> expectedCounts = Arrays.asList(new Object[]{"Type 1", 5}, new Object[]{"Type 2", 3});
        when(jobRepository.findJobTypeCount(location, minSalary, maxSalary)).thenReturn(expectedCounts);

        // Execute
        List<Object[]> result = jobService.getJobTypeCount(location, minSalary, maxSalary);

        // Verify
        assertEquals(expectedCounts, result);
    }

    @Test
    public void testGetSalaryRangeCount() {
        // Prepare
        String location = "TestLocation";
        String jobType = "TestJobType";
        List<Object[]> expectedCounts = Arrays.asList(new Object[]{10000, 5}, new Object[]{20000, 3});
        when(jobRepository.findSalaryRangeCount(location, jobType)).thenReturn(expectedCounts);

        // Execute
        List<Object[]> result = jobService.getSalaryRangeCount(location, jobType);

        // Verify
        assertEquals(expectedCounts, result);
    }

    @Test
    public void testGetJobsByFilterSearch() {
        // Prepare
        String location = "TestLocation";
        String jobType = "TestJobType";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        String search = "keyword1 keyword2 keyword3";
        List<Job> expectedJobs = Arrays.asList(new Job(), new Job());
        when(jobRepository.findJobByFiltersSearch(location, jobType, minSalary, maxSalary, "keyword1", "keyword2", "keyword3"))
                .thenReturn(expectedJobs);

        // Execute
        List<Job> result = jobService.getJobsByFilterSearch(location, jobType, minSalary, maxSalary, search);

        // Verify
        assertEquals(expectedJobs, result);
    }

    @Test
    public void testGetLocationsCountSearch() {
        // Prepare
        String jobType = "TestJobType";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        String search = "keyword1 keyword2 keyword3";
        List<Object[]> expectedCounts = Arrays.asList(new Object[]{"Location 1", 5}, new Object[]{"Location 2", 3});
        when(jobRepository.findLocationCountSearch(jobType, minSalary, maxSalary, "keyword1", "keyword2", "keyword3"))
                .thenReturn(expectedCounts);

        // Execute
        List<Object[]> result = jobService.getLocationsCountSearch(jobType, minSalary, maxSalary, search);

        // Verify
        assertEquals(expectedCounts, result);
    }

    @Test
    public void testGetJobTypeCountSearch() {
        // Prepare
        String location = "TestLocation";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        String search = "keyword1 keyword2 keyword3";
        List<Object[]> expectedCounts = Arrays.asList(new Object[]{"Type 1", 5}, new Object[]{"Type 2", 3});
        when(jobRepository.findJobTypeCountSearch(location, minSalary, maxSalary, "keyword1", "keyword2", "keyword3"))
                .thenReturn(expectedCounts);

        // Execute
        List<Object[]> result = jobService.getJobTypeCountSearch(location, minSalary, maxSalary, search);

        // Verify
        assertEquals(expectedCounts, result);
    }

    @Test
    public void testGetSalaryRangeCountSearch() {
        // Prepare
        String location = "TestLocation";
        String jobType = "TestJobType";
        String search = "keyword1 keyword2 keyword3";
        List<Object[]> expectedCounts = Arrays.asList(new Object[]{10000, 5}, new Object[]{20000, 3});
        when(jobRepository.findSalaryRangeCountSearch(location, jobType, "keyword1", "keyword2", "keyword3"))
                .thenReturn(expectedCounts);

        // Execute
        List<Object[]> result = jobService.getSalaryRangeCountSearch(location, jobType, search);

        // Verify
        assertEquals(expectedCounts, result);
    }

}

