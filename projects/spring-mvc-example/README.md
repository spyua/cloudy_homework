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

### 1. 啟動應用

```bash
cd spring-mvc-example
mvn spring-boot:run
```

### 2. 訪問應用

- 應用地址: http://localhost:8080/api
- API 文檔: http://localhost:8080/api/swagger-ui.html
- H2 控制台: http://localhost:8080/api/h2-console

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

這個範例展示了如何使用 Spring MVC Template 快速構建一個功能完整、結構清晰的 RESTful API。