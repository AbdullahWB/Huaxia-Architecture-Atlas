from __future__ import annotations

from pathlib import Path
from typing import List, Optional

import joblib
import numpy as np
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field

APP_DIR = Path(__file__).resolve().parent
ART_DIR = APP_DIR / "model" / "artifacts"
VECT_PATH = ART_DIR / "tfidf_vectorizer.joblib"
MODEL_PATH = ART_DIR / "type_model.joblib"

app = FastAPI(title="Huaxia Atlas ML Service", version="1.0.0")

_vectorizer = None
_model = None


class TextIn(BaseModel):
    text: str = Field(min_length=3, max_length=8000)


class TypeOut(BaseModel):
    predicted_type: str
    confidence: float


class TagsOut(BaseModel):
    tags: List[str]


def _ensure_loaded() -> None:
    global _vectorizer, _model
    if _vectorizer is not None and _model is not None:
        return

    if not VECT_PATH.exists() or not MODEL_PATH.exists():
        raise HTTPException(
            status_code=503,
            detail=(
                "Model artifacts not found. Run: "
                "python -m model.train  (or python model/train.py) "
                "to generate artifacts in ml-service/model/artifacts/"
            ),
        )

    # joblib load for scikit-learn artifacts. Only load trusted local files. :contentReference[oaicite:4]{index=4}
    _vectorizer = joblib.load(VECT_PATH)
    _model = joblib.load(MODEL_PATH)


@app.get("/health")
def health():
    ok = VECT_PATH.exists() and MODEL_PATH.exists()
    return {"status": "ok", "model_ready": ok}


@app.post("/predict/type", response_model=TypeOut)
def predict_type(payload: TextIn):
    _ensure_loaded()
    X = _vectorizer.transform([payload.text])
    pred = _model.predict(X)[0]

    # Confidence if model supports predict_proba
    confidence = 0.0
    if hasattr(_model, "predict_proba"):
        proba = _model.predict_proba(X)[0]
        confidence = float(np.max(proba))

    return {"predicted_type": str(pred), "confidence": confidence}


@app.post("/suggest/tags", response_model=TagsOut)
def suggest_tags(payload: TextIn, top_k: int = 6):
    """
    Simple tag suggestion:
    - Use TF-IDF feature weights from the fitted vectorizer
    - Return the top K terms for this input text
    """
    _ensure_loaded()
    top_k = max(3, min(int(top_k), 12))

    X = _vectorizer.transform([payload.text])
    vec = X.toarray()[0]
    if vec.sum() == 0:
        return {"tags": []}

    feature_names = _vectorizer.get_feature_names_out()
    top_idx = np.argsort(vec)[::-1][:top_k]
    tags = [feature_names[i].strip() for i in top_idx if vec[i] > 0]

    # make tags nicer (avoid duplicates)
    uniq = []
    seen = set()
    for t in tags:
        if t and t not in seen:
            uniq.append(t)
            seen.add(t)

    return {"tags": uniq}
