# summer
simple blog project


### JPQL로 작성된 쿼리를 queryDSL로 교체(2023.04.22)

#### JPQL의 문제점
- JPQL은 문자열(=String) 형태이기 때문에 개발자 의존적 형태
- Compile 단계에서 Type-Check가 불가능
- RunTime 단계에서 오류 발견 가능 (장애 risk 상승)

다음과 같은 JPQL의 문제를 해결하기 위해 queryDSL로 교체했습니다.

#### queryDSL 특징
- 문자가 아닌 코드로 작성
- Compile 단계에서 문법 오류를 확인 가능
- 코드 자동 완성 기능 활용 가능
- 동적 쿼리 구현 가능
