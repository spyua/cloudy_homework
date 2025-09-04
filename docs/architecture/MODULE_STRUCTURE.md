# 模組結構規劃 - 微服務架構設計

## 模組設計原則

### 1. 單一職責原則 (Single Responsibility Principle)
每個模組專注於單一業務領域，避免功能混雜和過度耦合。

### 2. 最小化依賴 (Minimal Dependencies)
- 減少模組間的直接依賴關係
- 使用事件驅動通信取代直接方法調用
- 共享程式碼抽取為獨立的公共庫

### 3. 限界上下文 (Bounded Context)
基於 DDD 概念，每個模組代表一個明確的業務邊界。

## 整體模組架構

```
cloudy-platform/
├── cloudy-common/              # 共享公共庫
├── cloudy-auth-service/        # 認證授權服務
├── cloudy-file-service/        # 檔案管理服務
├── cloudy-processing-service/  # 檔案處理服務
├── cloudy-notification-service/ # 通知服務
├── cloudy-gateway/             # API 閘道
├── cloudy-config/              # 配置管理
└── cloudy-infrastructure/      # 基礎設施配置
```

## 1. 公共模組 (cloudy-common)

### 職責範圍
- 跨服務的共享程式碼
- 通用工具類和常數
- 共享的資料傳輸物件
- 統一的異常處理

### 模組結構
```
cloudy-common/
├── src/main/java/com/cloudy/common/
│   ├── dto/                    # 共享 DTO
│   │   ├── ApiResponse.java
│   │   ├── PageRequest.java
│   │   └── PageResponse.java
│   ├── exception/              # 統一異常處理
│   │   ├── BusinessException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── ValidationException.java
│   ├── util/                   # 工具類
│   │   ├── DateUtil.java
│   │   ├── JsonUtil.java
│   │   └── ValidationUtil.java
│   ├── constant/               # 常數定義
│   │   ├── ErrorCodes.java
│   │   └── MessageConstants.java
│   └── config/                 # 通用配置
│       ├── JacksonConfig.java
│       └── WebMvcConfig.java
├── pom.xml
└── README.md
```

### POM 依賴
```xml
<dependencies>
    <!-- 僅包含最基本的依賴 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
    <dependency>
        <groupId>jakarta.validation</groupId>
        <artifactId>jakarta.validation-api</artifactId>
    </dependency>
</dependencies>
```

## 2. 認證授權服務 (cloudy-auth-service)

### 職責範圍
- 使用者註冊、登入、登出
- JWT Token 生成與驗證
- 權限管理
- 使用者資料管理

### 領域模型
```java
// 聚合根
@Entity
public class User {
    @Id
    private UserId id;
    private Username username;
    private Email email;
    private Password password;  // 值物件
    private Set<Role> roles;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}

// 值物件
public record Username(String value) {
    public Username {
        if (value == null || value.length() < 3) {
            throw new ValidationException("Username must be at least 3 characters");
        }
    }
}
```

### API 設計
```java
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(
        @Valid @RequestBody RegisterRequest request) {
        // 使用者註冊邏輯
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
        @Valid @RequestBody LoginRequest request) {
        // 使用者登入邏輯
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
        @Valid @RequestBody RefreshTokenRequest request) {
        // Token 刷新邏輯
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
        HttpServletRequest request) {
        // 使用者登出邏輯
    }
}
```

### 依賴關係
```xml
<dependencies>
    <dependency>
        <groupId>com.cloudy</groupId>
        <artifactId>cloudy-common</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
    </dependency>
</dependencies>
```

## 3. 檔案管理服務 (cloudy-file-service)

### 職責範圍
- 檔案上傳、下載、刪除
- 檔案元數據管理
- 檔案存取權限控制
- 檔案分享功能

### 領域模型
```java
@Entity
public class FileMetadata {
    @Id
    private FileId id;
    private UserId ownerId;
    private FileName fileName;
    private FileSize fileSize;
    private ContentType contentType;
    private StoragePath storagePath;
    private FileStatus status;
    private LocalDateTime uploadedAt;
    private LocalDateTime lastModifiedAt;
}

@Entity
public class FileShare {
    @Id
    private FileShareId id;
    private FileId fileId;
    private UserId sharedBy;
    private ShareToken shareToken;
    private SharePermission permission;
    private LocalDateTime expiresAt;
    private boolean isActive;
}
```

### 事件發佈
```java
@Service
public class FileUploadService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public FileMetadata uploadFile(UploadFileCommand command) {
        // 檔案上傳邏輯
        FileMetadata fileMetadata = processUpload(command);
        
        // 發佈檔案上傳事件
        eventPublisher.publishEvent(new FileUploadedEvent(
            fileMetadata.getId(),
            fileMetadata.getContentType(),
            fileMetadata.getStoragePath()
        ));
        
        return fileMetadata;
    }
}
```

### API 設計
```java
@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileDto>> uploadFile(
        @RequestParam("file") MultipartFile file,
        Authentication authentication) {
        // 檔案上傳
    }
    
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(
        @PathVariable String fileId,
        Authentication authentication) {
        // 檔案下載
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FileDto>>> listFiles(
        @Valid PageRequest pageRequest,
        Authentication authentication) {
        // 檔案列表
    }
    
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
        @PathVariable String fileId,
        Authentication authentication) {
        // 刪除檔案
    }
}
```

## 4. 檔案處理服務 (cloudy-processing-service)

### 職責範圍
- 圖片壓縮和格式轉換
- 文件預覽生成
- 檔案內容分析
- 異步處理任務管理

### 事件處理
```java
@Component
public class FileProcessingEventListener {
    
    @EventListener
    @Async
    public void handleFileUploadedEvent(FileUploadedEvent event) {
        ProcessingTask task = createProcessingTask(event);
        processFile(task);
    }
    
    private void processFile(ProcessingTask task) {
        try {
            // 執行檔案處理
            ProcessingResult result = fileProcessor.process(task);
            
            // 發佈處理完成事件
            eventPublisher.publishEvent(new FileProcessingCompletedEvent(
                task.getFileId(),
                result
            ));
            
        } catch (Exception e) {
            // 發佈處理失敗事件
            eventPublisher.publishEvent(new FileProcessingFailedEvent(
                task.getFileId(),
                e.getMessage()
            ));
        }
    }
}
```

### 處理策略模式
```java
public interface FileProcessor {
    boolean supports(ContentType contentType);
    ProcessingResult process(ProcessingTask task);
}

@Component
public class ImageProcessor implements FileProcessor {
    @Override
    public boolean supports(ContentType contentType) {
        return contentType.isImage();
    }
    
    @Override
    public ProcessingResult process(ProcessingTask task) {
        // 圖片壓縮邏輯
    }
}

@Component
public class DocumentProcessor implements FileProcessor {
    @Override
    public boolean supports(ContentType contentType) {
        return contentType.isDocument();
    }
    
    @Override
    public ProcessingResult process(ProcessingTask task) {
        // 文件處理邏輯
    }
}
```

## 5. 通知服務 (cloudy-notification-service)

### 職責範圍
- 郵件通知
- 系統內通知
- 推送通知
- 通知模板管理

### 事件監聽
```java
@Component
public class NotificationEventListener {
    
    @EventListener
    public void handleFileProcessingCompleted(FileProcessingCompletedEvent event) {
        NotificationRequest request = NotificationRequest.builder()
            .userId(event.getOwnerId())
            .type(NotificationType.FILE_PROCESSING_COMPLETED)
            .content(buildNotificationContent(event))
            .build();
            
        notificationService.send(request);
    }
}
```

## 6. API 閘道 (cloudy-gateway)

### 職責範圍
- 路由和負載均衡
- 認證和授權
- 限流和熔斷
- API 版本管理

### 閘道配置
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://cloudy-auth-service
          predicates:
            - Path=/api/v1/auth/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                
        - id: file-service
          uri: lb://cloudy-file-service
          predicates:
            - Path=/api/v1/files/**
          filters:
            - name: CircuitBreaker
              args:
                name: file-service-cb
                fallbackUri: forward:/fallback/files
```

## 7. 模組間通信設計

### 同步通信
僅用於必要的實時操作：
- API Gateway → 各服務
- 檔案下載請求（要求實時響應）

### 異步通信
用於大部分跨服務協作：
```java
// 事件定義
public record FileUploadedEvent(
    String fileId,
    String userId,
    String contentType,
    String storagePath,
    Instant timestamp
) {}

public record FileProcessingCompletedEvent(
    String fileId,
    String processedPath,
    ProcessingResult result,
    Instant timestamp
) {}
```

### 消息路由
```yaml
# RabbitMQ 配置
rabbitmq:
  exchanges:
    file-events: file.events
  queues:
    file-processing: file.processing.queue
    notifications: notifications.queue
  routing-keys:
    file-uploaded: file.uploaded
    file-processed: file.processed
```

## 8. 數據一致性策略

### Saga 模式
用於跨服務的業務流程：
```java
@Component
public class FileUploadSaga {
    
    @SagaOrchestrationStart(FileUploadStartedEvent.class)
    public void handleFileUploadStarted(FileUploadStartedEvent event) {
        // 開始文件上傳流程
        saveFileMetadata(event);
        uploadToStorage(event);
    }
    
    @SagaOrchestrationStep
    public void handleStorageUploadCompleted(StorageUploadCompletedEvent event) {
        // 存儲完成後觸發處理
        triggerFileProcessing(event);
    }
    
    @SagaOrchestrationStep
    public void handleProcessingCompleted(FileProcessingCompletedEvent event) {
        // 處理完成後發送通知
        sendNotification(event);
        updateFileStatus(event);
    }
}
```

## 9. 測試策略

### 單元測試
每個模組內部的單元測試，使用 Mock 隔離外部依賴。

### 集成測試
```java
@SpringBootTest
@Testcontainers
class FileServiceIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
            
    @Container
    static GenericContainer<?> minio = new GenericContainer<>("minio/minio")
            .withExposedPorts(9000)
            .withEnv("MINIO_ROOT_USER", "minioadmin")
            .withEnv("MINIO_ROOT_PASSWORD", "minioadmin");
    
    @Test
    void shouldUploadAndRetrieveFile() {
        // 整合測試邏輯
    }
}
```

### 契約測試
使用 Spring Cloud Contract 確保服務間 API 兼容性。

## 10. 部署隔離

### 獨立部署
每個服務可以獨立部署、擴展和回滾：

```yaml
# Kubernetes Deployment 示例
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cloudy-file-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: cloudy-file-service
  template:
    metadata:
      labels:
        app: cloudy-file-service
    spec:
      containers:
      - name: cloudy-file-service
        image: cloudy/file-service:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
```

這個模組結構確保了清晰的邊界、最小的依賴關係，並支持獨立開發和部署。