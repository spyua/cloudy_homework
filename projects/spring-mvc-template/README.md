# Spring MVC Template

現代化的 Spring Boot MVC 模板，集成最佳實踐和常用功能。

## 🏗️ 軟體架構設計

採用**分層架構設計模式**（Layered Architecture），遵循 Spring Boot 最佳實踐：

```
    ╔═══════════════════════════════════════════════════════════╗
    ║     Infrastructure Layer (Cross-cutting Concerns)        ║
    ║  ┌─────────────────────────────────────────────────────┐ ║
    ║  │ AOP | Config | Exception | Security | Util | Cache  │ ║
    ║  └─────────────────────────────────────────────────────┘ ║
    ╚═══════════════╤═══════════════╤═══════════════╤═════════╝
                    │               │               │
    ┌───────────────▼───────────────▼───────────────▼───────────┐
    │                   Presentation Layer                      │
    │                   (Controller + DTO)                      │
    ├────────────────────────────────────────────────────────────┤
    │                    Business Layer                         │
    │                     (Service)                             │
    ├────────────────────────────────────────────────────────────┤
    │                  Data Access Layer                        │
    │                (Repository + Entity)                      │
    └─────────────────────────┬──────────────────────────────────┘
                              │
    ┌─────────────────────────▼──────────────────────────────────┐
    │                       Database                            │
    └────────────────────────────────────────────────────────────┘
    
    ═══ 表示橫切所有層級（全層可用）
    ─── 表示標準分層架構
```

### 各層職責詳解

#### 🔧 **Infrastructure Layer（基礎設施層）- 橫切關注點**

Infrastructure Layer 並非傳統意義上的"底層"，而是**橫切所有層級**的基礎設施支援：

**📁 Config（配置類）**
- **WebConfig**: Web MVC 配置（CORS、攔截器等）
- **SecurityConfig**: Spring Security 安全配置
- **OpenApiConfig**: Swagger/OpenAPI 文檔配置
- **作用範圍**: 影響所有層級的行為

**📁 Exception（異常處理）**
- **GlobalExceptionHandler**: 全局異常處理器
  - 統一捕獲所有層拋出的異常
  - 返回標準錯誤格式
- **BusinessException**: 業務異常
- **ResourceNotFoundException**: 資源不存在異常
- **作用範圍**: 處理來自任何層的異常

**📁 Util（工具類）**
- **ValidationUtil**: 驗證工具類
- **作用範圍**: 可被任何層調用

**📁 Mapper**
- 使用 MapStruct 進行對象映射
- Entity ↔ DTO 自動轉換
- **作用範圍**: 主要用於層與層之間的數據轉換

#### 1️⃣ **Presentation Layer（展示層）**

**📁 Controller**
- **職責**：處理 HTTP 請求、路由映射、參數驗證
- **特點**：使用 `@RestController`、`@RequestMapping`
- **規範**：RESTful API 設計、統一回應格式

**📁 DTO (Data Transfer Object)**
- **ApiResponse.java**: 統一 API 回應格式
  - 包含 success、message、data、timestamp
  - 提供靜態工廠方法簡化創建
- **PageResponse.java**: 分頁回應格式
  - 支援分頁資訊封裝

#### 2️⃣ **Business Layer（業務層）**

**📁 Service**
- **職責**：業務邏輯處理、事務管理、緩存處理
- **特點**：使用 `@Service`、`@Transactional`
- **規範**：單一職責、介面導向設計

#### 3️⃣ **Data Access Layer（數據訪問層）**

**📁 Repository**
- **職責**：數據持久化操作
- **特點**：繼承 `JpaRepository`
- **規範**：方法命名規約、自定義查詢

**📁 Entity**
- **BaseEntity.java**: 基礎實體類
  - 自動管理 id、createdAt、updatedAt
  - 支援樂觀鎖（version）
  - 使用 JPA Auditing 自動填充時間戳


## 🚀 功能特色

### 核心技術棧

- ✅ **Spring Boot 3.2** - 最新穩定版本
- ✅ **Java 21** - 最新 LTS 版本
- ✅ **Spring Security 6** - 現代化安全框架
- ✅ **Spring Data JPA** - 數據訪問層
- ✅ **Spring AOP** - 面向切面編程
- ✅ **Redis Cache** - 分布式緩存
- ✅ **Swagger/OpenAPI 3** - API 文檔
- ✅ **MapStruct** - 對象映射
- ✅ **Lombok** - 減少樣板代碼
- ✅ **統一異常處理** - 全局異常攔截
- ✅ **統一回應格式** - API 回應標準化
- ✅ **TestContainers** - 整合測試
- ✅ **多環境配置** - dev/prod 環境隔離

### 核心功能特性

#### 🔐 **安全機制**
- Spring Security 6 整合
- JWT Token 認證支援
- CORS 配置
- XSS/CSRF 防護

#### 🗄️ **數據管理**
- Spring Data JPA 整合
- 自動時間戳管理（創建/更新時間）
- 樂觀鎖版本控制
- 支援多種數據庫（H2/PostgreSQL/MySQL）

#### ⚡ **性能優化**
- Redis 緩存支援
- HTTP/2 啟用
- GZIP 壓縮
- 連接池配置

#### 📊 **監控與文檔**
- Actuator 健康檢查
- Prometheus 指標輸出
- Swagger UI 自動生成
- 結構化日誌記錄

#### 🎯 **AOP 橫切關注點**
- **日誌切面** - 自動記錄 API 請求/響應
- **性能監控** - 追蹤方法執行時間
- **審計日誌** - 記錄敏感操作
- **自定義註解** - @TrackPerformance、@Auditable

## 📁 專案結構

```
src/main/java/com/template/
├── Application.java              # 主應用程式類
├── aspect/                      # AOP 切面
│   ├── LoggingAspect.java     # 日誌切面
│   ├── PerformanceAspect.java # 性能監控切面
│   └── AuditAspect.java       # 審計切面
├── annotation/                 # 自定義註解
│   ├── TrackPerformance.java  # 性能追蹤註解
│   └── Auditable.java         # 審計註解
├── config/                      # 配置類
│   ├── WebConfig.java          # Web 配置
│   ├── SecurityConfig.java     # 安全配置
│   └── OpenApiConfig.java      # API 文檔配置
├── controller/                 # 控制器層
├── service/                   # 業務層
├── repository/               # 數據訪問層
├── entity/                   # 實體類
│   └── BaseEntity.java       # 基礎實體
├── dto/                      # 數據傳輸對象
│   ├── ApiResponse.java      # 統一回應格式
│   └── PageResponse.java     # 分頁回應格式
├── mapper/                   # MapStruct 映射器
├── exception/                # 異常處理
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── BusinessException.java
└── util/                     # 工具類
    └── ValidationUtil.java   # 驗證工具
```

## 🛠️ 快速開始

### 1. 環境要求

- Java 21+
- Maven 3.9+
- Redis (可選，本地開發可關閉)

### 2. 啟動應用

```bash
# 開發模式啟動
mvn spring-boot:run

# 或者
mvn clean package
java -jar target/spring-mvc-template-1.0.0-SNAPSHOT.jar
```

### 3. 訪問應用

- 應用地址: http://localhost:8080/api
- API 文檔: http://localhost:8080/api/swagger-ui.html
- H2 控制台: http://localhost:8080/api/h2-console (開發環境)
- 健康檢查: http://localhost:8080/api/actuator/health

## 📝 使用說明

### 🚀 快速創建新功能步驟

1. **創建實體類** - 繼承 `BaseEntity`
2. **創建 Repository** - 繼承 `JpaRepository`
3. **創建 DTO** - 定義傳輸對象
4. **創建 Mapper** - 使用 MapStruct
5. **實現 Service** - 業務邏輯
6. **創建 Controller** - RESTful API

### 創建新的 API

1. **定義實體類 (Entity)**
```java
@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    // 其他欄位...
}
```

2. **創建 Repository**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
}
```

3. **定義 DTO**
```java
@Data
@Builder
public class UserDto {
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}
```

4. **創建 Mapper**
```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
    List<UserDto> toDtoList(List<User> users);
}
```

5. **實現 Service**
```java
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BusinessException("Email already exists");
        }
        
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toDto(user);
    }
}
```

6. **創建 Controller**
```java
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User CRUD operations")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<ApiResponse<UserDto>> createUser(
            @Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(ApiResponse.success("User created successfully", createdUser));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(
            @PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
```

### 💼 實際使用範例

**創建產品管理功能：**

```java
// 1. Entity
@Entity
public class Product extends BaseEntity {
    private String name;
    private BigDecimal price;
    private Integer stock;
}

// 2. Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceGreaterThan(BigDecimal price);
}

// 3. Service
@Service
@Transactional
public class ProductService {
    @Cacheable("products")
    public ProductDto getProduct(Long id) {
        return productRepository.findById(id)
            .map(productMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }
}

// 4. Controller
@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable Long id) {
        ProductDto product = productService.getProduct(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
}
```

### 環境配置

#### 開發環境 (application-dev.yml)
- 使用 H2 內存資料庫
- 開啟 SQL 日誌
- 啟用 H2 控制台

#### 生產環境 (application-prod.yml)
- 使用 PostgreSQL 資料庫
- 優化日誌級別
- 配置連接池

### 緩存使用

```java
@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        // 方法實現
    }
    
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        // 方法實現
    }
}
```

### AOP 使用範例

#### 1. 性能監控

```java
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    
    @GetMapping("/generate")
    @TrackPerformance(warnThreshold = 5000, logArgs = true)
    public ResponseEntity<ApiResponse<ReportDto>> generateReport(
            @RequestParam String type) {
        // 自動監控執行時間，超過 5 秒會警告
        return reportService.generate(type);
    }
}
```

#### 2. 審計日誌

```java
@Service
public class UserService {
    
    @Auditable(action = "DELETE_USER", resourceType = "User")
    public void deleteUser(Long userId) {
        // 自動記錄誰、何時、從哪裡刪除了用戶
        userRepository.deleteById(userId);
    }
    
    @Auditable(action = "UPDATE_ROLE", resourceType = "User", includeResult = true)
    public UserDto updateUserRole(Long userId, String newRole) {
        // 記錄角色變更的詳細信息
        // ...
    }
}
```

#### 3. 全類性能追蹤

```java
@Service
@TrackPerformance(warnThreshold = 2000)  // 類級別註解
public class DataProcessingService {
    // 此類中所有 public 方法都會被自動監控
    
    public void processData() {
        // 自動追蹤
    }
    
    public void analyzeResults() {
        // 自動追蹤
    }
}
```

## 🧪 測試

### 運行測試

```bash
# 運行所有測試
mvn test

# 運行特定測試類
mvn test -Dtest=UserServiceTest

# 生成測試報告
mvn test jacoco:report
```

### 整合測試範例

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class UserIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    @Order(1)
    void createUser_ShouldReturnSuccess() {
        UserDto userDto = UserDto.builder()
            .username("testuser")
            .email("test@example.com")
            .build();
            
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
            "/api/users", userDto, ApiResponse.class);
            
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSuccess()).isTrue();
    }
}
```

## 🔧 自定義配置

### 添加新的配置類

```java
@Configuration
@EnableConfigurationProperties(CustomProperties.class)
public class CustomConfig {
    
    @Bean
    public CustomService customService(CustomProperties properties) {
        return new CustomService(properties);
    }
}
```

### 添加新的異常類型

```java
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

// 在 GlobalExceptionHandler 中添加處理
@ExceptionHandler(ValidationException.class)
public ResponseEntity<ApiResponse<Object>> handleValidationException(ValidationException ex) {
    // 異常處理邏輯
}
```

## 📦 部署

### Docker 部署

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/spring-mvc-template-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Kubernetes 部署

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-mvc-template
spec:
  replicas: 3
  selector:
    matchLabels:
      app: spring-mvc-template
  template:
    metadata:
      labels:
        app: spring-mvc-template
    spec:
      containers:
      - name: app
        image: spring-mvc-template:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
```

## 🤝 貢獻指南

1. Fork 此專案
2. 創建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 創建 Pull Request

## 📄 許可證

此專案採用 MIT 許可證 - 查看 [LICENSE](LICENSE) 文件了解詳情。

## 💡 最佳實踐建議

1. **遵循分層架構** - 各層職責分明
2. **使用統一異常處理** - 提供一致的錯誤回應
3. **實施緩存策略** - 提升查詢性能
4. **編寫完整測試** - 單元測試 + 整合測試
5. **使用 DTO 模式** - 避免直接暴露實體
6. **版本控制 API** - 支援向後兼容
7. **記錄關鍵日誌** - 便於問題排查
8. **遵循 RESTful API 設計原則**
9. **使用適當的 HTTP 狀態碼**
10. **遵循安全最佳實踐**