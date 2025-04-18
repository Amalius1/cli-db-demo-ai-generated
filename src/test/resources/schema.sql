DROP TABLE IF EXISTS operations;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);
ALTER TABLE users ALTER COLUMN id RESTART WITH 4;

CREATE TABLE operations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    operation_name VARCHAR(255),
    amount DECIMAL(19, 2),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
ALTER TABLE operations ALTER COLUMN id RESTART WITH 36;
