# Plan maestro de desarrollo — Enfermería iDreyK 360

**Propósito:** llevar el proyecto Android existente a una versión funcional, coherente, comprobable y preparada para continuar su desarrollo en Android Studio. El ZIP de entrega y la actualización definitiva de la rama principal se reservan para el cierre, una vez completadas y verificadas las fases.

## Reglas de trabajo

1. Cada fase tiene alcance, tareas, criterios de aceptación y evidencia de pruebas.
2. Cada cambio se realiza en `integracion/escalas360-native`, con commits pequeños y mensajes descriptivos.
3. No se marca una tarea como terminada solo porque exista una pantalla: sus acciones, estados, datos, accesibilidad y navegación deben funcionar.
4. Después de cada bloque se ejecutan las pruebas relevantes. Si algo falla, se corrige antes de avanzar a una parte que dependa de ello.
5. Las funciones clínicas se mantienen educativas hasta verificar versión, fuente, derechos, revisión profesional y concordancia con protocolos locales.
6. No se guardan datos identificables de pacientes en ejemplos, pruebas o contenido de demostración.
7. Las fichas farmacológicas completas se excluyen de este plan y se reservan para una fase posterior.

## Mapa de fases

| Fase | Alcance | Criterio para cerrar |
|---|---|---|
| 0. Fundación técnica | Repositorio, Gradle, módulos, dependencias, CI, assets, modelos y carga offline | Compila, pruebas base y lint pasan; no hay referencias sin resolver |
| 1. Experiencia y navegación | Tema, componentes compartidos, inicio, navegación, búsqueda, estados vacíos/carga/error, teléfono/tablet | Flujos principales navegables, controles funcionales y pruebas UI |
| 2. Núcleo de Enfermería | Pacientes ficticios, valoración, Gordon, PAE, NANDA/NOC/NIC, SOAPIE, signos vitales, balance y evolución | Persistencia local segura, validación de campos y pruebas del flujo completo |
| 3. Herramientas clínicas | Escalas 360, calculadoras autorizadas, conversiones, monitorización y ECG educativo | Fórmulas y límites probados; población/versión correctas; sin decisiones clínicas automáticas |
| 4. Atlas y aprendizaje | Atlas de equipos, procedimientos, protocolos, biblioteca, cuadernos, casos, tarjetas y cuestionarios | Recursos visuales locales y contenidos trazables; rutas y navegación sin elementos inertes |
| 5. Datos y continuidad | SQLite/repositorio, importación, exportación, respaldo, sincronización opcional y privacidad | Recuperación, integridad, aislamiento de datos y pruebas de fallo |
| 6. Calidad integral | Accesibilidad, modo oscuro, movimiento reducido, rendimiento, pruebas en distintos tamaños, auditoría y documentación | Suite completa aprobada, incidencias críticas cerradas y guía de compilación actualizada |
| 7. Cierre de entrega | Congelar versión, inventario, evidencias, ZIP completo y revisión final de GitHub | ZIP verificado, reproducible y coherente con el commit de entrega |

## Fase 0 — Fundación técnica (en curso)

### Auditoría y correcciones identificadas
- [x] Identificar que el workflow de Android apuntaba al módulo Gradle inexistente `:android`; cambiar a `:app`.
- [x] Recuperar los tres archivos Kotlin del catálogo desde el directorio de staging y colocarlos en `app/src/main/java/com/idreyk/enfermeria360/`.
- [x] Copiar el JSON offline de 191 fichas a `app/src/main/assets/escalas_clinicas_v1.json`.
- [x] Incorporar el validador del catálogo.
- [x] Añadir la prueba de integridad del catálogo a la fuente del módulo.
- [x] Localizar y corregir dos aserciones desactualizadas en las pruebas de la biblioteca.
- [ ] Obtener CI verde para ensamblado Android, pruebas unitarias y lint.
- [ ] Comprobar que todos los recursos locales y rutas del catálogo se empaquetan y cargan.
- [ ] Retirar el staging solo cuando ya no sea necesario y comprobar que no hay copias divergentes.

### Evidencia exigida
- GitHub Actions sin fallos.
- `python3 tools/validate_scale_catalog.py app/src/main/assets/escalas_clinicas_v1.json` correcto.
- `gradle :app:testDebugUnitTest :app:assembleDebug :app:lintDebug` correcto.

## Fase 1 — Experiencia y navegación

- Auditar todas las rutas registradas en `MainActivity` y compararlas con accesos visibles.
- Revisar inicio, módulos, accesos rápidos, buscador global, favoritos, perfil y navegación inferior.
- Normalizar tarjetas, botones, selectores, diálogos, hojas inferiores, avisos y estados de carga/error.
- Mantener estilo aprobado y no reemplazar pantallas funcionales por imágenes planas.
- Verificar 360 dp, teléfono grande y tablet, vertical/horizontal; fuente ampliada y contraste.
- Añadir pruebas de navegación, restauración de estado y acciones principales.

## Fase 2 — Núcleo de Enfermería

- Revisar el ciclo: listado de pacientes ficticios → ficha → valoración → problemas → plan → evolución.
- Gordon: once patrones, campos, observaciones y hallazgos sin duplicaciones.
- PAE: NANDA-I 2024–2026, NOC y NIC con edición/año/licencia y trazabilidad; no inventar códigos ni etiquetas.
- SOAPIE: S, O, A, P, I y E; soportar registro profesional y práctica rápida claramente diferenciados.
- Signos vitales, balance hídrico, evolución y cuidados: validaciones de unidades, fecha/hora y campos.
- Prohibir ejemplos con identificadores reales o datos sensibles; mantener demostraciones sintéticas.

## Fase 3 — Herramientas clínicas

- Escalas 360: revisar 191 fichas por población, servicio, versión, propósito, límites, referencias y derechos.
- Mantener separadas práctica interactiva, simulación, registro externo, ficha documental y permiso pendiente.
- Calculadoras: dosis por peso, dilución, goteo, velocidad de infusión, unidades y balance, con entradas explícitas y manejo de errores.
- Monitorización: signos vitales y ECG para aprendizaje; distinguir señal ilustrativa, simulación y trazado real.
- ECG: ritmo, frecuencia, regularidad, ondas, intervalos y casos docentes con fuentes.
- No crear fichas farmacológicas completas durante esta fase.

## Fase 4 — Atlas y aprendizaje

- Atlas visual de equipos/materiales, componentes, usos, precauciones y cuidados de Enfermería.
- Protocolos y procedimientos con fuentes, vigencia y contexto local; separar guía educativa de orden institucional.
- Biblioteca temática, cuadernos, notas, casos clínicos, tarjetas, preguntas y progreso.
- Añadir ilustraciones locales optimizadas y accesibles; las imágenes no contendrán textos clínicos imprescindibles.

## Fase 5 — Datos y continuidad

- Revisar SQLite, repositorios y migraciones; asegurar transacciones y consistencia.
- Importación con vista previa, validación, conflicto/reemplazo y reversión cuando aplique.
- Exportación documentada; archivo legible, versionado y sin secretos.
- Sincronización opcional: offline-first; estados de red, reintentos, conflictos y errores comprensibles.
- Revisar permisos, almacenamiento, borrado, privacidad y reglas de acceso del backend.
- No incluir service_role ni credenciales privadas en el cliente Android.

## Fase 6 — Calidad integral

- Suite de pruebas de modelos, fórmulas, flujos, navegación, importación/exportación y persistencia.
- Revisiones de TalkBack, navegación por foco, contraste, tamaño de texto y áreas táctiles.
- Modo oscuro, movimiento reducido y rendimiento en equipos de recursos limitados.
- Auditoría de contenido: fuentes activas, duplicados, fechas, licencias, referencias locales y afirmaciones clínicas.
- Guía de Android Studio, versiones de JDK/Gradle/SDK y solución de problemas.
- No declarar validación clínica o aprobación MINSA/EsSalud sin evidencia específica y autorización correspondiente.

## Fase 7 — Cierre y entrega

- Congelar commit/tag, registrar versiones y dependencias.
- Verificar desde un checkout limpio y reproducir compilación.
- Crear ZIP con código fuente y documentación necesaria; excluir cachés, secretos, carpetas de construcción redundantes y datos personales.
- Validar el ZIP: integridad, estructura, instrucciones y coincidencia con el commit de entrega.
- Abrir/actualizar Pull Request a `main`; integrar solo cuando la revisión y CI sean satisfactorias.

## Seguimiento

Actualizar esta matriz en cada entrega. Una fase se marca como cerrada únicamente cuando todos sus criterios estén cubiertos por código y evidencia verificable.

**Estado inicial:** fase 0 en curso. La biblioteca clínica ha sido incorporada en la rama de trabajo, pero la compilación y la suite extendida todavía deben demostrar que la integración quedó estable.
