# 순수 함수로 관리하기



예를 들어 함수형 프로그래밍의 기준은 무엇일까?

* 함수 체이닝을 하고 있으면 우리는 함수형 프로그래밍을 하고 있는 것일까?
* 고차함수, 모나드 등 함수형 프로그래밍의 여러 개념들을 쓰고 있으면 함수형 프로그래밍을 하고 있는 것일까?
* 함수를 인자 (Arguments)로 넘겨주고, 반환 (return) 하고 있으면 함수형 프로그래밍을 하고 있는 것일까?

함수형 프로그래밍에서 가장 중요한 것은 **순수 함수(Pure Function)** 이다. 

* [What is Functional Programming?](https://www.guru99.com/functional-programming-tutorial.html) 

순수 함수란 부수효과 (Side Effect) 가 없는 함수를 이야기한다.

순수 함수와 순수하지 않은 함수를 적절하게 구분해서 프로그래밍하고 있지 않다면 함수형 프로그래밍을 하고 있다고 보기는 어렵다.

 

함수형 프로그래밍에서는 크게 3가지 주요 개념이 있다.

부수효과 함수 (action)

순수 함수 (계산)

데이터

 

부수효과 함수는 호출함수까지 부수효과로 오염시킨다.

 

암묵적 입력과 암묵적 출력
인자가 아닌 모든 입력은 암묵적 입력

전역 변수에서 가져온 값

쿠키나 외부 API 에서 가져온 값

Don 에서 꺼내온 값

 

리턴값이 아닌 모든 출력은 암묵적 출력

console.log 등

데이터베이스에 적용된 Command

Dom 변경



## 멱등성


## 부수효과 함수에서 순수함수 분리하기

예를 들어 다음과 같은 코드가 있을때 이들을 어떻게 

```ts
// Course.tsx
export default function Course() {
  ...
  useEffect(() => {
    if (isIos(navigator.userAgent, document) || isSafari(navigator.userAgent)) {
      const mode = appConfig.MODE;

      const domain = getCookieDomain(mode);

      Cookies.set('CloudFront-Is-Apple-Viewer', 'true', {
        domain: domain,
      });
    }
  }, []);
}
```

to-be
```ts
useEffect(() => {
    const originCookie = Cookies.get('CloudFront-Is-Apple-Viewer');
    const {cookieValue, cookieDomain} = parseCookie(originCookie);

    Cookies.set('CloudFront-Is-Apple-Viewer', getValue(cookieValue), getDomain(cookieDomain));
  }, []);
```

as-is
```ts
  const goBackPage = useCallback((slug = '') => {
    if (category === 'question') {
      navigate(`/${ courseSlug ? 
      `course/${courseSlug}/community?type=question` 
      : 'community/questions'
        }`
      );

      return;
    }

    if (category === 'chat') {
      navigate(`${ courseSlug ? 
      `course/${courseSlug}/community?type=chat` 
      : 'community/chats'
        }`
      );

      return;
    }

    if (category === 'study') {
      navigate(`/${ courseSlug ? 
      `course/${courseSlug}/community?type=study` 
      : 'community/studies'
        }`
      );

      return;
    }

      throw new Error('유효하지 않은 category 입니다.!');
    },
    [category]
  );
```

as-is
```ts
  const handleCancel = useCallback(() => {
    invariant(editorRef.current?.getContent, '에디터가 존재하지 않습니다.');

    const { title, tags, tag } = methods.getValues();

    const parsedTitle = filterEnter(title).trim();
    const parsedTags = tags.map(({ tagName }) => tagName);
    const parsedTag = tag.trim();
    const parsedBody = filterParagraphTag(editorRef.current.getContent());

    if (parsedTitle !== '' || parsedTags.length !== 0 || parsedTag !== '' || parsedBody !== '') {
      handleOpenModal(goBackPostPage);

      return;
    }

    goBackPostPage();
  }, [category, editorRef]);
```