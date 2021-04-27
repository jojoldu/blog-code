# 신규 서비스 구축시 고려 사항 (feat. Node.js)

## 애플리케이션

* 비지니스 시간과 시스템 시간을 분리한다
  * 이를테면 `createdAt`, `updatedAt`, `deletedAt` 등 Entity의 변경에 대한 내용은 실제 데이터베이스에 반영된 시간을 사용하되 (`@CreateDateColumn`, `@UpdateDateColumn` 등)
  * 비지니스 시간은 **애플리케이션에서 만든 시간**을 사용한다.
    * ex) 결제시간, 주문시간, 영상실행시간 등은 Controller와 같이 첫 Layer에서 시간을 만들어 service/repository/domain이 모두 같은 시간을 가지도록 한다.
