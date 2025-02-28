-- Create the database (if it doesn't exist)
CREATE DATABASE IF NOT EXISTS your_database_name; -- Replace 'your_database_name'

-- Use the database
USE your_database_name;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    active BOOLEAN DEFAULT TRUE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the products table
CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0
);

-- Create the orders table
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    quantity INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Example Insert Statements (Optional)
INSERT INTO users (username, password, email) VALUES ('testuser', '$2b$10$EXAMPLEHASHEDPASSWORD', 'test@example.com'); -- Replace with a real hashed password

INSERT INTO products (product_name, description, price, stock_quantity) VALUES
('Example Product 1', 'This is an example product.', 99.99, 100),
('Example Product 2', 'Another example product.', 49.99, 50);

INSERT INTO orders (user_id, product_id, quantity) VALUES (1, 1, 2);