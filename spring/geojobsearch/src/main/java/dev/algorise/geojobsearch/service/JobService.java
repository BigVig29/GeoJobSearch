package dev.algorise.geojobsearch.service;

import dev.algorise.geojobsearch.model.Job;
import dev.algorise.geojobsearch.repository.JobRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * Job service layer
 */
@Service // Indicates that the class is a service provider, a business logic holder.
public class JobService {
  
  @Autowired // Automatically injects the instance of OrderRepository into the OrderService.
  private final JobRepository jobRepository;

  public JobService(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  // RESTful Services
  /**
   * get all jobs.
   *
   *@return list of jobs
   */
  public List<Job> getAllJobs() {
    return jobRepository.findAll();
    
  }

  public Optional<Job> getJobById(Long id) {
    return jobRepository.findById(id);
  }

  public String getJobTitleByJobId(Long id) {
    Optional<Job> optionalJob = jobRepository.findById(id);
    return optionalJob.map(Job::getTitle).orElse("Job not found");
  }

  public String getJobDescriptionByJobId(Long id) {
    Optional<Job> optionalJob = jobRepository.findById(id);
    return optionalJob.map(Job::getDescription).orElse("Job not found");
  }

  public String getJobURLByJobId(Long id) {
    Optional<Job> optionalJob = jobRepository.findById(id);
    return optionalJob.map(Job::getJobURL).orElse("Job not found");
  }

  public String getLocationByJobId(Long id) {
    Optional<Job> optionalJob = jobRepository.findById(id);
    return optionalJob.map(Job::getLocation).orElse("Job not found");
  }

  public int getSalaryByJobId(Long id) {
    Optional<Job> optionalJob = jobRepository.findById(id);
    return optionalJob.map(job -> job.getSalary().intValue()).orElse(0);
  }

  public List<Job> filterJobsByCity(String city) {
    return jobRepository.findByCity(city);
  }




  /**
   * Get location count
   * @param jobType - a job type as a String
   * @param minSalary - a minimum salary as an Integer
   * @param maxSalary - a maximum salary as an Integer
   * @return - return a list of locations and their counts
   */
  public List<Object[]> getLocationsCount(String jobType, Integer minSalary, Integer maxSalary){

    return jobRepository.findLocationsCount(jobType,minSalary,maxSalary);
  }


  /**
   * Get location Search count
   * @param jobType - a job type as a String
   * @param minSalary - a minimum salary as an Integer
   * @param maxSalary - a maximum salary as an Integer
   * @return - return a list of locations and their counts
   */
  public List<Object[]> getLocationsCountSearch(String jobType, Integer minSalary, Integer maxSalary, String search){

    String keywordOne = null;
    String keywordTwo = null;
    String keywordThree = null;
     
    //Split search string into keywords
    String[] keywords = search.toLowerCase().split("\\s+",4);

    if(keywords.length > 0){

      keywordOne = keywords[0];
    }

    if(keywords.length > 1){

      keywordTwo = keywords[1];
    }

    if(keywords.length > 2){

      keywordThree = keywords[2];
    }

  
    return jobRepository.findLocationCountSearch(jobType, minSalary, maxSalary,keywordOne,keywordTwo,keywordThree);

  }


  /**
   * Get a list of job types and their counts
   * @param location - a location as a string
   * @param minSalary - a minimum salary as an Integer
   * @param maxSalary - a maximum salary as an Integer
   * @return - a list of job types and their counts
   */
  public List<Object[]> getJobTypeCount(String location, Integer minSalary, Integer maxSalary){

    return jobRepository.findJobTypeCount(location,minSalary,maxSalary);
  }


/**
   * Get location Search count
   * @param jobType - a job type as a String
   * @param minSalary - a minimum salary as an Integer
   * @param maxSalary - a maximum salary as an Integer
   * @return - return a list of locations and their counts
   */
  public List<Object[]> getJobTypeCountSearch(String location, Integer minSalary, Integer maxSalary, String search){

    String keywordOne = null;
    String keywordTwo = null;
    String keywordThree = null;
     
    //Split search string into keywords
    String[] keywords = search.toLowerCase().split("\\s+",4);

    if(keywords.length > 0){

      keywordOne = keywords[0];
    }

    if(keywords.length > 1){

      keywordTwo = keywords[1];
    }

    if(keywords.length > 2){

      keywordThree = keywords[2];
    }

  
    return jobRepository.findJobTypeCountSearch(location, minSalary, maxSalary,keywordOne,keywordTwo,keywordThree);

  }


  /**
   * Retrieves a job list based on the filter critea(location, minimum and maximum salary, and jobtype)
   * @param location - a location as String
   * @param jobType - a job type as a String
   * @param minSalary - a minimum salary as an Integer
   * @param maxSalary - a maximum salary as an Integer
   * @return - a list of jobs
   */
  public List<Job> getJobsByFilter(String location, String  jobType, Integer minSalary, Integer maxSalary){

    return jobRepository.findJobByFilters(location,jobType,minSalary,maxSalary);
  }


  public List<Job> getJobsByFilterSearch(String location, String  jobType, Integer minSalary, Integer maxSalary,String search){

      String keywordOne = null;
      String keywordTwo = null;
      String keywordThree = null;
     

      String[] keywords = search.toLowerCase().split("\\s+",4);

      if(keywords.length > 0){

        keywordOne = keywords[0];
      }

      if(keywords.length > 1){

        keywordTwo = keywords[1];
      }

      if(keywords.length > 2){

        keywordThree = keywords[2];
      }


    return jobRepository.findJobByFiltersSearch(location,jobType,minSalary,maxSalary,keywordOne,keywordTwo,keywordThree);
  }


  /**
   * Get a list of salaries and their counts
   * @param location - a location as a string
   * @param jobType - a job type as a String
   * @return a list of salaries and their counts
   */
  public List<Object[]> getSalaryRangeCount(String location, String jobType){

    return jobRepository.findSalaryRangeCount(location, jobType);

  }


  public List<Object[]> getSalaryRangeCountSearch(String location, String jobType, String search){

    String keywordOne = null;
    String keywordTwo = null;
    String keywordThree = null;
     
    //Split search string into keywords
    String[] keywords = search.toLowerCase().split("\\s+",4);

    if(keywords.length > 0){

      keywordOne = keywords[0];
    }

    if(keywords.length > 1){

      keywordTwo = keywords[1];
    }

    if(keywords.length > 2){

      keywordThree = keywords[2];
    }


    return jobRepository.findSalaryRangeCountSearch(location, jobType, keywordOne,keywordTwo,keywordThree);

  }




  /**
   * Filter job list by location
   *
   * @return return filtered jobs
   */
  public List<Job> filterByLocation(String location) {

    return jobRepository.findByLocation(location);
    
  }


  /**
   * Filter job list by search keyword
   *
   * @return return filtered jobs
   */

  
  public List<Job> updateJobsBySearch(List<Job> jobs, String search){ 

    // If search is null or empty, return the filtered jobs as is
    if (search == null || search.isEmpty()) {
        return jobs;
    }

    // Step 2: Perform in-memory search on the filtered jobs
    String[] keywords = search.toLowerCase().split("\\s+",4); // Split search string into keywords(first 4)

    // Filter the jobs based on whether any keyword is contained in the job's title or description
    return jobs.stream()
            .filter(job -> Arrays.stream(keywords).anyMatch(keyword -> 
                    job.getTitle().toLowerCase().contains(keyword) || 
                    job.getDescription().toLowerCase().contains(keyword)||
                    job.getCompany().toLowerCase().contains(keyword))) 
            .collect(Collectors.toList());

  }


  public List<Job> getJobsBySort(String location, String  jobType, Integer minSalary, Integer maxSalary,String search, String sortBy){


    if(sortBy == null){

      return updateJobsBySearch(getAllJobs(), search);

    }else if(sortBy.equals("salary")){


      return updateJobsBySearch(jobRepository.findJobBySortSalary(location,jobType,minSalary,maxSalary), search);

    }else if (sortBy.equals("date")){

      
      return updateJobsBySearch(jobRepository.findJobBySortDate(location,jobType,minSalary,maxSalary), search);

    }

    //default sort
    return updateJobsBySearch(jobRepository.findJobByFilters(location,jobType,minSalary,maxSalary), search);
    

  }


  public List<Object[]> filterJobsBySearch(List<Object[]> jobData, String search){ 

    // If search is null or empty, return the filtered jobs as is
    if (search == null || search.isEmpty()) {
        return jobData;
    }

    // Step 2: Perform in-memory search on the filtered jobs
    String[] keywords = search.toLowerCase().split("\\s+",4); // Split search string into keywords(first 4)

    // Filter the jobs based on whether any keyword is contained in the job's title or description
    return jobData.stream()
            .filter(data ->{

              Job job = (Job) data[0];

              return Arrays.stream(keywords).anyMatch(keyword -> 
                    job.getTitle().toLowerCase().contains(keyword) || 
                    job.getDescription().toLowerCase().contains(keyword)||
                    job.getCompany().toLowerCase().contains(keyword));

            }).collect(Collectors.toList());


  }

  public List<Object[]> getJobByCoordinates(String location, String jobType, Integer minSalary, Integer maxSalary, String search){

  
    List<Object[]> jobList = jobRepository.findJobsWithCoordinates(location, jobType, minSalary, maxSalary);

    return filterJobsBySearch(jobList, search);

  }


}
