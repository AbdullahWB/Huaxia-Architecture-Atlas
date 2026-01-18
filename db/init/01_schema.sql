-- Huaxia Architecture Atlas - Schema (Docker init)
-- NOTE: These scripts run from /docker-entrypoint-initdb.d on FIRST init only. :contentReference[oaicite:1]{index=1}
-- Ensure good Unicode support
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- (Optional but safe) Create and select DB. If Docker already created it via MYSQL_DATABASE, this is harmless.
CREATE DATABASE IF NOT EXISTS huaxia_atlas CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE huaxia_atlas;
-- =========================
-- 1) Buildings
-- =========================
CREATE TABLE IF NOT EXISTS buildings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    dynasty VARCHAR(100) NULL,
    location VARCHAR(200) NULL,
    type VARCHAR(80) NULL,
    year_built VARCHAR(80) NULL,
    description TEXT NULL,
    tags VARCHAR(500) NULL,
    -- comma-separated tags for AI/recommendations
    cover_image VARCHAR(300) NULL,
    -- store URL/path (e.g., /uploads/xyz.jpg)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE INDEX idx_buildings_dynasty ON buildings(dynasty);
CREATE INDEX idx_buildings_type ON buildings(type);
CREATE INDEX idx_buildings_created ON buildings(created_at);
-- =========================
-- 2) Community Posts
-- =========================
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_name VARCHAR(120) NULL,
    author_email VARCHAR(200) NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE INDEX idx_posts_status ON posts(status);
CREATE INDEX idx_posts_created ON posts(created_at);
-- =========================
-- 3) Contact Messages
-- =========================
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(200) NOT NULL,
    subject VARCHAR(200) NULL,
    message TEXT NOT NULL,
    is_read TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE INDEX idx_messages_is_read ON messages(is_read);
CREATE INDEX idx_messages_created ON messages(created_at);
-- =========================
-- 4) AI Logs (Search + Chat)
-- =========================
CREATE TABLE IF NOT EXISTS search_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    query_text VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE INDEX idx_search_logs_created ON search_logs(created_at);
CREATE TABLE IF NOT EXISTS chat_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_question TEXT NOT NULL,
    model_answer TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE INDEX idx_chat_logs_created ON chat_logs(created_at);
SET FOREIGN_KEY_CHECKS = 1;