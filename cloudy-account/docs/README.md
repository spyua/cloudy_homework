# Cloudy Account Service

## 1️⃣ 🔰 專案概要

| 欄位 | 說明 |
|------|------|
| **專案名稱** | Cloudy Account Service |
| **描述** | 基於 JWT 的使用者帳戶認證服務，整合 Google Cloud Platform 安全服務 |
| **主要程式語言** | Java |
| **核心功能** | RESTful API、JWT 認證、使用者註冊與登入、Google Cloud KMS 加密 |
| **執行環境** | JVM 17 |
| **框架** | Spring Boot 3.1.0 |
| **資料庫** | PostgreSQL (Google Cloud SQL) |
| **授權條款** | @TODO: 未指定授權條款 |
| **API 規格** | RESTful API (待生成 OpenAPI 3.1 規格) |

## 2️⃣ 📁 目錄與檔案慣例

| 目錄／檔案 | 預期用途 |
|------------|----------|
| `src/` | 主要程式碼及資源文件 |
| `src/main/java/` | Java 原始碼根目錄 |
| `src/main/java/com/ck/account/` | 主要應用程式套件 |
| `src/main/java/com/ck/account/controller/` | REST API 控制器 |
| `src/main/java/com/ck/account/controller/bean/` | API 請求/回應 DTO 物件 |
| `src/main/resources/` | 資源文件（組態檔等） |
| `src/main/resources/application.properties` | 主要應用程式組態檔 |
| `docs/` | 技術文件目錄 |
| `pom.xml` | Maven 建置設定檔 |
| `dockerfile` | Docker 容器建置檔 |
| `mvnw`, `mvnw.cmd` | Maven Wrapper 執行檔 |

## 3️⃣ 專案介紹與定位

Cloudy Account Service 是一個基於 Spring Boot 的微服務，專門處理使用者帳戶的註冊與認證功能。服務採用 JWT（JSON Web Token）作為認證機制，並整合 Google Cloud Platform 的安全服務，包括 Cloud KMS 進行密碼加密和 Secret Manager 管理敏感資訊。

### 技術堆疊
- **後端框架**: Spring Boot 3.1.0, Spring Security, Spring Data JPA
- **認證機制**: JWT (JSON Web Token)
- **資料庫**: PostgreSQL (Google Cloud SQL)
- **雲端服務**: Google Cloud KMS, Google Cloud Secret Manager
- **建置工具**: Maven
- **容器化**: Docker
- **程式語言**: Java 17

## 4️⃣ 安裝／建置／執行指南

### 前置需求
- **JDK**: OpenJDK 17 或以上版本
- **Maven**: 3.6+ (或使用內建的 Maven Wrapper)
- **Docker**: 最新版本（用於容器化執行）
- **Google Cloud 帳戶**: 需要設定 KMS 和 Secret Manager 服務

### CLI 執行方式

#### 開發模式執行（含熱重載）
```bash
# 使用 Maven Wrapper
./mvnw spring-boot:run

# 或使用系統 Maven
mvn spring-boot:run
```

#### 標準打包與執行
```bash
# 編譯並打包
./mvnw clean package

# 執行 JAR 檔案
java -jar target/cloudy-account-0.0.1-SNAPSHOT.jar
```

#### über-jar 打包與執行
```bash
# Spring Boot 預設已產生 über-jar
./mvnw clean package
java -jar target/cloudy-account-0.0.1-SNAPSHOT.jar
```

### Docker 執行方式

#### 建構映像檔
```bash
docker build -t cloudy-account:latest .
```

#### 執行容器
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e GOOGLE_APPLICATION_CREDENTIALS=/path/to/service-account.json \
  cloudy-account:latest
```

## 5️⃣ 組態說明與設定參數對照表

組態檔位置：`src/main/resources/application.properties`

| Key | 型別 | 預設值 | 說明 | 必填 |
|-----|------|--------|------|------|
| `spring.cloud.gcp.project-id` | string | my-project-1507703752854 | Google Cloud 專案 ID | Y |
| `spring.cloud.gcp.secretmanager.project-id` | string | my-project-1507703752854 | Secret Manager 專案 ID | Y |
| `spring.config.import` | string | sm:// | 啟用 Secret Manager 組態匯入 | Y |
| `cloud.gcp.kms.id` | string | login/accountLogin3 | Cloud KMS 金鑰 ID | Y |
| `spring.cloud.gcp.sql.database-name` | string | cloudy_homework | 資料庫名稱 | Y |
| `spring.cloud.gcp.sql.instance-connection-name` | string | - | Cloud SQL 實例連線名稱 | Y |
| `spring.datasource.username` | string | ${sm://security_db_acc} | 資料庫使用者名稱（從 Secret Manager 取得） | Y |
| `spring.datasource.password` | string | ${sm://security_db_pass} | 資料庫密碼（從 Secret Manager 取得） | Y |
| `spring.jpa.hibernate.ddl-auto` | string | none | Hibernate DDL 模式 | N |

## 6️⃣ API 端點清單

| 路徑 | HTTP 方法 | 參數（型別/必填） | 回應格式 | 描述 |
|------|-----------|-------------------|----------|------|
| `/login` | POST | body: UserInfoDto (Y) | JSON: UserInfoRelayDto | 使用者登入認證 |
| `/register` | POST | body: UserInfoDto (Y) | JSON: AccountInfoPo | 新使用者註冊 |

### Request/Response 物件結構

#### UserInfoDto (登入/註冊請求)
```json
{
  "userAccountID": "string",
  "userPassword": "string"
}
```

#### UserInfoRelayDto (登入回應)
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNjk..."
}
```

#### AccountInfoPo (註冊回應)
```json
{
  "userAccound": "string",
  "userPassword": "string",
  "createTime": "2023-07-01T10:00:00.000Z",
  "lastLogin": "2023-07-01T10:00:00.000Z"
}
```

## 7️⃣ 典型使用流程與 API 呼叫範例

### 使用者註冊流程
```bash
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{
    "userAccountID": "user123",
    "userPassword": "securePassword123"
  }'
```

**預期回應**:
```json
{
  "userAccound": "user123",
  "userPassword": "$2a$10$encrypted_password_hash",
  "createTime": "2023-07-01T10:00:00.000Z",
  "lastLogin": null
}
```

### 使用者登入流程
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "userAccountID": "user123",
    "userPassword": "securePassword123"
  }'
```

**預期回應**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNjkwODc2ODAwLCJleHAiOjE2OTA4OTQ4MDB9.signature"
}
```

### 後續 API 呼叫（使用 JWT Token）
```bash
curl -X GET http://localhost:8080/protected-endpoint \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## 8️⃣ 核心類別與元件說明

### Controller 層
- **`UserVerifyController`**: 提供使用者認證相關的 REST API 端點，處理登入和註冊請求

### DTO 層
- **`UserInfoDto`**: 登入/註冊請求的資料傳輸物件，包含帳號和密碼
- **`UserInfoRelayDto`**: 登入成功後的回應物件，包含 JWT Token

### 應用程式層
- **`CloudyAccountApplication`**: Spring Boot 主要啟動類別，設定元件掃描範圍

### 依賴的 Security 元件 (cloudy-security)
- **`JwtTokenUtil`**: JWT Token 生成、驗證和解析工具
- **`JwtUserDetailsServiceImpl`**: 使用者詳細資訊服務實作
- **`CloudKMSPasswordEncoder`**: 基於 Google Cloud KMS 的密碼編碼器
- **`AccountInfoPo`**: 使用者帳戶資訊實體類別

## 9️⃣ 系統架構

### 架構圖
```
Client Application
       ↓
[ REST API Layer ]
       ↓
[ Authentication Layer (JWT) ]
       ↓
[ Security Service Layer ]
       ↓
[ Data Access Layer (JPA) ]
       ↓
[ PostgreSQL Database (Cloud SQL) ]
       ↓
[ Google Cloud Services ]
  ├─ Cloud KMS (密碼加密)
  └─ Secret Manager (敏感資訊管理)
```

### 設計考量
- **微服務架構**: 專注於帳戶認證功能的單一職責
- **雲端原生**: 深度整合 Google Cloud Platform 服務
- **安全性**: 使用 Cloud KMS 進行密碼加密，Secret Manager 管理敏感設定
- **無狀態設計**: 採用 JWT Token 實現無狀態認證機制

## 🔟 常見問題（FAQ）與除錯指引

### Q1: 服務無法啟動，出現資料庫連線錯誤
**解決方案**:
1. 確認 Google Cloud SQL 實例正在運行
2. 檢查 `application.properties` 中的資料庫連線設定
3. 驗證 Secret Manager 中的資料庫憑證是否正確
```bash
# 檢查資料庫連線
gcloud sql instances describe cloudy-homework --project=my-project-1507703752854
```

### Q2: JWT Token 驗證失敗
**解決方案**:
1. 確認 Secret Manager 中的 `JWT_Secret` 設定
2. 檢查 Token 是否已過期（預設 5 小時）
3. 驗證 Token 格式是否為 `Bearer <token>`

### Q3: 密碼加密/解密失敗
**解決方案**:
1. 確認 Cloud KMS 金鑰權限設定
2. 檢查服務帳戶是否有存取 KMS 的權限
3. 驗證 `cloud.gcp.kms.id` 設定值

### 服務監控
- **健康檢查**: @TODO: 需要新增 `/actuator/health` 端點
- **應用程式日誌**: 使用 `docker logs <container-id>` 或 `kubectl logs <pod-name>`

## 1️⃣1️⃣ 未來改進建議

基於目前專案狀態，建議的改進方向：

1. **API 擴展**:
   - 新增使用者資訊查詢 API (`GET /user/{id}`)
   - 實作密碼重設功能
   - 新增 Token 刷新機制

2. **安全性增強**:
   - 實作 API 存取頻率限制
   - 新增多因素認證 (MFA)
   - 實作帳戶鎖定機制

3. **監控與觀測**:
   - 整合 Spring Boot Actuator
   - 新增詳細的應用程式日誌
   - 實作效能監控指標

4. **測試覆蓋率**:
   - 新增單元測試
   - 實作整合測試
   - API 端點測試自動化

5. **文件完善**:
   - 新增 Swagger UI 介面
   - 完善 API 文件註解
   - 提供使用範例和最佳實務

6. **部署優化**:
   - 實作 Kubernetes 部署設定
   - 新增環境別組態管理
   - 設定自動化 CI/CD 流程