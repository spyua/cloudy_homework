-- 插入範例使用者資料
INSERT INTO users (id, username, email, first_name, last_name, phone, date_of_birth, status, bio, created_at, updated_at, version) VALUES 
(1, 'johndoe', 'john.doe@example.com', 'John', 'Doe', '+1234567890', '1990-05-15', 'ACTIVE', 'Software Engineer with 5+ years experience', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(2, 'janedoe', 'jane.doe@example.com', 'Jane', 'Doe', '+1234567891', '1992-08-22', 'ACTIVE', 'Product Manager passionate about user experience', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(3, 'bobsmith', 'bob.smith@example.com', 'Bob', 'Smith', '+1234567892', '1988-12-03', 'ACTIVE', 'DevOps Engineer specializing in cloud infrastructure', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(4, 'alicejohnson', 'alice.johnson@example.com', 'Alice', 'Johnson', '+1234567893', '1995-02-28', 'INACTIVE', 'UI/UX Designer with focus on accessibility', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(5, 'charliebrown', 'charlie.brown@example.com', 'Charlie', 'Brown', '+1234567894', '1987-11-10', 'SUSPENDED', 'Full-stack developer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 重設自增序列
ALTER SEQUENCE users_id_seq RESTART WITH 6;