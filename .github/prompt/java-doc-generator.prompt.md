# 🤖 AI Agent 文件產生 Prompt Template（Java 專案專用 - 增強版）

---

> **使用說明**
> 本模板供 AI Agent 掃描 Java 專案後，自動產出完整技術文件與週邊 artefacts。
> 若欄位內容無法判斷，請以 `@TODO:` 標示並提供補充建議。
> **RESTful API 專案** 須另外輸出 **OpenAPI Spec**、**Postman Collection** 與 **自動化測試腳本**。

----

## 1️⃣ 🔰 專案概要

> AI Agent 請根據實際掃描結果並填入說明欄位中，並保留欄位和說明即可，不需要保留範例；不存在或無法確定者，請插入 `@TODO:` 並進行建議的後續補充。

| 欄位 | 說明 | 範例 |
|------|------|------|
| **專案名稱** | `<PROJECT_NAME>` | Payment Service |
| **描述** | `<PROJECT_DESCRIPTION>` | 微服務處理線上付款 |
| **主要程式語言** | `<LANGUAGE>` | Java |
| **核心功能** | `<MAIN_FEATURES>` | RESTful API、資料庫操作、訊息佇列 |
| **執行環境** | `<RUNTIME>` | JVM 17 / OpenJDK 21 |
| **框架** | `<FRAMEWORK>` | Spring Boot / Quarkus / Micronaut |
| **資料庫** | `<DATABASES>` | MongoDB / PostgreSQL / Redis |
| **授權條款** | `<LICENSE>` | MIT / Apache‑2.0 |
| **API 規格** | `<API_SPEC>`            | OpenAPI 3.1 (YAML)                |
---

## 2️⃣ 📁 目錄與檔案慣例

> AI Agent 請根據實際掃描結果，為下列目錄或檔案生成說明。除了列出的常用項，請額外列出並說明 **專案特有的重要目錄或檔案**，特別是 `src/` 下的關鍵子目錄（如 `resource/`, `service/`, `repository/`, `entity/`, `dto/`, `config/`, `exception/`, `converter/`, `filter/` 等）。不存在就插入 `@TODO:` 以備後續補充。

| 目錄／檔案 | 預期用途 |
|------------|---------|
| `src/` | 主要程式碼及資源文件 |
| `src/main/java/` | Java 原始碼根目錄 |
| `src/main/resources/` | 資源文件（組態檔、靜態文件等） |
| `src/test/` | 測試程式碼及測試資源 |
| `config/` | 外部化組態或環境設定檔 |
| `internal/`、`pkg/` | 封裝內部共用函式／模組（非 Java 常用） |
| `api/` | OpenAPI／GraphQL／gRPC 定義 |
| `scripts/` | 自動化腳本（建置、部署、維運） |
| `Dockerfile` | 建立容器映像檔 |
| `build.gradle`, `pom.xml` | 套件與建置管理（Gradle 或 Maven） |
| `README.md` | 專案說明文件 |
| `CHANGELOG.md` | 版本更新紀錄 |
| `docs/` | 深入技術文件或架構圖 |
| 其他掃描到的重要目錄/檔案 | 自動判斷並說明（包含 src/ 下的關鍵子目錄） |

---

## 3️⃣ 📄 文件產出清單

AI Agent 需生成（或補全）下列文件內容，以產出 `README.md` 為主體，並在需要時說明其他文件的生成：

### 3.1 `README.md`

內容結構請依次包含：

**專案介紹與定位** — 描述核心功能、技術堆疊、系統角色。
**安裝／建置／執行指南** — 明確列出終端機指令（JDK、Gradle/Maven、Docker、本機與容器執行模式）。
**組態說明與參數對照表** — 讀取 `application.yaml/properties`，列出所有偵測到的重要設定。
**典型使用流程與 API 呼叫範例**（含 `curl` 範例與預期回應）。
**API 端點清單** — 自動萃取或依 OpenAPI Spec 生成（若有）。
**FAQ 與除錯指引**。
**常用指令教學** — 針對偵測到的 `Makefile` / `Taskfile` / Gradle task 等。
**系統架構** — 技術堆疊列表、ASCII 互動圖、設計考量。
**未來改進建議**。

#### 3.1.1 專案介紹與定位
描述專案的核心功能、技術堆疊、在整個系統中的角色。

#### 3.1.2 安裝／建置／執行步驟
提供詳細的執行指南，包含：
-   **前置需求**: 列出必要的軟體版本（JDK, Maven/Gradle, Docker 等）。
-   **CLI 執行方式**:
    -   開發模式執行（含熱重載說明）。
    -   標準打包與執行指令（包含非 über-jar 結構說明）。
    -   `über-jar` 打包與執行指令（若專案設定支援）。
    -   原生執行檔打包與執行指令（若專案設定支援，含容器建置方式）。
-   **Docker 執行方式**:
    -   建構映像檔指令。
    -   執行容器指令（含埠號映射）。
    -   若偵測到 `docker-compose.yml` 或相關設定，提供 Docker Compose 執行範例，包含如何與依賴服務（如資料庫）互動。
-   在各步驟中明確列出**具體的終端機指令**。

#### 3.1.3 組態說明與設定參數對照表
-   說明組態檔的位置（如 `application.yaml`, `.properties`）以及環境變數如何覆蓋設定。
-   列出**所有偵測到的重要設定參數**對照表：

    | Key | 型別 | 預設值 | 說明 | 必填 |
    |-----|------|--------|------|-----|
    | `APP_PORT` 或 `server.port` | number | 8080 | 服務啟動埠 | N |
    | `spring.datasource.url` 或 `quarkus.datasource.url` | string | - | 資料庫連線 URL | Y (如果需要資料庫) |
    | `app.custom.setting` | string | default | 客製化設定範例 | N |
    | … | … | … | … | … |

#### 3.1.4 典型使用流程與 API 呼叫範例
-   描述使用者或客戶端如何與服務互動的典型步驟。
-   提供至少一個**具體的 API 呼叫範例**（如 `curl` 指令），並附上**預期的 JSON 回應範例**。

#### 3.1.5 API 端點清單
產生專案提供的所有 API 端點詳細表格，包含：

| 路徑             | HTTP 方法 | 參數（型別/必填）         | 回應格式         | 描述                |
|------------------|-----------|---------------------------|------------------|---------------------|
| `/api/payments`  | POST      | body: PaymentRequest (Y)  | JSON: PaymentRes | 建立付款交易         |
| `/api/payments`  | GET       | query: userId (string/N)  | JSON: PaymentRes[] | 查詢付款紀錄      |
| `/api/payments/{id}` | GET   | path: id (string/Y)       | JSON: PaymentRes | 取得單筆付款資訊     |
| `/api/payments/{id}` | DELETE| path: id (string/Y)       | JSON: Result     | 刪除付款紀錄         |

-   如果偵測到 OpenAPI/Swagger 文件，請**優先依據其內容**生成此表格，並提供 **Swagger UI 頁面連結**。
-   除了表格，請提供**重要 Request/Response 物件的 JSON 結構範例**及其欄位說明。

#### 3.1.6 常見問題（FAQ）與除錯指引
-   列出幾個可能遇到的常見問題（如資料庫連接失敗、認證問題等）。
-   針對每個問題提供**具體的解決方案或偵錯步驟**。
-   說明如何查看服務日誌。
-   若服務提供健康檢查或監控端點（如 `/health`, `/metrics`），請列出並說明其用途。

#### 3.1.7 常用指令教學（若偵測到 Makefile, Taskfile, etc.）
若偵測到 `Makefile`, `Taskfile.yml` 或類似的任務定義檔案，列出並說明其中重要的指令。

### 3.2 程式碼註解 (Javadoc)
為專案中的**關鍵程式碼元件**生成 Javadoc 樣板，包含：
-   **物件／模組層級註解**: 說明類別/介面的用途、重要欄位/方法的概觀。特別針對 `Entity`, `Service`, `Repository`, `Resource`, `DTO`, `Converter`, `Config`, `Exception Handler` 等類別。
-   **公開 API (方法)**: 說明方法用途、輸入參數 (`@param`)、回傳值 (`@return`)、可能拋出的例外 (`@throws`)。
-   **工具／共用函式**: 說明核心邏輯與使用時的注意事項。
-   請直接輸出 Javadoc 格式的**程式碼註解範例**，而非僅描述。

### 3.3 核心類別與元件說明
新增一個段落，扼要說明專案中的**核心類別、介面或元件**，按層次或功能分類（如 Entity 層、Repository 層、Service 層、Resource 層），並簡述它們在專案中的作用和重要性。例如：
-   `ChannelInfo` (Entity): 代表...，主要屬性...
-   `GenericRepository` (Interface): 定義...操作
-   `MongoDBChannelInfoRepository` (Repository): 實現...操作，使用...
-   `ChannelInfoService` (Service): 包含...業務邏輯
-   `ChannelInfoResource` (Resource): 提供...API 端點

### 3.4 CI/CD 設定
-   如果檔案 `github/workflows/*.yml`、`.gitlab-ci.yml`、`azure-pipelines.yml` 等存在：
    -   為每個 Job 補上描述、使用的 Agent、快取策略、Artifact 說明。
    -   說明觸發條件。
-   若 CI 檔案缺失：
    -   建立一版 **完整 YAML 範例**，包含至少 `lint` → `test` → `build` → `package/publish` 流程。
    -   請使用**常見的 CI 平台格式** (如 GitHub Actions, GitLab CI)，若無偏好則預設 GitHub Actions。
    -   在 YAML 範例中使用 `@TODO:` 標示需要配置的憑證或機密變數。
    -   明確說明每個 Job/Step 的目的。
    -   包含常見的 Java CI 步驟，如：設定 JDK、Maven/Gradle 指令、測試執行（含報告）、打包指令、Docker 映像檔建置（若有 Dockerfile）。若測試需要依賴服務（如資料庫），請在 CI 設定中包含啟動依賴服務的步驟（例如使用 Service Container 或 `docker run`）。

### 3.5 系統架構
新增一個段落，描述專案的整體架構：
-   **技術堆疊**: 列表形式呈現主要技術（框架、語言、資料庫、佇列、快取、監控工具等）。
-   **架構圖**: 使用簡單的文字或 ASCII 藝術繪製核心元件之間的互動關係圖（例如：Client -> API Gateway -> Service -> Database）。
-   **設計考量**: 簡要說明一些重要的設計原則或考量（如微服務、資料庫選擇原因、異步處理等）。

### 3.7 RESTful API 專屬輸出（僅當專案包含 API）

| Artefact               | 產出位置                                  | 說明                                              |
| ---------------------- | ------------------------------------- | ----------------------------------------------- |
| **OpenAPI Spec**       | `api/openapi.yaml`                    | 依程式註解或 Swagger Doc 萃取，格式為 **OpenAPI 3.1 YAML**。 |
| **Postman Collection** | `docs/<PROJECT_NAME>-collection.json` | 依 OpenAPI Spec 轉換，含所有端點與範例。                     |
| **Postman 測試腳本**       | 內嵌於 Collection                        | 自動為每個端點生成 **基本斷言**（HTTP Status、JSON Schema）。    |
| **CI/CD API 測試 Step**  | `github/workflows/...yml`             | 新增 `postman/newman` Step，自動執行 Collection 並匯出報告。 |

> **生成規則**
>
> 1. 若專案已有 Swagger/OpenAPI，優先使用並補完缺失欄位。
> 2. 若缺少規格，Agent 應根據 Controller 註解自動推斷、生成 OpenAPI Spec。
> 3. Postman Collection 應包含環境變數範例（Base URL、Token 等）。


### 3.8 未來改進建議
新增一個段落，列出基於目前專案狀態， AI Agent 建議的未來可以改進或增強的方向（例如：增加更多 API、改善錯誤處理、增加測試覆蓋率、實作認證授權、API 版本控制、詳細日誌記錄等）。

## 4️⃣ ✨ 加值輸出
-   偵測 Test Coverage 報告檔案，於 README 顯示徽章（Badge）及其狀態（若有偵測到相關設定或報告檔案）。

---

## 5️⃣ 💡 語調與格式規範
-   採 **技術文件標準語氣**，精準、清晰，條理分明。
-   善用 Markdown 格式：標題、列表、表格、程式碼區塊、粗體。
-   使用臺灣慣用字詞（「映像檔」「佇列」「佈署」等），避免簡體用語。
-   對於無法推論、需要人工確認或補充的資訊，一律在對應位置標示 `@TODO:`，並簡述需要補充的內容。

---

### ▶️ 建議執行指令
> **「請依照上述通用模板，詳細掃描目前程式庫，輸出完整的 README.md 文件內容，生成關鍵程式碼的 Javadoc 註解樣板，並根據掃描結果提供或補全 CI/CD 設定 YAML 範例；完成後列出本次操作產生的檔案清單與摘要。請特別注意安裝執行步驟、組態說明、API 端點的詳細度（含參數、回應格式、物件結構、Swagger 連結），以及核心類別、系統架構、未來建議等新加入的段落。」**
