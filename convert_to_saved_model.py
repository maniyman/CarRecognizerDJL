import tensorflow as tf

# Pfad zu deinem h5-Modell
h5_model_path = "car_brand_model_final.h5"

# Zielordner für das konvertierte SavedModel
saved_model_dir = "car_brand_saved_model"

# .h5 Modell laden
model = tf.keras.models.load_model(h5_model_path)

# Optional: Architektur anzeigen
model.summary()

# Als SavedModel speichern
model.export(saved_model_dir)


print(f"Modell erfolgreich gespeichert unter: {saved_model_dir}")
