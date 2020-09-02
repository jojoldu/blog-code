# Querydsl에서 상수 사용하기

쿼리 성능을 개선할 수 있는 여러방법 중에 가장 쉬운 방법은 **조회하는 컬럼의 수를 최소화**하는 것입니다.  

![intro](./images/intro.png)

* [dzone-6-simple-performance-tips-sql](https://dzone.com/articles/6-simple-performance-tips-sql)
  
그래서 이렇게 컬럼의 수를 최소화 하는 방법으로 가장 쉽게 해볼 수 있는 것이 **이미 알고 있는 값은 상수**로 사용하는 것입니다.  
  
이번 시간에는 Querydsl에서 상수를 사용하는 방법에 대해서 소개드리겠습니다.

## constant

```java
@Getter
@NoArgsConstructor
public class BookPageDto {
    private String name;
    private int pageNo;
    private int bookNo;
}
```

```java
public List<BookPageDto> getBookPage (int bookNo, int pageNo) {
    return queryFactory
            .select(Projections.fields(BookPageDto.class,
                    book.name,
                    Expressions.as(Expressions.constant(pageNo), "pageNo")
                    ))
            .from(book)
            .where(book.bookNo.eq(bookNo))
            .offset(pageNo)
            .limit(10)
            .fetch();
}
```

```java
@Test
void 상수값을_사용한다() throws Exception {
    //given
    int bookNo = 1;
    for (int i = 1; i <= 10; i++) {
        bookRepository.save(Book.builder()
                .name("a"+i)
                .bookNo(bookNo)
                .build());
    }

    //when
    List<BookPageDto> bookPages = bookQueryRepository.getBookPage(bookNo, 0);

    //then
    assertThat(bookPages).hasSize(10);
    assertThat(bookPages.get(0).getPageNo()).isEqualTo(0);
}
```

![test](./images/test.png)

## constantAs


```java
@Getter
@NoArgsConstructor
public class BookPageDto {
    private String name;
    private int pageNo;
    private int bookNo;
}
```

```java
public List<BookPageDto> getBookPage (int bookNo, int pageNo) {
    return queryFactory
            .select(Projections.fields(BookPageDto.class,
                    book.name,
                    Expressions.as(Expressions.constant(pageNo), "pageNo"),
                    Expressions.constantAs(bookNo, book.bookNo)
                    ))
            .from(book)
            .where(book.bookNo.eq(bookNo))
            .offset(pageNo)
            .limit(10)
            .fetch();
}
```


```java
public List<BookPageDto> getBookPage (int bookNo, int pageNo) {
    return queryFactory
            .select(Projections.fields(BookPageDto.class,
                    book.name,
                    as(constant(pageNo), "pageNo"),
                    constantAs(bookNo, book.bookNo)
                    ))
            .from(book)
            .where(book.bookNo.eq(bookNo))
            .offset(pageNo)
            .limit(10)
            .fetch();
}
```

```java
@Test
void 상수값을_사용한다() throws Exception {
    //given
    int bookNo = 1;
    for (int i = 1; i <= 10; i++) {
        bookRepository.save(Book.builder()
                .name("a"+i)
                .bookNo(bookNo)
                .build());
    }

    //when
    List<BookPageDto> bookPages = bookQueryRepository.getBookPage(bookNo, 0);

    //then
    assertThat(bookPages).hasSize(10);
    assertThat(bookPages.get(0).getPageNo()).isEqualTo(0);
    assertThat(bookPages.get(0).getBookNo()).isEqualTo(bookNo);
}
```

![test2](./images/test2.png)