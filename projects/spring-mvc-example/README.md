# Spring MVC Example - 用戶管理系統

這是一個基於 Spring MVC Template 的範例專案，展示如何使用模板創建一個完整的用戶管理系統。

## 🎯 專案目標

展示如何使用 Spring MVC Template 快速建立一個具有以下功能的 RESTful API：

- ✅ 用戶 CRUD 操作
- ✅ 高級搜索和過濾
- ✅ 分頁支持
- ✅ 資料驗證
- ✅ 統一異常處理
- ✅ API 文檔
- ✅ 緩存實現
- ✅ 完整測試覆蓋

## 🚀 快速開始

### 前置需求
- Java 21 或以上版本
- Maven 3.6 或以上版本
- (可選) Redis 服務器，用於快取功能

### 1. 克隆並建置專案

```bash
# 克隆專案
git clone <repository-url>
cd cloudy_homework/projects/spring-mvc-example

# 編譯專案
mvn clean compile

# 執行測試
mvn test
```

### 2. 啟動應用

```bash
# 方式一：使用 Maven 啟動
mvn spring-boot:run

# 方式二：打包後啟動
mvn clean package
java -jar target/spring-mvc-example-1.0.0-SNAPSHOT.jar
```

### 3. 驗證啟動成功

訪問以下 URL 確認服務正常運行：

- **API 健康檢查**: http://localhost:8080/api/actuator/health
- **API 文檔**: http://localhost:8080/api/swagger-ui.html  
- **H2 資料庫控制台**: http://localhost:8080/api/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - 用戶名: `sa`
  - 密碼: (留空)

## 📖 教學指南

### 步驟 1：理解專案結構

```
src/main/java/com/example/
├── controller/          # REST API 控制器
│   └── UserController.java
├── service/            # 業務邏輯層
│   └── UserService.java
├── repository/         # 資料存取層
│   └── UserRepository.java
├── entity/             # JPA 實體
│   ├── BaseEntity.java
│   └── User.java
├── dto/                # 資料傳輸物件
│   ├── RestResponse.java
│   ├── UserDto.java
│   ├── CreateUserRequest.java
│   └── UpdateUserRequest.java
├── mapper/             # MapStruct 映射器
│   └── UserMapper.java
├── config/             # 組態設定
├── exception/          # 異常處理
└── aspect/             # AOP 切面
```

### 步驟 2：第一個 API 呼叫

#### 創建用戶
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "dateOfBirth": "1990-05-15"
  }'
```

**預期回應**:
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

#### 查詢用戶
```bash
# 根據 ID 查詢
curl http://localhost:8080/api/users/1

# 分頁查詢所有用戶
curl "http://localhost:8080/api/users?page=0&size=5&sort=createdAt,desc"

# 搜索用戶
curl "http://localhost:8080/api/users/search?q=john"
```

### 步驟 3：探索進階功能

#### 1. 快取機制測試
```bash
# 第一次查詢（會從資料庫讀取）
time curl http://localhost:8080/api/users/1

# 第二次查詢（會從快取讀取，速度更快）
time curl http://localhost:8080/api/users/1
```

#### 2. 資料驗證測試
```bash
# 測試無效的 email 格式
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "email": "invalid-email",
    "firstName": "Test"
  }'
```

**預期回應**（驗證失敗）:
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": "Please provide a valid email address"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

#### 3. 異常處理測試
```bash
# 查詢不存在的用戶
curl http://localhost:8080/api/users/999
```

**預期回應**（資源未找到）:
```json
{
  "success": false,
  "message": "User not found with id: 999",
  "timestamp": "2024-01-01T10:00:00"
}
```

### 步驟 4：使用 Swagger UI 互動式測試

1. 打開瀏覽器訪問: http://localhost:8080/api/swagger-ui.html
2. 展開 `user-controller` 區塊
3. 點擊任意 API 端點，例如 `POST /users`
4. 點擊 "Try it out" 按鈕
5. 填入測試資料並點擊 "Execute"
6. 查看回應結果

### 步驟 5：監控和日誌

#### 查看應用程式日誌
```bash
# 查看日誌中的 AOP 切面資訊
tail -f logs/application.log | grep -E "(AUDIT|PERFORMANCE)"

# 或直接在控制台觀察 AOP 日誌輸出
```

#### 監控端點
```bash
# 健康檢查
curl http://localhost:8080/api/actuator/health

# 應用程式資訊
curl http://localhost:8080/api/actuator/info

# JVM 記憶體使用情況
curl http://localhost:8080/api/actuator/metrics/jvm.memory.used
```

## 🎓 學習重點

### 1. 分層架構理解
- **Controller**: 處理 HTTP 請求，資料驗證，呼叫 Service
- **Service**: 實現業務邏輯，事務管理，呼叫 Repository  
- **Repository**: 資料存取，JPA 查詢實現
- **Entity**: 資料庫實體映射
- **DTO**: 資料傳輸，避免直接暴露 Entity

### 2. 關鍵設計模式
- **依賴注入 (DI)**: 使用 `@RequiredArgsConstructor` 和 `final` 欄位
- **策略模式**: 不同的搜索和過濾策略
- **建造者模式**: `RestResponse` 的靜態工廠方法
- **模板方法**: `BaseEntity` 提供通用欄位

### 3. 企業級特性
- **統一異常處理**: `@RestControllerAdvice` 全局處理
- **AOP 橫切關注點**: 日誌、審計、性能監控
- **快取抽象**: `@Cacheable`, `@CacheEvict` 註解
- **資料驗證**: JSR-303 Bean Validation
- **API 文檔**: OpenAPI 3 自動生成

## 🔨 客製化開發

### 添加新的實體和 API

1. **創建實體類**（參考 `User.java`）
2. **創建 Repository 介面**（繼承 `JpaRepository`）
3. **創建 DTO 類**（請求/回應物件）
4. **創建 MapStruct Mapper**（實體與 DTO 轉換）
5. **實現 Service 類**（業務邏輯）
6. **創建 Controller 類**（REST API 端點）
7. **編寫測試**（單元測試和整合測試）

### 配置不同環境

```bash
# 開發環境
mvn spring-boot:run -Dspring.profiles.active=dev

# 測試環境  
mvn spring-boot:run -Dspring.profiles.active=test

# 生產環境
java -jar app.jar --spring.profiles.active=prod
```

## 📚 主要功能展示

### 創建用戶

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "firstName": "New",
    "lastName": "User",
    "phone": "+1234567890",
    "dateOfBirth": "1990-01-01",
    "bio": "A new user"
  }'
```

### 搜索和過濾用戶

```bash
# 通用搜索
curl "http://localhost:8080/api/users/search?q=john"

# 條件搜索  
curl "http://localhost:8080/api/users/search?firstName=John&status=ACTIVE"
```

## 🧪 運行測試

```bash
mvn test
```

## 🏗️ 技術架構

### 核心技術棧
- **Java 21** - 最新 LTS 版本
- **Spring Boot 3.2** - 現代化框架
- **Spring Security 6** - 安全框架  
- **Spring Data JPA** - 資料存取層
- **MapStruct 1.5.5.Final** - 物件映射
- **SpringDoc OpenAPI 3** - API 文檔
- **Redis** - 分散式快取
- **H2/MySQL** - 資料庫支援

### 架構設計亮點
- **分層架構**: Controller → Service → Repository → Entity
- **統一異常處理**: 全局異常攔截與標準化回應
- **AOP 整合**: 日誌記錄、審計追蹤、性能監控
- **DTO 映射**: MapStruct 自動化物件轉換
- **快取機制**: Redis 分散式快取實現
- **API 回應標準化**: `RestResponse<T>` 統一回應格式

## 🔧 開發指南

### 程式碼品質
本專案嚴格遵循 `.ai-rule/spring-code-standard.prompt.md` 中定義的程式碼標準：
- Google Java Style Guide 基準
- 嚴格的 import 順序規範
- Clean Code 與 SOLID 原則
- SLF4j 日誌標準（禁用 System.out）

### API 回應格式
所有 API 回應使用統一的 `RestResponse<T>` 格式：

```json
{
  "success": true,
  "message": "操作成功",
  "data": { /* 實際資料 */ },
  "timestamp": "2024-01-01T10:00:00"
}
```

### 範例 API 呼叫

#### 創建用戶（回傳 RestResponse 格式）
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "firstName": "New",
    "lastName": "User",
    "phone": "+1234567890",
    "dateOfBirth": "1990-01-01",
    "bio": "A new user"
  }'
```

#### 分頁查詢用戶
```bash
curl "http://localhost:8080/api/users?page=0&size=10&sort=createdAt,desc"
```

## 📈 效能特性
- **緩存策略**: 用戶資料自動快取，提升查詢效能
- **分頁支援**: 大量資料的高效分頁處理
- **索引優化**: 資料庫查詢效能優化
- **異步處理**: 非阻塞式資料處理

這個範例展示了如何使用 Spring MVC Template 快速構建一個功能完整、結構清晰、符合企業級標準的 RESTful API。