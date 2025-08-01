# 토스 메이커스 컨퍼런스 2025

## 20년 레거시를 넘어 미래를 준비하는 시스템 만들기

기존 레거시 (2020.8 인수시점)
- SVN을 협업 도구로 사용하지 않고 코드 보관소로만 활용
  - 실제 배포된 코드와 SVN 코드가 불일치함
- 출시 10년이 지난 OS 활용
- 사내 DNS 시스템 없음
  - 각 서버의 `/etc/hosts` 파일 수정해서 도메인 등록
- 인프라 자동화 도구가 없어서 수백대 서버를 수작업으로 수행
- 인프라 모니터링 시스템 부족
- 라우팅 테이블이 각 서버마다 갖고 있어 수백대 서버의 라우팅 테이블을 수정하면서 운영하고 있었음

목표: 새로 합류하는 팀원이 레거시 시스템 및 인프라 제약 사하엥 영향을 받지 않고 비즈니스 임팩트를 낼 수 있게 한다.

인프라 제약에 영향 받지 않는 환경 만들기
- AWS 사용
- 개발자를 위한 플랫폼 조직 구축

단계적 시스템 도입
- 깃헙 엔터프라이즈 도입
  - 어떤 소스코드가 최신인지 알 수 없는 상태에서 깃헙 도입은 생각보다 어려움이 많았음
  - 배포된 코드를 Disassemble 해서 원본 소스코드를 추정해서 깃헙에 반영할 최신코드 반영
- 빌드 표준화
  - 기존 레거시에서는 PC 환경에 따라 빌드와 컴파일이 실패할 때가 종종 발생함 
- MSA 전환 및 쿠버네티스 도입
- 고가용성 인프라 구성
  - 오픈스택 기반 IDC 와 AWS기반의 클라우드 혼합해서 사용
- 자동화된 코드 업데이트 체계 확보
  - Common Lib를 통해 로깅, 모니터링 관리를 별도로 할 필요 없이 진행
  - 보안 취약점 등이 발견되면 자동으로 Github PR이 생성됨
- 서비스 안정성 및 성능 고도화
  - 애플리케이션부터 인프라까지를 나눠서 까나리 배포를 진행
    - 신규 인프라와 코드에는 트래픽을 1% 단위로 조절해서 전달
  - HTTP/3, TLS 1.3 서비스 적용
  - 멀티 레이어 캐시
  - 멀티 플렉싱 서버 개발
- 대규모 데이터 조회 기술 확보
  - 검색 기간 제한 없이 결제 데이터 조회 기능 구현
  - Sub Second Latency로 서빙할 수 있는 SQL 기반 아키텍처
- 보안 강화
  - 경계 보안, 내부망 보안, AI를 활용한 보안 이벤트 분석
- 어플리케이션 개편
  - 스트럿츠 1.2를 부트3, 코틀린, 리액트로 재개발
  - 비즈니스 로직 중심의 애플리케이션으로 개편
  - 클라우드 네이티브로 구현
  - 고객사의 다양한 요구사항을 빠르게 수용하는데 도움

## 시스템 운영 자동화 어디까지 해봤니?



성과
- OS 재설치 6시간 -> 15분
- 펌웨어 업데이트 11시간 -> 40분
- 복합작업 6단계 422분 -> 76분

인프라 규모가 7년만에 30배 성장
3가지 핵심 문제
- 엔지니어 마다의 작업 방식 비일관성
- 다양한 환경에 의한 복잡성
- 수동 작업 비효율

4가지 목표
- 작업 방식의 표준화
- 환경 차이의 추상화
- 수작업의 최소화
- 방어 로직 도입

이를 해결하기 위한 자동화 툴 개발 - Toss Tool (`tt`)
- 서버 정보 조회 및 관리
- 디스크 장애 자동 처리
- 알림 시스템
- OS 자동 설치
- 펌웨어 업데이트

Toss Tool 배포 및 업데이트 방식
- RPM 저장소에서 관리
- OS 설치 시점에 설치 

장점
- 작업 방식이 표준화됨
- 앤서블 등을 활용해 수백대 서버에 동시에 `tt` 명령어 수행 가능해짐
- 방어 로직이 들어감
  - Zabbix Maintenance 여부 확인
  - 서비스 유형별 작업 허용 여부 판단
  - 세션 및 연결 상태 점검
  - 전원 제어 명령어 래핑 -> 사전 점검 수행
- 알림
  - `tt` 커맨드 실행후 문제가 발생하면 카프카를 통해 알림 발생
- IaC
  - 외부 설정 저장소 기반 자동화
  - 조건별 동작 가능
  - 디스크 구성 자동화

오케스트레이션을 위한 기술 검토
- 개별 작업 -> 워크플로우 기반 자동화
- 실시간 모니터링 및 시각화
- 단계별 실패 감지 -> 자동중단 재시도
- 기존 시스템과 Toss Tool 연계 자동화

웹 콘솔 페이지를 만들어 명령어 수행 및 그라파나를 통한 모니터링 제공


## 수천 개의 API/BATCH 서버를 하나의 설정 체계로 관리하기

배치 수천개 이상, 조금씩 설정이 다른 API 서버 수천개
만약 하나의 설정 체계로 이들을 모두 관리할 수 있다면 얼마나 좋을까?

문제정의)
- 모든 서버에 공통 환경 변수를 넣고 싶어요
- 특정 서버 그룹에만 자바 힙 메모리를 올려주세요
- 특정 빌드에서만 스크립트를 수행하고 싶어요

해결책
- 오버레이 아키텍처
  - 조합 가능한 계층형 레이어 설정 구조
    - 기본값 (우선 순위 가장 낮음)
    - 최종 설정값 (우선 순위 가장 높음)
    - Global (모든 서버에 공통 설정) -> Cluster (DR인프라, 클러스터 단위 설정) -> Phase (개발, 스테이징, 운영에 따른 설정)-> App-type -> App-group -> App (특정 서버에만 적용할 설정)
  
오버레이 아키텍처 단점 해소
- 템플릿 패턴
  - `-Xms{{XMS}}` 템플릿으로 관리하고 그 값은 `phase/dev.yaml` 에서 관리

### 토스페이먼츠 배치

한달 수조원 거래를 정산

적정 기술
- Jenkins, Airflow, K8s CronJob 중 Jenkins 선택

개발자가 선언만 하면 나머지는 데브옵스가 알아서 처리하는 구조를 만들자

배치 설정에서 해결해야하는 문제
- 수백개의 젠킨스 잡을 JVM 11에서 21로 업데이트 해야하는데...
- 배치 리소스를 나 혼자만 사용하고 싶은데 그러기엔 어려워

Job DSL)
- k8s처럼 yaml로 관리하자. -> Job DSL 플러그인 어뎁터
  - 페이먼츠 자체 구현하자
- 배치 리소스를 혼자 사용할 수 있도록 다이나민 프로비저닝 적용

다이나믹 프로비저닝)
- 1개 Job 마다 1개 Node 할당해서 다른 Job 프로세스가 차지하는 하드웨어 자원에 대해 신경쓰지 않도록 함