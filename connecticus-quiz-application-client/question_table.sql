CREATE DATABASE questiondb;

USE quizDb;

CREATE TABLE questiondb.question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    difficulty VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    options JSON,
    answer VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

