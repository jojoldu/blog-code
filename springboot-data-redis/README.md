# SpringBoot Data Redis 로컬 테스트 예제

안녕하세요? 이번 시간엔 SpringBoot Data Redis 로컬 테스트 예제를 진행해보려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/springboot-data-redis)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>
 
## 들어가며

회사 신규 프로젝트로 Redis 를 사용하게 되었습니다.  
로컬에서 개발하고 테스트 할 수 있는 환경구성이 필요했는데요.  
H2처럼 Redis도 **프로젝트에 의존**하는 로컬 환경을 구성하게 되서 정리합니다.  

> 개인적인 생각이지만, 저는 Github에서 프로젝트를 받은뒤 바로 실행이 될수있어야 한다고 생각합니다.  
프로젝트를 실행시키려면 AWS (SQS) 계정이 있어야 한다거나, 특정 데몬(Redis, MySQL)을 설치해야 하는 등이 있으면 개선이 필요하다고 생각합니다.  
누가 오더라도 ```git clone``` 만 받으면 바로 로컬 개발/테스트를 시작할 수 있도록 하는 것을 지향합니다.

사용할 기술은 다음과 같습니다.

* Spring Data Redis
  * Redis를 마치 JPA Repository를 이용하듯이 인터페이스를 제공하는 스프링 모듈
  * ```CrudRepository```를 지원하기 때문에 좀 더 직관적으로 사용 가능
* Lettuce
  * Redis Java Client
  * 현재 (Spring Boot 2.0.2) Spring Data Redis에서 공식지원하는 Client 
      * Jedis, Lettuce만 현재 공식지원
      * Redisson은 아직 미포함
      * 포함되도 사용할지는 고민 ([Redisson 사용 경험](http://redisgate.kr/redis/clients/redisson_intro.php))
  * Jedis가 거의 업데이트 되지 않음
      * [Why is Lettuce the default Redis client used in Spring Session Redis?](https://github.com/spring-projects/spring-session/issues/789) 
* Embedded Redis
  * H2와 같은 내장 Redis 데몬

자 그럼 위 3가지 기술을 통해 로컬 개발/테스트 환경을 구축해보겠습니다.

## 기본 환경 구성

먼저 3개 모듈의 의존성을 추가하겠습니다.  
  
**build.gradle**

```groovy
    // spring-data-redis
    compile('org.springframework.boot:spring-boot-starter-data-redis')
    //embedded-redis
    compile group: 'it.ozimov', name: 'embedded-redis', version: '0.7.2'
```

> 여기서 [it.ozimov.embedded-redis](https://github.com/ozimov/embedded-redis) 를 보고 의아해하실분들이 계실것 같습니다.  
기존에 내장 Redis를 쓸때 [kstyrc.embedded-redis](https://github.com/kstyrc/embedded-redis) 를 사용했는데요.  
3년동안 업데이트가 없어 kstyrc.embedded-redis를 Fork해서 만든 모듈입니다.  

그리고 Config 파일을 2개를 생성하겠습니다.  
  
**EmbeddedRedisConfig**

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j //lombok
@Profile("local") // profile이 local일때만 활성화
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
            redisServer = new RedisServer(redisPort);
            redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
```

위는 내장 Redis를 프로젝트가 ```profile=local```일때만 실행되도록 하는 설정입니다.  
그리고 아래에선 Spring Data Redis를 설정합니다.  
  
**RedisRepositoryConfig**

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
```

* RedisConnectionFactory를 통해 내장 혹은 외부의 Redis를 연결합니다.
* RedisTemplate을 통해 RedisConnection에서 넘겨준 byte 값을 객체 직렬화합니다.  
  
이제 설정은 모두 끝났습니다!  
바로 Redis 객체와 Repository를 만들어보겠습니다.  
  
**Point**

```java
@Getter
@RedisHash("point")
public class Point implements Serializable {

    @Id
    private String id;
    private Long amount;
    private LocalDateTime refreshTime;

    @Builder
    public Point(String id, Long amount, LocalDateTime refreshTime) {
        this.id = id;
        this.amount = amount;
        this.refreshTime = refreshTime;
    }
}
```

> 다양한 타입이 되는것을 보여드리기 위해 String, Long, LocalDateTime을 모두 사용했습니다.

**PointRedisRepository**

```java
public interface PointRedisRepository extends CrudRepository<Point, String> {
}
```

자 그럼 
 