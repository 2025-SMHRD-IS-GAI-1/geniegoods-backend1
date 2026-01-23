<table style="border:none; width:100%;">
  <tr style="border:none;">
    <td style="border:none; vertical-align:middle;">
      <img width="361" height="107" alt="Image" src="https://github.com/user-attachments/assets/bb57ba9c-27c7-4427-a148-32c0d378d0f8" />
    </td>
    <td style="border:none; text-align:right; vertical-align:middle;">
      <b>팀장</b> - 김태현
      <br>
      <b>팀원</b> - 이용일,  김다혜, 김태경, 한정훈
      <br><br><br>
       <b>프로젝트 기간</b>
       <br>
      2025.12.30 ~ 2026.01.27
    </td>
  </tr>
</table>


<br>


## 1. 서비스 소개
### 주제 : Google Gemini 2.5 Flash + YOLOv11 기반 사용자 맞춤 반려동물 굿즈 시안 자동 생성 및 주문 웹 서비스
- 여러개의 이미지를 첨부하고 프롬프트 작성 및 키워드 클릭후 굿즈 이미지 생성
- 굿즈 이미지와 비슷한 후보 2안 자동 생성.미리보기/다운로드
- 굿즈 생성 후 (Toss Pay, Kakao Pay, 휴대폰 결제, 무통장 입금) 결제 및 주문 가능


<br>


## 2. 주요 기능

- 객체탐지 및 이미지 생성/편집 기술을 결합하여, 사용자가 사진만 업로드해도 자동으로 재구성된 굿즈 시안을 볼수 있음
- 사용자는 굿즈 후보 3안을 자동 생성.미리보기/다운로드 할수 있음
- 생성된 굿즈를 주문까지 할 수 있는 편의성을 제공하여 굿즈 제작의 진입장벽을 낮춤


<br>


## 3. 기술 스택
<table>
  <tr>
    <th>구분</th>
    <th>사용 기술</th>
  </tr>

  <tr>
    <td><b>FrontEnd</b></td>
    <td>
      <img src="https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB" />
      <img src="https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E" />
      <img src="https://img.shields.io/badge/tailwindcss-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white" />
      <img src="https://img.shields.io/badge/axios.js-854195?style=for-the-badge&logo=axios&logoColor=5A29E4" />
      <img src="https://img.shields.io/badge/zustand-602c3c?style=for-the-badge&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAMAAAAolt3jAAAA8FBMVEVHcExXQzpKQDlFV16lpqyGh4tPPTdWT0weHRU7LRZGQzmxYjlaTkZsbmywVyxtXDSFhISXm6WWpcaytb6bm56gprY0LiiXmp2prLamsMa0XS42MSxkTUVDSkuyYzGihXdDV2GprbmedVxaRD1kTUWUdGFGOCN4a2OfpbI0SFFAMSddTkbCc0dWQiGFRypXQyJUQCBcTTWviDVXQyJcUDjlqCWxjkG+hBTiohtURD6lr8lORTtDVVZmPyxwSipaRSJDOzaWpsyYqMyYqM2dq8tPOjBERTs6QUKTcCeKaCJvViZdSDK4iSngoiDvqx7KkRuGEi1hAAAAOXRSTlMApZ78cB8hCAMQO/j/FOH4KlT1wFfJTjaY6SxtVexFn3Tn2sN6d671mVuJ+/PPN9CT6TfpS4C9jJaVLRihAAAAi0lEQVQIHXXBxRKCUAAF0Es/QMDubsVuGrv1///GBQ4bx3PwgwC8gFCRohs8QrQV0ZtKOZ9JcgBmU8MwqFa9kjNTUWB58f2jPOjU9juTBTbPq+vIar972MZjwPr1uDvqCFw2wQpQVm/t7Oo9gAgAFtrtZNtMFQFp7nkWU5IQECfjYbuQFvBFRJHgjw9L0A80UmaGpAAAAABJRU5ErkJggg==" />
      <img src="https://img.shields.io/badge/React_Router-CA4245?style=for-the-badge&logo=react-router&logoColor=white" />
      <img src="https://img.shields.io/badge/-React%20Query-FF4154?style=for-the-badge&logo=react%20query&logoColor=white" />
    </td>
  </tr>

  <tr>
    <td><b>Backend</b></td>
    <td>
      <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" />
      <img src="https://img.shields.io/badge/Apache Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black" />
      <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" />
      <img src="https://img.shields.io/badge/OAuth2-Authorization-000000" />
      <img src="https://img.shields.io/badge/JWT-Token-000000" />
      <img src="https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black" />
      <img src="https://img.shields.io/badge/JPA%2FHibernate-59666C?logo=hibernate&logoColor=white" />
      <img src="https://img.shields.io/badge/FastAPI-005571?style=for-the-badge&logo=fastapi" />
    </td>
  </tr>

  <tr>
    <td><b>모델</b></td>
    <td>
      <img src="https://img.shields.io/badge/YOLO11-Ultralytics-00FFFF?style=flat&logo=yolo" />
      <span>Nano Banana</span>
    </td>
  </tr>

  <tr>
    <td><b>개발도구</b></td>
    <td>
      <img src="https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=Eclipse&logoColor=white"/>
      <img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"/>
      <img src="https://img.shields.io/badge/Google Colab-F9AB00?style=for-the-badge&logo=googlecolab&logoColor=white"/>
      <img src="https://img.shields.io/badge/pycharm-000000?style=for-the-badge&logo=pycharm&logoColor=white"/>
      <img src="https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?logo=visualstudiocode&logoColor=fff&style=plastic" />
    </td>
  </tr>

  <tr>
    <td><b>협업도구</b></td>
    <td>
      <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"/>
    </td>
  </tr>

  <tr>
    <td><b>데이터베이스</b></td>
    <td>
      <img src="https://img.shields.io/badge/MySQL-4479A1?logo=MySQL&logoColor=white" />
    </td>
  </tr>

  <tr>
    <td><b>스토리지</b></td>
    <td>
      <img src="https://img.shields.io/badge/Amazon_S3-438534?style=flat&logo=amazon-s3" />
    </td>
  </tr>

  <tr>
    <td><b>디자인</b></td>
    <td>
      <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=Figma&logoColor=white"/>
    </td>
  </tr>
</table>


<br>


## 4. 시스템 아키텍쳐
![Image](https://github.com/user-attachments/assets/dd048114-6147-49d4-b444-51a4560a058e)


<br>


## 5. 유스케이스
<img width="788" height="567" alt="Image" src="https://github.com/user-attachments/assets/1d32fcab-8b71-4a2c-b5da-abd74d1ae772" />


<br>


## 6. 서비스 흐름도
<img width="995" height="691" alt="Image" src="https://github.com/user-attachments/assets/33925fd8-48e5-403c-a582-e351e5071524" />


<br>


## 7. ER 다이어그램
<img width="1026" height="592" alt="Image" src="https://github.com/user-attachments/assets/ebb5b51d-d430-4117-af38-8baecdba36aa" />



<br>


## 8. 주요 화면 구성
#### 로그인
<img width="1089" height="729" alt="Image" src="https://github.com/user-attachments/assets/858ad166-19b5-4e20-96d9-ac9f3cecaf5a" />

#### 메인 화면
<img width="1901" height="950" alt="Image" src="https://github.com/user-attachments/assets/ffff9ef2-cdc1-463d-a4bf-8d28248b23c5" />

#### 굿즈 만들기
<img width="1393" height="885" alt="Image" src="https://github.com/user-attachments/assets/e18e7fa9-4bc6-4a2f-b0ae-aa7ebb236464" />

#### 굿즈 시안 선택
<img width="1388" height="944" alt="Image" src="https://github.com/user-attachments/assets/fe23535c-5d56-4ddc-b601-20299a23341a" />

#### 내가 생성한 굿즈
<img width="1555" height="840" alt="Image" src="https://github.com/user-attachments/assets/d7a15813-cb4e-4a80-93d0-0b4c2b5130d7" />

#### 결제
<img width="792" height="738" alt="Image" src="https://github.com/user-attachments/assets/abbbf7a7-5af7-45c1-a41e-905f83430fb6" />

<br>

## 9. 팀원 역할
<table>
  <tr>
    <th>이름</th>
    <th>역할</th>
    <th>담당</th>
  </tr>
  <tr>
    <td>김태현</td>
    <td>PM, AI Modeling</td>
    <td>
      <p>YOLOv11-seg 추론/후처리 튜닝</p>
      <p>카테고리별 규격 프롬프트 가드레일 설계</p>
    </td>
  </tr>
  <tr>
    <td>한정훈</td>
    <td>Full-Stack, Cloud</td>
    <td>
      <p>Front-Back API 구현 및 연동</p>
      <p>Docker Compose 활용 빌드 및 NCP 서버 배포</p>
      <p>Github actions 이용 CI/CD 파이프라인 구축</p>
    </td>
  </tr>
  <tr>
    <td>김태경</td>
    <td>Back-End</td>
    <td>
      <p>굿즈, 주문 관련 페이지</p>
      <p>기능 구현 및 API 연동</p>
    </td>
  </tr>
  <tr>
    <td>이용일</td>
    <td>서비스 기획,문서화</td>
    <td>
      <p>서비스 기획 및 요구사항 정의</p>
      <p>기획 산출물 문서화</p>
    </td>
  </tr>
  <tr>
    <td>김다혜</td>
    <td>
      <p>UI/UX Design</p>
      <p>Publishing</p>
    </td>
    <td>
      <p>Figma 기반 전체 페이지 UI/UX 설계</p>
      <p>Figma 시안 기준</p>
      <p>웹 스타일,CSS 구현</p>
    </td>
  </tr>
</table>

<br>


## 10. 트러블 슈팅
<img width="1070" height="600" alt="Image" src="https://github.com/user-attachments/assets/cc89c8c6-629f-48af-a626-17383ad9fc6c" />


<br>


## 11. 시연 영상
([동영상 링크](https://youtu.be/z2ni7jIXqEU))


<br>
