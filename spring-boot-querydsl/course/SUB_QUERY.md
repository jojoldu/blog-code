# Querydsl Sub Query


## 1. select Sub Query

```java
@Override
    public List<StudentCount> findAllStudentCount() {
        return queryFactory
                .select(Projections.fields(StudentCount.class,
                        academy.name.as("academyName"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(student.id))
                                        .from(student)
                                        .where(student.academy.eq(academy)),
                                "studentCount")
                ))
                .from(academy)
                .fetch();
    }
```

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SubQueryTest {

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @After
    public void tearDown() throws Exception {
        studentRepository.deleteAll();
        academyRepository.deleteAll();
    }

    @Test
    public void SELECT절_서브쿼리로_사용된다() {
        //given
        String academyName1 = "name1";
        Academy academy1 = Academy.builder()
                .address("address1")
                .name(academyName1)
                .build();

        academy1.addStudent(Arrays.asList(
                new Student("student1", 1),
                new Student("student2", 2)
        ));

        String academyName2 = "name2";
        Academy academy2 = Academy.builder()
                .address("address2")
                .name(academyName2)
                .build();

        academy2.addStudent(Arrays.asList(
                new Student("student3", 3),
                new Student("student4", 4),
                new Student("student5", 5)
        ));

        academyRepository.saveAll(Arrays.asList(academy1, academy2));

        //when
        List<StudentCount> result = academyRepository.findAllStudentCount();

        //then
        assertThat(result.get(0).getAcademyName(), is(academyName1));
        assertThat(result.get(1).getAcademyName(), is(academyName2));
    }
}
```

## 2. where Sub Query

이번엔 ```where```절에 서브쿼리

```java
    @Override
    public List<Academy> findAllByStudentId(long studentId) {
        return queryFactory
                .selectFrom(academy)
                .where(academy.id.in(
                        JPAExpressions
                                .select(student.academy.id)
                                .from(student)
                                .where(student.id.eq(studentId))))
                .fetch();
    }
```

```java
@Test
    public void WHERE절_서브쿼리로_사용된다() {
        //given
        String academyName1 = "name1";
        Academy academy1 = Academy.builder()
                .address("address1")
                .name(academyName1)
                .build();

        academy1.addStudent(Arrays.asList(
                new Student("student1", 1),
                new Student("student2", 2)
        ));

        String academyName2 = "name2";
        Academy academy2 = Academy.builder()
                .address("address2")
                .name(academyName2)
                .build();

        academy2.addStudent(Arrays.asList(
                new Student("student3", 3),
                new Student("student4", 4),
                new Student("student5", 5)
        ));

        academyRepository.saveAll(Arrays.asList(academy1, academy2));

        //when
        List<Academy> result = academyRepository.findAllByStudentId(3);

        //then
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getName(), is(academyName2));
    }
```

## 참고

Querydsl은 **From절에서의 Subquery를 지원하지 않습니다**.

* [참고](https://github.com/querydsl/querydsl/issues/2185)