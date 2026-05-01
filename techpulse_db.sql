-- TechPulse Database Schema
-- Run this file to set up the complete database structure

CREATE DATABASE IF NOT EXISTS techpulse_db;
USE techpulse_db;

CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE sources (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    website_url VARCHAR(255),
    country VARCHAR(50)
);

CREATE TABLE articles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    summary TEXT,
    url VARCHAR(2000),
    published_at DATETIME,
    source_id INT,
    category_id INT,
    type ENUM('EXTERNAL', 'COMMUNITY') DEFAULT 'EXTERNAL',
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'APPROVED',
    FOREIGN KEY (source_id) REFERENCES sources(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('READER', 'CONTRIBUTOR', 'ADMIN') DEFAULT 'READER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id INT,
    category_id INT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Initial seed data
INSERT INTO categories (name) VALUES
    ('Artificial Intelligence'),
    ('Cybersecurity'),
    ('Cloud Computing'),
    ('Software Development');

INSERT INTO sources (name, website_url, country) VALUES
    ('TechCrunch', 'https://techcrunch.com', 'USA'),
    ('Wired', 'https://wired.com', 'USA');