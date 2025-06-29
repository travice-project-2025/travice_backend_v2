# <image width=28 height=28 src="https://github.com/user-attachments/assets/16f86bb4-cee9-4798-a66c-a47ee1026393"> Travice

<aside>
💡 여행 도우미 Travice는 AI를 활용하여 사용자 맞춤형 여행 계획을 생성합니다.<br>
💡 동행 찾기 게시판을 통해 자신이 원하는 여행 계획 혹은 여행 메이트를 찾을 수 있습니다 <br>
💡 링크 공유를 통해 자신의 계획을 공유하고, 공유한 멤버와 함께 계획을 수정할 수 있습니다.
</aside>

---

### 🔧 기술 스택

- **Frontend**  
  Vue3 · JavaScript
- **Backend**  
  Spring Boot
- **Database**  
  MySQL  
- **Infra & Tools**  
  Git · AWS EC2, RDS ( 예정 )  

---

### 🚀 구현 기능


#### 🔑 로그인 / 로그아웃 / 회원가입
- **로그인** / **로그아웃** 기능  
- **OAuth**를 통한 소셜 로그인 기능
- Naver, Google, Kakao 로그인 구현

---

#### 😋 개인 정보 수정
- 닉네임, 성별, 나이, 프로필 사진 수정
- 여행, 동행 횟수, 방문 지역 수 확인
- 여행 스탬프 조회

---

#### 🤖 계획 추천 서비스 ( GPT API )
- 여행지 및 사용자 정보를 기반으로 계획 생성
- 날짜, 여행지, 인원, 종류, 사용자 여행 성향, MBTI 입력
- 정보 기반 GPT 여행 계획 추천
- 위 정보를 기반으로 여행 계획 페이지 자동 생성

---

#### 📋 여행 계획 상세 페이지 
- **여행 계획 생성**
  - Kakao Map 구현
  - Kakao Local API 로 장소 검색 구현
  - TMap API로 실제 교통 정보를 반영한 경로 생성
  - drag & drop 으로 일정 변경
  - 일정 및 교통수단 조회
    
- **장소 추가 / 삭제 / 상세**
  - 장소 추가 시 장소, 시간, 이동수단, 메모, 주소 추가
  - 장소 삭제 기능 구현
  - 상세 조회 시 시간, 메모 등 조회 가능
    
---

#### 📌 동행 게시판 
  - 제목, 내용, 모집상태, 여행지, 인원, 기간, 선호 조건, 작성자 표기
  - 선호 조건에 따른 게시글 필터링 구현
  - 한 페이지에 5개의 게시글이 보이도록 Paging 구현
     
---

#### 🤝 동행 게시판 상세 페이지
- **상세 페이지 조회**
    - 작성자, 게시글, 여행 정보, 댓글 조회
    - 작성자가 본인일 경우 수정, 삭제 버튼 나오도록 구현
    - 게시글 조회 시 조회수 증가하도록 구현

- **상세 페이지 생성**
    - 자신의 여행 계획 중 하나를 선택하여 게시글을 작성하도록 구현
    - 동행 조건 ( 인원, 성별, 연령, 비용 ) 입력 

- **수정, 삭제**
    - 게시글 수정, 삭제 기능 구현
    - 게시글 작성자만 가능하도록 구현

- **댓글**
    - 댓글 작성, 수정, 삭제 기능 구현
    - 본인 댓글만 수정, 삭제 가능하도록 구현
    - 본인 댓글이 아니더라도 게시글 작성자는 삭제 가능하도록 구현

---

#### 🔗 공유 링크
- **공유 링크 생성**
    - Redis를 이용하여 공유 링크 생성
    - 보기 모드 링크 / 편집 모드 링크 를 구별하여 사용자 권한 관리
    - 한 번 공유 이후 링크가 바뀌도록 구현

- **링크를 통한 여행 계획 참여**
    - 보기 모드로 참여할 경우, 오직 조회만 가능하도록 구현
    - 편집 모드로 참여할 경우, 계획 수정 및 삭제가 가능하도록 구현

---

### 📸 주요화면

---

## 📂 프론트엔드 프로젝트 구조
```
├── .gitignore
├── .vscode
│  └── extensions.json
├── index.html
├── package-lock.json
├── package.json
├── public
├── src
│  ├── App.vue
│  ├── assets
│  │  ├── icons
│  │  │  ├── google.svg
│  │  │  ├── kakao.svg
│  │  │  └── naver.svg
│  │  ├── images
│  │  │  ├── airplane_img.png
│  │  │  ├── .
│  │  │  ├── .
│  │  │  ├── .
│  │  │  └── 부산.png
│  │  ├── styles
│  │  │  ├── base.css
│  │  │  └── variables.css
│  │  └── travel.mp4
│  ├── components
│  │  ├── auth
│  │  │  └── SocialLogin.vue
│  │  ├── common
│  │  │  └── AppHeader.vue
│  │  └── plan
│  │    ├── AddPlaceModal.vue
│  │    ├── DayTab.vue
│  │    ├── PlaceItem.vue
│  │    ├── PlaceList.vue
│  │    ├── PlaceSearch.vue
│  │    ├── PlanEditView.vue
│  │    ├── PlanRecommendationView.vue
│  │    ├── PlanViewMode.vue
│  │    └── TripMap.vue
│  ├── composables
│  │  ├── usePlanData.js
│  │  ├── usePlanDetail.js
│  │  ├── usePlanSave.js
│  │  └── userAuth.js
│  ├── layouts
│  ├── main.js
│  ├── pages
│  │  ├── BoardDetail.vue
│  │  ├── BoardPage.vue
│  │  ├── CreateBoard.vue
│  │  ├── CreatePlanWizard.vue
│  │  ├── JoinPlan.vue
│  │  ├── LoginPage.vue
│  │  ├── OnboardingPage.vue
│  │  ├── PlanCreatePage.vue
│  │  ├── PlanDetailPage.vue
│  │  ├── PlansPage.vue
│  │  └── ProfilePage.vue
│  ├── router
│  │  └── index.js
│  └── services
│    └── inviteService.js
└── vite.config.js

```

---
## 📂 백엔드 프로젝트 구조
```
├── .DS_Store
├── .env
├── .gitignore
├── .gradle
│    .
|    .
├── .idea
├── build
├── build.gradle
├── gradle
│    .
|    .
├── gradlew
├── gradlew.bat
├── LICENSE
├── settings.gradle
└── src
  ├── main
  │  ├── java
  │  │  └── com
  │  │    └── gmg
  │  │      └── travice
  │  │        ├── config
  │  │        │  ├── JacksonConfig.java
  │  │        │  ├── RedisConfig.java
  │  │        │  ├── SecurityConfig.java
  │  │        │  └── WebClientConfig.java
  │  │        ├── domain
  │  │        │  ├── board
  │  │        │  │  ├── controller
  │  │        │  │  │  └── BoardController.java
  │  │        │  │  ├── dto
  │  │        │  │  │  ├── BoardCreateDTO.java
  │  │        │  │  │  ├── BoardDetailDTO.java
  │  │        │  │  │  ├── BoardListDTO.java
  │  │        │  │  │  ├── BoardUpdateDTO.java
  │  │        │  │  │  ├── CommentCreateDTO.java
  │  │        │  │  │  ├── CommentDTO.java
  │  │        │  │  │  └── CommentResponseDTO.java
  │  │        │  │  ├── entity
  │  │        │  │  │  ├── Board.java
  │  │        │  │  │  ├── BoardType.java
  │  │        │  │  │  ├── Comment.java
  │  │        │  │  │  └── PreferenceGender.java
  │  │        │  │  ├── repository
  │  │        │  │  │  ├── BoardRepository.java
  │  │        │  │  │  └── CommentRepository.java
  │  │        │  │  └── service
  │  │        │  │    ├── BoardService.java
  │  │        │  │    └── BoardServiceImpl.java
  │  │        │  ├── invite
  │  │        │  │  ├── controller
  │  │        │  │  │  └── InviteLinkController.java
  │  │        │  │  ├── dto
  │  │        │  │  │  ├── request
  │  │        │  │  │  │  ├── CreateInviteLinkRequest.java
  │  │        │  │  │  │  └── JoinPlanRequest.java
  │  │        │  │  │  └── response
  │  │        │  │  │    ├── InviteLinkResponse.java
  │  │        │  │  │    ├── JoinPlanResponse.java
  │  │        │  │  │    └── PlanInfoResponse.java
  │  │        │  │  ├── entity
  │  │        │  │  │  └── InviteLink.java
  │  │        │  │  ├── invite
  │  │        │  │  │  └── dto
  │  │        │  │  ├── repository
  │  │        │  │  │  └── InviteLinkRepository.java
  │  │        │  │  └── service
  │  │        │  │    └── InviteLinkService.java
  │  │        │  ├── plan
  │  │        │  │  ├── controller
  │  │        │  │  │  └── PlanController.java
  │  │        │  │  ├── dto
  │  │        │  │  │  ├── PlanDetailDTO.java
  │  │        │  │  │  ├── PlanDTO.java
  │  │        │  │  │  ├── PlanListResponseDTO.java
  │  │        │  │  │  └── TransportDTO.java
  │  │        │  │  ├── entity
  │  │        │  │  │  ├── City.java
  │  │        │  │  │  ├── Plan.java
  │  │        │  │  │  ├── PlanDetail.java
  │  │        │  │  │  ├── PlanMember.java
  │  │        │  │  │  ├── Theme.java
  │  │        │  │  │  └── Transport.java
  │  │        │  │  ├── repository
  │  │        │  │  │  ├── CityRepository.java
  │  │        │  │  │  ├── PlanDetailRepository.java
  │  │        │  │  │  ├── PlanMemberRepository.java
  │  │        │  │  │  ├── PlanRepository.java
  │  │        │  │  │  └── TransportRepository.java
  │  │        │  │  └── service
  │  │        │  │    ├── PlanService.java
  │  │        │  │    └── PlanServiceImpl.java
  │  │        │  ├── recommend
  │  │        │  │  ├── controller
  │  │        │  │  │  └── PlanRecommendController.java
  │  │        │  │  ├── dto
  │  │        │  │  │  ├── RecommendRequestDTO.java
  │  │        │  │  │  └── RecommendResponseDTO.java
  │  │        │  │  ├── service
  │  │        │  │  │  └── OpenAiRecommendService.java
  │  │        │  │  └── util
  │  │        │  │    └── OpenAiPromptBuilder.java
  │  │        │  └── user
  │  │        │    ├── controller
  │  │        │    │  └── UserController.java
  │  │        │    ├── dto
  │  │        │    │  ├── UserDTO.java
  │  │        │    │  └── UserUpdateDTO.java
  │  │        │    ├── entity
  │  │        │    │  ├── GenderType.java
  │  │        │    │  ├── LoginType.java
  │  │        │    │  └── User.java
  │  │        │    ├── repository
  │  │        │    │  └── UserRepository.java
  │  │        │    └── service
  │  │        │      ├── UserService.java
  │  │        │      └── UserServiceImpl.java
  │  │        ├── feature
  │  │        │  └── login
  │  │        │    ├── controller
  │  │        │    │  └── AuthController.java
  │  │        │    ├── jwt
  │  │        │    │  ├── JWTFilter.java
  │  │        │    │  └── JWTUtil.java
  │  │        │    ├── oAuth
  │  │        │    │  ├── handler
  │  │        │    │  │  └── CustomSuccessHandler.java
  │  │        │    │  ├── provider
  │  │        │    │  │  ├── GoogleResponse.java
  │  │        │    │  │  ├── KakaoResponse.java
  │  │        │    │  │  ├── NaverResponse.java
  │  │        │    │  │  └── OAuth2Response.java
  │  │        │    │  └── user
  │  │        │    │    └── CustomOAuth2User.java
  │  │        │    └── service
  │  │        │      └── CustomOAuth2UserService.java
  │  │        └── TraviceApplication.java
  │  └── resources
  │    └── application.yml
  └── test
```

<br>

---

## 📈 ERD Diagram
![Image](https://github.com/user-attachments/assets/bd17d9ca-22a6-40ae-aa11-064820c65147)

---

## 개발자
<div align="center">

<table>
  <tr>
    <td align="center">
      <strong>이태호</strong><br>
      <a href="https://github.com/xogh7882">
        <img src="https://avatars.githubusercontent.com/u/109503919?v=4" width="150" height="150"><br>
        @xogh7882
      </a><br>
      FrontEnd │ BackEnd
    </td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td align="center">
      <strong>홍정인</strong><br>
      <a href="https://github.com/hongjungin">
        <img src="https://avatars.githubusercontent.com/u/94633439?v=4" width="150" height="150"><br>
        @hongjungin
      </a><br>
      FrontEnd │ BackEnd
    </td>
  </tr>
</table>

</div>
