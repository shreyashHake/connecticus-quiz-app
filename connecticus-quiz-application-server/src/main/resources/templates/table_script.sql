CREATE TABLE springboot.question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    difficulty VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    options JSON, -- Assuming options will be stored as JSON data
    answer VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

