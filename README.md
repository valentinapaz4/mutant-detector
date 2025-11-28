##Proyecto: Mutant Detector
#Alumna: Paz Murgo, María Vlaentina 

El presente proyecto implementa una API REST para detectar mutantes basándose en su secuencia de ADN.

##Despliegue de Producción (Render URL)
URL Puública de la API: https://mutant-detector-8hi0.onrender.com


## Endpoints

### POST /mutant
Detecta si un DNA es mutante.

**Request:**
```json
POST https://mutant-detector-8hi0.onrender.com/mutant
Content-Type: application/json

{
  "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}
```

**Respuestas:**
- `200 OK` - Es mutante
- `403 Forbidden` - NO es mutante
- `400 Bad Request` - DNA inválido

### GET /stats
Obtiene estadísticas de verificaciones.

**Request:**
```
GET https://mutant-detector-8hi0.onrender.com/stats
```

**Response:**
```json
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}
```

## Tecnologías Utilzadas
 
- Java 21
- Spring Boot 3.3.3
- H2 Database (en memoria)
- Gradle
- Docker
- Render (hosting)

## Ejecutar localmente

### Requisitos
- Java 21+
- Gradle 8+

### Pasos
```bash
# Clonar repositorio
https://github.com/valentinapaz4/mutant-detector/tree/main
cd mutant-detector

# Ejecutar
./gradlew bootRun

# La API estará en http://localhost:8080
```

## Testing
```bash
# Ejecutar tests
./gradlew test

# Ver reporte de cobertura
./gradlew jacocoTestReport
# Abrir: build/reports/jacoco/test/html/index.html
```

## Generar JAR
```bash
./gradlew clean bootJar -x test
# JAR generado en: build/libs/mutant-detector-0.0.1-SNAPSHOT.jar
```

## Docker
```bash
# Build
docker build -t mutant-detector .

# Run
docker run -p 8080:8080 mutant-detector
```

## Arquitectura
```
Controller → Service → Detector (algoritmo)
                ↓
          Repository → H2 Database
```
