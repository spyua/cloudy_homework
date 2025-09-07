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

## 📚 完整上手教學指南

### 🎯 教學目標

本指南將幫助您：
1. **理解模板架構** - 掌握企業級 Spring Boot 設計模式
2. **快速創建新專案** - 基於模板建立您的業務應用
3. **實現自定義功能** - 遵循最佳實踐添加業務邏輯
4. **部署到生產環境** - 企業級部署配置

### 🚀 階段一：模板探索 (20 分鐘)

#### 步驟 1：啟動並驗證模板

```bash
# 1. 克隆並進入專案目錄
cd projects/spring-mvc-template

# 2. 編譯驗證
mvn clean compile

# 3. 執行測試確保一切正常
mvn test

# 4. 啟動應用
mvn spring-boot:run
```

#### 步驟 2：探索核心功能

**A. 驗證健康檢查**
```bash
curl http://localhost:8080/api/actuator/health
```
預期回應：`{"status":"UP"}`

**B. 查看自動生成的 API 文檔**
- 瀏覽器訪問：http://localhost:8080/api/swagger-ui.html
- 這裡展示了所有可用的 API 端點規範

**C. 查看資料庫控制台**
- 訪問：http://localhost:8080/api/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- 用戶名：`sa`，密碼：空

**D. 監控指標查看**
```bash
# JVM 記憶體使用
curl http://localhost:8080/api/actuator/metrics/jvm.memory.used

# HTTP 請求統計
curl http://localhost:8080/api/actuator/metrics/http.server.requests
```

#### 步驟 3：理解 AOP 功能 

觀察控制台日誌，您會看到：
- `🚀 API START` - 請求開始日誌
- `✓ API SUCCESS` - 請求成功日誌  
- `📊 PERFORMANCE` - 性能監控日誌

### 🔨 階段二：基於模板創建新專案 (30 分鐘)

#### 步驟 1：專案結構複製與重新命名

```bash
# 1. 創建新專案目錄
cp -r spring-mvc-template my-awesome-project
cd my-awesome-project

# 2. 更新 pom.xml
# 修改以下內容：
# - <artifactId>spring-mvc-template</artifactId> → <artifactId>my-awesome-project</artifactId>
# - <name>Spring MVC Template</name> → <name>My Awesome Project</name>
```

#### 步驟 2：重新命名套件結構

```bash
# 1. 重新命名主要套件
# 從 com.template 改為 com.mycompany.awesome
mkdir -p src/main/java/com/mycompany/awesome
cp -r src/main/java/com/template/* src/main/java/com/mycompany/awesome/
rm -rf src/main/java/com/template

# 2. 批量替換套件引用
find . -name "*.java" -exec sed -i 's/com\.template/com.mycompany.awesome/g' {} \;
```

#### 步驟 3：更新配置文件

**A. application.yml**
```yaml
server:
  servlet:
    context-path: /api

spring:
  application:
    name: my-awesome-project  # 更新應用名稱
    
logging:
  level:
    com.mycompany.awesome: DEBUG  # 更新套件路徑
```

**B. 更新 Application.java**
```java
package com.mycompany.awesome;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### 步驟 4：驗證新專案

```bash
# 編譯新專案
mvn clean compile

# 執行測試
mvn test

# 啟動應用
mvn spring-boot:run
```

### 🎯 階段三：實現第一個業務功能 (45 分鐘)

讓我們實現一個**部門管理**功能，展示完整的開發流程：

#### 步驟 1：創建部門實體

```java
// src/main/java/com/mycompany/awesome/entity/Department.java
package com.mycompany.awesome.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class Department extends BaseEntity {
    
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(length = 50)
    private String managerEmail;
    
    @Column
    private Integer employeeCount = 0;
}
```

#### 步驟 2：創建資料存取層

```java
// src/main/java/com/mycompany/awesome/repository/DepartmentRepository.java
package com.mycompany.awesome.repository;

import com.mycompany.awesome.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // 根據名稱查找
    Optional<Department> findByName(String name);
    
    // 檢查名稱是否存在
    boolean existsByName(String name);
    
    // 查找活躍的部門
    List<Department> findByActiveTrue();
    
    // 分頁查找活躍部門
    Page<Department> findByActiveTrue(Pageable pageable);
    
    // 根據名稱搜索（模糊查詢）
    @Query("SELECT d FROM Department d WHERE d.name LIKE %:keyword% OR d.description LIKE %:keyword%")
    Page<Department> searchDepartments(String keyword, Pageable pageable);
    
    // 統計活躍部門數量
    @Query("SELECT COUNT(d) FROM Department d WHERE d.active = true")
    Long countActiveDepartments();
}
```

#### 步驟 3：創建 DTO 類

```java
// src/main/java/com/mycompany/awesome/dto/DepartmentDto.java
package com.mycompany.awesome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    
    private Long id;
    
    @NotBlank(message = "部門名稱不能為空")
    @Size(min = 2, max = 100, message = "部門名稱長度應在 2-100 字符之間")
    private String name;
    
    @Size(max = 500, message = "部門描述不能超過 500 字符")
    private String description;
    
    private Boolean active;
    
    @Email(message = "請提供有效的管理員 Email")
    private String managerEmail;
    
    @Min(value = 0, message = "員工數量不能為負數")
    private Integer employeeCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
```

```java
// src/main/java/com/mycompany/awesome/dto/CreateDepartmentRequest.java
package com.mycompany.awesome.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateDepartmentRequest {
    
    @NotBlank(message = "部門名稱不能為空")
    @Size(min = 2, max = 100, message = "部門名稱長度應在 2-100 字符之間")
    private String name;
    
    @Size(max = 500, message = "部門描述不能超過 500 字符")
    private String description;
    
    @Email(message = "請提供有效的管理員 Email")
    private String managerEmail;
    
    @Min(value = 0, message = "員工數量不能為負數")
    private Integer employeeCount = 0;
}
```

#### 步驟 4：創建 MapStruct 映射器

```java
// src/main/java/com/mycompany/awesome/mapper/DepartmentMapper.java
package com.mycompany.awesome.mapper;

import com.mycompany.awesome.dto.CreateDepartmentRequest;
import com.mycompany.awesome.dto.DepartmentDto;
import com.mycompany.awesome.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    
    // Entity → DTO
    DepartmentDto toDto(Department department);
    
    // DTO → Entity  
    Department toEntity(DepartmentDto departmentDto);
    
    // CreateRequest → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "active", constant = "true")
    Department toEntity(CreateDepartmentRequest request);
    
    // List 轉換
    List<DepartmentDto> toDtoList(List<Department> departments);
    
    // 更新實體（保留不變的欄位）
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(DepartmentDto dto, @MappingTarget Department entity);
}
```

#### 步驟 5：實現業務邏輯層

```java
// src/main/java/com/mycompany/awesome/service/DepartmentService.java
package com.mycompany.awesome.service;

import com.mycompany.awesome.annotation.Auditable;
import com.mycompany.awesome.annotation.TrackPerformance;
import com.mycompany.awesome.dto.CreateDepartmentRequest;
import com.mycompany.awesome.dto.DepartmentDto;
import com.mycompany.awesome.dto.PageResponse;
import com.mycompany.awesome.entity.Department;
import com.mycompany.awesome.exception.BusinessException;
import com.mycompany.awesome.exception.ResourceNotFoundException;
import com.mycompany.awesome.mapper.DepartmentMapper;
import com.mycompany.awesome.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    
    // 創建部門
    @Transactional
    @Auditable(action = "CREATE_DEPARTMENT", resourceType = "Department")
    public DepartmentDto createDepartment(CreateDepartmentRequest request) {
        log.info("創建新部門：{}", request.getName());
        
        // 檢查部門名稱是否已存在
        if (departmentRepository.existsByName(request.getName())) {
            throw new BusinessException("部門名稱已存在：" + request.getName());
        }
        
        Department department = departmentMapper.toEntity(request);
        Department savedDepartment = departmentRepository.save(department);
        
        log.info("部門創建成功，ID：{}", savedDepartment.getId());
        return departmentMapper.toDto(savedDepartment);
    }
    
    // 根據 ID 查找部門（使用緩存）
    @Cacheable(value = "departments", key = "#id")
    @TrackPerformance
    public DepartmentDto getDepartmentById(Long id) {
        log.debug("查找部門 ID：{}", id);
        
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        
        return departmentMapper.toDto(department);
    }
    
    // 查找所有活躍部門
    @Cacheable(value = "activeDepartments")
    public List<DepartmentDto> getActiveDepartments() {
        log.debug("查找所有活躍部門");
        
        List<Department> departments = departmentRepository.findByActiveTrue();
        return departmentMapper.toDtoList(departments);
    }
    
    // 分頁查詢部門
    @TrackPerformance(warnThreshold = 1000)
    public PageResponse<DepartmentDto> getAllDepartments(Pageable pageable) {
        log.debug("分頁查詢部門：{}", pageable);
        
        Page<Department> departmentPage = departmentRepository.findAll(pageable);
        List<DepartmentDto> dtoList = departmentMapper.toDtoList(departmentPage.getContent());
        
        return PageResponse.<DepartmentDto>builder()
                .content(dtoList)
                .pageNumber(departmentPage.getNumber())
                .pageSize(departmentPage.getSize())
                .totalElements(departmentPage.getTotalElements())
                .totalPages(departmentPage.getTotalPages())
                .first(departmentPage.isFirst())
                .last(departmentPage.isLast())
                .hasNext(departmentPage.hasNext())
                .hasPrevious(departmentPage.hasPrevious())
                .build();
    }
    
    // 搜索部門
    public PageResponse<DepartmentDto> searchDepartments(String keyword, Pageable pageable) {
        log.debug("搜索部門關鍵字：{}", keyword);
        
        Page<Department> departmentPage;
        
        if (StringUtils.hasText(keyword)) {
            departmentPage = departmentRepository.searchDepartments(keyword, pageable);
        } else {
            departmentPage = departmentRepository.findAll(pageable);
        }
        
        List<DepartmentDto> dtoList = departmentMapper.toDtoList(departmentPage.getContent());
        
        return PageResponse.<DepartmentDto>builder()
                .content(dtoList)
                .pageNumber(departmentPage.getNumber())
                .pageSize(departmentPage.getSize())
                .totalElements(departmentPage.getTotalElements())
                .totalPages(departmentPage.getTotalPages())
                .first(departmentPage.isFirst())
                .last(departmentPage.isLast())
                .hasNext(departmentPage.hasNext())
                .hasPrevious(departmentPage.hasPrevious())
                .build();
    }
    
    // 更新部門
    @Transactional
    @CachePut(value = "departments", key = "#id")
    @Auditable(action = "UPDATE_DEPARTMENT", resourceType = "Department", includeArgs = true)
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        log.info("更新部門 ID：{}", id);
        
        Department existingDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        
        // 檢查名稱衝突（排除自己）
        if (!existingDepartment.getName().equals(departmentDto.getName()) 
            && departmentRepository.existsByName(departmentDto.getName())) {
            throw new BusinessException("部門名稱已存在：" + departmentDto.getName());
        }
        
        departmentMapper.updateEntity(departmentDto, existingDepartment);
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        
        log.info("部門更新成功 ID：{}", id);
        return departmentMapper.toDto(updatedDepartment);
    }
    
    // 刪除部門（軟刪除 - 設為非活躍）
    @Transactional
    @CacheEvict(value = {"departments", "activeDepartments"}, key = "#id")
    @Auditable(action = "DELETE_DEPARTMENT", resourceType = "Department")
    public void deactivateDepartment(Long id) {
        log.info("停用部門 ID：{}", id);
        
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        
        department.setActive(false);
        departmentRepository.save(department);
        
        log.info("部門已停用 ID：{}", id);
    }
    
    // 獲取統計資訊
    @TrackPerformance
    public Object getDepartmentStatistics() {
        log.debug("獲取部門統計資訊");
        
        Long activeDepartmentCount = departmentRepository.countActiveDepartments();
        Long totalDepartmentCount = departmentRepository.count();
        
        return java.util.Map.of(
            "active", activeDepartmentCount,
            "total", totalDepartmentCount,
            "inactive", totalDepartmentCount - activeDepartmentCount
        );
    }
}
```

#### 步驟 6：創建控制器層

```java
// src/main/java/com/mycompany/awesome/controller/DepartmentController.java
package com.mycompany.awesome.controller;

import com.mycompany.awesome.dto.*;
import com.mycompany.awesome.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "部門管理 API")
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    @PostMapping
    @Operation(summary = "創建部門", description = "創建新的部門")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "部門創建成功",
            content = @Content(schema = @Schema(implementation = DepartmentDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "請求參數無效"),
        @ApiResponse(responseCode = "409", description = "部門名稱已存在")
    })
    public ResponseEntity<com.mycompany.awesome.dto.ApiResponse<DepartmentDto>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {
        
        log.info("創建部門請求：{}", request.getName());
        DepartmentDto createdDepartment = departmentService.createDepartment(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(com.mycompany.awesome.dto.ApiResponse.success("部門創建成功", createdDepartment));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根據 ID 查找部門", description = "根據部門 ID 查找部門詳細資訊")
    public ResponseEntity<com.mycompany.awesome.dto.ApiResponse<DepartmentDto>> getDepartmentById(
            @Parameter(description = "部門 ID", required = true)
            @PathVariable Long id) {
        
        log.debug("查找部門 ID：{}", id);
        DepartmentDto department = departmentService.getDepartmentById(id);
        
        return ResponseEntity.ok(com.mycompany.awesome.dto.ApiResponse.success(department));
    }
    
    @GetMapping("/active")
    @Operation(summary = "獲取活躍部門", description = "獲取所有活躍狀態的部門列表")
    public ResponseEntity<com.mycompany.awesome.dto.ApiResponse<List<DepartmentDto>>> getActiveDepartments() {
        
        log.debug("獲取活躍部門列表");
        List<DepartmentDto> activeDepartments = departmentService.getActiveDepartments();
        
        return ResponseEntity.ok(com.mycompany.awesome.dto.ApiResponse.success(activeDepartments));
    }
    
    @GetMapping
    @Operation(summary = "分頁查詢部門", description = "分頁查詢所有部門，支援排序")
    public ResponseEntity<com.mycompany.awesome.dto.ApiResponse<PageResponse<DepartmentDto>>> getAllDepartments(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        log.debug("分頁查詢部門：{}", pageable);
        PageResponse<DepartmentDto> departments = departmentService.getAllDepartments(pageable);
        
        return ResponseEntity.ok(com.mycompany.awesome.dto.ApiResponse.success(departments));
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索部門", description = "根據關鍵字搜索部門名稱或描述")
    public ResponseEntity<com.mycompany.awesome.dto.ApiResponse<PageResponse<DepartmentDto>>> searchDepartments(
            @Parameter(description = "搜索關鍵字")
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        log.debug("搜索部門關鍵字：{}", keyword);
        PageResponse<DepartmentDto> departments = departmentService.searchDepartments(keyword, pageable);
        
        return ResponseEntity.ok(com.mycompany.awesome.dto.ApiResponse.success(departments));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新部門", description = "更新部門詳細資訊")
    public ResponseEntity<com.mycompany.awesome.dto.ApiResponse<DepartmentDto>> updateDepartment(
            @Parameter(description = "部門 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDto departmentDto) {
        
        log.info("更新部門 ID：{}", id);
        DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentDto);
        
        return ResponseEntity.ok(com.mycompany.awesome.dto.ApiResponse.success("部門更新成功", updatedDepartment));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "停用部門", description = "停用指定的部門（軟刪除）")
    public ResponseEntity<com.mycompany.awesome.dto.ApiResponse<Void>> deactivateDepartment(
            @Parameter(description = "部門 ID", required = true)
            @PathVariable Long id) {
        
        log.info("停用部門 ID：{}", id);
        departmentService.deactivateDepartment(id);
        
        return ResponseEntity.ok(com.mycompany.awesome.dto.ApiResponse.success("部門已成功停用", null));
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "獲取部門統計", description = "獲取部門數量統計資訊")
    public ResponseEntity<com.mycompany.awesome.dto.ApiResponse<Object>> getDepartmentStatistics() {
        
        log.debug("獲取部門統計資訊");
        Object statistics = departmentService.getDepartmentStatistics();
        
        return ResponseEntity.ok(com.mycompany.awesome.dto.ApiResponse.success("統計資訊獲取成功", statistics));
    }
}
```

#### 步驟 7：測試新功能

```bash
# 1. 重新啟動應用
mvn spring-boot:run

# 2. 創建部門
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "技術部",
    "description": "負責公司技術研發工作",
    "managerEmail": "tech@company.com",
    "employeeCount": 15
  }'

# 3. 查詢部門
curl http://localhost:8080/api/departments/1

# 4. 獲取活躍部門列表
curl http://localhost:8080/api/departments/active

# 5. 分頁查詢
curl "http://localhost:8080/api/departments?page=0&size=5"

# 6. 搜索部門
curl "http://localhost:8080/api/departments/search?keyword=技術"

# 7. 查看統計
curl http://localhost:8080/api/departments/statistics
```

### 🎓 階段四：進階主題與生產部署 (30 分鐘)

#### 步驟 1：配置生產環境

**A. 創建生產配置文件 - application-prod.yml**
```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myawesome_db
    username: ${DB_USERNAME:myawesome_user}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      max-lifetime: 1200000
      
  jpa:
    hibernate:
      ddl-auto: validate  # 生產環境只驗證，不自動創建
    show-sql: false
    properties:
      hibernate:
        format_sql: false
        
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD}
    timeout: 2000ms
    jedis:
      pool:
        max-active: 100
        max-idle: 50
        min-idle: 10

logging:
  level:
    root: INFO
    com.mycompany.awesome: INFO
  file:
    name: logs/application.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
```

**B. Docker 容器化部署**

創建 Dockerfile：
```dockerfile
# Dockerfile
FROM openjdk:21-jdk-slim

# 設置工作目錄
WORKDIR /app

# 複製 JAR 文件
COPY target/my-awesome-project-1.0.0-SNAPSHOT.jar app.jar

# 設置 JVM 參數
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

# 暴露端口
EXPOSE 8080

# 健康檢查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# 啟動應用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

創建 docker-compose.yml：
```yaml
# docker-compose.yml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_USERNAME=myawesome_user
      - DB_PASSWORD=${DB_PASSWORD}
      - REDIS_HOST=redis
      - REDIS_PASSWORD=${REDIS_PASSWORD}
    depends_on:
      - postgres
      - redis
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/api/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
    restart: unless-stopped

  postgres:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=myawesome_db
      - POSTGRES_USER=myawesome_user
      - POSTGRES_PASSWORD=${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    restart: unless-stopped

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    command: redis-server --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis_data:/data
    restart: unless-stopped

volumes:
  postgres_data:
  redis_data:
```

#### 步驟 2：性能監控與日誌

**A. 添加性能監控**

創建自定義監控端點：
```java
// src/main/java/com/mycompany/awesome/controller/MonitoringController.java
@RestController
@RequestMapping("/actuator/custom")
@Tag(name = "Monitoring", description = "自定義監控端點")
public class MonitoringController {
    
    private final DepartmentService departmentService;
    private final MeterRegistry meterRegistry;
    
    @GetMapping("/business-metrics")
    public ResponseEntity<Map<String, Object>> getBusinessMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // 業務指標
        Object departmentStats = departmentService.getDepartmentStatistics();
        metrics.put("departments", departmentStats);
        
        // 系統指標
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        metrics.put("memory", Map.of(
            "total", totalMemory,
            "used", totalMemory - freeMemory,
            "free", freeMemory
        ));
        
        return ResponseEntity.ok(metrics);
    }
}
```

**B. 配置結構化日誌**

logback-spring.xml 配置：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    
    <!-- 控制台輸出 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <mdc/>
                <message/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>
    
    <!-- 文件輸出 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/application-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>100MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <mdc/>
                <message/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

#### 步驟 3：安全性強化

**A. 添加 API 安全配置**
```java
// 更新 SecurityConfig.java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/departments/active").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .headers(headers -> headers
                .frameOptions().deny()
                .contentTypeOptions().and()
                .httpStrictTransportSecurity(hsts -> hsts
                    .maxAgeInSeconds(31536000)
                    .includeSubdomains(true)
                )
            );
            
        return http.build();
    }
}
```

**B. 環境變數管理**

創建 .env.example 文件：
```bash
# Database Configuration
DB_PASSWORD=your_secure_database_password

# Redis Configuration  
REDIS_PASSWORD=your_secure_redis_password

# Application Security
JWT_SECRET=your-256-bit-secret-key-for-jwt-tokens

# External Service API Keys (if needed)
EXTERNAL_API_KEY=your_external_service_api_key
```

#### 步驟 4：CI/CD 部署流程

**A. GitHub Actions 配置**

創建 `.github/workflows/deploy.yml`：
```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'
        
    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        restore-keys: ${{ runner.os }}-m2
        
    - name: Run tests
      run: mvn clean test
      
    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Maven Tests
        path: target/surefire-reports/*.xml
        reporter: java-junit

  build:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'
        
    - name: Build with Maven
      run: mvn clean package -DskipTests
      
    - name: Build Docker image
      run: |
        docker build -t myawesome-app:${{ github.sha }} .
        docker tag myawesome-app:${{ github.sha }} myawesome-app:latest
        
    - name: Deploy to production
      run: |
        # 這裡添加您的部署邏輯
        # 例如：推送到 Docker registry、部署到 Kubernetes 等
        echo "Deploying to production..."
```

#### 步驟 5：監控和警報設置

**A. Prometheus 指標**

添加自定義指標：
```java
// src/main/java/com/mycompany/awesome/config/MetricsConfig.java
@Configuration
public class MetricsConfig {
    
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
    
    @Bean
    public CounterService counterService(MeterRegistry registry) {
        return new CounterService(registry);
    }
}

// 業務指標服務
@Service
public class CounterService {
    private final Counter departmentCreatedCounter;
    private final Timer departmentQueryTimer;
    
    public CounterService(MeterRegistry registry) {
        this.departmentCreatedCounter = Counter.builder("departments.created")
            .description("Number of departments created")
            .register(registry);
            
        this.departmentQueryTimer = Timer.builder("departments.query.time")
            .description("Time taken to query departments")
            .register(registry);
    }
    
    public void incrementDepartmentCreated() {
        departmentCreatedCounter.increment();
    }
    
    public Timer.Sample startTimer() {
        return Timer.start();
    }
}
```

**B. 健康檢查端點**

自定義健康檢查：
```java
// src/main/java/com/mycompany/awesome/health/CustomHealthIndicator.java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    private final DepartmentRepository departmentRepository;
    
    @Override
    public Health health() {
        try {
            // 檢查資料庫連接
            long count = departmentRepository.count();
            
            return Health.up()
                .withDetail("database", "Available")
                .withDetail("department.count", count)
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("database", "Unavailable")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

#### 步驟 6：負載測試

創建簡單的負載測試腳本：
```bash
#!/bin/bash
# load_test.sh

echo "開始負載測試..."

# 創建部門的並發測試
for i in {1..100}; do
  curl -X POST http://localhost:8080/api/departments \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"部門$i\",
      \"description\": \"測試部門$i\",
      \"managerEmail\": \"manager$i@company.com\",
      \"employeeCount\": $((RANDOM % 50 + 1))
    }" &
done

wait

echo "創建完成，開始查詢測試..."

# 查詢測試
for i in {1..200}; do
  curl -s "http://localhost:8080/api/departments/active" > /dev/null &
  curl -s "http://localhost:8080/api/departments?page=0&size=10" > /dev/null &
done

wait

echo "負載測試完成！"
```

### 🏆 完成效果驗證

完成所有步驟後，您將擁有：

1. **生產就緒的應用** - 具備企業級配置和安全性
2. **完整的部門管理系統** - CRUD、搜索、分頁、統計功能
3. **監控和日誌** - 結構化日誌、性能指標、健康檢查
4. **容器化部署** - Docker + Docker Compose 支持
5. **CI/CD 流程** - 自動化測試和部署
6. **AOP 功能體驗** - 審計日誌、性能監控實際運行

### 📈 進階學習建議

1. **微服務拆分** - 學習如何將單體應用拆分為微服務
2. **分布式追蹤** - 集成 Zipkin 或 Jaeger
3. **事件驅動架構** - 使用 Spring Cloud Stream
4. **API 網關** - 集成 Spring Cloud Gateway
5. **服務發現** - 使用 Consul 或 Eureka

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