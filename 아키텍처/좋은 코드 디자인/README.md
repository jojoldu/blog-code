# 좋은 코드 구현하기


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