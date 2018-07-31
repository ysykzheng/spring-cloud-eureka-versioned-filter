package com.rongyiapp.springcloudeurekaversionedfilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonFilterConfiguration {

  @Autowired
  private VersionedMapping versionedMapping;

  @Bean
  public VersionedServerListFilter serverListFilter() {
    VersionedServerListFilter filter = new VersionedServerListFilter(versionedMapping);
    return filter;
  }

}
