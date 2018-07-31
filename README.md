# spring-cloud-eureka-versioned-filter

server configuration(application.yml):
```yaml
eureka:
  instance:
    metadataMap:
      versions: v2,v1
```

client configuration(application.yml):
```yaml
spring-cloud-eureka-versioned-filter:
  versione-mapping:
    mapping-list:
      - service-name: test-user-server
        version: v2
      - service-name: test-your-service
        version: version-for-yours
```

client code configuration
```java
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
```
