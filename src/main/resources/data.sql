INSERT INTO users (id, email, first_name, last_name)
VALUES (1, 'john.doe@example.com', 'John', 'Doe');

INSERT INTO users (id, email, first_name, last_name)
VALUES (2, 'jane.smith@example.com', 'Jane', 'Smith');

INSERT INTO users (id, email, first_name, last_name)
VALUES (3, 'bob.wilson@example.com', 'Bob', 'Wilson');


-- Original operations
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (1, 'DEPOSIT', 100.50, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (2, 'WITHDRAWAL', 50.00, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (3, 'TRANSFER', 200.75, 3);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (4, 'PURCHASE', 75.00, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (5, 'REFUND', 30.20, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (6, 'TOP_UP', 120.00, 3);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (7, 'PAYMENT', 45.60, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (8, 'DEPOSIT', 300.00, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (9, 'WITHDRAWAL', 85.10, 3);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (10, 'TRANSFER', 150.75, 1);

-- Additional Deposit operations
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (11, 'DEPOSIT', 500.25, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (12, 'DEPOSIT', 750.00, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (13, 'DEPOSIT', 1200.50, 3);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (14, 'DEPOSIT', 325.75, 1);

-- Additional Withdrawal operations
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (15, 'WITHDRAWAL', 200.00, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (16, 'WITHDRAWAL', 150.50, 3);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (17, 'WITHDRAWAL', 75.25, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (18, 'WITHDRAWAL', 300.00, 2);

-- Additional Transfer operations
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (19, 'TRANSFER', 450.00, 3);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (20, 'TRANSFER', 275.50, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (21, 'TRANSFER', 180.25, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (22, 'TRANSFER', 550.00, 3);

-- Additional Purchase operations
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (23, 'PURCHASE', 120.75, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (24, 'PURCHASE', 89.99, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (25, 'PURCHASE', 199.50, 3);

-- Additional Refund operations
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (26, 'REFUND', 45.50, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (27, 'REFUND', 89.99, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (28, 'REFUND', 25.00, 3);

-- Additional Top-Up operations
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (29, 'TOP_UP', 50.00, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (30, 'TOP_UP', 100.00, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (31, 'TOP_UP', 75.50, 3);

-- Additional Payment operations
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (32, 'PAYMENT', 125.00, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (33, 'PAYMENT', 75.25, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (34, 'PAYMENT', 200.00, 3);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (35, 'PAYMENT', 150.50, 1);
