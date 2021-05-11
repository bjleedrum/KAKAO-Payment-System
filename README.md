# [KAKAO-Payment-System]
결제요청을 받아 카드사와 통신하는 인터페이스를 제공하는 결제 시스템
 - 결제
 - 결제 취소
 - 결제정보 조회

# 1. 개발 프레임워크
 - Windows 10
 - Eclipse (Java)
![image](https://user-images.githubusercontent.com/83941428/117798854-81561500-b28c-11eb-84d6-843803d1917f.png)

 - H2 Embeded Data Base
![image](https://user-images.githubusercontent.com/83941428/117798966-9d59b680-b28c-11eb-821a-a19f950325b5.png)

# 2. 테이블 설계
**[Payment Master]**

결제금액 원장. 결제할때 한줄 생성된다.

이후에 데이터 변경시(취소) 현재 금액들이 업데이트 된다.

최초 금액 필드가 있어 최초와 현재 잔액을 쉽게 비교할 수 있다.
![image](https://user-images.githubusercontent.com/83941428/117798259-eb21ef00-b28b-11eb-860b-83c594d71eb4.png)
 - UNIQUE_ID : 마스터 테이블 유니크 키
 - FIRST_AMOUNT : 최초 결제금액 (불변)
 - FIRST_VAT : 최초 부가가치세 (불변)
 - CURRENT_AMOUNT : 현재 결제 잔액
 - CURRENT_VAT : 현재 부가가치세 잔액
 - CREATER : 최초 생성자
 - CREATE_DT : 최초 생성일시
 - CHANGER : 변경자
 - CHANGE_DT : 변경일시


**[Payment History]**
![image](https://user-images.githubusercontent.com/83941428/117800439-19083300-b28e-11eb-9c0d-d436b442b6c3.png)
 - HISTORY_ID : 히스토리 테이블 유니크 키
 - TYPE : PAYMENT(결제) / CANCEL(취소)
 - UNIQUE_ID : 마스터 테이블 조인 키
 - AMOUNT : 금액
 - VAT : 부가가치세
 - DATA : 카드사에 보내지는 인터페이스 전문
 - CREATER : 최초 생성자
 - CREATE_DT : 최초 생성일시
 - CHANGER : 변경자
 - CHANGE_DT : 변경일시



# 3. 문제해결 전략

# 4. 실행방법

# 5. 맺음말
이렇게 혼자서 DB부터 프레임웍, 소스, 테스트케이스, 빌드, 배포, 설명... 다 해본적이 얼마만인지 ㅎ

삼성생명이 ERP를 도입후 맨날 재미없는 SAP만 하다가 JAVA 코딩을 하니, 과제 제출을 떠나서 요 몇일 정말 즐겁게 코딩했습니다. 오랜만에 밤도 한번 새봤고요~ 옛날 대학교 시절이 생각나는 한주 였습니다.

저는 개발도 좋아하지만 많은 경험을 통한 Business area 에도 강점이 있습니다.

우리 나라에 많은 보험사들이 있지만 다들 거대하고 복잡해서 요즘 같은 시대에 기민하게 움직이기 힘든 구조 입니다.

카카오에서 보험을 한다니 기대가 크네요, 얼마전 카카오페이 깔고 보험서비스 한번 둘러봤는데 직관적이고 좋아 보였습니다.

지금 보험은 너무 어렵고 복잡합니다. 저도 보험을 오래 다뤄봤지만 현재의 시스템과 프로세스는 아쉬운 점이 많습니다.

보험이 복잡하고 어렵고 항상 손해보는 느낌이 나게 마련인데 카카오에서 지금까지 과감한 도전을 한것처럼 보험에서도 대 변혁을 일으켜 주기를 바라고 있습니다.

정말로 심플하고 그래피컬한 화면, 복잡하고 어려운 용어는 모두 휴지통에 넣어버리고, 가입/심사/청구도 카카오 플랫폼을 활용해서 손가락 몇번 누르면 끝나게하고 정기적으로 예쁘고 귀여운 꼭 필요한 내용만 알기쉽게 보내주면 1등 안할래야 안할수가 없을거 같습니다. 와~ 상상만 해도 너무 즐겁고 행복하네요.

지금 대형 보험사들은 변화하기 어렵습니다. 카카오만이 게임 체인저가 될 수 있다고 생각합니다. 그리고 그 변화에 저도 도움이 됐으면 좋겠습니다.


감사합니다~
