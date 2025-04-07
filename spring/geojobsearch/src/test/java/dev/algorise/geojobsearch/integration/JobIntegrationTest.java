package dev.algorise.geojobsearch.integration;

import dev.algorise.geojobsearch.api.JobController;
import dev.algorise.geojobsearch.model.Job;
import dev.algorise.geojobsearch.service.JobService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class JobIntegrationTest {

    //Mock for api endpoints
    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    @BeforeEach
    public void set() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllJobs() {
        //Tests the /api/jobs endpoint
        List<Job> jobs = Arrays.asList(new Job(), new Job());
        when(jobService.getAllJobs()).thenReturn(jobs);

        List<Job> returnedJobs = jobController.getAllJobs();

        assertEquals(jobs, returnedJobs);
    }

    @Test
    public void testGetJobById() {
        //Tests the /api/{id} endpoint
        Long id = 99999L;
        Job job = new Job();
        when(jobService.getJobById(id)).thenReturn(Optional.of(job));

        Optional<Job> returnedJob = jobController.getJobById(id);

        assertEquals(job, returnedJob.get());
    }

    @Test
    public void testGetJobTitleByJobId() {
        //Tests the /api/{id}/title endpoint
        Long id = 1L;
        String title = "Software Engineer";
        when(jobService.getJobTitleByJobId(id)).thenReturn(title);

        String returnedTitle = jobController.getJobTitleByJobId(id);

        assertEquals(title, returnedTitle);
    }

    @Test
    public void testGetJobDescriptionByJobId() {
        //Tests the /api/{id}/description endpoint
        Long id = 1L;
        String description = "We are looking for a skilled software engineer to join our team.";
        when(jobService.getJobDescriptionByJobId(id)).thenReturn(description);

        String returnedDescription = jobController.getJobDescriptionByJobId(id);

        assertEquals(description, returnedDescription);
    }

    @Test
    public void testGetJobURLByJobId() {
        //Tests the /api/{id}/url endpoint
        Long id = 1L;
        String url = "https://example.com/job001";
        when(jobService.getJobURLByJobId(id)).thenReturn(url);

        String returnedURL = jobController.getJobURLByJobId(id);

        assertEquals(url, returnedURL);
    }

    @Test
    public void testGetLocationByJobId() {
        //Tests the /api/{id}/location endpoint
        Long id = 1L;
        String location = "San Francisco, CA";
        when(jobService.getLocationByJobId(id)).thenReturn(location);

        String returnedLocation = jobController.getLocationByJobId(id);

        assertEquals(location, returnedLocation);
    }

    @Test
    public void testGetSalaryByJobId() {
        //Tests the /api/{id}/salary endpoint
        Long id = 1L;
        int salary = 100000;
        when(jobService.getSalaryByJobId(id)).thenReturn(salary);

        int returnedSalary = jobController.getSalaryByJobId(id);

        assertEquals(salary, returnedSalary);
    }

    @Test
    public void testFilterJobsByCity_SpecificCity() {
        //Tests the /api/jobs/filter-City endpoint
        // Mocking the behavior of jobService.filterJobsByCity(city) to return a list of jobs filtered by the city "San Francisco"
        when(jobService.filterJobsByCity("San Francisco")).thenReturn(Arrays.asList(new Job(), new Job()));

        // Calling the controller method to filter jobs by the city "San Francisco"
        List<Job> filteredJobs = jobController.filterJobs("San Francisco");

        // Asserting that the returned list contains at least one job
        assertEquals(2, filteredJobs.size());
    }

    @Test
    public void testGetLocations() {
        //Tests the /api/jobs/locations endpoint
        // Mocking the behavior of jobService.getLocationsCount(jobType, minSalary, maxSalary)
        when(jobService.getLocationsCount(null, null, null))
            .thenReturn(Arrays.asList(
                new Object[]{"San Francisco, CA", 1},
                new Object[]{"New York, NY", 1},
                new Object[]{"Seattle, WA", 1}
            ));

        // Calling the controller method to get locations
        List<Map<String, Object>> locations = jobController.getLocations(null, null, null);

        // Asserting the count of locations
        assertEquals(3, locations.size());
        // Asserting the location and count
        assertEquals("San Francisco, CA", locations.get(0).get("location"));
        assertEquals(1, locations.get(0).get("count"));
        assertEquals("New York, NY", locations.get(1).get("location"));
        assertEquals(1, locations.get(1).get("count"));
        assertEquals("Seattle, WA", locations.get(2).get("location"));
        assertEquals(1, locations.get(2).get("count"));
    }

    @Test
    public void testGetJobTypes() {
        //Tests the /api/jobs/job-types endpoint
        // Mocking the behavior of jobService.getJobTypeCount(location, minSalary, maxSalary)
        when(jobService.getJobTypeCount(null, null, null))
            .thenReturn(Arrays.asList(
                new Object[]{"Full-time", 3},
                new Object[]{"Full-time", 3}
            ));

        // Calling the controller method to get job types
        List<Map<String, Object>> jobTypes = jobController.getJobTypes(null, null, null);

        // Asserting the count of job types
        assertEquals(2, jobTypes.size());
        // Asserting the job type and count
        assertEquals("Full-time", jobTypes.get(0).get("jobType"));
        assertEquals(3, jobTypes.get(0).get("count"));
    }

    @Test
    public void testGetSalary() {
        //Tests the /api/jobs/salary endpoint
        // Mocking the behavior of jobService.getSalaryRangeCount(location, jobType)
        when(jobService.getSalaryRangeCount(null, null))
            .thenReturn(Arrays.asList(
                new Object[]{90000, 100000, 3},
                new Object[]{100000, 85000, 2}
            ));

        // Calling the controller method to get salary ranges
        List<Map<String, Object>> salaryRanges = jobController.getSalary(null, null);

        // Asserting the count of salary ranges
        assertEquals(2, salaryRanges.size());
        // Asserting the salary ranges and their counts
        assertEquals(90000, salaryRanges.get(0).get("minSalary"));
        assertEquals(100000, salaryRanges.get(0).get("maxSalary"));
        assertEquals(3, salaryRanges.get(0).get("count"));
    }

    @Test
    public void testGetJobByFilter_Location() {
        //Tests the /api/jobs/filter endpoint
        // Mocking the behavior of jobService.getJobsByFilter() to return jobs filtered by location
        when(jobService.getJobsByFilter("San Francisco, CA", null, null, null))
            .thenReturn(Arrays.asList(
                new Job(), new Job()
            ));

        // Calling the controller method to get jobs filtered by location
        List<Job> filteredJobs = jobController.getJobByFilter("San Francisco, CA", null, null, null);

        // Asserting the count of returned jobs
        assertEquals(2, filteredJobs.size());
    }


    @Test
    public void testUpdateJobBySearch_KeywordFound() {
        //This tests the /api/jobs/search endpoint
        // Mocking the behavior of jobService.updateJobsBySearch() to return jobs containing the keyword "Software"
        when(jobService.updateJobsBySearch(jobService.getAllJobs(), "Software"))
            .thenReturn(Arrays.asList(
                new Job(), new Job()
            ));

        // Calling the controller method to update jobs by search with the keyword "Software"
        List<Job> updatedJobs = jobController.updateJobBySearch("Software");

        // Asserting the count of returned jobs
        assertEquals(2, updatedJobs.size());
    }

    @Test
    public void testGetLocationsSearch() {
        //Tests the /api/jobs/locations/search endpoint
        // Mock data
        List<Object[]> mockLocationCounts = Arrays.asList(
                new Object[]{"New York, NY", 5},
                new Object[]{"San Francisco, CA", 3}
        );
        // Mock service behavior
        when(jobService.getLocationsCountSearch(null, null, null, "Software Engineer")).thenReturn(mockLocationCounts);

        // Call the controller method
        List<Map<String, Object>> result = jobController.getLocationsSearch(null, null, null, "Software Engineer");

        // Assert
        assertEquals(mockLocationCounts.size(), result.size());
        for (int i = 0; i < mockLocationCounts.size(); i++) {
            Map<String, Object> expectedResult = Map.of(
                    "location", mockLocationCounts.get(i)[0],
                    "count", mockLocationCounts.get(i)[1]
            );
            Map<String, Object> actualResult = result.get(i);
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void testGetJobTypesSearch() {
        //Tests the /api/jobs/job-types/search endpoint
        // Mock data
        List<Object[]> mockJobTypeCounts = Arrays.asList(
                new Object[]{"Full-time", 10},
                new Object[]{"Part-time", 3}
        );
        // Mock service behavior
        when(jobService.getJobTypeCountSearch(null, null, null, "Software Engineer")).thenReturn(mockJobTypeCounts);

        // Call the controller method
        List<Map<String, Object>> result = jobController.getJobTypesSearch(null, null, null, "Software Engineer");

        // Assert
        assertEquals(mockJobTypeCounts.size(), result.size());
        for (int i = 0; i < mockJobTypeCounts.size(); i++) {
            Map<String, Object> expectedResult = Map.of(
                    "jobType", mockJobTypeCounts.get(i)[0],
                    "count", mockJobTypeCounts.get(i)[1]
            );
            Map<String, Object> actualResult = result.get(i);
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void testGetSalarySearch() {
        //Tests the /api/jobs/salary/search endpoint
        // Mock data
        List<Object[]> mockSalaryRangeCounts = Arrays.asList(
                new Object[]{80000, 120000, 7},
                new Object[]{60000, 100000, 3}
        );
        // Mock service behavior
        when(jobService.getSalaryRangeCountSearch(null, null, "Software Engineer")).thenReturn(mockSalaryRangeCounts);

        // Call the controller method
        List<Map<String, Object>> result = jobController.getSalary(null, null, "Software Engineer");

        // Assert
        assertEquals(mockSalaryRangeCounts.size(), result.size());
        for (int i = 0; i < mockSalaryRangeCounts.size(); i++) {
            Map<String, Object> expectedResult = Map.of(
                    "minSalary", mockSalaryRangeCounts.get(i)[0],
                    "maxSalary", mockSalaryRangeCounts.get(i)[1],
                    "count", mockSalaryRangeCounts.get(i)[2]
            );
            Map<String, Object> actualResult = result.get(i);
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void testGetJobBySort() {
        //This tests the /api/jobs/sort endpoint
        // Mock data
        List<Job> mockJobs = Arrays.asList(
                new Job(),
                new Job(),
                new Job()
        );
        // Mock service behavior
        when(jobService.getJobsBySort(null, null, null, null, null, null)).thenReturn(mockJobs);

        // Call the controller method
        List<Job> result = jobController.getJobBySort(null, null, null, null, null, null);

        // Assert
        assertEquals(mockJobs.size(), result.size());
        for (int i = 0; i < mockJobs.size(); i++) {
            Job expected = mockJobs.get(i);
            Job actual = result.get(i);
            assertEquals(expected.getJobID(), actual.getJobID());
            assertEquals(expected.getTitle(), actual.getTitle());
            assertEquals(expected.getCompany(), actual.getCompany());
            assertEquals(expected.getLocation(), actual.getLocation());
            assertEquals(expected.getJobType(), actual.getJobType());
            assertEquals(expected.getSalary(), actual.getSalary());
            assertEquals(expected.getJobURL(), actual.getJobURL());
        }
    }

}


