# Spring batch에서 N+1 문제 해결

안녕하세요? 이번 시간엔 Spring batch에서 N+1 문제 해결을 진행해보려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/spring-batch-n1)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>
  
## 1. 테스트 환경

프로젝트는 SpringBoot Batch + Lombok + Spock으로 구성됩니다.  

> 스프링부트의 버전은 2.1.3 입니다.
  
해당 기술들이 처음이셔도 기존에 사용되던 기술과 크게 다르지 않기 때문에 보시는데 어려움이 없으실 것 같습니다.  
  
다음은 기본적인 Entity와 Repository를 생성하겠습니다.  
엔티티는 총 4개로 구성됩니다.  

![관계도](./images/관계도.png)

3개의 엔티티의 코드는 다음과 같습니다.  
(굳이 안보셔도 됩니다.)

```java
@NoArgsConstructor
@Getter
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<Employee> employees = new ArrayList<>();

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.updateStore(this);
    }

    public void addEmployee(Employee employee){
        this.employees.add(employee);
        employee.updateStore(this);
    }
}
```

```java
@NoArgsConstructor
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private long price;

    @ManyToOne
    @JoinColumn(name = "store_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_STORE"))
    private Store store;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public void updateStore(Store store){
        this.store = store;
    }
}
```

```java
@NoArgsConstructor
@Getter
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate hireDate;

    @ManyToOne
    @JoinColumn(name = "store_id", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_STORE"))
    private Store store;

    public Employee(String name, LocalDate hireDate) {
        this.name = name;
        this.hireDate = hireDate;
    }

    public void updateStore(Store store){
        this.store = store;
    }
}
```

그리고 배치의 Writer로 저장될 ```StoreHistory```를 생성합니다.

```java
@NoArgsConstructor
@Getter
@Entity
public class StoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;
    private String productNames;
    private String employeeNames;

    public StoreHistory(Store store, List<Product> products, List<Employee> employees) {
        this.storeName = store.getName();
        this.productNames = products.stream()
                .map(Product::getName)
                .collect(Collectors.joining( "," ));

        this.employeeNames = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining( "," ));
    }
}
```


엔티티가 다 생성되었으니 배치 코드를 작성하겠습니다.

```java
@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class StoreBackupBatchConfiguration {

    public static final String JOB_NAME = "storeBackupBatch";
    private static final String STEP_NAME = JOB_NAME+"Step";

    private EntityManagerFactory entityManagerFactory;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public StoreBackupBatchConfiguration(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Value("${chunkSize:1000}")
    private int chunkSize; // Parameter로 chunkSize를 던지면 해당 값으로, 없으면 1000을 기본으로

    private static String ADDRESS_PARAM = null;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean
    @JobScope
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Store, StoreHistory>chunk(chunkSize)
                .reader(reader(ADDRESS_PARAM))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Store> reader (
            @Value("#{jobParameters[address]}") String address) {

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("address", address+"%");

        JpaPagingItemReader<Store> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select s From Store s where s.address like :address");
        reader.setParameterValues(parameters);
        reader.setPageSize(chunkSize);

        return reader;
    }

    public ItemProcessor<Store, StoreHistory> processor() {
        return item -> new StoreHistory(item, item.getProducts(), item.getEmployees());
    }

    public JpaItemWriter<StoreHistory> writer() {
        JpaItemWriter<StoreHistory> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}

```

조건(```like %address```)에 맞는 ```Store```를 조회하여 ```StoreHistory```로 복사하는 단순한 배치 프로그램입니다.  
배치 코드를 테스트할 테스트 코드를 작성하겠습니다.  
  
먼저 스프링배치 테스트 환경을 위해 설정파일을 하나 생성합니다.  

```java
@EnableBatchProcessing
@Configuration
@EnableAutoConfiguration
@ComponentScan
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class TestJobConfiguration {

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }
}
```

이를 기반으로 테스트 클래스를 추가합니다.

```java
@SpringBootTest
@TestPropertySource(properties = "job.name=storeBackupBatch")
class StoreBackupBatchConfigurationTest extends Specification {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils

    @Autowired
    StoreRepository storeRepository

    @Autowired
    StoreHistoryRepository storeHistoryRepository

    def "Store 정보가 StoreHistory로 복사된다" () {
        given:
        Store store1 = new Store("서점", "서울시 강남구")
        store1.addProduct(new Product("책1_1", 10000L))
        store1.addProduct(new Product("책1_2", 20000L))
        store1.addEmployee(new Employee("직원1", LocalDate.now()))
        storeRepository.save(store1)

        Store store2 = new Store("서점2", "서울시 강남구")
        store2.addProduct(new Product("책2_1", 10000L))
        store2.addProduct(new Product("책2_2", 20000L))
        store2.addEmployee(new Employee("직원2", LocalDate.now()))
        storeRepository.save(store2)

        Store store3 = new Store("서점3", "서울시 강남구")
        store3.addProduct(new Product("책3_1", 10000L))
        store3.addProduct(new Product("책3_2", 20000L))
        store3.addEmployee(new Employee("직원3", LocalDate.now()))
        storeRepository.save(store3)

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("address", "서울")
                .toJobParameters()
        when:
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        then:
        jobExecution.status == BatchStatus.COMPLETED
    }
}
```

그럼 이제 테스트를 실행해보겠습니다.  

## 2. 문제 상황

테스트를 실행해보시면 테스트는 성공적으로 통과하지만, 로그에 문제가 있어 보입니다.  

![N_1문제발생](./images/N_1문제발생.png)

 ```Store```와 ```Product```, ```Employee```가 1대다 관계다보니 ```reader.read()```과정에서 자연스레 JPA N+1 문제가 발생했습니다.  

> JPA N+1 문제란?  
 ```@OneToMany``` 등에서 하위 엔티티들을 Lazy Loading으로 가져올때마다 자식 조회 쿼리가 추가로 발생하는 이슈 ([참고](http://jojoldu.tistory.com/165))

이 문제를 해결하기 위해 ```join fetch```를 추가하겠습니다.  
먼저 ```Product``` 만 걸어보겠습니다.

![product만](./images/product만.png)

이 쿼리를 자세히 확인해보면

```sql
select store0_.id as id1_2_0_, products1_.id as id1_1_1_, store0_.address as address2_2_0_, store0_.name as name3_2_0_, products1_.name as name2_1_1_, products1_.price as price3_1_1_, products1_.store_id as store_id4_1_1_, products1_.store_id as store_id4_1_0__, products1_.id as id1_1_0__ 
from store store0_ 
inner join product products1_ on store0_.id=products1_.store_id 
where store0_.address like ?
```

한번의 ```join```이 발생한것을 알 수 있습니다.  
  
자 그럼 여기서 추가로 Product외에 다른 자식 엔티티인 ```Employee```에도 ```join fetch```를 걸어보겠습니다.  
그리고 테스트를 수행해보면!

![employee추가](./images/employee추가.png)

예상치 못한 ```MultipleBagFetchException```을 만나게 됩니다.  
즉, 한번에 **2개 이상의 자식 엔티티에는** ```join fetch```을 사용할 수 없어 문제가 발생하였습니다.  
  
이 문제는 꼭 ```join fetch```에서만 발생하진 않습니다.  
엔티티 클래스의 ```FetchType```을 ```Eager```로 두고 있어도 발생할 수 있습니다.  
가장 편한 해결책은 Lazy Loading 하는 것이지만, 앞에서 보신것처럼  JPA N+1 문제가 발생해서 성능상 큰 문제가 발생합니다.  
이러지도 저러지도 못하는 상황에서 해결할 수 있는 방법이 없을까요?

## 3. 해결

### 3-1. default_batch_fetch_size

Hibernate에서는 **여러 자식들이 있을때 N+1 문제를 회피**하기 위해  ```hibernate.default_batch_fetch_size``` 라는 옵션이 있습니다.  

src/**test**/resources/application.yml에 다음과 같은 옵션을 추가합니다.  

> **테스트 환경**에서 옵션을 적용하려면 test/resources에 적용해야만 합니다.

![옵션](./images/옵션.png)

```yml
spring:
  jpa:
    properties:
      hibernate.default_batch_fetch_size: 1000
```

이 **```batch-size``` 옵션은 하위 엔티티를 로딩할때 한번에 상위 엔티티 ID를 지정한 숫자만큼 ```in Query```로 로딩**해줍니다.  
현재 프로젝트를 예로 들면 ```batch-size:1000```으로 되어있으면, 상위 엔티티인 **Store의 id 1000개를 in Query**로 ```Product``` 와 ```Employee```를 조회하게 됩니다.  
  
설명한대로 옵션이 작동하는지 **JpaRepository**로 테스트해보겠습니다.  
  
```java

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public long find() {
        List<Store> stores = storeRepository.findAll();
        long productSum = stores.stream()
                .map(Store::getProducts)
                .flatMap(Collection::stream)
                .mapToLong(Product::getPrice)
                .sum();

        stores.stream()
                .map(Store::getEmployees)
                .flatMap(Collection::stream)
                .map(Employee::getName)
                .collect(Collectors.toList());

        return productSum;
    }
}
```

서비스 메소드에 트랜잭션을 걸고, Store의 Product와 Employee를 사용하여 **N+1이 발생하는지 체크**합니다.

테스트 코드는 다음과 같습니다.

```java
@SpringBootTest
class StoreServiceTest extends Specification{
    @Autowired
    StoreRepository storeRepository

    @Autowired
    StoreService storeService

    @Test
    void "Repository 의 BatchSize" () {
        given:
        Store store1 = new Store("서점", "서울시 강남구")
        store1.addProduct(new Product("책1_1", 10000L))
        store1.addProduct(new Product("책1_2", 20000L))
        store1.addEmployee(new Employee("직원1", LocalDate.now()))
        store1.addEmployee(new Employee("직원2", LocalDate.now()))
        storeRepository.save(store1)

        Store store2 = new Store("서점2", "서울시 강남구")
        store2.addProduct(new Product("책2_1", 10000L))
        store2.addProduct(new Product("책2_2", 20000L))
        store2.addEmployee(new Employee("직원2_1", LocalDate.now()))
        store2.addEmployee(new Employee("직원2_2", LocalDate.now()))
        storeRepository.save(store2)

        when:
        long size = storeService.find()

        then:
        size == 60000L
    }
}
```

테스트 코드를 실행해보겠습니다.  

![default1](./images/default1.png)

**in Query로 product와 employee가 조회**됩니다!  

> in query 확인을 위해 Query 포맷팅 옵션을 추가한 상태입니다.

혹시 원래 그런건 아닌지 의심될수 있으니, **옵션을 제거하고 다시 돌려봅니다**

![default2](./images/default2.png)

옵션을 제거하니 N+1이 다시 발생하는 것을 알 수 있습니다.  
  
자 그럼 **hibernate.default_batch_fetch_size가 JPA에서 정상 작동**하는 것을 확인했으니, Batch에서도 그런지 확인해보겠습니다.  
  
옵션을 다시 추가한 뒤에 **Batch Test**를 진행해봅니다.

![default3](./images/default3.png)

**배치에서는 작동하지 않습니다!**  
  
왜 그럴까요?  
  
혹시 JpaPagingItemReader만 안되는것일까요?

### 3-2. Hibernate 테스트

JpaPagingItemReader에서만 안되는지 Hibernate Item Reader에서 테스트해보겠습니다.  
  
Batch 코드에서 ItemReader 부분을 다음과 같이 변경합니다.

```java
@Bean
@StepScope
public HibernatePagingItemReader<Store> reader(@Value("#{jobParameters[address]}") String address) {
    Map<String, Object> parameters = new LinkedHashMap<>();
    parameters.put("address", address + "%");
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

    HibernatePagingItemReader<Store> reader = new HibernatePagingItemReader<>();
    reader.setQueryString("FROM Store s WHERE s.address LIKE :address");
    reader.setParameterValues(parameters);
    reader.setSessionFactory(sessionFactory);
    reader.setFetchSize(chunkSize);
    reader.setUseStatelessSession(false);

    return reader;
}
```

그리고 다시 테스트를 돌려보겠습니다.  

![hibernate1](./images/hibernate1.png)

HibernatePaingItemReader에서는 **옵션이 잘 작동합니다**.  
음.. HibernatePaingItemReader에서만 잘 작동하는걸까요?  
  
**HibernateCursorItemReader** 에서도 작동하는지 확인해봅니다.

```java

    @Bean
    @StepScope
    public HibernateCursorItemReader<Store> reader(@Value("#{jobParameters[address]}") String address) {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("address", address+"%");
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        HibernateCursorItemReader<Store> reader = new HibernateCursorItemReader<>();
        reader.setQueryString("FROM Store s WHERE s.address LIKE :address");
        reader.setParameterValues(parameters);
        reader.setSessionFactory(sessionFactory);
        reader.setFetchSize(chunkSize);
        reader.setUseStatelessSession(false);

        return reader;
    }
```

![hibernate2](./images/hibernate2.png)

HibernateCursorItemReader에서도 정상 작동합니다.  
  
즉, ORM Reader 중 **JpaPagingItemReader에서만 작동되지 않습니다**.  
  
JpaPagingItemReader에서는 해결이 안될까요?  

### 3-3. Custom JpaPagingItemReader

왜 JpaPagingItemReader에서만 안될까요?  
코드를 살펴보면 Hibernate Item Reader와는 차이가 나는 코드가 하나 있습니다.  
다른 PagingItemReader의 경우 **트랜잭션을 Chunk에 맡깁니다**.  
하지만 JpaPagingItemReader의 **트랜잭션을 Reader에서 진행**합니다.  

![custom1](./images/custom1.png)

이러다보니 **트랜잭션 안에서만 작동하는 hibernate.default_batch_fetch_size**가 **단일 객체에서만** 발동하게 됩니다.  
  
스프링배치는 이미 **Chunk 단위로 트랜잭션** 을 보장하고 있기 때문에, JpaPagingItemReader 내부의 트랜잭션을 걷어내겠습니다.  
  
JpaPagingItemReader 코드를 고칠순 없기 때문에, 직접 Custom Reader를 생성합니다.

```java
public class JpaPagingFetchItemReader <T> extends AbstractPagingItemReader<T> {
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    private final Map<String, Object> jpaPropertyMap = new HashMap<>();

    private String queryString;

    private JpaQueryProvider queryProvider;

    private Map<String, Object> parameterValues;

    private boolean transacted = true;//default value

    public JpaPagingFetchItemReader() {
        setName(ClassUtils.getShortName(JpaPagingFetchItemReader.class));
    }

    /**
     * Create a query using an appropriate query provider (entityManager OR
     * queryProvider).
     */
    private Query createQuery() {
        if (queryProvider == null) {
            return entityManager.createQuery(queryString);
        }
        else {
            return queryProvider.createQuery();
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * The parameter values to be used for the query execution.
     *
     * @param parameterValues the values keyed by the parameter named used in
     * the query string.
     */
    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * By default (true) the EntityTransaction will be started and committed around the read.
     * Can be overridden (false) in cases where the JPA implementation doesn't support a
     * particular transaction.  (e.g. Hibernate with a JTA transaction).  NOTE: may cause
     * problems in guaranteeing the object consistency in the EntityManagerFactory.
     *
     * @param transacted indicator
     */
    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        if (queryProvider == null) {
            Assert.notNull(entityManagerFactory, "EntityManager is required when queryProvider is null");
            Assert.hasLength(queryString, "Query string is required when queryProvider is null");
        }
    }

    /**
     * @param queryString JPQL query string
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * @param queryProvider JPA query provider
     */
    public void setQueryProvider(JpaQueryProvider queryProvider) {
        this.queryProvider = queryProvider;
    }

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();

        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }
        // set entityManager to queryProvider, so it participates
        // in JpaPagingItemReader's managed transaction
        if (queryProvider != null) {
            queryProvider.setEntityManager(entityManager);
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage() {
        if (transacted) {
            entityManager.clear();
        }//end if

        Query query = createQuery().setFirstResult(getPage() * getPageSize()).setMaxResults(getPageSize());

        if (parameterValues != null) {
            for (Map.Entry<String, Object> me : parameterValues.entrySet()) {
                query.setParameter(me.getKey(), me.getValue());
            }
        }

        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        }
        else {
            results.clear();
        }

        if (!transacted) {
            List<T> queryResult = query.getResultList();
            for (T entity : queryResult) {
                entityManager.detach(entity);
                results.add(entity);
            }//end if
        } else {
            results.addAll(query.getResultList());
        }//end if
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }

    @Override
    protected void doClose() throws Exception {
        entityManager.close();
        super.doClose();
    }
}

```

![custom2](./images/custom2.png)


TransactionManager를 사용하는 코드를 모두 제거한 ItemReader를 만들고, 이를 사용합니다.  

![custom3](./images/custom3.png)

다시 테스트 코드를 돌려보면!

![custom4](./images/custom4.png)

성공적으로 ```hibernate.default_batch_fetch_size``` 옵션이 적용된 것을 확인할 수 있습니다.  
  
이 방법으로 쓰면 될까요?  
아쉽게도, 이 방법에는 **어떤 사이드 이펙트가 있는지 알 수 없습니다**.
그래서 아직까지 추천하기가 어렵습니다.

## 4. 결론

* ```join fetch```는 **하나의 자식에만 적용**가능
* Spring Data의 JpaRepository / Spring Batch의 HibernateItemReader에서는
    * ```hibernate.default_batch_fetch_size```로 N+1 문제를 피할 수 있다.
    * ```@BatchSize```도 가능
* Spring Boot 2.1.3 (Spring Batch 4.1.1)까지는 ```hibernate.default_batch_fetch_size``` 옵션이 **JpaPagingItemReader에서 작동하지 않는다**.
* Custom하게 수정해서 쓸 순 있지만, 검증되지 않은 방식

> 현재 해당 내용의 수정을 [PR](https://github.com/spring-projects/spring-batch/pull/713)로 보냈습니다.  
Merge되면 이 블로그의 내용은 수정 될 수 있습니다.

## 참고

* [7 tips-to-boost-your-hibernate-performance/](http://www.thoughts-on-java.org/tips-to-boost-your-hibernate-performance/)

* [hibernate-facts-multi-level-fetching](https://vladmihalcea.com/2013/10/22/hibernate-facts-multi-level-fetching/)

* [권남 위키](http://kwonnam.pe.kr/wiki/java/hibernate/performance)

* [beware-of-hibernate-batch-fetching](https://prasanthmathialagan.wordpress.com/2017/04/20/beware-of-hibernate-batch-fetching/)