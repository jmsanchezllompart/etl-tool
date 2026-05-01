CREATE TABLE order_items
(
    user_id       INT,
    user_email    TEXT,
    order_id      INT,
    order_date    TIMESTAMP,
    item_price    NUMERIC(10, 2),
    item_id       INT,
    item_quantity INT
);

INSERT INTO order_items (user_id, user_email, order_id, order_date, item_price, item_id, item_quantity)
VALUES (1, 'alice@example.com', 1001, '2024-01-01 10:00:00', 19.99, 501, 2),
       (2, 'bob@example.com', 1002, '2024-01-02 11:30:00', 9.99, 502, 1),
       (1, 'alice@example.com', 1003, '2024-01-03 09:15:00', 29.99, 503, 3),
       (3, 'carol@example.com', 1004, '2024-01-04 14:45:00', 15.50, 504, 1);