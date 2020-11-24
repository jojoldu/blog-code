# JPA Entity Select에서 Update 쿼리 발생할 경우

```java
@Getter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNo;

    @Convert(converter = PaysConverter.class)
    private List<Pay> pays = new ArrayList<>();

    public Order(String orderNo, List<Pay> pays) {
        this.orderNo = orderNo;
        this.pays = pays;
    }
}
```

```java
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Pay {
    private String code;
    private Long amount;
    private List<PayDetail> details = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayDetail {
        private String salesType;
        private Long amount;
    }
}
```

```java
public class PaysConverter implements AttributeConverter<List<Pay>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Pay> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public List<Pay> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Pay>>(){});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }
}
```


```java
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public void showPayDetailAmount (String orderNo) {
        log.info(">>>>>> findAllByOrderNo");
        List<Order> orders = orderRepository.findAllByOrderNo(orderNo);
        for (Order order : orders) {
            order.getPays();
        }
    }
}
```