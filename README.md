# PAIR

<img width="1241" height="792" alt="메인" src="https://github.com/user-attachments/assets/c289b6a2-04fe-464b-a59f-89728708e8e0" />


## **프로젝트 소개**
**Spring Boot 기반 음식 카테고리 기반 식기 추천 기능을 제공하는 리빙/주방용품 이커머스 웹 서비스**

회원 관리, 상품 조회, 주문/결제, 장바구니, 관리자 시스템까지 이커머스 전 흐름을 직접 설계하고 구현한 개인 프로젝트입니다. Spring Security와 PortOne 결제 API를 연동해 실무 환경을 반영한 시스템을 구축하고, 동시성 문제 해결을 위한 재고 차감 로직을 직접 설계하고 검증하며 안정적인 트랜잭션 처리 구조를 구현했습니다.


## **기능**
- 회원 관리: Spring Security 기반 로그인/회원가입 및 사용자와 관리자 권한 분리
  
- 상품 시스템: 페이징 및 정렬 기능을 포함한 상품 목록, 위시리스트, 평점, 재고 관리
  
- 주문/결제: PortOne API 연동을 통한 실제 결제 프로세스 구현 및 주문 처리
  
- 장바구니: 상품 담기, 수량 변경, 금액 계산 기능 구현
  
- 마이페이지: 최근 본 상품, 주문 내역 조회, 구매 상품 리뷰 작성
  
- 관리자 시스템: 상품 등록/수정 및 옵션 동적 처리
  


## **기술 스택**
Back-end: Java 17, Spring Boot 3.3.3, Spring Security 6

Front-end: HTML5/CSS3, Thymeleaf, JavaScript, Bootstrap

Database: Oracle, MyBatis



## **ERD**
<img width="1206" height="763" alt="ERD" src="https://github.com/user-attachments/assets/91f63f55-b092-4d31-b925-6cce8370d6c8" />



## **트러블 슈팅**
**1. Spring Security 6 설정 방식 변경 대응**
- **문제 상황**
    - Spring Boot 3.3.3 버전에서 기존에 사용하던 WebSecurityConfigurerAdapter 상속 방식이 완전히 제거되어 보안 설정 코드가 작동하지 않음
- **원인**
    - Spring Security 6부터 컴포넌트 기반 설정이 강제되면서 상속 구조가 폐기됨. 기존 .and() 방식도 람다 구조로 바뀌어 설정 코드를 전부 다시 작성해야 했음
- **해결**
    - SecurityFilterChain을 직접 Bean으로 등록하는 방식으로 전환
    - .and() 대신 람다식을 적용해 각 보안 설정 범위를 명확히 구분
    - requestMatchers로 관리자(ROLE_ADMIN)와 사용자(ROLE_USER) 접근 권한 분리
    
**2. 가변적 옵션 조합을 위한 동적 재귀 알고리즘 구현**
- **문제 상황**
    - 상품마다 옵션 종류와 개수가 달라 고정 컬럼으로는 대응 불가. 다중 옵션 조합을 관리자가 일일이 수동 등록해야 해서 누락 위험이 있었음
- **원인**
    - 단일 테이블 구조로는 다차원 옵션 데이터를 유연하게 관리하기 어려움
- **해결**
    - TBL_OPTION_NAME / TBL_OPTION_VALUE / TBL_OPTION_COMB 3단계 계층 구조로 테이블 설계, 옵션 추가에도 유연하게 대응 가능
    - 다차원 배열을 순회하며 모든 경우의 수를 자동 추출하는 재귀 알고리즘 직접 구현
    - 생성된 조합을 테이블로 시각화, 조합별 재고와 추가 금액을 한눈에 관리할 수 있는 인터페이스 제공
    
**3. 주문/결제 시 재고 차감 동시성 고려 설계 및 검증**
- **문제 인식**
    - 주문 처리 과정에서 동시 요청 발생 시 재고 불일치 및 수량이 음수로 감소하는 문제가 발생할 수 있음
- **해결**
    - 원자적 UPDATE, 비관적 락, 낙관적 락 방식 비교 검토
    - 단순 재고 차감의 경우 DB 수준에서 정합성을 보장할 수 있는 원자적 UPDATE 방식이 가장 적합하다고 판단
- **검증**
    - ExecutorService, CountDownLatch를 이용한 멀티스레드 테스트를 통해 동시 요청 환경에서도 재고 음수 발생 없이 정확히 차감됨을 검증


## **시연 영상**
**관리자**

https://youtu.be/UKCOrYb93iY

**일반사용자**

https://youtu.be/ZduHqGySd0U



