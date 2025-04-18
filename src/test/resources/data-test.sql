-- Test users
INSERT INTO users (id, email, first_name, last_name)
VALUES (1, 'test.user1@example.com', 'Test', 'User1');

INSERT INTO users (id, email, first_name, last_name)
VALUES (2, 'test.user2@example.com', 'Test', 'User2');

-- Test operations for User 1
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (1, 'DEPOSIT', 100.50, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (2, 'WITHDRAWAL', 50.00, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (3, 'TRANSFER', 200.75, 1);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (4, 'PURCHASE', 75.00, 1);

-- Test operations for User 2
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (5, 'DEPOSIT', 300.00, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (6, 'TOP_UP', 120.00, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (7, 'PAYMENT', 45.60, 2);
INSERT INTO operations (id, operation_name, amount, user_id) VALUES (8, 'REFUND', 30.20, 2);
