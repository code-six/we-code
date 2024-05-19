# we-code

>[코드 식스(6조) 프로젝트 페이지 (Notion)](https://www.notion.so/6a94725cc77c40a0b2ee645d1e5c714c)
<br>

>[프로젝트 기획안 (PDF)](https://kdt-goorm.slack.com/files/U06BMUQKSKE/F070ZTFSJ7P/project_proposal.pdf)
<br>

>[UI 프로토타입 (Figma)](https://www.figma.com/file/aENtsNNmriJLQ7ikxacdpW/%EC%BD%94%EB%93%9C%EC%8B%9D%EC%8A%A4-UI-%EC%B4%88%EC%95%88?type=design&node-id=0-1&mode=design)
<br>

## 팀: 코드6

| ![yjiiny](https://avatars.githubusercontent.com/u/153581188) | ![OiPKL](https://avatars.githubusercontent.com/u/130027416) | ![jw-park-github](https://avatars.githubusercontent.com/u/165545220) | ![rpghks07](https://avatars.githubusercontent.com/u/2668683) |
| --- | --- | --- | --- |
| [yjiiny](https://github.com/yjiiny) | [OiPKL](https://github.com/OiPKL) | [jw-park-github](https://github.com/jw-park-github) | [rpghks07](https://github.com/rpghks07) |

<br>

## 1. 프로젝트 개요
<strong>ㆍ 시행처</strong>: [구름 x 인프런] 자바 스프링 & 리액트 풀스택 개발자 성장 과정<br><br>
<strong>ㆍ 기간</strong>: 2024.04 ~ 진행중<br><br>
<strong>ㆍ 프로그래밍 언어</strong>: Java, HTML, CSS, JavaScript<br><br>
<strong>ㆍ 프레임워크</strong>: Spring Boot 3.2.5<br><br>
<strong>ㆍ DBMS</strong>: H2<br><br>
<strong>ㆍ 배포</strong>: 카카오 크램폴린<br><br>

## 2. 주요 기능
* 코드(Post) 및 댓글(Comment) CRUD 기능
  
* 질문(Question) 및 답변(Answer) CRUD 기능
  
* 회원가입, 로그인, 로그아웃, 회원 정보 관리
  

## 3. DB

## 3-1) ERD
<img width="960" alt="ERD" src="https://github.com/code-six/we-code-0509/assets/165545220/74d42c57-966f-4f94-bf18-8e0b4a8998d0"><br>


## 3-2) DB 설계도

### **SiteUser (회원)**

| 필드명 | 자료형 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | Long | PK, Not Null, Increment | 고유값 |
| email | String | Not Null | 이메일 주소 |
| password | String | Not Null | 비밀번호 |
| username | String | Not Null | 닉네임 |

### **Post (게시글)**

| 필드명 | 자료형 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | Integer | PK, Not Null, Increment | 고유값 |
| subject | String | Not Null | 게시글 제목 |
| author | SiteUser | Not Null | 게시글 작성자 |
| content | String | Not Null | 게시글 내용 |
| createDate | LocalDateTime | Not Null | 게시글 작성일 |
| modifyDate | LocalDateTime | Not Null | 게시글 수정일 |

### **Comment (댓글)**

| 필드명 | 자료형 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | Integer | PK, Not Null, Increment | 고유값 |
| author | SiteUser | Not Null | 댓글 작성자 |
| content | String | Not Null | 댓글 내용 |
| createDate | LocalDateTime | Not Null | 댓글 작성일 |
| modifyDate | LocalDateTime | Not Null | 댓글 수정일 |

### **Question (질문)**

| 필드명 | 자료형 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | Integer | PK, Not Null, Increment | 고유값 |
| subject | String | Not Null | 질문 제목 |
| author | SiteUser | Not Null | 질문 작성자 |
| content | String | Not Null | 질문 내용 |
| createDate | LocalDateTime | Not Null | 질문 작성일 |
| modifyDate | LocalDateTime | Not Null | 질문 수정일 |

### **Answer (답변)**

| 필드명 | 자료형 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | Integer | PK, Not Null, Increment | 고유값 |
| author | SiteUser | Not Null | 답변 작성자 |
| content | String | Not Null | 답변 내용 |
| createDate | LocalDateTime | Not Null | 답변 작성일 |
| modifyDate | LocalDateTime | Not Null | 답변 수정일 |

## 4. API 설계

### **SiteUser (회원)**

| 기능            | Method | URL                | Return                          |
|----------------|--------|--------------------|---------------------------------|
| 회원가입 폼 표시  | GET    | `/user/signup`     | Return `signup_form` 페이지      |
| 회원가입         | POST   | `/user/signup`     | Redirect to `메인 페이지`         |
| 로그인 폼 표시   | GET    | `/user/login`      | Return `login_form` 페이지       |

### **Post (게시글)**

| 기능         | Method | URL                    | Return                        |
|--------------|--------|------------------------|-------------------------------|
| 게시글 목록 조회 | GET    | `/post/list`           | Return `post_list` 페이지      |
| 게시글 상세 조회 | GET    | `/post/detail/{id}`    | Return `post_detail` 페이지    |
| 게시글 생성 폼 표시| GET    | `/post/create`         | Return `post_form` 페이지      |
| 게시글 생성   | POST   | `/post/create`         | Redirect to `게시글 목록 페이지` |
| 게시글 수정 폼 표시| GET    | `/post/modify/{id}`    | Return `post_form` 페이지      |
| 게시글 수정   | POST   | `/post/modify/{id}`    | Redirect to `게시글 상세 페이지` |
| 게시글 삭제   | GET    | `/post/delete/{id}`    | Redirect to `게시글 목록 페이지` |
| 게시글 투표   | GET    | `/post/vote/{id}`      | Redirect to `게시글 상세 페이지` |

### **Comment (댓글)**

| 기능         | Method | URL                          | Return                        |
|--------------|--------|------------------------------|-------------------------------|
| 댓글 생성    | POST   | `/comment/create/{postId}`   | Redirect to `게시글 상세 페이지` |
| 댓글 수정 폼 표시 | GET    | `/comment/modify/{commentId}` | Return `comment_form` 페이지   |
| 댓글 수정    | POST   | `/comment/modify/{commentId}` | Redirect to `게시글 상세 페이지` |
| 댓글 삭제    | GET    | `/comment/delete/{commentId}` | Redirect to `게시글 상세 페이지` |
| 댓글 투표    | GET    | `/comment/vote/{commentId}`   | Redirect to `게시글 상세 페이지` |

### **Question (질문)**

| 기능         | Method | URL                      | Return                          |
|--------------|--------|--------------------------|---------------------------------|
| 질문 목록 조회  | GET    | `/question/list`         | Return `question_list` 페이지    |
| 질문 상세 조회  | GET    | `/question/detail/{id}`  | Return `question_detail` 페이지  |
| 질문 생성 폼 표시| GET    | `/question/create`       | Return `question_form` 페이지    |
| 질문 생성     | POST   | `/question/create`       | Redirect to `질문 목록 페이지`     |
| 질문 수정 폼 표시| GET    | `/question/modify/{id}`  | Return `question_form` 페이지    |
| 질문 수정     | POST   | `/question/modify/{id}`  | Redirect to `질문 상세 페이지`     |
| 질문 삭제     | GET    | `/question/delete/{id}`  | Redirect to `메인 페이지`         |
| 질문 투표     | GET    | `/question/vote/{id}`    | Redirect to `질문 상세 페이지`     |

### **Answer (답변)**

| 기능           | Method | URL                   | Return                         |
|--------------|--------|-----------------------|--------------------------------|
| 메인 페이지 리다이렉트 | GET    | `/`                   | Redirect to `/question/list`    |
| 답변 생성        | POST   | `/answer/create/{id}` | Redirect to `질문 상세 페이지`     |
| 답변 수정 폼 표시   | GET    | `/answer/modify/{id}` | Return `answer_form` 페이지    |
| 답변 수정       | POST   | `/answer/modify/{id}` | Redirect to `질문 상세 페이지`     |
| 답변 삭제       | GET    | `/answer/delete/{id}` | Redirect to `질문 상세 페이지`     |
| 답변 투표       | GET    | `/answer/vote/{id}`   | Redirect to `질문 상세 페이지`     |


## 5. 상세 화면
<strong>[/post/list]</strong><br>
![메인화면_코드 목록](https://github.com/code-six/we-code/assets/165545220/3e6fceba-c38f-431a-8c00-67e5326506fd)

<strong>[/post/create]</strong><br>
![코드 등록 화면](https://github.com/code-six/we-code/assets/165545220/877320f0-d19b-44f8-a2bd-c32b91a2be46)

<strong>[/question/create]</strong><br>
![질문 등록 화면](https://github.com/code-six/we-code/assets/165545220/ebae0661-b3f7-453a-9c78-fd60e8683b89)

<strong>[/question/list]</strong><br>
![질문 상세 화면](https://github.com/code-six/we-code/assets/165545220/c69962cc-899a-4212-a78e-4aa0cd39a73d)

<strong>[/user/mypage]</strong><br>
![회원 정보 수정](https://github.com/code-six/we-code/assets/165545220/378c6589-e6c0-4b64-a6e3-551c3a998320)

<strong>[/user/signup]</strong><br>
![회원가입](https://github.com/code-six/we-code/assets/165545220/acbeee8c-9561-494f-a2d7-3e8df477d4d4)
