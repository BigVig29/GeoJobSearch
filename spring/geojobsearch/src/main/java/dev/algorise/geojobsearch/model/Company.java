package dev.algorise.geojobsearch.model;

import jakarta.persistence.Column; // Import the Table annotation
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * table entity.
 */
@Entity
@Table(name = "Company")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CompanyUID")
  private String companyUID;

  @Column(name = "Name")
  private String name;

  @Column(name = "City")
  private String city;

  @Column(name = "Province")
  private String province;

  @Column(name = "Address")
  private String address;

  @Column(name = "Latitude")
  private BigDecimal latitude;

  @Column(name = "Longitude")
  private BigDecimal longitude;



  // Getters and setters for all fields

  public String getCompanyUID() {
    return companyUID;
  }

  public void setCompanyUID(String companyUID) {

    this.companyUID = companyUID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }


  
}