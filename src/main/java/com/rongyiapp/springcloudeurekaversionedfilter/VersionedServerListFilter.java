package com.rongyiapp.springcloudeurekaversionedfilter;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.client.IClientConfigAware;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerListFilter;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class VersionedServerListFilter<T extends Server> extends
                                                         AbstractServerListFilter<T> implements IClientConfigAware {

  private static final String VERSION_KEY = "versions";

  private final Map<String, String> mapping;

  public VersionedServerListFilter(Map<String, String> mapping) {
    this.mapping = mapping;
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
                            .filter(server -> filterServer(server))
                            .collect(Collectors.toList());

  }

  private boolean filterServer(DiscoveryEnabledServer server) {
    InstanceInfo instanceInfo = server.getInstanceInfo();
    String appName = instanceInfo.getAppName();
    if (this.mapping.isEmpty()) {
      return true;
    }
    //不区分大小写
    Optional<String> stringOptional = this.mapping.keySet().stream().filter(key -> StringUtils.equalsIgnoreCase(appName, key)).findFirst();

    if (!stringOptional.isPresent()) {
      return true;
    } else {
      String version = this.mapping.get(stringOptional.get());
      List<String> versions = this.getInstanceVersions(instanceInfo);
      return versions.isEmpty() || versions.contains(version);
    }
  }
}
