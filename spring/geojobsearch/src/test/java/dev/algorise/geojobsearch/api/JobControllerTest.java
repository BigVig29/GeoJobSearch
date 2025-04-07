package dev.algorise.geojobsearch.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.algorise.geojobsearch.model.Job;
import dev.algorise.geojobsearch.service.JobService;

public class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllJobs() {
        List<Job> jobs = Arrays.asList(new Job(), new Job());
        when(jobService.getAllJobs()).thenReturn(jobs);

        List<Job> returnedJobs = jobController.getAllJobs();

        assertEquals(jobs, returnedJobs);
    }

    @Test
    public void testGetJobById() {
        Long id = 1L;
        Job job = new Job();
        when(jobService.getJobById(id)).thenReturn(Optional.of(job));

        Optional<Job> returnedJob = jobController.getJobById(id);

        assertEquals(job, returnedJob.get());
    }

    @Test
    public void testGetJobTitleByJobId() {
        Long id = 1L;
        String title = "TestTitle";
        when(jobService.getJobTitleByJobId(id)).thenReturn(title);

        String returnedTitle = jobController.getJobTitleByJobId(id);

        assertEquals(title, returnedTitle);
    }

    @Test
    public void testGetJobDescriptionByJobId() {
        Long id = 1L;
        String description = "TestDescription";
        when(jobService.getJobDescriptionByJobId(id)).thenReturn(description);

        String returnedDescription = jobController.getJobDescriptionByJobId(id);

        assertEquals(description, returnedDescription);
    }

    @Test
    public void testGetJobURLByJobId() {
        Long id = 1L;
        String url = "TestURL";
        when(jobService.getJobURLByJobId(id)).thenReturn(url);

        String returnedURL = jobController.getJobURLByJobId(id);

        assertEquals(url, returnedURL);
    }

    @Test
    public void testGetLocationByJobId() {
        Long id = 1L;
        String location = "TestLocation";
        when(jobService.getLocationByJobId(id)).thenReturn(location);

        String returnedLocation = jobController.getLocationByJobId(id);

        assertEquals(location, returnedLocation);
    }

    @Test
    public void testGetSalaryByJobId() {
        Long id = 1L;
        int salary = 50000;
        when(jobService.getSalaryByJobId(id)).thenReturn(salary);

        int returnedSalary = jobController.getSalaryByJobId(id);

        assertEquals(salary, returnedSalary);
    }

    @Test
    public void testFilterJobs() {
        // Arrange
        String city = "TestCity";
        List<Job> filteredJobs = Arrays.asList(new Job(), new Job());
        when(jobService.filterJobsByCity(city)).thenReturn(filteredJobs);
        
        // Act
        List<Job> returnedJobs = jobController.filterJobs(city);
        
        // Assert
        assertEquals(filteredJobs, returnedJobs);
        
        // Additional assertion to ensure that getAllJobs is called if city parameter is null
        when(jobService.getAllJobs()).thenReturn(filteredJobs);
        returnedJobs = jobController.filterJobs(null);
        assertEquals(filteredJobs, returnedJobs);
    }


    @Test
    public void testGetLocations() {
        List<Object[]> locationCounts = Arrays.asList(new Object[]{"Location 1", 5}, new Object[]{"Location 2", 3});
        // Mock the behavior of jobService.getLocationsCount() to return the list of location counts
        when(jobService.getLocationsCount("Location",0,0)).thenReturn(locationCounts);

        // Call the controller method to get the list of locations
        List<Map<String, Object>> locations = jobController.getLocations("Location",0,0);

        // Assert that the returned locations list matches the expected result
        assertEquals(locationCounts.size(), locations.size());
        for (int i = 0; i < locationCounts.size(); i++) {
            Object[] locationCount = locationCounts.get(i);
            Map<String, Object> location = locations.get(i);
            assertEquals(locationCount[0], location.get("location"));
            assertEquals(locationCount[1], location.get("count"));
        }
    }

    @Test
    public void testGetJobTypes() {
    String location = "TestLocation";
    Integer minSalary = 10000;
    Integer maxSalary = 50000;
    List<Object[]> jobTypeCounts = Arrays.asList(new Object[]{"Type 1", 5}, new Object[]{"Type 2", 3});
    when(jobService.getJobTypeCount(location, minSalary, maxSalary)).thenReturn(jobTypeCounts);

    List<Map<String, Object>> jobTypes = jobController.getJobTypes(location, minSalary, maxSalary);

    assertEquals(jobTypeCounts.size(), jobTypes.size());
    for (int i = 0; i < jobTypeCounts.size(); i++) {
        Object[] jobTypeCount = jobTypeCounts.get(i);
        Map<String, Object> jobType = jobTypes.get(i);
        assertEquals(jobTypeCount[0], jobType.get("jobType"));
        assertEquals(jobTypeCount[1], jobType.get("count"));
    }
}

    @Test
    public void testGetSalary() {
        String location = "TestLocation";
        String jobType = "TestType";
        List<Object[]> salaryRangeCounts = Arrays.asList(new Object[]{10000, 50000, 5}, new Object[]{50000, 100000, 3});
        when(jobService.getSalaryRangeCount(location, jobType)).thenReturn(salaryRangeCounts);

        List<Map<String, Object>> salaryRanges = jobController.getSalary(location, jobType);

        assertEquals(salaryRangeCounts.size(), salaryRanges.size());
        for (int i = 0; i < salaryRangeCounts.size(); i++) {
            Object[] salaryRangeCount = salaryRangeCounts.get(i);
            Map<String, Object> salaryRange = salaryRanges.get(i);
            assertEquals(salaryRangeCount[0], salaryRange.get("minSalary"));
            assertEquals(salaryRangeCount[1], salaryRange.get("maxSalary"));
            assertEquals(salaryRangeCount[2], salaryRange.get("count"));
        }
    }

    @Test
    public void testGetJobByFilter() {
        String location = "TestLocation";
        String jobType = "TestType";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        List<Job> filteredJobs = Arrays.asList(new Job(), new Job());
        when(jobService.getJobsByFilter(location, jobType, minSalary, maxSalary)).thenReturn(filteredJobs);

        List<Job> returnedJobs = jobController.getJobByFilter(location, jobType, minSalary, maxSalary);

        assertEquals(filteredJobs, returnedJobs);
    }

    @Test
    public void testUpdateJobBySearchWithKeyword() {
        // Arrange
        String keyword = "test";
        List<Job> jobs = Arrays.asList(new Job(), new Job());
        when(jobService.updateJobsBySearch(jobService.getAllJobs(), keyword)).thenReturn(jobs);

        // Act
        List<Job> returnedJobs = jobController.updateJobBySearch(keyword);

        // Assert
        assertEquals(jobs, returnedJobs);
    }

    @Test
    public void testUpdateJobBySearchWithEmptyKeyword() {
        // Arrange
        List<Job> jobs = Arrays.asList(new Job(), new Job());
        when(jobService.getAllJobs()).thenReturn(jobs);

        // Act
        List<Job> returnedJobs = jobController.updateJobBySearch(null);

        // Assert
        assertEquals(jobs, returnedJobs);
    }

    @Test
    public void testGetSalaryWithNullLocationAndJobType() {
        // Arrange
        List<Object[]> salaryRangeCounts = Arrays.asList(new Object[]{10000, 50000, 5}, new Object[]{50000, 100000, 3});
        when(jobService.getSalaryRangeCount(null, null)).thenReturn(salaryRangeCounts);

        // Act
        List<Map<String, Object>> salaryRanges = jobController.getSalary(null, null);

        // Assert
        assertEquals(salaryRangeCounts.size(), salaryRanges.size());
        for (int i = 0; i < salaryRangeCounts.size(); i++) {
            Object[] salaryRangeCount = salaryRangeCounts.get(i);
            Map<String, Object> salaryRange = salaryRanges.get(i);
            assertEquals(salaryRangeCount[0], salaryRange.get("minSalary"));
            assertEquals(salaryRangeCount[1], salaryRange.get("maxSalary"));
            assertEquals(salaryRangeCount[2], salaryRange.get("count"));
        }
    }

    @Test
    public void testGetLocationsSearch() {
        // Prepare
        String keyword = "example";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        List<Object[]> locationCounts = Arrays.asList(new Object[]{"Location 1", 5}, new Object[]{"Location 2", 3});
        when(jobService.getLocationsCountSearch(keyword, minSalary, maxSalary, keyword)).thenReturn(locationCounts);

        // Execute
        List<Map<String, Object>> result = jobController.getLocationsSearch(keyword, minSalary, maxSalary, keyword);

        // Verify
        assertEquals(locationCounts.size(), result.size());
    }

    @Test
    public void testGetJobTypesSearch() {
        // Prepare
        String keyword = "example";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        String location = "TestLocation";
        List<Object[]> jobTypeCounts = Arrays.asList(new Object[]{"Type 1", 5}, new Object[]{"Type 2", 3});
        when(jobService.getJobTypeCountSearch(location, minSalary, maxSalary, keyword)).thenReturn(jobTypeCounts);

        // Execute
        List<Map<String, Object>> result = jobController.getJobTypesSearch(location, minSalary, maxSalary, keyword);

        // Verify
        assertEquals(jobTypeCounts.size(), result.size());
    }

    @Test
    public void testGetJobByFilterWithSearch() {
        // Prepare
        String location = "TestLocation";
        String jobType = "TestType";
        Integer minSalary = 10000;
        Integer maxSalary = 50000;
        String search = "TestSearch";
        List<Job> expectedJobs = Arrays.asList(new Job(), new Job());
        when(jobService.getJobsByFilterSearch(location, jobType, minSalary, maxSalary, search)).thenReturn(expectedJobs);

        // Execute
        List<Job> result = jobController.getJobByFilter(location, jobType, minSalary, maxSalary, search);

        // Verify
        assertEquals(expectedJobs.size(), result.size());
    }
}
