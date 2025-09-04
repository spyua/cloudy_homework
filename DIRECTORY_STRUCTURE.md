# 📁 專案目錄結構說明

## 🏗️ 整體架構

經過重新組織後，專案採用清晰的分層目錄結構，將不同性質的內容分類管理：

```
📁 cloudy_homework/                    # 根目錄
├── 📂 projects/                      # 🚀 可執行專案區域
│   ├── 📦 spring-mvc-template/       # Spring MVC 模板專案
│   │   ├── 📄 pom.xml               # Maven 配置文件
│   │   ├── 📄 README.md             # 模板專案說明
│   │   ├── 📂 src/main/java/        # 主要源代碼
│   │   │   └── 📂 com/template/     # 模板基礎架構
│   │   ├── 📂 src/main/resources/   # 配置資源
│   │   └── 📂 src/test/             # 測試代碼
│   │
│   ├── 📦 spring-mvc-example/        # 用戶管理系統範例
│   │   ├── 📄 pom.xml               # Maven 配置文件
│   │   ├── 📄 README.md             # 範例專案說明
│   │   ├── 📂 src/main/java/        # 主要源代碼
│   │   │   ├── 📂 com/example/      # 具體業務實現
│   │   │   └── 📂 com/template/     # 繼承的模板基礎
│   │   ├── 📂 src/main/resources/   # 配置資源與初始化資料
│   │   └── 📂 src/test/             # 測試代碼
│   │
│   └── 📄 README.md                 # 專案區域說明
│
├── 📂 docs/                         # 📚 技術文檔區域
│   ├── 📂 architecture/             # 架構設計文檔
│   │   ├── 📄 ARCHITECTURE_DESIGN.md    # 整體架構設計
│   │   ├── 📄 TECH_STACK.md             # 技術棧選擇
│   │   ├── 📄 MODULE_STRUCTURE.md       # 模組結構規劃
│   │   └── 📄 DEVELOPMENT_ENVIRONMENT.md # 開發環境建置
│   │
│   ├── 📂 guides/                   # 開發指南
│   │   ├── 📄 PROJECT_SUMMARY.md        # 專案總結
│   │   └── 📄 SPRING_MVC_PROJECT_SUMMARY.md # Spring MVC 專案指南
│   │
│   └── 📄 README.md                 # 文檔區域說明
│
├── 📂 legacy/                       # 🗃️ 歷史專案區域
│   ├── 📂 cloudy-homework-original/ # 原始 cloudy homework 專案
│   │   ├── 📦 cloudy-account/       # 原始帳戶模組
│   │   ├── 📦 cloudy-files/         # 原始檔案模組  
│   │   ├── 📦 cloudy-security/      # 原始安全模組
│   │   ├── 📦 cloudy-event/         # 原始事件模組
│   │   ├── 📄 CLAUDE.md             # 原始專案說明
│   │   └── 📄 pom.xml               # 原始 Maven 父專案
│   │
│   └── 📄 README.md                 # 歷史區域說明
│
├── 📄 README.md                     # 📋 主要專案說明
└── 📄 DIRECTORY_STRUCTURE.md        # 📁 目錄結構說明 (此文件)
```

## 🎯 設計理念

### 1. 分離關注點 (Separation of Concerns)
- **projects/**: 實際可執行的程式碼專案
- **docs/**: 純文檔資料，不包含程式碼
- **legacy/**: 歷史資料，供參考對比使用

### 2. 層次化組織 (Hierarchical Organization)
- **第一層**: 按性質分類（專案/文檔/歷史）
- **第二層**: 按功能細分（不同專案/不同文檔類型）
- **第三層**: 按標準結構組織（Maven 結構/文檔分類）

### 3. 直觀易懂 (Intuitive Navigation)
- 每個目錄都有對應的 README.md 說明
- 使用 emoji 和清晰的命名提高可讀性
- 相關內容就近放置，減少跳轉

## 📖 使用指南

### 🚀 對於開發者

#### 快速開始新專案
1. **複製模板**: `cp -r projects/spring-mvc-template my-new-project`
2. **修改配置**: 更新 pom.xml 和包名
3. **參考範例**: 查看 `projects/spring-mvc-example` 的實現
4. **閱讀文檔**: 參考 `docs/` 中的技術指南

#### 學習最佳實踐
1. **對比學習**: 比較 template 和 example 的差異
2. **架構理解**: 閱讀 `docs/architecture/` 了解設計思路
3. **實踐應用**: 運行範例專案體驗功能

### 📚 對於架構師

#### 架構參考
1. **設計理念**: `docs/architecture/ARCHITECTURE_DESIGN.md`
2. **技術選型**: `docs/architecture/TECH_STACK.md`
3. **模組設計**: `docs/architecture/MODULE_STRUCTURE.md`
4. **對比分析**: `docs/guides/PROJECT_SUMMARY.md`

#### 團隊標準
- 基於模板建立團隊開發標準
- 使用文檔規範技術選型
- 參考最佳實踐指導團隊

### 🎓 對於學習者

#### 學習路線
1. **從範例開始**: 運行 `projects/spring-mvc-example`
2. **理解模板**: 分析 `projects/spring-mvc-template`
3. **深入架構**: 閱讀 `docs/architecture/` 系列文檔
4. **歷史對比**: 查看 `legacy/` 了解演進過程

## 🔧 維護說明

### 目錄命名規範
- **專案目錄**: kebab-case，如 `spring-mvc-template`
- **文檔目錄**: 單詞分隔，如 `architecture`, `guides`
- **文件命名**: UPPER_CASE.md，如 `README.md`, `TECH_STACK.md`

### 內容組織原則
- **就近原則**: 相關內容放在同一目錄
- **單一職責**: 每個目錄只負責一類內容
- **清晰命名**: 目錄和文件名要能清楚表達內容

### 文檔維護
- 每個目錄必須有 README.md 說明
- 重要變更需要更新相關文檔
- 保持文檔與代碼的同步

## 🚀 優勢特點

### 1. 開發效率提升
- **模板化**: 快速啟動新專案
- **標準化**: 統一的代碼結構和風格
- **文檔化**: 完整的技術文檔支持

### 2. 學習成本降低  
- **漸進式**: 從範例到模板到架構文檔
- **對比式**: 新舊實現的直接對比
- **實踐式**: 可直接運行的完整專案

### 3. 維護便利性
- **模組化**: 清晰的職責邊界
- **版本化**: 歷史版本的保留和追溯
- **標準化**: 統一的組織和命名規範

這個重新組織的目錄結構為 Spring Boot 開發提供了一個清晰、實用、易維護的資源庫，適合不同層次的使用者需求。