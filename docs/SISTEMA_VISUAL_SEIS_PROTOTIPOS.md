# Sistema visual Escalas 360 — especificación de diseño

## Decisión visual aprobada por el propietario

Se conservan los seis lenguajes de composición que se aprobaron en el trabajo de prototipos. No se fuerza una plantilla idéntica para todos los instrumentos. Cada ficha tiene una escena clínica propia, ilustración 3D chibi de alta calidad y controles Compose nativos.

La imagen de referencia de hospitalización (enfermera junto a paciente adulto mayor) determina el acabado: personajes 3D animados, ojos expresivos, materiales suaves, luz de hospital cálida, ambiente clínico reconocible, paneles pastel y bordes redondeados. La infografía de úlceras por presión determina la organización pedagógica del contenido, no el estilo plano ni una captura convertida en pantalla.

## Seis prototipos, seis escenas

1. **Glasgow — valoración neurológica en hospitalización:** enfermera al lado de la cama, paciente adulto, cabecera y monitor discretos; el panel interactivo nativo muestra respuesta ocular, verbal y motora por separado. No usar expresiones del personaje como estímulos de puntuación.
2. **EVA — entrevista de dolor:** enfermera a la altura del paciente, conversación empática y paciente señalando la intensidad. La escala se dibuja con Compose; no usar caras de FPS-R/Wong-Baker ni inventar descriptores. La línea digital se identifica como simulación cuando no es una VAS calibrada.
3. **RASS — entorno UCI:** enfermera observa respuesta a voz/estímulo según protocolo, cama UCI y monitor ambiental. La pantalla distingue registro externo de práctica; no reproduce el formulario protegido ni administra estímulos.
4. **Apgar — atención neonatal:** escena de equipo neonatal y recién nacido en superficie térmica. Cinco componentes se muestran como tarjetas nativas. La ilustración nunca representa los criterios oficiales ni sustituye evaluación inmediata.
5. **Capurro — valoración neonatal:** escena de evaluación física respetuosa del recién nacido, con acercamientos esquemáticos no instructivos y paneles separados para variantes A/B. La variante, componentes y fórmula permanecen explícitos; no mezclar versiones.
6. **Norton — prevención de lesión por presión:** enfermera conversa con paciente mayor junto a cama; escena de cuidado de piel y movilización segura. Los cinco dominios se describen en contenido original; no reproducir el formulario sin permiso.

## Diseño nativo, no una imagen de pantalla

- Ilustraciones en PNG/WebP/WebP lossless o vector según calidad, en recursos locales de Android; no poner textos clínicos dentro de las imágenes.
- Toda la interfaz visible (títulos, tarjetas, pestañas, selectores, resultados, accesibilidad, navegación, filtros y referencias) se implementa en Jetpack Compose.
- Imágenes solo como acompañamiento editorial; no se incrustan capturas de una interfaz simulada.
- En teléfono vertical: ficha de una columna, ilustración hero compacta, criterios apilados.
- En tablet/horizontal: escena en columna lateral o franja superior y contenido en dos columnas; tabla de criterios en ancho completo.
- Reutilizar la paleta, tipografías y componentes del Prototipo B de la app fuente. No inventar un nuevo tema ni cambiar Farmacología/inicio.
- Soportar tema claro/oscuro, tamaño de fuente del sistema, TalkBack, contraste, movimiento reducido y áreas táctiles suficientes.
- El color no comunica gravedad ni puntuación; todo estado incluye texto y/o icono.

## Estructura de ficha

1. Nombre, sigla, población y propósito.
2. Contexto de uso/servicios asociados.
3. Qué evalúa y qué no evalúa.
4. Versión y variante exactas.
5. Componentes o dimensiones (sin reproducir material protegido).
6. Modalidad disponible: documental, práctica educativa, transcripción externa o calculadora autorizada.
7. Interpretación y límites, solo cuando estén respaldados.
8. Rol de Enfermería y registro.
9. Precauciones, factores que interfieren y datos no valorables.
10. Fuentes, fecha de revisión, licencia y estado de revisión profesional.

## Regla de implementación

No inferir que un instrumento se puede digitalizar por estar disponible en Internet. No habilitar formularios ni cálculos si la versión, el contenido, los derechos y la revisión clínica no están resueltos. Mantener el estado de permiso/validación visible y trazable.
