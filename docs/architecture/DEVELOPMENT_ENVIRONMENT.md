# 開發環境建置指南

## 1. 本地開發環境設置

### 必要工具安裝
```bash
# Java 21 (推薦使用 SDKMAN)
curl -s "https://get.sdkman.io" | bash
sdk install java 21.0.1-tem
sdk use java 21.0.1-tem

# Maven 3.9+
sdk install maven 3.9.5

# Docker & Docker Compose
# macOS
brew install docker docker-compose

# Ubuntu/Debian
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
sudo apt-get install docker-compose-plugin

# Node.js (用於前端工具)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
nvm install 18
nvm use 18
```

### IDE 配置

#### IntelliJ IDEA 設定
```xml
<!-- .idea/codeStyles/Project.xml -->
<component name="ProjectCodeStyleConfiguration">
  <code_scheme name="Project" version="173">
    <JavaCodeStyleSettings>
      <option name="IMPORT_LAYOUT_TABLE">
        <value>
          <package name="" withSubpackages="true" static="true" />
          <emptyLine />
          <package name="java" withSubpackages="true" static="false" />
          <package name="javax" withSubpackages="true" static="false" />
          <emptyLine />
          <package name="org" withSubpackages="true" static="false" />
          <package name="com" withSubpackages="true" static="false" />
          <emptyLine />
          <package name="" withSubpackages="true" static="false" />
        </value>
      </option>
    </JavaCodeStyleSettings>
  </code_scheme>
</component>
```

#### 必裝外掛
- **Lombok Plugin**: 支援 Lombok 註解
- **MapStruct Support**: MapStruct 代碼生成
- **SonarLint**: 實時代碼質量檢查
- **Docker**: Docker 支援
- **Kubernetes**: K8s 資源管理

#### VS Code 設定
```json
// .vscode/settings.json
{
  "java.home": "/path/to/java21",
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-21",
      "path": "/path/to/java21"
    }
  ],
  "java.compile.nullAnalysis.mode": "automatic",
  "editor.formatOnSave": true,
  "editor.codeActionsOnSave": {
    "source.organizeImports": true
  }
}
```

## 2. 基礎設施服務

### Docker Compose 本地環境
```yaml
# docker-compose.dev.yml
version: '3.8'

services:
  # PostgreSQL 主資料庫
  postgres:
    image: postgres:16-alpine
    container_name: cloudy-postgres
    environment:
      POSTGRES_DB: cloudy_dev
      POSTGRES_USER: cloudy_user
      POSTGRES_PASSWORD: cloudy_password
      PGDATA: /var/lib/postgresql/data/pgdata
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./scripts/init-db.sql:/docker-entrypoint-initdb.d/init-db.sql
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U cloudy_user -d cloudy_dev"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Redis 快取服務
  redis:
    image: redis:7-alpine
    container_name: cloudy-redis
    command: redis-server --appendonly yes --requirepass redis_password
    volumes:
      - redis_data:/data
    ports:
      - "6379:6379"
    healthcheck:
      test: ["CMD", "redis-cli", "--raw", "incr", "ping"]
      interval: 30s
      timeout: 10s
      retries: 3

  # MinIO 物件儲存
  minio:
    image: minio/minio:latest
    container_name: cloudy-minio
    command: server /data --console-address ":9001"
    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin123
    volumes:
      - minio_data:/data
    ports:
      - "9000:9000"  # API
      - "9001:9001"  # Console
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9000/minio/health/live"]
      interval: 30s
      timeout: 10s
      retries: 3

  # RabbitMQ 訊息佇列
  rabbitmq:
    image: rabbitmq:3.12-management-alpine
    container_name: cloudy-rabbitmq
    environment:
      RABBITMQ_DEFAULT_USER: rabbitmq_user
      RABBITMQ_DEFAULT_PASS: rabbitmq_password
      RABBITMQ_DEFAULT_VHOST: cloudy_vhost
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
      - ./config/rabbitmq/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf
    ports:
      - "5672:5672"   # AMQP
      - "15672:15672" # Management UI
    healthcheck:
      test: ["CMD", "rabbitmqctl", "status"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Elasticsearch (可選 - 用於日誌搜尋)
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
    container_name: cloudy-elasticsearch
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - es_data:/usr/share/elasticsearch/data
    ports:
      - "9200:9200"
      - "9300:9300"
    profiles:
      - logging

  # Kibana (可選 - 用於日誌視覺化)
  kibana:
    image: docker.elastic.co/kibana/kibana:8.11.0
    container_name: cloudy-kibana
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch
    profiles:
      - logging

volumes:
  postgres_data:
  redis_data:
  minio_data:
  rabbitmq_data:
  es_data:

networks:
  default:
    name: cloudy-network
```

### 啟動指令
```bash
# 啟動基本服務
docker-compose -f docker-compose.dev.yml up -d

# 啟動包含日誌服務
docker-compose -f docker-compose.dev.yml --profile logging up -d

# 檢查服務狀態
docker-compose -f docker-compose.dev.yml ps

# 查看日誌
docker-compose -f docker-compose.dev.yml logs -f [service-name]

# 停止服務
docker-compose -f docker-compose.dev.yml down
```

## 3. 資料庫初始化

### 資料庫遷移腳本
```sql
-- scripts/init-db.sql
CREATE DATABASE cloudy_auth;
CREATE DATABASE cloudy_files;
CREATE DATABASE cloudy_processing;
CREATE DATABASE cloudy_notifications;

-- 建立使用者和權限
CREATE USER auth_user WITH PASSWORD 'auth_password';
CREATE USER files_user WITH PASSWORD 'files_password';
CREATE USER processing_user WITH PASSWORD 'processing_password';
CREATE USER notifications_user WITH PASSWORD 'notifications_password';

GRANT ALL PRIVILEGES ON DATABASE cloudy_auth TO auth_user;
GRANT ALL PRIVILEGES ON DATABASE cloudy_files TO files_user;
GRANT ALL PRIVILEGES ON DATABASE cloudy_processing TO processing_user;
GRANT ALL PRIVILEGES ON DATABASE cloudy_notifications TO notifications_user;
```

### Flyway 遷移配置
```properties
# application-dev.properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
```

## 4. 專案腳手架生成

### Maven Archetype 創建
```bash
# 創建父專案
mvn archetype:generate \
  -DgroupId=com.cloudy \
  -DartifactId=cloudy-platform \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

# 創建各個模組
cd cloudy-platform

mvn archetype:generate \
  -DgroupId=com.cloudy \
  -DartifactId=cloudy-common \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

mvn archetype:generate \
  -DgroupId=com.cloudy \
  -DartifactId=cloudy-auth-service \
  -DarchetypeArtifactId=maven-archetype-webapp \
  -DinteractiveMode=false
```

### 專案結構腳本
```bash
#!/bin/bash
# scripts/create-project-structure.sh

PROJECT_NAME="cloudy-platform"
BASE_PACKAGE="com.cloudy"

# 創建根目錄
mkdir -p $PROJECT_NAME
cd $PROJECT_NAME

# 創建父 POM
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.cloudy</groupId>
    <artifactId>cloudy-platform</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    
    <modules>
        <module>cloudy-common</module>
        <module>cloudy-auth-service</module>
        <module>cloudy-file-service</module>
        <module>cloudy-processing-service</module>
        <module>cloudy-notification-service</module>
        <module>cloudy-gateway</module>
    </modules>
    
    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>3.2.0</spring-boot.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
    </properties>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
EOF

# 創建各個模組
MODULES=("cloudy-common" "cloudy-auth-service" "cloudy-file-service" 
         "cloudy-processing-service" "cloudy-notification-service" "cloudy-gateway")

for MODULE in "${MODULES[@]}"; do
    mkdir -p $MODULE/src/{main,test}/java/${BASE_PACKAGE//.//}/${MODULE/cloudy-/}
    mkdir -p $MODULE/src/{main,test}/resources
    
    # 創建模組 POM
    cat > $MODULE/pom.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.cloudy</groupId>
        <artifactId>cloudy-platform</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    
    <artifactId>$MODULE</artifactId>
    <packaging>jar</packaging>
</project>
EOF
done

echo "專案結構創建完成！"
```

## 5. 開發工具配置

### Makefile
```makefile
# Makefile
.PHONY: help clean build test package run-dev stop-dev logs

help: ## 顯示幫助訊息
	@echo "可用指令："
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

clean: ## 清理建置檔案
	mvn clean
	docker system prune -f

build: ## 建置專案
	mvn clean compile

test: ## 執行測試
	mvn test

package: ## 打包應用
	mvn clean package -DskipTests

run-dev: ## 啟動開發環境
	docker-compose -f docker-compose.dev.yml up -d
	@echo "等待服務啟動..."
	@sleep 10
	@echo "開發環境已啟動！"
	@echo "資料庫: localhost:5432"
	@echo "Redis: localhost:6379"
	@echo "MinIO: http://localhost:9001"
	@echo "RabbitMQ: http://localhost:15672"

stop-dev: ## 停止開發環境
	docker-compose -f docker-compose.dev.yml down

logs: ## 查看服務日誌
	docker-compose -f docker-compose.dev.yml logs -f

run-auth: ## 啟動認證服務
	cd cloudy-auth-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev

run-files: ## 啟動檔案服務
	cd cloudy-file-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev

run-gateway: ## 啟動閘道服務
	cd cloudy-gateway && mvn spring-boot:run -Dspring-boot.run.profiles=dev

install-hooks: ## 安裝 Git hooks
	cp scripts/pre-commit .git/hooks/
	cp scripts/pre-push .git/hooks/
	chmod +x .git/hooks/pre-commit
	chmod +x .git/hooks/pre-push
```

### Git Hooks
```bash
#!/bin/bash
# scripts/pre-commit
# Git pre-commit hook

echo "執行 pre-commit 檢查..."

# 檢查程式碼格式
echo "檢查程式碼格式..."
mvn spotless:check
if [ $? -ne 0 ]; then
    echo "程式碼格式檢查失敗！執行 'mvn spotless:apply' 修復格式。"
    exit 1
fi

# 執行靜態分析
echo "執行靜態分析..."
mvn spotbugs:check
if [ $? -ne 0 ]; then
    echo "靜態分析發現問題！請查看報告並修復。"
    exit 1
fi

# 執行快速測試
echo "執行單元測試..."
mvn test -Dtest.profile=fast
if [ $? -ne 0 ]; then
    echo "測試失敗！請修復後再提交。"
    exit 1
fi

echo "pre-commit 檢查通過！"
```

## 6. 測試環境設置

### TestContainers 配置
```java
// src/test/java/com/cloudy/common/test/TestContainerConfig.java
@TestConfiguration
public class TestContainerConfig {
    
    @Bean
    @Primary
    public DataSource testDataSource() {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("test_db")
                .withUsername("test_user")
                .withPassword("test_password")
                .withReuse(true);
        
        postgres.start();
        
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
        
        return dataSource;
    }
    
    @Bean
    @Primary
    public RedisConnectionFactory testRedisConnectionFactory() {
        GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
                .withExposedPorts(6379)
                .withReuse(true);
        
        redis.start();
        
        LettuceConnectionFactory factory = new LettuceConnectionFactory(
            redis.getHost(), 
            redis.getMappedPort(6379)
        );
        factory.afterPropertiesSet();
        
        return factory;
    }
}
```

### 測試配置檔案
```yaml
# src/test/resources/application-test.yml
spring:
  profiles:
    active: test
  
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
    driver-class-name: org.h2.Driver
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  redis:
    host: localhost
    port: 6379
  
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

logging:
  level:
    com.cloudy: DEBUG
    org.springframework.web: DEBUG
```

## 7. CI/CD 管道

### GitHub Actions
```yaml
# .github/workflows/ci.yml
name: CI Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

env:
  JAVA_VERSION: '21'
  MAVEN_OPTS: '-Xmx1024m'

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:16-alpine
        env:
          POSTGRES_DB: test_db
          POSTGRES_USER: test_user
          POSTGRES_PASSWORD: test_password
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432
          
      redis:
        image: redis:7-alpine
        options: >-
          --health-cmd "redis-cli ping"
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 6379:6379

    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: ${{ env.JAVA_VERSION }}
        distribution: 'temurin'
        
    - name: Cache Maven dependencies
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        restore-keys: ${{ runner.os }}-m2
        
    - name: Run tests
      run: mvn clean test -B
      
    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Maven Tests
        path: '**/target/surefire-reports/TEST-*.xml'
        reporter: java-junit
        
    - name: Code Coverage
      run: mvn jacoco:report
      
    - name: Upload coverage to Codecov
      uses: codecov/codecov-action@v3
      with:
        file: ./target/site/jacoco/jacoco.xml

  security-scan:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - name: Run OWASP Dependency Check
      uses: dependency-check/Dependency-Check_Action@main
      with:
        project: 'cloudy-platform'
        path: '.'
        format: 'HTML'
        
    - name: Upload security scan results
      uses: actions/upload-artifact@v3
      with:
        name: dependency-check-report
        path: reports/

  build:
    needs: [test, security-scan]
    runs-on: ubuntu-latest
    if: github.event_name == 'push'
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: ${{ env.JAVA_VERSION }}
        distribution: 'temurin'
        
    - name: Build with Maven
      run: mvn clean package -DskipTests
      
    - name: Build Docker images
      run: |
        mvn jib:build -Dimage=cloudy/auth-service:${{ github.sha }} -pl cloudy-auth-service
        mvn jib:build -Dimage=cloudy/file-service:${{ github.sha }} -pl cloudy-file-service
        mvn jib:build -Dimage=cloudy/gateway:${{ github.sha }} -pl cloudy-gateway
```

## 8. 程式碼品質配置

### SpotBugs 配置
```xml
<!-- spotbugs-exclude.xml -->
<FindBugsFilter>
    <Match>
        <Class name="~.*\.dto\..*"/>
    </Match>
    <Match>
        <Class name="~.*Application"/>
        <Method name="main"/>
    </Match>
</FindBugsFilter>
```

### Checkstyle 配置
```xml
<!-- checkstyle.xml -->
<!DOCTYPE module PUBLIC
    "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
    "https://checkstyle.org/dtds/configuration_1_3.dtd">
<module name="Checker">
    <module name="TreeWalker">
        <module name="UnusedImports"/>
        <module name="RedundantImport"/>
        <module name="IllegalImport"/>
        <module name="AvoidStarImport"/>
        <module name="ConstantName"/>
        <module name="LocalFinalVariableName"/>
        <module name="LocalVariableName"/>
        <module name="MemberName"/>
        <module name="MethodName"/>
        <module name="PackageName"/>
        <module name="ParameterName"/>
        <module name="StaticVariableName"/>
        <module name="TypeName"/>
    </module>
</module>
```

## 9. 監控與可觀察性

### 本地監控堆疊
```yaml
# docker-compose.monitoring.yml
version: '3.8'

services:
  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./config/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'

  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    ports:
      - "3000:3000"
    environment:
      GF_SECURITY_ADMIN_PASSWORD: admin
    volumes:
      - ./config/grafana/dashboards:/var/lib/grafana/dashboards
      - ./config/grafana/provisioning:/etc/grafana/provisioning

  jaeger:
    image: jaegertracing/all-in-one:latest
    container_name: jaeger
    ports:
      - "16686:16686"
      - "14268:14268"
    environment:
      COLLECTOR_OTLP_ENABLED: true
```

## 10. 文檔生成

### OpenAPI 配置
```java
@Configuration
@EnableWebMvc
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Cloudy Platform API")
                .version("1.0.0")
                .description("雲端檔案管理平台 API 文檔")
                .contact(new Contact()
                    .name("Cloudy Team")
                    .email("team@cloudy.com")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("開發環境"),
                new Server().url("https://api.cloudy.com").description("生產環境")
            ));
    }
}
```

### 文檔生成腳本
```bash
#!/bin/bash
# scripts/generate-docs.sh

echo "生成 API 文檔..."

# 啟動應用程式
./mvnw spring-boot:run -Dspring-boot.run.profiles=docs &
APP_PID=$!

# 等待應用程式啟動
sleep 30

# 生成 OpenAPI 規範
curl -s http://localhost:8080/v3/api-docs > docs/openapi.json

# 生成 HTML 文檔
npx @redocly/cli build-docs docs/openapi.json --output docs/api.html

# 關閉應用程式
kill $APP_PID

echo "文檔生成完成！查看 docs/api.html"
```

這套完整的開發環境配置為現代 Spring Boot 微服務開發提供了堅實的基礎。