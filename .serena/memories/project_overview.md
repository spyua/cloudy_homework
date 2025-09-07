# Spring Boot 開發資源庫概述

## 專案目的
這是一個現代 Spring Boot 開發資源庫，提供可重複使用的模板、實際範例和詳細的架構指南，幫助開發者快速建立生產就緒的應用程式。

## 核心專案
1. **spring-mvc-template** (模板專案) - 提供現代化、生產就緒的 Spring Boot MVC 起始模板
2. **spring-mvc-example** (範例專案) - 展示如何使用模板構建完整的用戶管理業務系統

## 技術棧
- **Java 21** - 最新 LTS 版本
- **Spring Boot 3.2** - 現代化框架
- **Spring Security 6** - 安全架構
- **Spring Data JPA** - 資料存取層
- **MapStruct 1.5.5.Final** - 物件映射
- **SpringDoc OpenAPI 3** - API 文檔自動生成
- **JUnit 5 + AssertJ** - 測試框架
- **Maven** - 建構工具

## 分層架構
```
controller/ → service/ → repository/ → entity/
     ↓           ↓
   dto/ ←─── mapper/
```

## 核心功能特性
- 統一異常處理與 API 回應格式
- AOP 整合 (日誌、審計、效能監控)
- MapStruct 自動 DTO-Entity 轉換
- Redis 快取整合
- 多環境配置支援
- 健康檢查端點 (Actuator)
- 容器化支援 (Docker)