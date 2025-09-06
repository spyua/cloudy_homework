## A. 基準與優先原則

* **基準**：以 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) 為基礎。
* **覆寫規則**：

  * 縮排：使用 **Tab**（視為 4 空白）。
  * 行寬：**120 字元**。
  * Import 次序：

    1. `java.*`
    2. `jakarta.*`（或 `javax.*` 舊版）
    3. 其他第三方 (`org.*`, `com.*`, …，字母序)
    4. `org.springframework.*`（獨立一組，字母序）
    5. **Static imports**：最後一組
  * 花括號：K\&R 風格。
* **優先原則**：一致性與可讀性 > 聰明技巧 > 短小程式碼。

---

## B. Clean Code 原則

* **單一職責 (SRP)**：每個類別/方法應只做一件事。
* **方法短小**：通常 <20 行，邏輯明確。
* **命名即文件**：清楚、具表達力，避免縮寫或隱晦詞。
* **避免魔法數字**：用常數或 Enum 取代。
* **早退 (Guard Clauses)**：減少巢狀 `if`。
* **錯誤處理**：例外用於異常流程，而非正常流程。
* **注重「為什麼」的註解**，避免「做什麼」的重複描述。

---

## C. OO 與常用 Pattern 指南

* **物件導向核心**：

  * 抽象 (Abstraction)
  * 封裝 (Encapsulation)
  * 繼承 (Inheritance)
  * 多型 (Polymorphism)
* **SOLID 原則**：

  1. 單一職責 (SRP)
  2. 開放封閉 (OCP)
  3. 里氏替換 (LSP)
  4. 介面隔離 (ISP)
  5. 依賴反轉 (DIP)
* **常用設計模式**（Spring 常見場景）：

  * **DI/IoC**（內建於 Spring）：依賴注入。
  * **Factory/Builder**：物件建立。
  * **Strategy**：演算法可替換。
  * **Template Method**：流程骨架。
  * **Observer/Event Listener**：Spring 事件。
  * **Decorator**：功能擴充（AOP Filter, Interceptor）。
  * **Singleton**：Spring Bean scope。
* **最佳實務**：

  * 使用介面抽象業務邏輯。
  * 減少繼承層級，偏好組合 (Composition over Inheritance)。
  * 使用模式需有「語境」與「價值」，避免 pattern for pattern’s sake。

---

## D. 文件與註解

* **Javadoc**：

  * `public`、`protected` 成員必填。
  * `@since`：純文字版本號（`@since 1.2.3`）。
  * `@throws`：僅針對受檢例外必須。
* **註解**：僅解釋「為什麼」，避免多餘「做什麼」。

---

## E. 程式設計實務

* **修飾詞**：盡量使用最嚴格可見性。
* **`final`**：建議用於不可重新指定的 field，避免濫用在參數/區域變數。
* **Override**：重寫必須標註 `@Override`。
* **Logging**：使用 SLF4j；禁止 `System.out`/`System.err`。
* **Streams**：僅在可讀性提升時使用，最多 3 個中介操作。
* **`var`**：僅在型別明顯可見時使用。
* **測試**：JUnit 5 + AssertJ。

---

## F. Spring 配置管理

* **優先順序**：

  1. `application.yml`
  2. `@ConfigurationProperties`（constructor binding）
  3. `@Configuration` 類別（複雜初始化/條件邏輯）
* **建議**：偏好不可變 `record` 搭配 `@ConfigurationProperties`。

---

## G. 自動化落地配套

* 提供 `.editorconfig` 控制縮排、行寬。
* 提供 Checkstyle/Spotless 設定檔，PR pipeline 強制檢查。
* 提供 IDE Code Style XML，統一團隊環境。

---

## 範例程式（含 OO/Pattern 思維）

```java
package com.example.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 範例服務：以 Strategy Pattern 管理不同的 Widget 過濾策略。
 *
 * @since 1.1.0
 */
@Service
public class WidgetService {

    private static final Logger log = LoggerFactory.getLogger(WidgetService.class);

    private final WidgetRepository widgetRepository;
    private final FilterStrategy filterStrategy;

    public WidgetService(WidgetRepository widgetRepository, FilterStrategy filterStrategy) {
        this.widgetRepository = widgetRepository;
        this.filterStrategy = filterStrategy;
    }

    public List<String> findActiveWidgetNames(String filter) {
        if (!filterStrategy.isValid(filter)) {
            log.warn("Invalid filter: {}", filter);
            throw new IllegalArgumentException("Invalid filter");
        }

        return widgetRepository.findActive(filter).stream()
                .map(Widget::getName)
                .toList();
    }
}
```
