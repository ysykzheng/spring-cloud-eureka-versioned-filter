package com.rongyiapp.springcloudeurekaversionedfilter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring-cloud-eureka-versioned-filter.versioned-mapping")
public class VersionedMapping {

  private List<Mapping> mappingList = new ArrayList<>();

  public List<Mapping> getMappingList() {
    return mappingList;
  }

  public void setMappingList(List<Mapping> mappingList) {
    this.mappingList = mappingList;
  }

  public static class Mapping {
    private String serviceName;

    private String version;

    public String getServiceName() {
      return serviceName;
    }

    public void setServiceName(String serviceName) {
      this.serviceName = serviceName;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }
  }


}
