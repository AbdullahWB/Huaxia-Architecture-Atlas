from __future__ import annotations

import json
from pathlib import Path
from typing import List, Dict

import joblib
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression


ART_DIR = Path(__file__).resolve().parent / "artifacts"
MODEL_PATH = ART_DIR / "type_model.joblib"
VECT_PATH = ART_DIR / "tfidf_vectorizer.joblib"


def load_training_data() -> List[Dict[str, str]]:
    """
    Training data format:
    [
      {"text": "some description", "label": "Bridge"},
      {"text": "some description", "label": "Palace"}
    ]

    You can optionally create:
      ml-service/model/training_data.json
    """
    p = Path(__file__).resolve().parent / "training_data.json"
    if p.exists():
        return json.loads(p.read_text(encoding="utf-8"))

    # Fallback minimal dataset (so it trains even if you haven't created a file yet)
    # You should replace/extend this with your real project data later.
    return [
        {
            "text": "Stone arch bridge engineering river crossing masonry span",
            "label": "Bridge",
        },
        {
            "text": "Imperial palace complex courtyards axial planning timber halls",
            "label": "Palace",
        },
        {
            "text": "Courtyard residence siheyuan housing symmetry enclosed space",
            "label": "Residence",
        },
        {
            "text": "Local government office compound formal halls courtyard sequence",
            "label": "Office",
        },
        {
            "text": "City wall fortification gates watchtowers defensive masonry",
            "label": "Fortification",
        },
        {
            "text": "Academy courtyard study hall education scholars teaching",
            "label": "Academy",
        },
        {
            "text": "Urban lane hutong street layout neighborhood courtyard houses",
            "label": "Urban Fabric",
        },
    ]


def train_and_save() -> None:
    data = load_training_data()
    texts = [d["text"] for d in data]
    labels = [d["label"] for d in data]

    # Tokenization tuned to be permissive; no stop_words to avoid harming non-English tokens.
    vectorizer = TfidfVectorizer(lowercase=True, ngram_range=(1, 2), max_features=5000)
    X = vectorizer.fit_transform(texts)

    clf = LogisticRegression(max_iter=2000)
    clf.fit(X, labels)

    ART_DIR.mkdir(parents=True, exist_ok=True)
    joblib.dump(vectorizer, VECT_PATH)
    joblib.dump(clf, MODEL_PATH)
    print(f"Saved: {VECT_PATH}")
    print(f"Saved: {MODEL_PATH}")


if __name__ == "__main__":
    train_and_save()
