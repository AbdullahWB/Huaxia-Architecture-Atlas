![Huaxia Architecture Atlas](./asset/hero.png)

# Huaxia Architecture Atlas

Huaxia Architecture Atlas is a Spring Boot web app for exploring ancient Chinese architecture
(pre-1911). It includes a public catalog, AI chat and explain tools, community posts, products,
and admin workflows.

## Features

- Explore, search, and view curated buildings
- AI chat and AI explain summaries
- Community posts with moderation
- Product catalog with reviews and orders
- User dashboard with notifications
- Admin dashboard with queues and reports

## Stack

- Spring Boot (MVC)
- Thymeleaf
- MySQL (Docker)
- Spring Security
- Optional ML service (FastAPI + scikit-learn)

## Quick start (local)

1) Copy env file and fill values:

```bash
copy .env.example .env
```

2) Start MySQL and Adminer:

```bash
docker compose up -d
```

3) Run the Spring Boot app:

```bash
mvn -DskipTests clean spring-boot:run
```

Open the app:
- http://localhost:8080
- Adminer (DB UI): http://localhost:8081

## Database seeding

`db/init/01_schema.sql` and `db/init/02_seed.sql` run only on a fresh DB volume.
If you want to reset all data and re-seed:

```bash
docker compose down -v
docker compose up -d
```

## Optional ML service

The ML service suggests building types and tags. See `ml-service/README.md` to train
and run it, then set `ML_SERVICE_URL` in `.env`.

## Configuration

Key env vars are in `.env.example`:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `DEEPSEEK_API_KEY` (optional, AI chat)
- `ML_SERVICE_URL` (optional)

## Troubleshooting

- No new seed data: reset the DB volume and restart containers.
- Images missing: check `cover_image` or `image_url` in the database.

---

Banner image is a placeholder. Replace it with your own.
