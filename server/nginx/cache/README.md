# Nginx 리버스 프록시
일정 수준 이상의 규모를 가진 웹 사이트에서는 웹 서버(Apache 혹은 Nginx)와 웹 어플리케이션 서버 (Tomcat)를 분리하여 웹 서버를 리버스 프록시 서버(Reverse Proxy Server)로 사용합니다.<br/>

서비스의 도메인을 리버스 프록시로 등록하여 (예를 들어 zum.com이란 요청이 오게 되면 )사용자의 요청 (클라이언트)은 리버스 프록시 서버로
여기서 얘기하는 리버스는 **역전** 이란 뜻이 아닌 **뒷쪽** 이란 뜻이라고 생각하면 됩니다. <br/>
자세한 내용은 [joinc님의 포스팅](http://www.joinc.co.kr/w/man/12/proxy)을 참조하시면 더욱 이해하기 쉬울것 같습니다. <br/>

# Nginx 캐시 문제
[Virtual box에 centos설치하기](http://webdevnovice.tistory.com/2) <br/>
이거 최고다 전부 따라가면서 설치하면 된다. <br/>
현재까지 파일지라 설치전까지 진행 (2016.10.29)
[Nginx 캐시 설명](http://www.joinc.co.kr/w/man/12/nginx/static)
