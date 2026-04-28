# PAIR

<img width="1241" height="792" alt="메인" src="https://github.com/user-attachments/assets/c289b6a2-04fe-464b-a59f-89728708e8e0" />


## **프로젝트 소개**
**Spring Boot 기반 음식 카테고리 기반 식기 추천 기능을 제공하는 리빙/주방용품 이커머스 웹 서비스**

회원, 상품 조회, 주문/결제, 장바구니, 관리자 시스템까지 이커머스 전 흐름을 직접 설계하고 구현한 개인 프로젝트입니다. Spring Security 인증/인가, PortOne 결제 API 연동, Redis 기반 재고 선점으로 동시성 문제를 해결했으며, CountDownLatch를 활용한 멀티스레드 테스트로 검증했습니다.
  


## **기능**
- **인증/인가:** Spring Security 기반 로그인 및 사용자/관리자 권한 분리
- **재고 & 동시성 제어:** Redis 기반 재고 선점 처리, 결제 실패 시 재고 자동 복구 로직 구현
- **주문/결제 시스템:** PortOne API 연동 결제 처리, 재고-결제 간 데이터 정합성 보장
- **상품 시스템:** 페이징 및 정렬 처리, 위시리스트 및 평점 기능 구현
- **관리자 기능:** 상품 등록 및 관리
  


## **기술 스택**
Back-end: Java 17, Spring Boot 3.3.3, Spring Security 6

Front-end: HTML5/CSS3, Thymeleaf, JavaScript, Bootstrap

Database: Oracle, MyBatis

  

## **ERD**
<img width="1206" height="763" alt="ERD" src="https://github.com/user-attachments/assets/91f63f55-b092-4d31-b925-6cce8370d6c8" />



## **트러블 슈팅
**1. 주문/결제 동시성 문제 해결 — Redis 재고 선점**
- **문제 상황**
    - 결제 후 재고를 차감하는 구조에서 남은 재고가 1개일때, 동시에 두 명이 결제 성공하면 한 명은 환불을 받아야 하는 문제가 발생할 수 있음
- **해결 방법 고민**
   - 비관적 락: 결제 API가 트랜잭션 밖에 있어 락을 오래 잡게 되어 사용 불가
   - 낙관적 락: 재고 부족 시 재시도가 의미 없어 부적합
   - 원자적 UPDATE: DB 정합성은 보장되나 결제 후 환불 문제 발생
   - 결론 → 락 계열로는 결제 구간 동시성 보장 불가
- **해결** 결제 전 Redis DECRBY로 재고를 원자적으로 선점. Redis 싱글 스레드 특성상 decrement 실행 중 다른 클라이언트가 끼어들 수 없어 동시성 보장
- **검증** CountDownLatch로 10개 스레드 동시 요청 테스트 → 성공 1, 실패 9 확인
- 📝 [상세 내용 보기](https://hsmm.tistory.com/52)

 
**2. Spring Security 6 설정 방식 변경 대응**
- **문제 상황**
    - Spring Boot 3.3.3 버전에서 기존에 사용하던 WebSecurityConfigurerAdapter 상속 방식이 완전히 제거되어 보안 설정 코드가 작동하지 않음
- **원인**
    - Spring Security 6부터 컴포넌트 기반 설정이 강제되면서 상속 구조가 폐기됨. 기존 .and() 방식도 람다 구조로 바뀌어 설정 코드를 전부 다시 작성해야 했음
- **해결**
    - SecurityFilterChain을 직접 Bean으로 등록하는 방식으로 전환
    - .and() 대신 람다식을 적용해 각 보안 설정 범위를 명확히 구분
    - requestMatchers로 관리자(ROLE_ADMIN)와 사용자(ROLE_USER) 접근 권한 분리

**3. 가변적 옵션 조합을 위한 동적 알고리즘 구현**
- **문제 상황**
    - 상품마다 옵션 종류와 개수가 달라 고정 컬럼으로는 대응 불가. 다중 옵션 조합을 관리자가 일일이 수동 등록해야 해서 누락 위험이 있었음
- **원인**
    - 단일 테이블 구조로는 다차원 옵션 데이터를 유연하게 관리하기 어려움
- **해결**
    - TBL_OPTION_NAME / TBL_OPTION_VALUE / TBL_OPTION_COMB 3단계 계층 구조로 테이블 설계, 옵션 추가에도 유연하게 대응 가능
    - 이전 조합에 현재 옵션값을 순차적으로 합산하는 방식으로 옵션 종류와 개수에 상관없이 모든 경우의 수를 동적으로 생성하는 알고리즘 직접 구현
    - 생성된 조합을 테이블로 시각화, 조합별 재고와 추가 금액을 한눈에 관리할 수 있는 인터페이스 제공
    


## **시연 영상**
**관리자**

https://youtu.be/UKCOrYb93iY

**일반사용자**

https://youtu.be/ZduHqGySd0U



