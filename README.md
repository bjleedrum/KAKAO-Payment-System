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

최대한 간단히 꼭 필요한 컬럼만으로 구성 했습니다.


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

결제, 취소 이력 테이블. 이벤트가 있을때마다 한줄씩 생성되며 그때마다 카드사로 전문이 송신된다.

마스터 테이블의 유니크 키로 조회하면 전체 이력을 볼수 있다
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
- 과제를 처음 보고 살짝 당황했습니다. 사전 정보가 전혀 없었기에 그냥 알고리즘 같은 문제 나오지 않을까 예상했었는데 이렇게 소형 시스템을 개발하라고 할줄은...ㅎㅎ

생각해보니 알고리즘은 카피가 가능하니 낼수가 없었겠군요.

그동안 대형 프로젝트 위주로 일을 해와서 이렇게 전체 시스템을 구축하는것에 대한 걱정이 많았습니다.

알다시피 이쪽 일은 각자 역할이 정해져 있어서 자기가 맡은 영역만 처리하면 되니까요. 이렇게 E2E로 개발해본건 정말 오랜만입니다.

하지만 제가 우려했던것 보다는 훨씬 작업이 수월하게 진행 되었습니다. Thanks God~

- 프레임워크와 개발 언어는 고민할것도 없이 익숙한 이클립스와 자바로 선정 했습니다.

그동안 ProFrame(Tmax Soft), AnyFrame(Samsung) 을 많이 사용 했습니다.

DB는 유명한 H2로 했습니다. H2도 이번에 처음 써봤습니다
- 개발환경 세팅이 생각보다 빠른 시간에 끝난게 신의 한수 였습니다.

옛날 학생때도 이런거 세팅에 좀 약한면이 있었는데, 이클립스 설치 후에 DB2 설치와 셋업에 애를 먹을거로 예상했는데 생각보다 훨씬 수월하게 설치와 셋업이 끝나서 개발 시간을 충분히 확보할 수 있었습니다.
- ㄴㄷㅎㄷㄱㅎㄷㄱ
- ㄷㅎㄷㅎㄷㅎㄱㄷㅎ
- ㄷㅎㄷㄱㅎ
- ㄷㅎㄷㅎ
- ㄷㅎㄷㅎㄱㄷ
- ㄷㅎㄷㄱㅎㄷㄱㅎㄱ
- ㄷㅎㄷㄱㅎㄷ
- ㄷㅎㄷㅎㄱㄷㅎ
- 


 
4. 가장 중요한건 DB 모델링 이었습니다. 모델링을 어떻게 하냐에 따라서 코딩 스타일이 달라지니 신경을 안쓸수가 없었는데, 처음에 생각한 모델이 좀 별로였습니다. 어쩐지 개발하면서 뭔가 계속 꼬이는 느낌이...ㅎㅎ;

이대로는 안되겠다 싶어서 중간에 DB 한번 뒤엎고 다시 설계해서 모델링이 최적화 되었더니 그 다음부터는 일사천리로 개발이 진행 됐습니다.

5. 간단한 CRUD 시스템이라 최대한 간결하고 깔끔하게 짜려고 노력했습니다. 너무 세분화는 안하고 모듈화는 시간 관계상 적당한 선에서 타협 했습니다.

 - Main : 메인 클래스. 입력값의 명령에 따라 각 수행 모듈로 분기. 테스트를 위해서 인풋 테스트 파일위치 지정
 - Payment : 핵심 기능 모듈. 결제/취소/조회를 담당
 - Util : 이것저것 필요한 기능들 모아놓은 유틸 클래스, 에러코드/메세지 처리
 - Encryption : 암호화/복호화 담당
 - 

6. 에러코드, 메세지는 소스안에서 전역변수로 해결했습니다. 코드, 메세지 몇개 안되니까요

7. 너무 Low Lelel 의 Validation 은 고려하지 않았습니다. ex) 숫자필드에 문자가 들어온다던가, 특수문자가 있다던가 하는..

밸리데이션은 클라이언트 단에서 통과되어 온다고 가정했습니다.

요구사항에 명시되어 있는것들은 체크 로직에 넣었습니다.

8. 



# 4. 실행방법
DB가 제 로컬에만 설치되어 있어 다른사람들도 실행할 수 있는 방법이 있는지는 잘 모르겠습니다. 제가 이런쪽에는 좀 약해서..

이클립스 프로젝트는 아카이브로 압축한것 첨부했고요,

혹시 필요할까봐 이클립스에서 사용한 외부 Json, H2 의 jar 파일도 넣어놨습니다.

실행이 가능하다면 이클립스에 있는 test 파일에 테스트 케이스 있으니 main.java 실행하면 됩니다.

소스도 개별 파일로 첨부 했습니다.



# 5. 맺음말
이렇게 혼자서 DB부터 프레임웍, 소스, 테스트케이스, 빌드, 배포, 설명... 다 해본적이 얼마만인지.. ㅎ

삼성생명이 ERP를 도입후 맨날 재미없는 SAP만 하다가 JAVA 코딩을 하니, 과제 제출을 떠나서 요 몇일 정말 즐겁게 코딩했습니다. 오랜만에 밤도 한번 새봤고요~ 옛날 대학교 시절이 생각나는 한주 였습니다.

저는 개발도 좋아하지만 많은 경험을 통한 Business area 에도 강점이 있습니다.

우리 나라에 많은 보험사들이 있지만 다들 거대하고 복잡해서 요즘 같은 시대에 기민하게 움직이기 힘든 구조 입니다.

카카오에서 보험을 한다니 기대가 크네요, 얼마전 카카오페이 깔고 보험서비스 한번 둘러봤는데 직관적이고 좋아 보였습니다.

지금 보험은 너무 어렵고 복잡합니다. 저도 보험을 오래 다뤄봤지만 현재의 시스템과 프로세스는 아쉬운 점이 많습니다.

보험이 복잡하고 어렵고 항상 손해보는 느낌이 나게 마련인데 카카오에서 지금까지 과감한 도전을 한것처럼 보험에서도 대 변혁을 일으켜 주기를 바라고 있습니다.

정말로 심플하고 그래피컬한 화면, 복잡하고 어려운 용어는 모두 휴지통에 넣어버리고, 가입/심사/청구도 카카오 플랫폼을 활용해서 손가락 몇번 누르면 끝나게하고 정기적으로 예쁘고 귀여운 꼭 필요한 내용만 알기쉽게 보내주면 1등 안할래야 안할수가 없을거 같습니다. 와~ 상상만 해도 너무 즐겁고 행복하네요.

대형 보험사들은 변화하기 어렵습니다. 카카오만이 게임 체인저가 될 수 있다고 생각합니다.

카카오에서 모두가 깜짝 놀랄만한 대 변화를 기대하며, 그리고 그 변화에 저도 도움이 됐으면 좋겠습니다.


감사합니다~
