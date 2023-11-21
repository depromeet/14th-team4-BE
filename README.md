# 14th-team4-BE

디프만 14기 4팀 백엔드 레포입니다

## 기술스택

- 기술 스택
- gradle project
- spring boot 3.x
- java 17
- mysql 8.x
- spring data jpa
- querydsl
- jdbc template

## 모듈 구조

- application(애플리케이션 모듈)
- domain(도메인 모듈)
- common(공통 모듈)
- infra(외부 모듈)
    - ex) aws, poi-excel, ..

## 모델 계층의 의존 관계 흐름

- application : xxxController
    - 독립적으로 실행 가능한 어플리케이션 모듈
    - 하위에서 설계 했던 모듈들을 조립하여 실행 시킨다
    - 사실상 여기에서는 설계한 모든 모듈을 의존하여 실행한다
- domain : xxxService, xxxDomain, xxxRepository
    - 논의 필요) Service 가 application 또는 domain에 ?
    - 시스템의 중심 도메인을 다루는 모듈
    - jpa entity 클래스와 repository가 직접적으로 연결되는 모듈
    - 실질적으로 디비에 접근하여 조회, 저장, 수정 및 삭제가 이루어진다
- client : xxxClient
    - 내부 통신 모듈
    - 만약 이번 맛집의 다른 서버들. 즉, 내부에 api로 호출하는 payment, message, auth 등을 정의함
- infra : aws-s3, aws-sqs, kafka 등등
    - 외부 라이브러리, 외부 통신 모듈
    - 언제든 교체가 가능해야 함으로 내부 시스템에 인프라의 코드가 들어가면 안된다

## gradle

### settings.gradle

- 빌드 대상 프로젝트 설정 스크립트
- 모듈의 대상 디렉토리를 모듈로 설정

### build.gradle

- 프로젝트의 의존관계, 테스크 정의할 때 사용

