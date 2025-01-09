## ✨ 프로젝트 소개
- Goorm X Piggy 프로젝트는 백엔드 개발자들 간에 협업을 통하여 실제 시간에서 사용되는 소프트웨어를 클론 코딩하여 디버깅과 최적화를 수행하는 것을 목표로 하고 있습니다!
- Base Project: [PiggyMetrics](https://github.com/sqshq/piggymetrics)

## 📆 개발 기간
- 24.12.12(목) ~ 25.01.10(금)

## ✨ 주요 목표
- Maven 기반의 프로젝트를 Gradle로 변경하여 가독성있는 프로젝트로 구성합니다.
- MSA 환경을 이해하고 서비스 간의 게이트웨이를 통하여 라우팅 될 수 있도록 합니다.
- 각 서비스마다 발생하는 오류를 해결하고, 의존성을 최소화 및 어노테이션을 이용하여 코드 설계 원칙에 맞는 코드를 작성할 수 있도록 합니다.
- 효율적인 브랜치 전략과 PR을 통해 팀원 간의 협력을 강화합니다.
- 모니터링 도구를 이용하여 시스템의 흐름과 에러를 식별합니다.

## ✨ 멤버 구성
| ![박정곤](https://github.com/user-attachments/assets/74b1e113-fbae-4fa5-a5c4-e33b67523153) | ![image](https://github.com/user-attachments/assets/fc76c1ad-6922-4bba-8c78-87e8e19da61a) | ![김상호](https://github.com/user-attachments/assets/421ac141-e472-4e7a-8283-3bfdd2f440e3)| ![이유영](https://github.com/user-attachments/assets/054cc92f-08ad-44a8-936f-df3662d5d2f8)  | ![image](https://github.com/user-attachments/assets/de4153be-e89e-4de2-9b84-90e5a412a2fe)  | ![image](https://github.com/user-attachments/assets/75ecc75d-31b9-404d-9267-88720af05531) |
|:---:|:---:|:---:|:---:|:---:|:---:|
| 박정곤  |  서동준  | 김상호   | 이유영  | 박진홍 | 이지은 |
| PM, Backend |  Backend  |  Backend | Backend  | Backend  | Backend  |
| https://github.com/wjd4204 | https://github.com/SD-gif | https://github.com/ksah3756 | https://github.com/YuyoungRhee | https://github.com/JiinHong  | https://github.com/leeje0506 | 


## 📃 API 명세서

### **AccountService**
| **기능**        | **URL**                       | **HTTP 메서드** | **설명**                                |
|------------------|-------------------------------|-----------------|-----------------------------------------|
| 계정 조회        | `/api/v1/account/{name}`      | `GET`          | 이름으로 계정을 조회하는 API             |
| 계정 정보 조회   | `/api/v1/account/current`     | `GET`          | 현재 사용자의 계정 정보를 조회           |
| 계정 정보 저장   | `/api/v1/account/current`     | `PUT`          | 현재 사용자의 계정 정보를 저장           |
| 계정 생성        | `/api/v1/account`             | `POST`         | 새로운 계정을 생성                       |

### **StatisticsService**
| **기능**        | **URL**                             | **HTTP 메서드** | **설명**                                |
|------------------|-------------------------------------|-----------------|-----------------------------------------|
| 통계 조회        | `/api/v1/statistics/current`        | `GET`          | 현재 계정의 통계를 조회                  |
| 특정 계정 통계 조회 | `/api/v1/statistics/{accountName}` | `GET`          | 계정의 이름을 이용하여 통계를 조회       |
| 특정 계정 통계 저장 | `/api/v1/statistics/{account}`    | `PUT`          | 특정 계정의 통계 정보를 저장             |

### **NotificationService**
| **기능**        | **URL**                                  | **HTTP 메서드** | **설명**                                |
|------------------|------------------------------------------|-----------------|-----------------------------------------|
| 알림 설정 조회   | `/notifications/recipients/current`      | `GET`          | 현재 사용자의 알림 설정 조회             |
| 알림 유형 저장   | `/notifications/recipients/current/{type}` | `PUT`          | 사용자의 특정 알림 유형 설정 저장        |

## 개발 환경
Web.

![js](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)&nbsp; ![CSS](https://img.shields.io/badge/CSS-663399?style=for-the-badge&logo=CSS&logoColor=white)&nbsp;

Server.

![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)&nbsp; ![SpringSecurity](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white)&nbsp; ![mongo](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white)&nbsp; ![Amazon](https://img.shields.io/badge/Amazon-FF9900?style=for-the-badge&logo=Amazon&logoColor=white)&nbsp;

Monitoring

![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=Prometheus&logoColor=white)&nbsp; ![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=Grafana&logoColor=white)&nbsp;

Etc.

![Intellij](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white)&nbsp; ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)&nbsp; ![Github](https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white)&nbsp;
