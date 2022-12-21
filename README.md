## kakaopay-membership

개발 환경
---
분야| stack |
--|--|
 |언어 | Java 8 |
 |프레임워크 | springBoot 2.3.4|
 |DB | mysql|
 |빌드 툴 | Maven
 |Persistence 프레임워크 | JPA/Hibernate |
 |API 테스트 툴 | Postman |
 | IDE | IntelliJ

DB 설계
---
<img width="631" alt="image" src="https://user-images.githubusercontent.com/49789734/205942296-22ff3e52-2e27-4592-9839-ce0c076f0a7e.png">

#### 테이블 목록

|테이블 명|  이름  | 
| ------ | ------ | 
| tb_barcode   | 바코드 테이블 | 
| tb_point  | 포인트 테이블 | 
| tb_store   | 상점 테이블 | 
| tb_history   | 내역 테이블 | 
| tb_serial   | 바코드 채번 테이블 | 


API 명세
---

### 실행 API 주소

```

localhost:8080/api/v1

```

### 통합 바코드 발급 API
---


| 메소드 | 경로   | 짧은 설명 |
| ------ | ------ | --------- |
| GET   | /membership/barcode | 통합 바코드 발급 API    |

### 요청 헤더

```

Content-Type: application/json,
userId: 사용자 ID

```

### 응답 바디

#### 성공

##### 1. userId와 대응되는 바코드가 있을 때
```json
{
    "status": 200,
    "message": "바코드 생성 성공",
    "data": "32a1242901"
}
```

##### 2. userId와 대응되는 바코드가 없을 때
```json

{
    "status": 200,
    "message": "바코드 발급 성공",
    "data": "32a1242901"
}
```
#### 실패

##### userId는 별도의 Auth 관련 서비스에서 인증을 했다고 가정



### 포인트 적립 API
---


| 메소드 | 경로   | 짧은 설명 |
| ------ | ------ | --------- |
| POST   | /membership/point | 포인트 적립 API    |

### 요청 헤더

```

Content-Type: application/json,

```

### 응답 바디

```json
{
  "storeId": 10001,
  "point": 1000,
  "barcode": "23dded01ed"
}
```

#### 성공

```json
{
    "status": 200,
    "message": "포인트 적립 성공",
    "data": null
}
```

#### 실패

##### 1. 등록되지 않은 바코드
```json

{
    "status": 200,
    "message": "등록되지 않는 바코드",
    "data": null
}
```

##### 2. 등록되지 않은 상점일때
```json

{
    "status": 200,
    "message": "등록되지 않은 상점",
    "data": null
}
```

### 포인트 사용 API
---


| 메소드 | 경로   | 짧은 설명 |
| ------ | ------ | --------- |
| PUT   | /membership/point | 포인트 사용 API    |

### 요청 헤더

```

Content-Type: application/json,

```

### 응답 바디

```json
{
  "storeId": 10001,
  "point": 1000,
  "barcode": "23dded01ed"
}
```

#### 성공

```json
{
    "status": 200,
    "message": "포인트 사용 성공",
    "data": null
}
```

#### 실패

##### 1. 등록되지 않은 바코드
```json

{
    "status": 200,
    "message": "등록되지 않는 바코드",
    "data": null
}
```

##### 2. 등록되지 않은 상점
```json

{
    "status": 200,
    "message": "등록되지 않은 상점",
    "data": null
}
```


##### 3. 포인트 부족
```json

{
    "status": 200,
    "message": "포인트 부족",
    "data": null
}
```


### 내역 조회 API
---


| 메소드 | 경로   | 짧은 설명 |
| ------ | ------ | --------- |
| GET   | /membership/barcode/history | 통합 바코드 발급 API    |

### 요청 헤더

```

Content-Type: application/json,

```

### 요청 파라미터

| 변수 | 타입   | 짧은 설명 |
| ------ | ------ | --------- |
| barcode   | String | 바코드    |
| startDate  | String | 조회 시작 조건    |
| endDate  | String | 조회 끝     |

### 응답 바디

#### 성공

```json
{
    "status": 200,
    "message": "내역 조회 성공",
    "data": [
        {
            "category": "A",
            "partnerName": "식품1",
            "type": "earn",
            "approvedAt": "2022-12-06 22:18:34"
        },
        {
            "category": "A",
            "partnerName": "식품1",
            "type": "use",
            "approvedAt": "2022-12-06 22:20:01"
        },
        {
            "category": "A",
            "partnerName": "식품1",
            "type": "earn",
            "approvedAt": "2022-12-06 22:20:02"
        }
    ]
}
```

#### 실패


##### 1. 등록되지 않은 바코드
```json

{
    "status": 200,
    "message": "등록되지 않는 바코드",
    "data": null
}
```

#### 응답 공통

##### DB 오류

```json
{
    "status": 600,
    "success": false,
    "message": "데이터베이스 "
}
```

##### INTERNAL_SERVER_ERROR

```json
{
    "status": 500,
    "success": false,
    "message": "서버 내부 에러"
}
```

핵심 문제 해결 전략
---
### 통합 바코드 발급
 1. userId의 유효성은 외부의 테이블 및 Auth 관련 서비스가 있다고 가정
  - 테스트 코드에서는 신규 채번일때 userId 난수로 발생
 2. 바코드 고유 채번 프로시저(sp_get_serial)사용
  - UUID(버전 1) 네트워크 랜 카드와 시간을 기반으로 유니크한 ID를 생성
  - MD5() 암호 추가 후 10자리 채번
  - tb_serial 중복 확인 후 Insert 
 3. 탈퇴,변경이 없으므로 요구한 데이터 외 생성시간만 추가

### 포인트 적립, 사용
1. 기본적으로 @Transaction 어노테이션 사용, (격리 레벨 2로 올릴지 고민 했지만, 우선 DBMS Deafualt를 따름)
2. 적립 시 해당 상점의 업종이 없을 경우, 해당 업종으로 포인트 추가
3. 개인별 포인트, 상점은 공통 컬럼임 업종별로 상점, 포인트 테이블로 분리
4. 하나의 바코드의 여러 업종을 가질 수 있어서 포인트 테이블 (바코드,업종) 복합키 설정
5. 상점, 바코드 ,포인트 부족 등 예외 처리 추가

### 내역 조회
1. 내역 테이블을 계속 쌓이는 로그 테이블로 생각해서, 별도의 FK를 걸지 않음

### 테스트 코드 작성
1. 테스트 별로 @Nested로 묶어서 진행
 - 각 API 별 성공, 실패 케이스 반환

