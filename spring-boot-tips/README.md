# @Reuqest Body에서는 Setter가 필요없다?

회사에서 근무하던중 새로오신 신입 개발자분이 저에게 하나의 질문을 했습니다.  
POST 요청시에 ```Setter```가 필요없는것 같다고.  

여태 제가 알던것과는 달라서 어떻게 된 일인지 궁금했습니다.  
정말 POST 요청시에는 Setter가 필요없을까요?  
그럼 GET 요청시에는 Setter가 필요할까요?  

한번 확인해보겠습니다.  

> 모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/spring-boot-tips)에 있으니 참고하세요

## 1. Post 요청 테스트

첫번째로 POST 요청시 Setter가 필요없는지 먼저 테스트해봅니다.  
테스트해볼 RequestDto는 아래와 같습니다.

```java
@Getter
@ToString
@NoArgsConstructor
public class RequestSetterDto {
    private String name;
    private Long amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private RequestType requestType;

    @Builder
    public RequestSetterDto(@Nonnull String name, @Nonnull Long amount) {
        this.name = name;
        this.amount = amount;
    }

    public RequestSetterDto(String name, Long amount, LocalDate date, RequestType requestType) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.requestType = requestType;
    }

    @AllArgsConstructor
    public enum RequestType {
        GET ("get"),
        POST ("post");

        private String method;
    }

}
```

다양한 타입을 테스트해보기 위해 ```String```, ```Long```, ```LocalDate```, ```Enum``` 4가지 타입을 모두 사용했습니다.  
보시는것처럼 **Setter는 선언하지 않았습니다**  
  
그리고 이 DTO를 수신할 Controller를 생성합니다.

```java

@Slf4j
@RestController
public class RequestDtoSetterController {

    @PostMapping("/request/setter")
    public RequestSetterDto postRequestSetter (@RequestBody RequestSetterDto requestSetterDto) {
        log.info(requestSetterDto.getName() + " : " + requestSetterDto.getAmount());

        return requestSetterDto;
    }
}

```

자 그럼 이 코드들을 테스트할 테스트 코드를 작성해보겠습니다.

```java
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RequestDtoSetterController.class)
public class RequestDtoSetterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void RequestBody에서는_setter가_없어도된다() throws Exception {
        String content = objectMapper.writeValueAsString(new RequestSetterDto("jojoldu", 1000L));
        mvc
                .perform(post("/request/setter")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(content));
    }
}
```

Post로 Request Dto에 값을 채워 보냈으니

* Setter가 없어도 되면 정상적으로 값이 채워져서 테스트가 통과될것이며
* Setter가 있어야만 하면 값이 ```null```로 채워져 테스트가 실패할 것입니다.

테스트를 돌려보면!

![1](./blog/request-setter/images/1.png)

Setter없이 성공적으로 값이 들어가는 것을 확인할 수 있습니다.  
이게 어떻게 된 일인지 ```@RequestBody``` 어노테이션을 시작으로 코드를 탐색하기 시작했습니다.  

![2](./blog/request-setter/images/2.png)

![3](./blog/request-setter/images/3.png)

마지막 코드까지 따라가보면 실제로 **필드에 값을 바로 등록**하는 것을 확인할 수 있습니다.

![4](./blog/request-setter/images/4.png)

코드에서 본것처럼 ```@RequestBody```는 **Setter가 없어도 값을 세팅**하는 것을 확인하였습니다.  
  
이제 Post용 DTO에서는 더이상 Setter를 쓸 필요가 없겠죠?

## 2. Get 요청 테스트

두번째는 Get 메소드의 DTO에서도 Setter가 필요없는지 확인해보겠습니다.  
DTO는 1번째와 같은 DTO를 사용하며, Controller는 아래와 같습니다.

```java
    @GetMapping("/request/setter")
    public RequestSetterDto getRequestSetter (RequestSetterDto requestSetterDto) {
        log.info(requestSetterDto.getName() + " : " + requestSetterDto.getAmount());

        return requestSetterDto;
    }
```



