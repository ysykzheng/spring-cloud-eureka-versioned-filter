package com.rongyiapp.springcloudeurekaversionedfilter;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.client.IClientConfigAware;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerListFilter;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.rongyiapp.springcloudeurekaversionedfilter.VersionedMapping.Mapping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class VersionedServerListFilter<T extends Server> extends
                                                         AbstractServerListFilter<T> implements IClientConfigAware {

  private static final String VERSION_KEY = "versions";

  private final VersionedMapping versionedMapping;

  public VersionedServerListFilter(VersionedMapping versionedMapping) {
    this.versionedMapping = versionedMapping;
  }

  @Override
  public void initWithNiwsConfig(IClientConfig clientConfig) {
  }

  private List<String> getInstanceVersions(InstanceInfo instanceInfo) {
    List<String> result = new ArrayList<>();
    String rawVersions = instanceInfo.getMetadata().get(VERSION_KEY);
    if (StringUtils.isNotBlank(rawVersions)) {
      result.addAll(Arrays.asList(rawVersions.split(",")));
    }
    return result;
  }

  @Override
  public List<T> getFilteredListOfServers(List<T> servers) {
    return (List<T>) servers.stream()
                            .map(server -> (DiscoveryEnabledServer) server)
                            .peek(discoveryEnabledServer -> System.out.println(discoveryEnabledServer.getId()))
                            .filter(server -> filterServer(server))
                            .collect(Collectors.toList());

  }

  private boolean filterServer(DiscoveryEnabledServer server) {
    InstanceInfo instanceInfo = server.getInstanceInfo();
    String appName = instanceInfo.getAppName();
    System.out.println("appName=" + appName);
    if (this.versionedMapping.getMappingList().isEmpty()) {
      return true;
    }
    //不区分大小写
    Optional<Mapping> optionalMapping = this.versionedMapping.getMappingList()
                                                             .stream()
                                                             .filter(mapping -> StringUtils.equalsIgnoreCase(appName, mapping.getServiceName())).findFirst();
    if (!optionalMapping.isPresent()) {
      return true;
    } else {
      Mapping mapping = optionalMapping.get();
      List<String> versions = this.getInstanceVersions(instanceInfo);
      System.out.println(versions);
      return versions.isEmpty() || versions.contains(mapping.getVersion());
    }
  }
}
