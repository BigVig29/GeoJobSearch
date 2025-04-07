package dev.algorise.geojobsearch.api;


import dev.algorise.geojobsearch.model.Job;
import dev.algorise.geojobsearch.service.JobService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller class to handle job-related HTTP requests.
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController {
  private final JobService jobService;


  @Autowired
  public JobController(JobService jobService) {
    this.jobService = jobService;
  }

  /**
 * HTTP GET method to retrieve all jobs.
 *
 * @return A list of all jobs.
  */
  @GetMapping
  public List<Job> getAllJobs() {
    return jobService.getAllJobs();
  }

  @GetMapping("/{id}") 
  // Maps HTTP GET requests onto specific handler methods, with a URL template variable.
  public Optional<Job> getJobById(@PathVariable Long id) { 
    // @PathVariable binds the method parameter to a URI template variable.
    return jobService.getJobById(id);
  }


  @GetMapping("/{id}/title") // New endpoint for retrieving job title by ID
  public String getJobTitleByJobId(@PathVariable Long id) {
    return jobService.getJobTitleByJobId(id);
  }

  @GetMapping("/{id}/description") // New endpoint for retrieving job description by ID
  public String getJobDescriptionByJobId(@PathVariable Long id) {
    return jobService.getJobDescriptionByJobId(id);
  }

  @GetMapping("/{id}/url")
  public String getJobURLByJobId(@PathVariable Long id) {
    return jobService.getJobURLByJobId(id);
  }

  @GetMapping("/{id}/location")
  public String getLocationByJobId(@PathVariable Long id) {
    return jobService.getLocationByJobId(id);
  }

  @GetMapping("/{id}/salary")
  public int getSalaryByJobId(@PathVariable Long id) {
    return jobService.getSalaryByJobId(id);
  }

  @GetMapping("/filter-City")
  public List<Job> filterJobs(@RequestParam(required = false) String city) {
    if (city != null) {
      return jobService.filterJobsByCity(city);
    } else {
      // Handle case when city parameter is not provided
      // For now, let's return all jobs
      return jobService.getAllJobs();
    }
  }




  @GetMapping("/locations")
  public List<Map<String, Object>> getLocations(
      @RequestParam(required = false) String jobType,
      @RequestParam(required = false) Integer minSalary,
      @RequestParam(required = false) Integer maxSalary
  ){

      List<Object[]> locationCounts = jobService.getLocationsCount(jobType,minSalary,maxSalary);

      return locationCounts.stream()
                          .map(obj -> Map.of(

                              "location", obj[0],
                              "count", obj[1])).collect(Collectors.toList());
  }





  @GetMapping("/locations/search")
  public List<Map<String, Object>> getLocationsSearch(
      @RequestParam(required = false) String jobType,
      @RequestParam(required = false) Integer minSalary,
      @RequestParam(required = false) Integer maxSalary,
      @RequestParam(required = false) String search
  ){
    List<Object[]> locationCounts;

    if(search == null){

      locationCounts = jobService.getLocationsCount(jobType,minSalary,maxSalary);
    }else{

      locationCounts = jobService.getLocationsCountSearch(jobType,minSalary,maxSalary,search);

    }

      return locationCounts.stream()
                          .map(obj -> Map.of(

                              "location", obj[0],
                              "count", obj[1])).collect(Collectors.toList());
  }



  @GetMapping("/job-types")
    public List<Map<String, Object>> getJobTypes(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) Integer minSalary,
        @RequestParam(required = false) Integer maxsalary
    ){

        List<Object[]> jobTypeCounts = jobService.getJobTypeCount(location,minSalary,maxsalary);

        return jobTypeCounts.stream()
                            .map(obj -> Map.of(

                                "jobType", obj[0],
                                "count", obj[1])).collect(Collectors.toList());
    }


    @GetMapping("/job-types/search")
    public List<Map<String, Object>> getJobTypesSearch(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) Integer minSalary,
        @RequestParam(required = false) Integer maxSalary,
        @RequestParam(required=false) String search
    ){

      List<Object[]> jobTypeCounts;

      if(search == null){

        jobTypeCounts = jobService.getJobTypeCount(location,minSalary,maxSalary);

      }else{

         jobTypeCounts = jobService.getJobTypeCountSearch(location,minSalary,maxSalary,search);


      }

    
        return jobTypeCounts.stream()
                            .map(obj -> Map.of(

                                "jobType", obj[0],
                                "count", obj[1])).collect(Collectors.toList());
    }



    @GetMapping("/salary")
    public List<Map<String, Object>> getSalary(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String jobType
    ){

        List<Object[]> salaryRangeCounts = jobService.getSalaryRangeCount(location,jobType);

        return salaryRangeCounts.stream()
                            .map(obj -> Map.of(

                                "minSalary", obj[0],
                                "maxSalary",obj[1],
                                "count", obj[2])).collect(Collectors.toList());
    }

    @GetMapping("/salary/search")
    public List<Map<String, Object>> getSalary(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String jobType,
        @RequestParam(required = false) String search
    ){

      List<Object[]> salaryRangeCounts;

      if(search == null){

        salaryRangeCounts = jobService.getSalaryRangeCount(location,jobType);


      }else{

        salaryRangeCounts = jobService.getSalaryRangeCountSearch(location,jobType,search);

      }


        return salaryRangeCounts.stream()
                            .map(obj -> Map.of(

                                "minSalary", obj[0],
                                "maxSalary",obj[1],
                                "count", obj[2])).collect(Collectors.toList());
    }


    @GetMapping("/filter")
    public List<Job> getJobByFilter(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String jobType,
        @RequestParam(required = false) Integer minSalary,
        @RequestParam(required = false) Integer maxSalary
    ){

        
        return jobService.getJobsByFilter(location, jobType, minSalary, maxSalary);

    }



    @GetMapping("/filter/search")
    public List<Job> getJobByFilter(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String jobType,
        @RequestParam(required = false) Integer minSalary,
        @RequestParam(required = false) Integer maxSalary,
        @RequestParam(required = false) String search
    ){

      if(search == null){

          return jobService.getJobsByFilter(location, jobType, minSalary, maxSalary);


      }else{

          return jobService.getJobsByFilterSearch(location, jobType, minSalary, maxSalary,search);

      }


    }

    @GetMapping("/search")
    public List<Job> updateJobBySearch(
        @RequestParam(required = false) String keyword)
    {

      

        List<Job> searchList = jobService.updateJobsBySearch(jobService.getAllJobs(), keyword);

        if(!searchList.isEmpty()){
            return searchList;
        }else{
            return jobService.getAllJobs();
        }

    }


    @GetMapping("/sort")
    public List<Job> getJobBySort(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String jobType,
        @RequestParam(required = false) Integer minSalary,
        @RequestParam(required = false) Integer maxSalary,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String sortBy
    ){

  
          return jobService.getJobsBySort(location, jobType, minSalary, maxSalary,search,sortBy);

    }


    @GetMapping("/coordinates")
    public List<Object[]> getJobCoordinates(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String jobType,
        @RequestParam(required = false) Integer minSalary,
        @RequestParam(required = false) Integer maxSalary,
        @RequestParam(required = false) String search
    ){

  
       return  jobService.getJobByCoordinates(location, jobType, minSalary, maxSalary,search);
    }



}
