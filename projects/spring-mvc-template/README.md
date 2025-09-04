# Spring MVC Template

現代化的 Spring Boot MVC 模板，集成最佳實踐和常用功能。

## 🚀 功能特色

- ✅ **Spring Boot 3.2** - 最新穩定版本
- ✅ **Java 21** - 最新 LTS 版本
- ✅ **Spring Security 6** - 現代化安全框架
- ✅ **Spring Data JPA** - 數據訪問層
- ✅ **Redis Cache** - 分布式緩存
- ✅ **Swagger/OpenAPI 3** - API 文檔
- ✅ **MapStruct** - 對象映射
- ✅ **Lombok** - 減少樣板代碼
- ✅ **統一異常處理** - 全局異常攔截
- ✅ **統一回應格式** - API 回應標準化
- ✅ **TestContainers** - 整合測試
- ✅ **多環境配置** - dev/prod 環境隔離

## 📁 專案結構

```
src/main/java/com/template/
├── Application.java              # 主應用程式類
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

## 💡 最佳實踐

1. **遵循 RESTful API 設計原則**
2. **使用適當的 HTTP 狀態碼**
3. **實施適當的異常處理**
4. **編寫全面的測試用例**
5. **使用緩存提升性能**
6. **實施適當的日誌記錄**
7. **遵循安全最佳實踐**