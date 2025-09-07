# 🏗️ Spring Boot 開發資源庫

這個資源庫包含了現代 Spring Boot 開發的完整資源，包括可重複使用的模板、實際範例和詳細的架構指南。

## 📚 完整文檔網站
> 🌟 **全新體驗**: [訪問 GitHub Pages 文檔網站](https://username.github.io/cloudy_homework/) 獲得更好的閱讀體驗

## 📁 專案結構

```
📁 cloudy_homework/
├── 📂 projects/                    # 🚀 可執行專案
│   ├── 📦 spring-mvc-template/     # 📋 Spring MVC 模板專案
│   └── 📦 spring-mvc-example/      # 🎯 用戶管理系統範例
├── 📂 docs/                       # 📚 完整技術文檔 (GitHub Pages)
│   ├── 📂 tutorials/              # 🎓 完整教學指南
│   ├── 📂 architecture/           # 🏛️ 架構設計文檔
│   └── 📂 examples/               # 💼 範例展示
├── 📂 legacy/                     # 🗃️ 歷史專案
│   └── 📂 cloudy-homework-original/ # 原始 cloudy homework 專案
└── 📄 README.md                   # 📋 專案總覽 (此文件)
```

## 🚀 快速開始

### 1. Spring MVC Template (模板專案)
**目的**: 提供一個現代化、生產就緒的 Spring Boot MVC 起始模板

```bash
cd projects/spring-mvc-template
mvn spring-boot:run
```

**功能特色**:
- ✅ Spring Boot 3.2 + Java 21
- ✅ 統一異常處理與API回應格式
- ✅ MapStruct 物件映射
- ✅ OpenAPI 3 文檔自動生成
- ✅ Redis 快取整合
- ✅ 多環境配置支援
- ✅ 完整的測試架構

**適用場景**:
- 新專案的起始模板
- 學習現代 Spring Boot 最佳實踐
- 快速原型開發

### 2. Spring MVC Example (範例專案)  
**目的**: 展示如何使用模板構建完整的業務系統

```bash
cd projects/spring-mvc-example
mvn spring-boot:run
```

**業務功能**:
- ✅ 完整的用戶 CRUD 操作
- ✅ 高級搜索與過濾
- ✅ 分頁與排序
- ✅ 資料驗證與錯誤處理
- ✅ 緩存機制實現
- ✅ 完整的測試覆蓋

**學習重點**:
- 分層架構的實際應用
- Repository 查詢方法設計
- DTO 映射最佳實踐
- 業務邏輯組織方式

## 📚 學習資源

### 🎓 完整教學指南 *(推薦從這裡開始)*
- 🔥 [**Spring MVC Template 完整上手教學**](./docs/tutorials/spring-mvc-template-tutorial.md) - 從模板探索到生產部署的完整 125 分鐘教學
- 💼 [**Spring MVC Example 實戰指南**](./docs/tutorials/spring-mvc-example-tutorial.md) - 用戶管理系統深度解析和最佳實踐
- 📖 [**教學總覽**](./docs/tutorials/README.md) - 不同學習路徑和建議

### 🏛️ 架構設計
- [**整體架構設計**](./docs/architecture/ARCHITECTURE_DESIGN.md) - 微服務架構與現代設計理念
- [**技術棧選擇**](./docs/architecture/TECH_STACK.md) - 現代 Java 技術棧詳解
- [**模組結構規劃**](./docs/architecture/MODULE_STRUCTURE.md) - 清晰的服務邊界設計
- [**開發環境建置**](./docs/architecture/DEVELOPMENT_ENVIRONMENT.md) - 完整的開發工具鏈

### 💼 範例展示
- [**用戶管理系統**](./docs/examples/user-management.md) - 完整業務實現展示
- [**設計模式應用**](./docs/examples/design-patterns.md) - 企業級設計模式實踐

### 📖 開發指南
- [**專案總結**](./docs/guides/PROJECT_SUMMARY.md) - 重構分析與架構對比
- [**Spring MVC 專案指南**](./docs/guides/SPRING_MVC_PROJECT_SUMMARY.md) - 模板使用與最佳實踐

## 🎯 使用建議

### 📚 初學者路徑
1. **開始學習**: 先閱讀 [教學總覽](./docs/tutorials/README.md) 了解整體架構
2. **動手實踐**: 跟隨 [Template 完整教學](./docs/tutorials/spring-mvc-template-tutorial.md) 逐步實作
3. **深入理解**: 參考 [Example 實戰指南](./docs/tutorials/spring-mvc-example-tutorial.md) 學習業務實現

### 🚀 進階開發者路徑  
1. **快速上手**: 直接從 [Template 教學](./docs/tutorials/spring-mvc-template-tutorial.md) 開始
2. **技術深入**: 研讀 [技術棧文檔](./docs/architecture/TECH_STACK.md)
3. **生產實施**: 實施生產環境部署和監控

### 👨‍💼 團隊負責人路徑
1. **項目評估**: 閱讀 [專案總結](./docs/guides/PROJECT_SUMMARY.md) 了解整體價值  
2. **架構決策**: 評估 [架構設計](./docs/architecture/ARCHITECTURE_DESIGN.md) 的適用性
3. **團隊標準**: 制定基於模板的團隊開發標準

## 🛠️ 核心技術特點

### 現代化技術棧
- **Java 21** - 最新 LTS 版本，性能和安全性優化
- **Spring Boot 3.2** - 原生支援、現代化配置
- **Spring Security 6** - 先進的安全架構
- **Spring Data JPA** - 簡化的資料存取

### 開發體驗優化
- **統一異常處理** - 全局攔截，標準化回應
- **自動 API 文檔** - OpenAPI 3 規範
- **熱重載支援** - DevTools 開發效率
- **程式碼品質** - SpotBugs、JaCoCo 檢查

### 生產就緒特性
- **健康檢查** - Actuator 監控端點
- **分散式快取** - Redis 整合
- **多環境配置** - Profile 環境隔離
- **容器化支援** - Docker 就緒

## 🔄 版本演進

### v1.0 (當前版本)
- ✅ 基礎 Spring MVC 模板
- ✅ 用戶管理範例系統
- ✅ 完整的技術文檔
- ✅ 測試與驗證

### 未來規劃
- 🔄 微服務模板擴展
- 🔄 React/Vue 前端整合範例
- 🔄 DevOps 與部署腳本
- 🔄 性能測試與優化指南

## 🤝 貢獻與回饋

### 如何使用這些資源
1. **克隆或下載** 整個資源庫
2. **選擇合適的專案** 作為起始點
3. **閱讀相關文檔** 理解設計決策
4. **根據需求調整** 配置和業務邏輯

### 回饋與改進
- 🐛 發現問題請提出 Issue
- 💡 改進建議歡迎討論
- 🔀 貢獻代碼請提交 PR
- 📚 文檔改進也很重要

## 📞 支援與聯繫

### 技術支援
- 📖 **首先查看文檔**: 大部分問題都有詳細說明
- 🔍 **搜索現有 Issues**: 可能已有解決方案
- 💬 **提出新問題**: 描述清楚問題場景

### 學習資源
- [Spring Boot 官方文檔](https://spring.io/projects/spring-boot)
- [Spring Security 參考指南](https://spring.io/projects/spring-security)
- [Spring Data JPA 文檔](https://spring.io/projects/spring-data-jpa)

---

**🎯 目標**: 提供現代化、高品質的 Spring Boot 開發起始點，幫助開發者快速建立生產就緒的應用程式。

**💡 理念**: 最佳實踐、代碼品質、開發效率的完美平衡。