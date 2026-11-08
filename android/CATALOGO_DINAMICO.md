# Catálogo dinámico Escalas 360

`Escalas360CompleteCatalogScreen` carga:
- `source/catalogo_fuente_Enfermer-a-app_2026-11-02.json` (45 fichas fuente)
- Todos los JSON en `expansiones/` (108 fichas añadidas)
- `guias_seis_prototipos_clinicos_v1.json` para ampliar las seis fichas guía.

La biblioteca mantiene un registro por instrumento con servicios transversales. Las tarjetas y fichas son Compose nativo; las ilustraciones se cargan como WebP desde `scales360_art/` con fallback nativo si falta un recurso.

Filtros actuales: búsqueda general y categorías destacadas. Se pueden ampliar a servicio/población sin duplicar instrumentos.
