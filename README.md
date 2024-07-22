# spring-gift-order


---

- **기타 리팩토링 사항**
    - [ ] 쓰이지 않는 게터,세터 등 제거하기(쓰일 때 생성하자)
    - [ ] 엔티티에 CascadeType설정 되어있어서 서비스 계층에서 delete 기능 삭제해야하는 부분들 삭제하기
    - [ ] 컨트롤러 계층 및 서비스 계층에서 DTO를 사용해야 할 상황에 Entity를 사용한 코드들 리팩토링하기
    - [ ] 각 서비스 계층에 있는 convertDTO에 관련된 메서드들을 따로 Mapper패키지로 만들어서 분리하여 관리하도록 리팩토링하기
    - [ ] authContoller의 getAllUserse도 Page로 변경
    - [ ] ProductViewController에 PUT/POST/DELETE 방식 요청의 응답도 생성하기
    - [ ] 오류처리 / 인증(JWT관련) 에 대해 추가적인 학습 후에 관련 코드 리팩토링
    - [ ] ProductController(다른 컨트롤러에 대해서도) 에서는 서비스 계층에서 가져와서 리턴하는 역할만 하도록 서비스 계층으로 코드들 이동시키는 리팩토링
    - [ ] User서비스 계층의 generateToken()메서드 body에 대칭키 기반의 HS512 말고 비대팅 키 기반의 RS256로 리팩토링
    - [ ] User서비스 계층에 있는 jwt 관련 로직들 따로 분리해서 관리하도록 리팩토링