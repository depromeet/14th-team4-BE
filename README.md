<p align="center"><img src="https://github.com/depromeet/14th-team4-BE/assets/76957700/ebcea06b-cfd7-4d58-b617-8c068e1eb5a0"  width="800" height="450"/></p>

<br/><br/>

<h2 align="center">🍽️ 또잇또잇 - 두 번 이상 간 맛집을 공유하는 서비스</h2>
<br/><br/>

![image](https://github.com/depromeet/14th-team4-BE/assets/76957700/2a02b176-7557-48aa-9091-b270c37c2f8c)

## 링크

- Android Play Store
- [Server Document](https://api.ddoeat.site/docs/index.html)

----

## 1. 기술스택

### Backend

- gradle project
- spring boot 3.x
- java 17
- mysql 8.x
- spring data jpa

- jdbc template

### Infra

- Naver Cloud Platform
    - server
    - object storage
    - (image optimizer)
- Docker
- Github Actions CI/CD
- Nginx

### Communication

- Jira
- Notion
- Figma

## 2. 시스템 아키텍처
<img width="1142" alt="스크린샷 2024-02-14 오후 11 15 56" src="https://github.com/depromeet/14th-team4-BE/assets/76957700/63785025-06d8-42e4-a87b-3d118b3f9259">


## 3. 모듈 구조

- application(애플리케이션 모듈)
- domain(도메인 모듈)
- common(공통 모듈)
- infra(외부 모듈)
    - ex) aws, poi-excel, ..

### 모델 계층의 의존 관계 흐름

- application : xxxController, xxxService
    - 독립적으로 실행 가능한 어플리케이션 모듈
    - 하위에서 설계 했던 모듈들을 조립하여 실행 시킨다
    - 사실상 여기에서는 설계한 모든 모듈을 의존하여 실행한다
- domain : xxxDomain, xxxRepository
    - 시스템의 중심 도메인을 다루는 모듈
    - jpa Entity, (Entity) Repository가 직접적으로 연결되는 모듈
- common
    - 공통으로 쓰이는 것들을 모아둔 모듈
    - 공통 응답, 공통 에러 헨들러, 로그 설정 등을 정의함
- infra : s3, redis 등등
    - 외부 라이브러리, 외부 통신 모듈
    - 언제든 교체가 가능해야 함으로 내부 시스템에 인프라의 코드가 들어가면 안된다

### gradle

#### settings.gradle

      - 빌드 대상 프로젝트 설정 스크립트
      - 모듈의 대상 디렉토리를 모듈로 설정

#### build.gradle

      - 프로젝트의 의존관계, 테스크 정의할 때 사용

## 4. ERD

![image](https://github.com/depromeet/14th-team4-BE/assets/57219160/d31749c8-05b3-485a-b587-cb43bc7e552e)

## 5. 또잇또잇 백엔드팀 규칙 !!

### 5-1. 개발 프로세스

1. 지라 티켓 생성
2. 지라 티켓 기반 branch (`feature/#티켓번호`) 생성
3. 개발 완료시 PR
4. develop merge : 코드 리뷰 & approve 1명 이상 완료시

### 5-2. 커밋 규칙

- `feat`: 새로운 기능 추가
- `fix`: 버그 수정
- `hotfix`: 라이브 상황에서 대응해야하는 경우
- `docs`: 문서 수정
- `style`: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- `refactor`: 코드 리펙토링
- `test`: 테스트 코드, 리펙토링 테스트 코드 추가
- `chore`: 빌드 업무 수정, 패키지 매니저 수정

## 6. 팀원

### 🧑🏻‍💻 Server Engineers

|<img src="https://avatars.githubusercontent.com/u/57219160?v=4" width="80">|<img src="https://avatars.githubusercontent.com/u/76957700?v=4"  width="80">|<img src="https://avatars.githubusercontent.com/u/112103038?v=4" width="80">|
|:---:|:---:|:---:|
|[허강준](https://github.com/highjune)|[유희수](https://github.com/Ryuhyis)|[최필환](https://github.com/thisishwan2)|
  <br>

### Front Engineers

|<img src="https://avatars.githubusercontent.com/u/82137004?v=4" width="80">|<img src="https://avatars.githubusercontent.com/u/110076475?v=4" width="80">|<img src="https://avatars.githubusercontent.com/u/27201591?v=4" width="80">|<img src="https://avatars.githubusercontent.com/u/89122773?v=4" width="80">|
|:---:|:---:|:---:|:---:|
|[이상조](https://github.com/sjoleee)|[오예린](https://github.com/YelynnOh)|[유은지](https://github.com/y00eunji)|[유준상](https://github.com/YOOJS1205)|
  <br>

