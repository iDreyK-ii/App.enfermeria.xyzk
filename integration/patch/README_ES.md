# Paquete de integración · Escalas 360

Este directorio contiene los archivos preparados para integrarse en el proyecto Android original:

**Origen objetivo:** [iDreyK-ii/Enfermer-a-app](https://github.com/iDreyK-ii/Enfermer-a-app), rama `desarrollo`  
**Commit de origen consultado:** `90acfca6d1ee47361271be3b2384913032858e46`

## Qué se preparó

Se consolidaron 191 fichas únicas (45 existentes + 146 nuevas) con 86 referencias. Cada ficha incluye población, objetivo, método/resumen, límites, derechos, servicios hospitalarios, tipo de instrumento, rol de Enfermería, contexto peruano, revisión documental, disponibilidad y rutas.

### Archivos de reemplazo

Copiar conservando las rutas:

- `app/src/main/assets/escalas_clinicas_v1.json`
- `app/src/main/java/com/idreyk/enfermeria360/ClinicalCatalog.kt`
- `app/src/main/java/com/idreyk/enfermeria360/ScreensClinicalCatalog.kt`
- `app/src/main/java/com/idreyk/enfermeria360/ClinicalVisualLearning.kt`
- `app/src/test/java/com/idreyk/enfermeria360/ClinicalCatalogTests.kt`

Las versiones listas se encuentran en este directorio `app/`.

## Cambios de la biblioteca

- Búsqueda offline ampliada.
- Filtros por población, clasificación del instrumento, servicio hospitalario y tipo de acceso.
- Tarjetas en una columna para teléfono y dos columnas a partir de 600 dp para tablet.
- Fichas detalladas con servicios relacionados, rol de Enfermería, contexto peruano, derechos y fuentes.
- Agrupación de las 44 categorías técnicas en clases más breves para el filtro.
- Reutilización de las siete ilustraciones originales del proyecto, con fallback general y sin convertir dibujos en estímulos o puntuaciones.
- El parser aumenta el límite a 500 KB y 220 instrumentos/100 fuentes; mantiene profundidad JSON limitada, HTTPS, poblaciones permitidas, revisión clínica pendiente y rutas nativas explícitas.
- Las 13 rutas que ya tenía la app se conservan; las nuevas incorporaciones son documentales o con permiso pendiente y no reciben atajos interactivos.

## Instrumentos prioritarios incorporados

TISS-76, TISS-28, NAS, NEMS, APACHE II/IV, SAPS II/III, SOFA, qSOFA, SIRS, herramientas de triaje, trauma, ictus, sedación, herramientas obstétricas/neonatales, pediátricas, geriátricas, nutricionales, oncológicas, heridas, ostomías, cardiológicas, respiratorias, renales y quirúrgicas.

TISS y NEMS no se presentan como equivalentes de NAS. Las herramientas pronósticas, de triaje, de imagen y de clasificación de complicaciones no se titulan como escalas de Enfermería.

## Seguridad, derechos y contenido clínico

- No se copiaron formularios, tablas de puntuación, ítems protegidos ni algoritmos licenciados si no había permiso acreditado.
- Una ficha documental no ofrece calculadora.
- Los instrumentos con derechos pendientes no ofrecen formulario.
- Las versiones y límites se señalan de manera explícita.
- La app no calcula diagnósticos, tratamientos, dosis, decisiones de alta, riesgo individual exacto ni dotación de personal.
- La revisión clínica independiente del catálogo y la aprobación institucional siguen pendientes.

## Validación local pendiente

La integración debe comprobarse en el proyecto anfitrión, no en este repositorio de staging. Después de copiar los archivos:

```bash
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

Ejecutar además la comprobación del catálogo:

```bash
python tools/validate_scale_catalog.py app/src/main/assets/escalas_clinicas_v1.json
```

Probar en teléfono vertical y tablet horizontal, modo oscuro, texto ampliado, TalkBack, movimiento reducido, búsqueda/filtros y rutas preexistentes. No publicar como clínicamente validado hasta completar revisión por profesionales de cada servicio.

## Por qué no está en el repositorio original todavía

La conexión GitHub disponible puede leer el repositorio original, pero la instalación de GitHub App está configurada como **Selected repositories** y no incluye `Enfermer-a-app`. Los intentos de escritura al repositorio original fueron rechazados con `403 Resource not accessible by integration`. Por eso los archivos se prepararon en este repositorio de staging y no se afirma que estén subidos o compilados en la app original.

Para permitir la integración directa, abre:

https://github.com/settings/installations/179902894

Añade `iDreyK-ii/Enfermer-a-app` a los repositorios seleccionados y habilita permisos de contenido (Contents: read/write) y pull requests (read/write). Tras ello se puede aplicar esta rama de trabajo al repositorio original, crear el PR, ejecutar CI y corregir cualquier error real de compilación.
