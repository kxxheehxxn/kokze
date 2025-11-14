CREATE TABLE IF NOT EXISTS users (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  email          VARCHAR(255) NOT NULL,
  name           VARCHAR(100) NOT NULL,
  role           VARCHAR(50)  NULL,
  provider       VARCHAR(50)  NULL,
  password_hash  VARCHAR(255) NULL,
  phone          VARCHAR(30)  NULL,
  active         TINYINT(1)   NOT NULL DEFAULT 1,
  last_login_at  TIMESTAMP    NULL,
  created_at     TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  tendency       varchar(50)  NOT NULL ,
  UNIQUE KEY ux_users_email (email),
  KEY ix_users_last_login_at (last_login_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;