package dev.algorise.geojobsearch.model;

import jakarta.persistence.Column; // Import the Table annotation
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.FetchType;

/**
 * table entity.
 */
@Entity
@Table(name = "Jobs")
public class Job {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "JobID")
  private Long jobID;

  @Column(name = "Title")
  private String title;

  @Column(name = "Company")
  private String company;

  @Column(name = "Location")
  private String location;

  @Column(name = "City")
  private String city;

  @Column(name = "Province")
  private String province;

  @Column(name = "Description")
  private String description;

  @Column(name = "Salary")
  private BigDecimal salary;

  @Column(name = "JobType")
  private String jobType;

  @Column(name = "Date")
  private Date date;

  @Column(name = "JobURL")
  private String jobURL;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CompanyUID", referencedColumnName = "CompanyUID")
  @JsonIgnore
  private Company companyT;

  // Getters and setters for all fields
  public Long getJobID() {
    return jobID;
  }

  public void setJobID(Long jobID) {
    this.jobID = jobID;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public Date getDate() {
    return date != null ? new Date(date.getTime()) : null;
  }

  public void setDate(Date date) {
    this.date = date != null ? new Date(date.getTime()) : null;
  }

  public String getJobURL() {
    return jobURL;
  }

  public void setJobURL(String jobURL) {
    this.jobURL = jobURL;
  }

  public Company getCompanyT(){

    return companyT;
  }

  public void setCompanyT(Company companyT){

    this.companyT = companyT;
  }
}