# Plan de integración y control de alcance

## Fuente y destino

- Fuente editorial y de código de referencia: `iDreyK-ii/Enfermer-a-app`, rama `desarrollo`.
- SHA de catálogo fuente copiado: `4b05acef4fed3a30bcde2f0f23522f21e42dcf18`.
- Catálogo fuente: 45 fichas, 43 referencias, fecha editorial 2026-11-02; se declara expresamente inventario inicial no exhaustivo y revisión clínica pendiente.
- Destino: `iDreyK-ii/App.enfermeria.xyzk`.
- Rama de trabajo: `integracion/escalas360-native`.
- Este repositorio destino se mantiene como módulo de integración; no se sobreescribe la app original ni se modifica `desarrollo`.

## Estado de permisos

La API de GitHub confirmó `admin/maintain/pull/push/triage = true` para ambos repositorios. Se creó la rama de trabajo en el destino. La rama principal solo contiene la inicialización del README.

## Arquitectura fuente que se debe preservar

- Android nativo Kotlin + Jetpack Compose.
- Namespace `com.idreyk.enfermeria360`.
- Prototipo B, paleta, Outfit/Nunito Sans y componentes compartidos.
- Biblioteca offline y catálogo editorial JSON: `research/escalas/catalogo_fuente.json`; asset empaquetado `app/src/main/assets/escalas_clinicas_v1.json`.
- Navegación, ViewModel, repositorio de contenido y SQLite v2.
- Modo oscuro, accesibilidad, movimiento reducido y layout adaptable.
- No tocar farmacología, inicio ni Supabase en este módulo.

## Funcionalidad ya existente en la fuente (no asumir aprobada clínicamente)

El catálogo declara 45 instrumentos y 43 fuentes:
- Glasgow adulto y pediátrico, FOUR, NIHSS.
- EVA y escala numérica; FPS-R/Wong-Baker con estado de permiso.
- FLACC, r-FLACC, NIPS, PIPP-R, PAINAD, CPOT, BPS.
- 4AT, RASS, CAM-ICU, ICDSC, COMFORT-B, SBS, CAPD, pCAM-ICU, psCAM-ICU.
- Braden, Morse, Braden QD, Norton.
- Apgar, Silverman-Andersen, Capurro A/B, New Ballard.
- NEWS2, PEWS, MUST, MNA-SF, Barthel, Katz, Lawton-Brody, TUG, Tinetti, MoCA, PHQ-9, GAD-7.

Rutas ya declaradas en fuente incluyen Glasgow adulto, Glasgow pediátrico, EVA, escala numérica, 4AT, Apgar, Silverman, Capurro A/B y registros externos de RASS, Braden, Morse y Norton. Verificar código antes de duplicar.

## Instrumentos a ampliar tras investigación trazable

TISS-28, TISS-76, NAS, NEMS, APACHE II/IV, SAPS II/III, SOFA, MEWS, ESI, Manchester, CTAS, SATS, escalas de cardiología, respiratorio, renal, obstetricia, UCI neonatal/pediátrica, salud mental, oncología, quemados, heridas y rehabilitación. No se presentan como validados localmente hasta cotejar fuentes y protocolos.

## Estados de contenido

- `DOCUMENTAL`: explicación y referencias, sin formulario.
- `EDUCATIONAL_PRACTICE`: simulación educativa claramente identificada, datos sintéticos y sin uso asistencial.
- `EXTERNAL_RECORD`: transcribir una puntuación obtenida externamente; no administra ni calcula el instrumento.
- `CALCULATOR_ALLOWED`: solo si contenido/version/licencia y revisión profesional están documentados.
- `PERMISSION_REQUIRED`: no reproducir formulario/estímulos; mostrar resumen original y enlace al titular.
- `CLINICAL_REVIEW_REQUIRED`: no marcar como validado ni habilitar para pacientes reales.

## Fases

1. Congelar la copia de la fuente editorial y su SHA.
2. Completar matriz de instrumentos, población, servicios, fuentes, versiones, derechos y modalidad.
3. Verificar las seis fichas iniciales: Glasgow, EVA, RASS, Apgar, Capurro A/B y Norton.
4. Construir las seis escenas ilustradas aprobadas y las pantallas Compose nativas.
5. Añadir el resto del catálogo por bloques de servicio, sin duplicar escalas transversales.
6. Implementar pruebas de modelos, límites, estados no valorables, rotación, accesibilidad y tamaños de pantalla.
7. Compilar contra el proyecto anfitrión real; solo entonces abrir PR o integrar.
8. No publicar como herramienta clínica sin revisión profesional documentada.

## Criterios de aceptación

- Catálogo derivado del JSON fuente, con IDs estables y sin pérdida de metadatos.
- Cada escala aparece una sola vez en biblioteca y se asocia a múltiples servicios.
- No se mezclan versiones ni poblaciones.
- No se muestran cálculos parciales como totales válidos.
- No se inventan umbrales ni se copian instrumentos con permisos no resueltos.
- UI Compose nativa y adaptable; recursos de ilustración separados del texto.
- Rama de trabajo aislada y commits revisables.
