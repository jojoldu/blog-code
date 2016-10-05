/**
 * Created by jojoldu@gmail.com on 2016-10-05.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

// 이 코드를 require.js가 로딩된 뒤 기타 모듈을 로딩하기 전에 둔다.
require.config({
    baseUrl: "js", // 모듈을 로딩할 기본 패스를 지정한다.
    waitSeconds: 15 // 모듈의 로딩 시간을 지정한다. 이 시간을 초과하면 Timeout Error 가 throw 된다
});