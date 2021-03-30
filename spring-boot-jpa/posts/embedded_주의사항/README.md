# JPA 사용시 @Embedded 주의사항

```java
	@Embedded
	private BoardAttaches boardAttaches = BoardAttaches.EMPTY;
```

```java
	public static final BoardAttaches EMPTY = new BoardAttaches();
```