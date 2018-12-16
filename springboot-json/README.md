# springboot-json

안녕하세요? 이번 시간엔 Spring과 JSON에 대해 정리해보려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/springboot-json)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>
 
## 0. 들어가며

Spring을 사용할 경우 Controller에서 요청 받는/응답  주는 DTO에서 ```LocalDate```와 ```LocalDateTime```을 사용할 경우가 종종 있습니다.  
  
헌데 이럴 경우 많은 분들이 직렬화를 못해 **String으로 받은 후 서비스 레이어에서 변환**하는 것을 보았습니다.  
Spring에선 굳이 이럴 필요가 없으니 아래 내용을 한번 참고하셔서 번거로운 직렬화 과정을 쉽게 해결하시길 바랍니다.  
  
개발 환경은 아래와 같습니다.
Spring Boot Web을 쓰신다면 버전 관계 없이 똑같이 사용하실 수 있습니다.

* Spring Boot Starter Web 2.1.1
* Java8

자 그럼 하나씩 예제를 진행해보겠습니다.

## 1. Request Parameter

첫번째로 해볼 것은 Request Parameter입니다.  
보통 Get 요청시 URL Parameter로 필드 데이터를 명시할때가 많은데요.  
Spring에선 이를 2가지 방법으로 해결할 수 있습니다.

* ```@ModelAttribute```로 DTO 객체를 받는다.
* ```@RequestParamter```로 필드별로 받는다. 

여기서 이 2가지에서 어떻게 ```LocalDate```와 ```LocalDateTime```을 직렬화 해서 받을 수 있는지 보겠습니다.  


### 1-1. @ModelAttribute

아래와 같은 Controller 메소드가 있다고 가정하겠습니다.

```java
@GetMapping("/get")
public String get(GetModel getModel) {
    log.info("get 요청 데이터 = {}", getModel);

    return "get 성공";
}
```

> ```@ModelAttribute```을 지정하지 않아도 별도로 어노테이션 지정이 없으면 ```@ModelAttribute```을 자동 할당합니다.

HTTP GET 요청을 ```/get``` 주소로 보내면 URL 파라미터의 각 필드들이 ```GetModel```의 필드에 매핑되는 코드입니다.  
  
정상적으로 요청이 왔다면 ```getModel```에는 Request Parameter들이 딱딱 들어가있겠죠?  
  
GetModel의 코드는 아래와 같습니다.

```java
@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetModel {
    private String name;
    private LocalDateTime requestDateTime;
}
```

테스트 대상인 ```LocalDateTime```엔 **아무런 어노테이션이 할당되어 있지 않습니다**.  
자 그리고 이 코드가 정상적으로 작동하는지 검증할 수 있는 테스트 코드를 작성합니다.  

```java
@RunWith(SpringRunner.class)
@WebMvcTest
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ModelAttribute의_LocalDate는_변환된다() throws Exception {
        //given
        String url = "/get?name=jojoldu&requestDateTime=2018-12-15T10:00:00";

        //when
        ResultActions resultActions = this.mockMvc.perform(get(url));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("get 성공")));

    }
}
```

이 상태에서 테스트를 실행해보시면!

![1](./images/1.png)

테스트가 실패합니다.  
이유는 requestDateTime을 직려로하 하지 못했다는 것인데요.  

많은 분들이 이 부분에서 실패하고 String으로 그냥 받아서 처리한다는 것을 들었습니다.  

먼저 ```@JsonFormat```을 사용해볼까요?

![2-1](./images/2-1.png)

```java
@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetModel {
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestDateTime;
}
```

> 일반적으로 Get요청시에 LocalDateTime 파라미터가 필요할 경우 ```2018-12-15 10:00:00``` 보다는 ```2018-12-15T10:00:00``` 을 선호합니다.  
띄어쓰기로 인해서 값이 잘못 넘어올수도 있기 때문인데요.  
대신 ```T```를 그대로 포맷에선 쓸수 없어서 ` 로 감싸 표현합니다.
 
이렇게 ```@JsonFormat```으로 ```LocalDateTime``` 포맷을 지정했습니다.  
그리고 다시 테스트를 수행해보면!

![2-2](./images/2-2.png)

전과 마찬가지로 또! 테스트가 실패합니다.  
  
자 그럼 어떻게 해야할까요?  
여기서 해결할 수 있는 방법은 바로 ```@DateTimeFormat```입니다.  


```java

@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetModel {
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestDateTime;
}
```

![2-3](./images/2-3.png)



```java
    @Test
    public void requestParameter의_LocalDate는_변환된다() throws Exception {
        //given
        String url = "/requestParameter?requestDateTime=2018-12-15T10:00:00";

        //when
        ResultActions resultActions = this.mockMvc.perform(get(url));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("requestParameter 성공")));

    }
}
```

## @DateTimeFormat vs @JsonFormat

자 그럼 여기서 한가지 궁금한게 있습니다.  
바로 ```@JsonFormat```과 ```@DateTimeFormat```의 차이입니다.  
 ```@JsonFormat```은 Jackson의 어노테이션이고, ```@DateTimeFormat```은 Spring의 어노테이션입니다.  

 ```@JsonFormat```은 ```LocalDate``` 혹은 ```LocalDateTime```을 JSON으로 직렬화하는 동안 포맷을 관리합니다.  
  
Spring의 기본 JSON 컨버터는 Jackson이다보니, **JSON으로 변환을 할때는 항상 Jackson을 통해서**만 진행됩니다.  

![jackson](./images/jackson.png)

Jackson은 Spring의 어노테이션인 ```@DateTimeFormat``` 을 전혀 알 수 없습니다.  
(완전히 별개의 라이브러리들이니깐요.)  
  
그래서 ```@DateTimeFormat```을 지정했다 하더라도, Jackson은 이 어노테이션은 전혀 고려하지 않고 JSON 직렬화을 진행하기 때문에 효과가 없는 것입니다.  
  
반대로 **JSON 직렬화외에는 Jackson이 사용되지 않기 때문에** ```@JsonFormat```은 효과가 없습니다.  
그래서 RequestParameter나 ModelAttribute에선 ```@DateTimeFormat``` 만 적용될 수 있습니다.  

* [stackoverflow](https://stackoverflow.com/questions/37871033/spring-datetimeformat-configuration-for-java-time)


## 2. Request Body


## 3. Response Body