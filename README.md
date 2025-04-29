# Car Recognizer DJL

Dieses Projekt ist ein Car Brand Recognition System basierend auf TensorFlow und Spring Boot, verpackt in einem Docker-Container und bereitgestellt über Azure.

## Projektbeschreibung

Das Ziel dieses Projekts ist es, Automarken anhand von Bildern zu erkennen. Der Server ist in Java (Spring Boot) geschrieben und nutzt ein vortrainiertes TensorFlow-Modell zur Bildklassifikation.

## Features

- Erkennung von verschiedenen Automarken anhand von hochgeladenen Bildern
- Bereitgestellt als REST-API (Spring Boot)
- Verpackt als Docker-Container
- Deployment auf Azure App Service
- Nutzung eines eigenen trainierten Modells

## Technologien

- Java 17 / Spring Boot
- TensorFlow (Python)
- Docker
- Azure Container Registry & Azure App Service

## Endpoints

| Methode | Pfad         | Beschreibung                         |
|--------:|:-------------|:--------------------------------------|
| POST    | /predict     | Automarke anhand eines Bildes vorhersagen |
| GET     | /health      | Gesundheitsstatus der Anwendung      |

## Projektstruktur

```bash
CarRecognizerDJL/
├── src/
├── target/
├── Dockerfile
├── README.md
└── ...
```

## Lokales Setup

```bash
mvn clean package
docker build -t carrecognizer-djl .
docker run -p 8080:8080 carrecognizer-djl
```

Zugriff dann über `http://localhost:8080`.

## Deployment

Das Docker-Image wird in Azure Container Registry hochgeladen und als Web App über Azure App Service bereitgestellt.

---

## Autor

- Dein Name (ft. ChatGPT 😄)
