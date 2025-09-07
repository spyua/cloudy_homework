# 代碼風格與慣例

## 基準標準
- **基礎**: Google Java Style Guide
- **縮排**: Tab (視為 4 空格)
- **行寬**: 120 字元
- **花括號**: K&R 風格

## Import 順序
1. `java.*`
2. `jakarta.*` (或 `javax.*`)
3. 第三方套件 (`org.*`, `com.*` 等，字母順序)
4. `org.springframework.*` (獨立一組，字母順序)
5. **Static imports** (最後一組)

## Clean Code 原則
- **單一職責原則 (SRP)**: 每個類別/方法只做一件事
- **方法簡短**: 通常 < 20 行，邏輯清晰
- **命名即文件**: 清楚、具表達力，避免縮寫
- **避免魔法數字**: 使用常數或 Enum
- **早期返回 (Guard Clauses)**: 減少巢狀 `if`
- **適當的例外處理**: 例外用於異常情況，非正常流程

## SOLID 原則
1. 單一職責 (SRP)
2. 開放封閉 (OCP)
3. 里氏替換 (LSP)
4. 介面隔離 (ISP)
5. 依賴反轉 (DIP)

## 日誌規範
- **必須使用 SLF4J**，絕不使用 System.out.println()
- 日誌等級: ERROR > WARN > INFO > DEBUG > TRACE
- 重要業務操作需記錄 INFO 日誌
- 異常處理必須記錄 ERROR 日誌

## Spring Boot 特定慣例
- 使用 `@ConfigurationProperties` 進行配置綁定
- 偏好構造器注入而非欄位注入
- 使用 `@Service`, `@Repository`, `@Controller` 等語義明確的註解
- 統一異常處理使用 `@ControllerAdvice`