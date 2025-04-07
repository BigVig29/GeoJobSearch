package dev.algorise.geojobsearch.repository;
import dev.algorise.geojobsearch.model.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {


  @Query("SELECT j.location, COUNT(j) AS jobCount FROM Job j " + 
    "WHERE (:jobType IS NULL OR j.jobType = :jobType) " +
    "AND (:minSalary IS NULL OR j.salary >= :minSalary ) " +
    "AND (:maxSalary IS NULL OR j.salary <= :maxSalary ) " + 
    "GROUP BY j.location")
    List<Object[]> findLocationsCount(  @Param("jobType") String jobType,  
                                        @Param("minSalary") Integer minSalary, 
                                        @Param("maxSalary") Integer maxSalary);


@Query("SELECT j.location, COUNT(j) AS jobCount FROM Job j " + 
    "WHERE (:jobType IS NULL OR j.jobType = :jobType) " +
    "AND (:minSalary IS NULL OR j.salary >= :minSalary ) " +
    "AND (:maxSalary IS NULL OR j.salary <= :maxSalary ) " +
    "AND (:keywordOne IS NULL OR j.title LIKE CONCAT('%', :keywordOne, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordOne, '%') OR j.company LIKE CONCAT('%', :keywordOne, '%') ) " +
    "AND (:keywordTwo IS NULL OR j.title LIKE CONCAT('%', :keywordTwo, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordTwo, '%') OR j.company LIKE CONCAT('%', :keywordTwo, '%') ) " +
    "AND (:keywordThree IS NULL OR j.title LIKE CONCAT('%', :keywordThree, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordThree, '%') OR j.company LIKE CONCAT('%', :keywordThree, '%') ) " +
    "GROUP BY j.location")
    List<Object[]> findLocationCountSearch(@Param("jobType") String jobType,  
                                        @Param("minSalary") Integer minSalary, 
                                        @Param("maxSalary") Integer maxSalary,
                                        @Param("keywordOne") String keywordOne,
                                        @Param("keywordTwo") String keywordTwo,
                                        @Param("keywordThree") String keywordThree);


    
   @Query("SELECT j.jobType, COUNT(j) AS jobCount FROM Job j " + 
    "WHERE (:location IS NULL OR j.location = :location) " +
    "AND (:minSalary IS NULL OR j.salary >= :minSalary ) " +
    "AND (:maxSalary IS NULL OR j.salary <= :maxSalary ) " + 
    "GROUP BY j.jobType")
    List<Object[]> findJobTypeCount(@Param("location")String location, 
                                    @Param("minSalary")Integer minSalary, 
                                    @Param("maxSalary")Integer maxSalary);



    @Query("SELECT j.jobType, COUNT(j) AS jobCount FROM Job j " + 
    "WHERE (:location IS NULL OR j.location = :location) " +
    "AND (:minSalary IS NULL OR j.salary >= :minSalary ) " +
    "AND (:maxSalary IS NULL OR j.salary <= :maxSalary ) " + 
    "AND (:keywordOne IS NULL OR j.title LIKE CONCAT('%', :keywordOne, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordOne, '%') OR j.company LIKE CONCAT('%', :keywordOne, '%') ) " +
    "AND (:keywordTwo IS NULL OR j.title LIKE CONCAT('%', :keywordTwo, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordTwo, '%') OR j.company LIKE CONCAT('%', :keywordTwo, '%') ) " +
    "AND (:keywordThree IS NULL OR j.title LIKE CONCAT('%', :keywordThree, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordThree, '%') OR j.company LIKE CONCAT('%', :keywordThree, '%') ) " +
    "GROUP BY j.jobType")
    List<Object[]> findJobTypeCountSearch(@Param("location") String location,  
                                        @Param("minSalary") Integer minSalary, 
                                        @Param("maxSalary") Integer maxSalary,
                                        @Param("keywordOne") String keywordOne,
                                        @Param("keywordTwo") String keywordTwo,
                                        @Param("keywordThree") String keywordThree);


    @Query("SELECT FLOOR(j.salary / 50000) * 50000  AS minSalary, " +
            "(FLOOR(j.salary / 50000) + 1) * 50000 - 1 AS maxSalary, " +
            "COUNT(j) as jobCount FROM Job j " +
            "WHERE (:location IS NULL OR j.location = :location) " +
            "AND (:jobType IS NULL OR j.jobType = :jobType) " +
            "GROUP BY FLOOR(j.salary / 50000) * 50000 " +
            "ORDER BY minSalary")
    List<Object[]> findSalaryRangeCount(@Param("location")String location, 
                                        @Param ("jobType")String jobType);



     @Query("SELECT FLOOR(j.salary / 50000) * 50000  AS minSalary, " +
            "(FLOOR(j.salary / 50000) + 1) * 50000 - 1 AS maxSalary, " +
            "COUNT(j) as jobCount FROM Job j " +
            "WHERE (:location IS NULL OR j.location = :location) " +
            "AND (:jobType IS NULL OR j.jobType = :jobType) " +
            "AND (:keywordOne IS NULL OR j.title LIKE CONCAT('%', :keywordOne, '%') " +
            "OR j.description LIKE CONCAT('%', :keywordOne, '%') OR j.company LIKE CONCAT('%', :keywordOne, '%') ) " +
            "AND (:keywordTwo IS NULL OR j.title LIKE CONCAT('%', :keywordTwo, '%') " +
            "OR j.description LIKE CONCAT('%', :keywordTwo, '%') OR j.company LIKE CONCAT('%', :keywordTwo, '%') ) " +
            "AND (:keywordThree IS NULL OR j.title LIKE CONCAT('%', :keywordThree, '%') " +
            "OR j.description LIKE CONCAT('%', :keywordThree, '%') OR j.company LIKE CONCAT('%', :keywordThree, '%') ) " +
            "GROUP BY FLOOR(j.salary / 50000) * 50000 " +
            "ORDER BY minSalary")
    List<Object[]> findSalaryRangeCountSearch(@Param("location")String location, 
                                        @Param ("jobType")String jobType,
                                        @Param("keywordOne") String keywordOne,
                                        @Param("keywordTwo") String keywordTwo,
                                        @Param("keywordThree") String keywordThree);


    @Query("SELECT j FROM Job j " +
            "WHERE (:location is NULL OR j.location = :location) " + 
            "AND (:jobType IS NULL OR j.jobType = :jobType) " +
            "AND (:minSalary IS NULL OR j.salary >= :minSalary ) " +
            "AND (:maxSalary IS NULL OR j.salary <= :maxSalary )")
    List<Job> findJobByFilters(@Param("location")String location, 
                                    @Param("jobType")String jobType,
                                    @Param("minSalary")Integer minSalary, 
                                    @Param("maxSalary")Integer maxSalary);

    @Query("SELECT j FROM Job j " +
    "WHERE (:location is NULL OR j.location = :location) " + 
    "AND (:jobType IS NULL OR j.jobType = :jobType) " +
    "AND (:minSalary IS NULL OR j.salary >= :minSalary ) " +
    "AND (:maxSalary IS NULL OR j.salary <= :maxSalary ) " +
    "AND (:keywordOne IS NULL OR j.title LIKE CONCAT('%', :keywordOne, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordOne, '%') OR j.company LIKE CONCAT('%', :keywordOne, '%') ) " +
    "AND (:keywordTwo IS NULL OR j.title LIKE CONCAT('%', :keywordTwo, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordTwo, '%') OR j.company LIKE CONCAT('%', :keywordTwo, '%') ) " +
    "AND (:keywordThree IS NULL OR j.title LIKE CONCAT('%', :keywordThree, '%') " +
    "OR j.description LIKE CONCAT('%', :keywordThree, '%') OR j.company LIKE CONCAT('%', :keywordThree, '%') ) ")
    List<Job> findJobByFiltersSearch(@Param("location")String location, 
                                    @Param("jobType")String jobType,
                                    @Param("minSalary")Integer minSalary, 
                                    @Param("maxSalary")Integer maxSalary,
                                    @Param("keywordOne") String keywordOne,
                                    @Param("keywordTwo") String keywordTwo,
                                    @Param("keywordThree") String keywordThree);


    @Query("SELECT j FROM Job j " +
    "WHERE (:location is NULL OR j.location = :location) " + 
    "AND (:jobType IS NULL OR j.jobType = :jobType) " +
    "AND (:minSalary IS NULL OR j.salary >= :minSalary ) " +
    "AND (:maxSalary IS NULL OR j.salary <= :maxSalary ) " +
    "ORDER BY j.date DESC")
    List<Job> findJobBySortDate(@Param("location")String location, 
                                    @Param("jobType")String jobType,
                                    @Param("minSalary")Integer minSalary, 
                                    @Param("maxSalary")Integer maxSalary);

    @Query("SELECT j FROM Job j " +
    "WHERE (:location is NULL OR j.location = :location) " + 
    "AND (:jobType IS NULL OR j.jobType = :jobType) " +
    "AND (:minSalary IS NULL OR j.salary >= :minSalary ) " +
    "AND (:maxSalary IS NULL OR j.salary <= :maxSalary ) " +
    "ORDER BY j.salary DESC")
    List<Job> findJobBySortSalary(@Param("location")String location, 
                                    @Param("jobType")String jobType,
                                    @Param("minSalary")Integer minSalary, 
                                    @Param("maxSalary")Integer maxSalary);



    @Query("SELECT j, c.latitude, c.longitude FROM Job j JOIN j.companyT c " +
    "WHERE (:jobType IS NULL OR j.jobType = :jobType) " +
    "AND (:location IS NULL OR j.location = :location) " +
    "AND (:minSalary IS NULL OR j.salary >= :minSalary) " +
    "AND (:maxSalary IS NULL OR j.salary <= :maxSalary)")
     List<Object[]> findJobsWithCoordinates(@Param("location")String location, 
                                            @Param("jobType")String jobType,
                                            @Param("minSalary")Integer minSalary,
                                            @Param("maxSalary")Integer maxSalary);





  //retrieve jobs by a specific location
  List<Job> findByLocation(String location);

  List<Job> findByCity(String city);
}
