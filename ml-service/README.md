# Huaxia Architecture Atlas — ML Service (Python local)

This is a small FastAPI + scikit-learn service used by the Spring Boot app to provide:

- Predict building **type** from description
- Suggest **tags** from description

FastAPI runs via an ASGI server such as Uvicorn. :contentReference[oaicite:5]{index=5}

## Setup

From `ml-service/`:

```bash
python -m venv .venv
# Windows: .venv\Scripts\activate
# macOS/Linux: source .venv/bin/activate
pip install -r requirements.txt
