# Huaxia Architecture Atlas

Java Web (Spring Boot) project: Ancient Chinese Architecture Information System (pre-1911)

## Stack

- Spring Boot (MVC)
- Thymeleaf
- MySQL (Docker)
- Spring Security (Admin login)

## Local run (Docker DB + Spring Boot)

1) Create DB init SQL files (teacher schema + seed):
   - db/init/01_schema.sql
   - db/init/02_seed.sql

2) Start MySQL:

   ```bash
   docker compose up -d
