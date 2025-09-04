# 📦 可執行專案

這個目錄包含了可以直接運行和使用的 Spring Boot 專案。

## 📋 專案清單

### 🌟 spring-mvc-template
**類型**: 模板專案  
**用途**: 作為新專案的起始模板

- **啟動**: `cd spring-mvc-template && mvn spring-boot:run`
- **訪問**: http://localhost:8080/api
- **文檔**: http://localhost:8080/api/swagger-ui.html

**包含功能**:
- ✅ 完整的分層架構
- ✅ 統一異常處理
- ✅ API 文檔自動生成
- ✅ 多環境配置
- ✅ 測試框架配置

### 🎯 spring-mvc-example  
**類型**: 業務範例專案  
**用途**: 展示如何使用模板構建完整系統

- **啟動**: `cd spring-mvc-example && mvn spring-boot:run`
- **訪問**: http://localhost:8080/api
- **文檔**: http://localhost:8080/api/swagger-ui.html

**業務功能**:
- ✅ 用戶管理 CRUD
- ✅ 高級搜索過濾
- ✅ 分頁排序
- ✅ 資料驗證
- ✅ 緩存實現

## 🚀 快速開始

1. **選擇專案**: 根據需求選擇模板或範例
2. **安裝依賴**: `mvn clean install`
3. **啟動應用**: `mvn spring-boot:run`
4. **查看文檔**: 訪問 Swagger UI

## 🛠️ 開發建議

### 使用模板創建新專案
1. 複製 `spring-mvc-template` 目錄
2. 修改 `pom.xml` 中的專案資訊
3. 更新包名和類名
4. 根據需求添加業務邏輯

### 學習參考
- 對比兩個專案的差異
- 理解分層架構的實現
- 學習最佳實踐的應用