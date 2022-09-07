--DROP TABLE purchases;

CREATE TABLE IF NOT EXISTS customers
(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS products
(
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(256) NOT NULL,
    price NUMERIC(1000, 4) NOT NULL
);

CREATE TABLE IF NOT EXISTS purchases
(
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    value_date DATE NOT NULL
);

--INSERT INTO customers(id, first_name, last_name)
--VALUES(1, 'Антон', 'Иванов'), (2, 'Петр', 'Петров'), (3, 'Петр', 'Кулаков'),
--        (4, 'Ирина', 'Петрова'), (5, 'Ольга', 'Васильева'), (6, 'Мария', 'Логинова');
--        (7, 'Игорь', 'Петров'), (8, 'Ольга', 'Петрова')

--INSERT INTO products(id, product_name, price)
--VALUES(1, 'Вода', 40.5), (2, 'Хлеб', 50), (3, 'Молоко', 75.5), (4, 'Масло сливочное', 170.3),
--        (5, 'Сахар', 76.4), (6, 'Сыр', 340.2), (7, 'Колбаса', 250.7), (8, 'Кофе', 470);
--
--INSERT INTO purchases(id, customer_id, product_id, value_date)
--VALUES(1, 1, 1, '2022-08-25'), (2, 1, 2, '2022-08-25'), (3, 1, 3, '2022-08-25'),
--        (4, 2, 3, '2022-08-25'), (5, 2, 4, '2022-08-25'), (6, 2, 5, '2022-08-25'),
--        (7, 3, 5, '2022-08-26'), (8, 3, 6, '2022-08-26'), (9, 3, 7, '2022-08-26'),
--        (10, 4, 6, '2022-08-26'), (11, 4, 7, '2022-08-26'), (12, 4, 8, '2022-08-26'),
--        (13, 5, 8, '2022-08-29'), (14, 5, 1, '2022-08-29'), (15, 5, 2, '2022-08-29'),
--        (16, 6, 3, '2022-08-30'), (17, 6, 4, '2022-08-30'), (18, 6, 5, '2022-08-30'),
--        (19, 2, 4, '2022-08-25'), (20, 2, 5, '2022-08-25');