# spring-gift-order
## 1단계 - 카카오 로그인
### 카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

- **요구 조건**
  - 카카오계정 로그인을 통해 인증 코드를 받는다.
  - 토큰 받기를 읽고 액세스 토큰을 추출한다.
  - 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
  - 특히 시크릿 키는 GitHub나 클라이언트 코드 등 외부에서 볼 수 있는 곳에 추가하지 않는다.

- **구현 기능**
  - [X] KakaoUserEntity 생성
  - [X] KakaoUserDTO 생성
  - [X] KakaoUserRepository 생성
  - [X] KakaoOAuthService 구현
    - 구현 메서드
      1) 인가 코드를 이용해서 카카오 측으로부터 토큰(엑세스, 리프래쉬)을 받아온다.
      2) 엑세스 토큰을 이용해서 카카오 측으로부터 사용자 정보를 받아온다.
      3) 엑세스 토큰이 만료되면, 리프래쉬 토큰을 이용해서 새로운 엑세스 토큰을 받아온다.
  - [X] KakaoUserService 구현
    - 구현 메서드
      1) KakaoOAuthService를 통해 카카오로부터 받아온 사용자 정보를 서버 쪽 DB에 저장한다.
  - [X] KakaoAuthService 구현
    - 구현 메서드
      1) KakaoUserService, KakaoAuthService의 메서드들을 조합해서 인가코드 -> 토큰 -> 사용자 정보 -> 서버 DB 까지의 일련의 과정을 수행함.
  - [X] KakaoUserMapeer 구현
  - [ ] KakaoOAuthService에 앱키, 시크릿 키 사용 로직 추가하기
  - [ ] refreshAccessToken 메서드(엑세스 토큰 갱신) 사용 로직 추가하기
  - [ ] 테스트 코드 추가하기
  - [x] OAuth말고 기본 회원가입 및 로그인 구현 기능에서 토큰 만료 및 리프레시 구현



- 구현 시나리오
  1) 사용자에게 카카오 로그인 화면이 띄어진다.
  2) 사용자가 카카오 로그인을 하면 카카오는 "~을 허용하시겠습니까?" 와 같은 화면을 사용자에게 보낸다.
  3) 사용자가 ok하면 사용자가 모르게(알 필요도 없다) 사용자(클라이언트 즉, 리소스 오너)에서 서버쪽으로 인가 코드가 보내진다.
  4) 서버에서는 인가 코드를 받고, 카카오쪽으로 토큰을 요청한다.
  5) 카카오쪽에서 서버로 엑세스 토큰을 발급한다.
  6) 서버는 엑세스 토큰을 갖고 카카오쪽에게 사용자 정보를 요청하고 받는다.
  7) 서버는 자기 쪽 DB에 저장한다.


---

- **기타 리팩토링 사항**
  - [x] 테스트 코드에 중복되는 코드들 통합시키기
  - [x] 옵션 서비스 계층에 필요한 트랜잭션 처리 추가하기
  - [x] 옵션 서비스 계층에 중복코드 private 메서드로 분리시키기
  - [x] 쓰이지 않는 게터,세터 등 제거하기(쓰일 때 생성하자)
  - [x] 엔티티에 CascadeType설정 되어있어서 서비스 계층에서 delete 기능 삭제해야하는 부분들 삭제하기
  - [x] authContoller의 getAllUserse도 Page로 변경
  - [x] ProductViewController에 PUT/POST/DELETE 방식 요청의 응답도 생성하기
  - [x] ProductController(다른 컨트롤러에 대해서도) 에서는 서비스 계층에서 가져와서 리턴하는 역할만 하도록 서비스 계층으로 코드들 이동시키는 리팩토링
  - [x] 각 서비스 계층에 있는 convertDTO에 관련된 메서드들을 따로 Mapper패키지로 만들어서 분리하여 관리하도록 리팩토링하기
  - [x] 컨트롤러 계층 및 서비스 계층에서 DTO를 사용해야 할 상황에 Entity를 사용한 코드들 리팩토링하기
  - [ ] 오류처리 / 인증(JWT관련) 에 대해 추가적인 학습 후에 관련 코드 리팩토링
  - [ ] User서비스 계층에 있는 jwt 관련 로직들 따로 분리해서 관리하도록 리팩토링
  - [ ] User서비스 계층의 generateToken()메서드 body에 대칭키 기반의 HS512 말고 비대팅 키 기반의 RS256로 리팩토링