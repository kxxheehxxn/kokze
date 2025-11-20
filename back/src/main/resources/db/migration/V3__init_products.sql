CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          category VARCHAR(30) NOT NULL,
                          risk_level VARCHAR(20) NOT NULL,
                          interest_rate DECIMAL(5,2),
                          min_balance DECIMAL(15,0),
                          description TEXT
);