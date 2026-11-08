# Enfermería iDreyK 360

Proyecto Android nativo de desarrollo para aprendizaje y apoyo educativo de Enfermería.

## Estado de desarrollo

- **Rama activa:** [`integracion/escalas360-native`](https://github.com/iDreyK-ii/App.enfermeria.xyzk/tree/integracion/escalas360-native).
- **Plan de trabajo:** [Plan de desarrollo por fases hasta el 100 %](docs/PLAN_DESARROLLO_100.md).
- **Rama principal:** `main`; se mantiene estable mientras la integración se revisa y supera las comprobaciones.
- **Stack:** Kotlin, Jetpack Compose, Gradle y recursos offline.
- **Namespace:** `com.idreyk.enfermeria360`.

> Los cambios ya están subidos a la rama de trabajo. La rama `main` no refleja aún todo el desarrollo: los cambios se revisarán mediante Pull Request cuando el conjunto sea compilable y verificable.

## Diseño y experiencia

Se conserva el lenguaje visual aprobado para iDreyK 360: paleta turquesa, rosa y violeta/azul, superficies redondeadas, jerarquía clara, tipografías Outfit y Nunito Sans, ilustraciones clínicas locales y controles nativos de Compose. La interfaz debe adaptarse a teléfonos y tablets, orientación vertical/horizontal, modo oscuro, texto ampliado, TalkBack y movimiento reducido.

## Principios de contenido

- Las ilustraciones acompañan; no sustituyen controles, datos, formularios ni escalas estandarizadas.
- Separar fichas documentales, práctica educativa, simulación, registro externo y calculadoras expresamente autorizadas.
- No inventar puntuaciones, puntos de corte, dosis, resultados, protocolos ni indicaciones.
- Cada contenido clínico debe conservar fuentes, versión, población, límites, derechos y estado de revisión.
- El desarrollo educativo no constituye validación clínica ni sustituye protocolos institucionales.

## Alcance

La construcción por fases incluye navegación y panel principal, valoración y PAE, biblioteca clínica, herramientas de cálculo, monitorización/ECG, atlas, aprendizaje, protocolos, casos, almacenamiento, importación/exportación, accesibilidad y control de calidad.

**Fichas farmacológicas completas:** quedan fuera de las fases actuales y se desarrollarán en una etapa posterior, como se acordó. Se conserva la estructura que ya existe sin declarar terminada esa sección.

## Verificación local

Desde la raíz del proyecto:

```bash
python3 tools/validate_scale_catalog.py app/src/main/assets/escalas_clinicas_v1.json
gradle :app:testDebugUnitTest
gradle :app:assembleDebug
gradle :app:lintDebug
```

GitHub Actions ejecuta estas comprobaciones en cada actualización de la rama de trabajo.
