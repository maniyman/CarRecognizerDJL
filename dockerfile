# --- Basis-Image ---
    FROM openjdk:17-jdk-slim

    # --- Arbeitsverzeichnis ---
    WORKDIR /app
    
    # --- Notwendige Linux-Tools installieren ---
    RUN apt-get update && apt-get install -y \
        python3 \
        python3-pip \
        libgomp1 \
        libmagic1 \
        bash \
        && rm -rf /var/lib/apt/lists/*
    
    # --- TensorFlow und weitere Python-Module installieren ---
    RUN pip3 install --no-cache-dir tensorflow numpy pillow
    
    # --- JAR-Datei kopieren (angepasst!) ---
    COPY target/carrecognizer-1.0-SNAPSHOT.jar app.jar
    
    # --- Modell kopieren (angepasst!) ---
    COPY src/main/resources/model/car_brand_saved_model/ /app/model/car_brand_saved_model/
    
    # --- Port freigeben ---
    EXPOSE 8080
    
    # --- Anwendung starten ---
    ENTRYPOINT ["java", "-jar", "app.jar"]
    