# 開發常用指令

## Template 專案指令
```bash
cd projects/spring-mvc-template
mvn spring-boot:run                  # 啟動應用程式
mvn test                            # 執行所有測試
mvn test -Dtest=ClassName           # 執行單一測試類別
mvn clean compile                   # 清理並編譯
mvn clean package                   # 打包應用程式
```

## Example 專案指令  
```bash
cd projects/spring-mvc-example
mvn spring-boot:run                  # 啟動應用程式
mvn test                            # 執行所有測試
mvn test -Dtest=ClassName           # 執行單一測試類別
mvn clean compile                   # 清理並編譯
mvn clean package                   # 打包應用程式
```

## 測試指令
- `mvn test` - 執行所有單元測試
- `mvn integration-test` - 執行整合測試 (如果有)
- `mvn verify` - 執行完整驗證流程

## 代碼品質檢查
- Maven 已配置 SpotBugs 和 JaCoCo 插件進行代碼品質檢查
- 測試覆蓋率報告會在 `target/site/jacoco/` 生成

## Git 工作流程
- 主分支: `main`
- 功能分支: `feat-*`
- 當前分支: `feat-template`

## 系統指令 (Linux)
- `ls` - 列出檔案和目錄
- `find` - 搜尋檔案
- `grep` - 文字搜尋
- `git status` - 查看 Git 狀態
- `git log --oneline -10` - 查看最近 10 次提交