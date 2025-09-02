# Javadoc 註解建議

## Controller 層

### UserVerifyController.java
```java
/**
 * 使用者認證控制器
 * 
 * 提供使用者登入和註冊的 REST API 端點，整合 JWT 認證機制。
 * 所有認證相關的 HTTP 請求都透過此控制器處理。
 * 
 * @author @TODO: 開發者名稱
 * @version 0.0.1
 * @since 2023-07-01
 */
@RestController
@CrossOrigin
public class UserVerifyController {

    /**
     * 使用者登入認證
     * 
     * 驗證使用者提供的帳號密碼，成功後產生並回傳 JWT Token。
     * 
     * @param userInfoDto 包含使用者帳號和密碼的請求物件
     * @return ResponseEntity 包含 JWT Token 的回應物件
     * @throws Exception 當認證失敗或系統錯誤時拋出例外
     * 
     * @apiNote 端點路徑: POST /login
     * @apiNote 請求格式: application/json
     * @apiNote 回應格式: application/json
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInfoDto userInfoDto) throws Exception {

    /**
     * 新使用者註冊
     * 
     * 建立新的使用者帳戶，密碼會透過 Cloud KMS 進行加密儲存。
     * 
     * @param user 包含新使用者資訊的請求物件
     * @return ResponseEntity 包含新建立帳戶資訊的回應物件
     * @throws Exception 當帳戶已存在或系統錯誤時拋出例外
     * 
     * @apiNote 端點路徑: POST /register
     * @apiNote 請求格式: application/json
     * @apiNote 回應格式: application/json
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserInfoDto user) throws Exception {

    /**
     * 私有認證方法
     * 
     * 使用 Spring Security 的 AuthenticationManager 進行使用者認證。
     * 
     * @param username 使用者名稱
     * @param password 使用者密碼
     * @throws Exception 當使用者被停用或認證失敗時拋出例外
     */
    private void authenticate(String username, String password) throws Exception {
```

## DTO 層

### UserInfoDto.java
```java
/**
 * 使用者資訊資料傳輸物件
 * 
 * 用於前端與後端之間傳遞使用者帳號和密碼資訊，
 * 適用於登入和註冊 API 的請求參數。
 * 
 * @author @TODO: 開發者名稱
 * @version 0.0.1
 * @since 2023-07-01
 */
@Getter
@Setter
public class UserInfoDto {

    /**
     * 使用者帳戶識別碼
     * 
     * 唯一識別使用者的帳戶 ID，用於登入和註冊。
     */
    private String userAccountID;

    /**
     * 使用者密碼
     * 
     * 明文密碼，在傳輸過程中應使用 HTTPS 保護，
     * 儲存時會透過 Cloud KMS 進行加密。
     */
    private String userPassword;
```

### UserInfoRelayDto.java
```java
/**
 * 使用者認證回應資料傳輸物件
 * 
 * 用於回傳 JWT Token 給前端，作為後續 API 呼叫的認證憑證。
 * 
 * @author @TODO: 開發者名稱
 * @version 0.0.1
 * @since 2023-07-01
 */
@Getter
@Setter
public class UserInfoRelayDto {

    /**
     * JWT 認證 Token
     * 
     * 經過數位簽章的 JSON Web Token，包含使用者身份資訊，
     * 有效期限為 5 小時。前端應在 Authorization Header 中使用此 Token。
     */
    String token;

    /**
     * 建構函式
     * 
     * @param token JWT 認證 Token 字串
     */
    public UserInfoRelayDto(String token) {
```

## Application 層

### CloudyAccountApplication.java
```java
/**
 * Cloudy Account Service 主要應用程式啟動類別
 * 
 * Spring Boot 應用程式的進入點，設定元件掃描範圍以包含
 * 相依的 cloudy-security 模組，並啟用 JPA 和安全性功能。
 * 
 * @author @TODO: 開發者名稱
 * @version 0.0.1
 * @since 2023-07-01
 * 
 * @see com.ck.security Spring Security 設定模組
 * @see com.ck.account.controller.UserVerifyController 主要 API 控制器
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ck.*"})
@ComponentScan(basePackages = {"com.ck.*"})
@EntityScan("com.ck.*")
public class CloudyAccountApplication {

    /**
     * 應用程式主要進入點
     * 
     * 啟動 Spring Boot 應用程式，載入所有設定並開始監聽 HTTP 請求。
     * 預設監聽埠為 8080。
     * 
     * @param args 命令列參數
     */
    public static void main(String[] args) {
```

## 建議的程式碼改進

### 1. 錯誤處理改善
建議在 Controller 中新增統一的例外處理器：

```java
/**
 * 全域例外處理器
 * 
 * 統一處理應用程式中的各種例外情況，提供一致的錯誤回應格式。
 * 
 * @author @TODO: 開發者名稱
 * @version 0.0.1
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理認證相關例外
     * 
     * @param ex 認證例外
     * @return 標準化的錯誤回應
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        // 實作邏輯
    }
}
```

### 2. 服務層分離
建議新增服務層來處理業務邏輯：

```java
/**
 * 使用者帳戶服務
 * 
 * 處理使用者帳戶相關的業務邏輯，包括註冊、登入驗證等功能。
 * 將業務邏輯從 Controller 分離，提高程式碼的可維護性。
 * 
 * @author @TODO: 開發者名稱
 * @version 0.0.1
 */
@Service
public class UserAccountService {

    /**
     * 處理使用者登入邏輯
     * 
     * @param userInfoDto 使用者登入資訊
     * @return JWT Token 封裝物件
     * @throws AuthenticationException 當認證失敗時拋出
     */
    public UserInfoRelayDto processLogin(UserInfoDto userInfoDto) throws AuthenticationException {
        // 實作邏輯
    }
}
```

### 3. 資料驗證
建議新增 Bean Validation 註解：

```java
/**
 * 使用者資訊資料傳輸物件（增強版）
 */
public class UserInfoDto {

    /**
     * 使用者帳戶識別碼
     * 
     * @constraint 必填欄位，長度 3-50 字元，僅允許英數字和底線
     */
    @NotBlank(message = "帳戶 ID 不能為空")
    @Size(min = 3, max = 50, message = "帳戶 ID 長度必須在 3-50 字元之間")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "帳戶 ID 僅允許英數字和底線")
    private String userAccountID;

    /**
     * 使用者密碼
     * 
     * @constraint 必填欄位，最少 8 字元，至少包含一個大寫字母、小寫字母和數字
     */
    @NotBlank(message = "密碼不能為空")
    @Size(min = 8, message = "密碼長度至少 8 字元")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", 
             message = "密碼必須包含至少一個大寫字母、小寫字母和數字")
    private String userPassword;
}
```