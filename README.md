# 요약
해당 레포지토리는 *** 백오피스용 모듈이며, Admin과 Seller의 Settlement(정산) 기능을 구현합니다.

# 디렉터리 구조
```
millie/
├── HELP.md
├── README.md
├── build/
├── build.gradle
├── gradle/wrapper/
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── libs/
├── settings.gradle
├── src/
│   ├── main/
│   │   ├── java/com/millie/
│   │   │   ├── MillieApplication.java
│   │   │   ├── admin/
│   │   │   │   ├── member/
│   │   │   │   ├── order/
│   │   │   │   ├── product/
│   │   │   │   └── settlement/
│   │   │   │       ├── SettlementController.java
│   │   │   │       └── SettlementService.java
│   │   │   ├── common/
│   │   │   │   ├── configuration/ControllerExceptionAdvice.java
│   │   │   │   ├── domain/entity/
│   │   │   │   ├── exception/
│   │   │   │   │   ├── BaseErrorCode.java
│   │   │   │   │   ├── BaseException.java
│   │   │   │   │   ├── ErrorCode.java
│   │   │   │   │   └── InvalidSettlementRequestException.java
│   │   │   │   ├── interceptor/
│   │   │   │   ├── model/ResponseModel.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── AdminRepository.java
│   │   │   │   │   ├── OrderRepository.java
│   │   │   │   │   ├── SellerRepository.java
│   │   │   │   │   ├── SettlementRepository.java
│   │   │   │   │   └── SettlementRequestRepository.java
│   │   │   │   └── type/SettlementRequestStatusType.java
│   │   │   └── seller/
│   │   │       ├── dto/SettlementRequestDto.java
│   │   │       ├── product/
│   │   │       └── settlement/
│   │   │           ├── SettlementController.java
│   │   │           └── SettlementService.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── initailize.sql (초기 DDL)
│   └── test/java/com/millie/MillieApplicationTests.java
└── tools/docker-compose.yaml (로컬 MySQL 실행용 Docker Compose 파일)
```

* 비지니스 로직 상 ADMIN과 판매자는 다른 객체이고, 서버가 분리되면 가장 편리하지만 해당 프로젝트의 규모가 작아 단일 모듈로 구성
* 단, 모듈과 서버를 분리하기 쉽도록 디렉터리 구조에서 `admin`과 `seller`로 구분
* core에 해당하는 Entity와 Repository, exception, interceptor 등은 `common` 디렉터리로 분리하여 공통으로 사용
* 모든 코드에서 두번이상 중복되는 코드는 메서드와 Optional로 이용하여 (예: 주문서 등의 객체 조회) 중복을 최소화
* 비지니스 로직은 Entity안에 둠으로써 Seller-Admin 로직 이원화 이슈 최소화

* 도메인 간 통신이 적어 정산 Service가 주문 Repository를 직접 참조하는 구조지만, 도메인 간 통신이 잦아지는 경우 Service 하위에 `Component`와 같은 레이어를 두어
타 도메인이 호출할 수 있도록 변경해야함
* enum 작성 시 반드시 postFix로 `Type`을 붙여야함. 예: `SettlementRequestStatusType`


# 통신방식
* REST API

성공 응답
```
{
    "success": true,
    "response": {
        "data": {
            "key": "value"
        },
        "message": "요청 성공 메시지"
    }
}
```

실패 응답
```
{
    "success": true,
    "error": {
        "code": "NOT_FOUND_SETTLEMENT_REQUEST",
        "message": "요청 실패 메시지"
    }
}
```

# 스펙 
* Spring Boot 3.2.5
* Java 17
* MySQL 8.0.34

