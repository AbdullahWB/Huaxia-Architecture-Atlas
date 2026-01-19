![Huaxia Atlas ML Service](https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=1600&q=80)

# Huaxia Atlas ML Service

Small FastAPI + scikit-learn service used by the main app to:

- Predict a building type from text
- Suggest tags from text

## Endpoints

- `GET /health`
- `POST /predict/type`
- `POST /suggest/tags?top_k=6`

## Setup

From `ml-service/`:

```bash
python -m venv .venv
# Windows
.venv\Scripts\activate
# macOS or Linux
source .venv/bin/activate

pip install -r requirements.txt
```

## Train the model

```bash
python -m model.train
```

This writes artifacts to `ml-service/model/artifacts/`.

## Run the service

```bash
uvicorn app:app --reload --host 0.0.0.0 --port 8001
```

## Connect from the main app

Set `ML_SERVICE_URL` in `.env`, for example:

```bash
ML_SERVICE_URL=http://127.0.0.1:8001
```

---

Banner image is a placeholder. Replace it with your own.
