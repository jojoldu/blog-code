# 엑셀 다운로드시 OOM 발생 원인과 해결책

실제 장애 상황과 동일한 환경을 위해 다음을 사전에 진행

* EC2에 MariaDB 10.1.x 버전 설치
* Xtrabackup으로 운영 데이터 복원
* Pinpoint 로 모니터링
* Beanstalk 에 어드민 배포