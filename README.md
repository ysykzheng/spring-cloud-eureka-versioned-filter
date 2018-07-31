# spring-cloud-eureka-versioned-filter

server configuration(application.yml):
```yaml
spring:
  application:
    name: test-user-server
    
eureka:
  instance:
    metadataMap:
      versions: v2,v1
```

client code configuration
```java
@Configuration
public class RibbonFilterConfiguration {
  @Bean
  public VersionedServerListFilter serverListFilter() {

    Map<String,String> mapping=new HashMap<>();
    mapping.put("test-user-server","v1");

    VersionedServerListFilter filter = new VersionedServerListFilter(mapping);
    return filter;
  }

}

```
