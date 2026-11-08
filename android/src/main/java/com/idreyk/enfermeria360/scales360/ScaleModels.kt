package com.idreyk.enfermeria360.scales360

/**
 * Datos de presentación de la biblioteca Escalas 360.
 *
 * Este modelo NO contiene criterios clínicos ni puntuaciones. Los instrumentos
 * deben cargarse desde el catálogo editorial versionado y conservar sus estados
 * de permiso y revisión.
 */
enum class ScaleContentMode {
    DOCUMENTAL,
    EDUCATIONAL_PRACTICE,
    EXTERNAL_RECORD,
    CALCULATOR_ALLOWED,
    PERMISSION_REQUIRED,
    CLINICAL_REVIEW_REQUIRED,
}

enum class ScalePrototype {
    NEUROLOGICAL_BEDSIDE,
    PAIN_INTERVIEW,
    ICU_SEDATION,
    NEWBORN_APGAR,
    NEWBORN_CAPURRO,
    PRESSURE_INJURY_PREVENTION,
    GENERAL_CLINICAL,
}

data class ScaleLibraryItem(
    val id: String,
    val name: String,
    val shortDescription: String,
    val population: String,
    val services: List<String>,
    val prototype: ScalePrototype,
    val contentMode: ScaleContentMode,
    val isReferencePrototype: Boolean = false,
)

/**
 * Seis instrumentos de referencia aprobados para definir el lenguaje visual.
 * Los estados son conservadores: no conceden autorización clínica o de copyright.
 */
val Escalas360ReferenceItems = listOf(
    ScaleLibraryItem(
        id = "glasgow",
        name = "Glasgow",
        shortDescription = "Valoración estructurada de la respuesta neurológica.",
        population = "Adultos; mantener variante pediátrica separada.",
        services = listOf("Emergencia", "Medicina", "Cirugía", "UCI", "Neurología"),
        prototype = ScalePrototype.NEUROLOGICAL_BEDSIDE,
        contentMode = ScaleContentMode.EDUCATIONAL_PRACTICE,
        isReferencePrototype = true,
    ),
    ScaleLibraryItem(
        id = "eva",
        name = "EVA",
        shortDescription = "Autoinforme de intensidad del dolor mediante línea visual.",
        population = "Personas capaces de comprender y responder al formato.",
        services = listOf("Emergencia", "Medicina", "Cirugía", "UCI", "URPA", "Pediatría según protocolo"),
        prototype = ScalePrototype.PAIN_INTERVIEW,
        contentMode = ScaleContentMode.EDUCATIONAL_PRACTICE,
        isReferencePrototype = true,
    ),
    ScaleLibraryItem(
        id = "rass",
        name = "RASS",
        shortDescription = "Observación de agitación y profundidad de sedación.",
        population = "Adultos; no extrapolar automáticamente a escalas pediátricas.",
        services = listOf("UCI", "Emergencia", "Áreas de sedación según protocolo"),
        prototype = ScalePrototype.ICU_SEDATION,
        contentMode = ScaleContentMode.EXTERNAL_RECORD,
        isReferencePrototype = true,
    ),
    ScaleLibraryItem(
        id = "apgar",
        name = "Apgar",
        shortDescription = "Descripción del estado del recién nacido tras el nacimiento.",
        population = "Recién nacidos.",
        services = listOf("Sala de partos", "Neonatología", "Alojamiento conjunto"),
        prototype = ScalePrototype.NEWBORN_APGAR,
        contentMode = ScaleContentMode.EDUCATIONAL_PRACTICE,
        isReferencePrototype = true,
    ),
    ScaleLibraryItem(
        id = "capurro",
        name = "Capurro",
        shortDescription = "Estimación de edad gestacional; identificar variante A o B.",
        population = "Recién nacidos elegibles según protocolo.",
        services = listOf("Neonatología", "Alojamiento conjunto"),
        prototype = ScalePrototype.NEWBORN_CAPURRO,
        contentMode = ScaleContentMode.EDUCATIONAL_PRACTICE,
        isReferencePrototype = true,
    ),
    ScaleLibraryItem(
        id = "norton",
        name = "Norton",
        shortDescription = "Valoración del riesgo de lesión por presión.",
        population = "Adultos; revisar versión y contexto de aplicación.",
        services = listOf("Medicina", "Cirugía", "Geriatría", "UCI", "Rehabilitación"),
        prototype = ScalePrototype.PRESSURE_INJURY_PREVENTION,
        contentMode = ScaleContentMode.EXTERNAL_RECORD,
        isReferencePrototype = true,
    ),
)
