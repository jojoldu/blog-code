# HTTP API 디자인

모든 API는 예측 가능해야한다.  
이 URL만 봐도 무엇을 하는 것인지 상세 스펙을 보지 않아도 알 수 있어야 한다.

## 상태 변화

- Get: 상태 변화를 주지 않는 기능
- Post: 상태 변화를 주는 기능

## 다양한 상태 표현

- `/order/:orderId/cancel`


## Path, Request, Response

해당 리소스의 고유키 까지만 path variable에 포함시킨다.
그 외 고유키가 아닌 것은 모두 Request Param/body에 포함시킨다
