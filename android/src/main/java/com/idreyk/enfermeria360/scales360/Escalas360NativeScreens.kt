package com.idreyk.enfermeria360.scales360

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

private val EscalasBg = Color(0xFFFAF6FF)
private val EscalasInk = Color(0xFF30253E)
private val EscalasMuted = Color(0xFF7A6785)
private val EscalasBorder = Color(0xFFEBDFF1)
private val EscalasPurple = Color(0xFFA271CD)
private val EscalasPink = Color(0xFFD64792)
private val EscalasTeal = Color(0xFF23AEB3)
private val EscalasBlue = Color(0xFF6898DA)
private val EscalasGold = Color(0xFFD9A04B)

private data class GuideComponent(val name: String, val description: String)
private data class GuideReference(val title: String, val organization: String, val url: String)
private data class GuideVariant(
    val id: String,
    val name: String,
    val localReference: String,
    val components: List<String>,
    val formula: String,
    val correction: String
)
private data class ScaleGuideUi(
    val id: String,
    val name: String,
    val abbreviation: String,
    val purpose: String,
    val population: String,
    val scene: String,
    val services: List<String>,
    val components: List<GuideComponent>,
    val levels: List<Pair<String, String>>,
    val variants: List<GuideVariant>,
    val scoringSummary: String,
    val timing: String,
    val nursingContext: String,
    val recording: String,
    val interpretation: String,
    val limitations: List<String>,
    val appMode: String,
    val currentRoute: String,
    val references: List<GuideReference>,
    val reviewStatus: String
)

private fun JSONObject.stringOrEmpty(key: String): String = optString(key, "")
private fun JSONObject.stringList(key: String): List<String> {
    val array = optJSONArray(key) ?: return emptyList()
    return buildList { for (i in 0 until array.length()) add(array.optString(i)) }
}
private fun parseStringArray(array: JSONArray?): List<String> {
    if (array == null) return emptyList()
    return buildList { for (i in 0 until array.length()) add(array.optString(i)) }
}
private fun loadGuides(context: Context): Map<String, ScaleGuideUi> {
    val text = context.assets.open("guias_seis_prototipos_clinicos_v1.json")
        .bufferedReader(Charsets.UTF_8).use { it.readText() }
    val root = JSONObject(text)
    val instruments = root.getJSONArray("instruments")
    val guides = linkedMapOf<String, ScaleGuideUi>()
    for (i in 0 until instruments.length()) {
        val item = instruments.getJSONObject(i)
        val components = buildList {
            val a = item.optJSONArray("components") ?: JSONArray()
            for (j in 0 until a.length()) {
                val c = a.getJSONObject(j)
                add(GuideComponent(c.stringOrEmpty("name"), c.stringOrEmpty("description")))
            }
        }
        val levels = buildList {
            val a = item.optJSONArray("level_summary") ?: JSONArray()
            for (j in 0 until a.length()) {
                val l = a.getJSONObject(j)
                add(l.stringOrEmpty("value") to l.stringOrEmpty("label"))
            }
        }
        val variants = buildList {
            val a = item.optJSONArray("variants") ?: JSONArray()
            for (j in 0 until a.length()) {
                val v = a.getJSONObject(j)
                add(
                    GuideVariant(
                        v.stringOrEmpty("id"), v.stringOrEmpty("name"),
                        v.stringOrEmpty("local_reference"), parseStringArray(v.optJSONArray("components")),
                        v.stringOrEmpty("formula"), v.stringOrEmpty("correction")
                    )
                )
            }
        }
        val refs = buildList {
            val a = item.optJSONArray("references") ?: JSONArray()
            for (j in 0 until a.length()) {
                val r = a.getJSONObject(j)
                add(GuideReference(r.stringOrEmpty("title"), r.stringOrEmpty("organization"), r.stringOrEmpty("url")))
            }
        }
        guides[item.stringOrEmpty("id")] = ScaleGuideUi(
            id = item.stringOrEmpty("id"),
            name = item.stringOrEmpty("name"),
            abbreviation = item.stringOrEmpty("abbreviation"),
            purpose = item.stringOrEmpty("purpose"),
            population = item.stringOrEmpty("population"),
            scene = item.stringOrEmpty("hero_scene"),
            services = item.stringList("services"),
            components = components,
            levels = levels,
            variants = variants,
            scoringSummary = item.stringOrEmpty("scoring_summary"),
            timing = item.stringOrEmpty("timing"),
            nursingContext = item.stringOrEmpty("use_in_nursing"),
            recording = item.stringOrEmpty("recording"),
            interpretation = item.stringOrEmpty("interpretation"),
            limitations = item.stringList("limitations"),
            appMode = item.stringOrEmpty("app_mode"),
            currentRoute = item.stringOrEmpty("current_route"),
            references = refs,
            reviewStatus = item.stringOrEmpty("review_status")
        )
    }
    return guides
}

private fun accentFor(id: String): Color = when (id) {
    "glasgow" -> Color(0xFF7850B5)
    "eva" -> EscalasPink
    "rass" -> EscalasTeal
    "apgar" -> Color(0xFFB34195)
    "capurro" -> Color(0xFF299EAA)
    "norton" -> Color(0xFFCC873E)
    else -> EscalasPurple
}
private fun artFor(id: String): String = when (id) {
    "glasgow" -> "scales360_art/scale_glasgow.webp"
    "eva" -> "scales360_art/scale_eva.webp"
    "rass" -> "scales360_art/scale_rass.webp"
    "apgar" -> "scales360_art/scale_apgar.webp"
    "capurro" -> "scales360_art/scale_capurro.webp"
    "norton" -> "scales360_art/scale_norton.webp"
    else -> "scales360_art/scale_general.webp"
}
private fun labelForMode(mode: ScaleContentMode): String = when (mode) {
    ScaleContentMode.DOCUMENTAL -> "Ficha educativa"
    ScaleContentMode.EDUCATIONAL_PRACTICE -> "Práctica educativa"
    ScaleContentMode.EXTERNAL_RECORD -> "Registro externo"
    ScaleContentMode.CALCULATOR_ALLOWED -> "Calculadora autorizada"
    ScaleContentMode.PERMISSION_REQUIRED -> "Permiso pendiente"
    ScaleContentMode.CLINICAL_REVIEW_REQUIRED -> "Revisión clínica pendiente"
}

@Composable
private fun ScaleHeroArt(
    assetPath: String,
    accent: Color,
    modifier: Modifier = Modifier,
    contentDescription: String
) {
    val context = LocalContext.current
    val bitmap by produceState<ImageBitmap?>(initialValue = null, assetPath) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                context.assets.open(assetPath).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
            }.getOrNull()
        }
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(accent.copy(alpha = .25f), Color.White, accent.copy(alpha = .12f)))),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap!!,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("✚", color = accent, style = MaterialTheme.typography.headlineLarge)
                Text("Ilustración clínica", color = EscalasInk, style = MaterialTheme.typography.labelMedium)
                Text("Recurso visual local", color = EscalasMuted, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

/**
 * Pantalla raíz del módulo. Las pantallas y controles son nativos Compose;
 * el PNG/WebP se utiliza únicamente como ilustración editorial.
 *
 * onOpenExistingRoute debe conectarse con la navegación del aplicativo anfitrión:
 * Glasgow -> glasgow; EVA -> eva; Apgar -> escala/apgar; Capurro -> capurro;
 * RASS/Norton -> registro externo adulto.
 */
@Composable
fun Escalas360NativeModuleScreen(
    onBack: () -> Unit,
    onOpenExistingRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var guides by remember { mutableStateOf<Map<String, ScaleGuideUi>>(emptyMap()) }
    var selectedId by remember { mutableStateOf<String?>(null) }
    var search by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("Todas") }
    LaunchedEffect(Unit) { guides = withContext(Dispatchers.IO) { runCatching { loadGuides(context) }.getOrDefault(emptyMap()) } }

    val selectedGuide = selectedId?.let { guides[it] }
    if (selectedGuide != null) {
        ScaleGuideDetailScreen(
            guide = selectedGuide,
            onBack = { selectedId = null },
            onOpenExistingRoute = onOpenExistingRoute,
            modifier = modifier
        )
        return
    }

    val filtered = Escalas360ReferenceItems.filter { item ->
        val queryMatch = search.isBlank() ||
            item.name.contains(search, ignoreCase = true) ||
            item.shortDescription.contains(search, ignoreCase = true) ||
            item.services.any { it.contains(search, ignoreCase = true) }
        val serviceMatch = filter == "Todas" || item.services.any { it.contains(filter, ignoreCase = true) }
        queryMatch && serviceMatch
    }

    BoxWithConstraints(modifier.fillMaxSize().background(EscalasBg)) {
        val tablet = maxWidth >= 600.dp
        Column(
            Modifier.fillMaxSize().padding(horizontal = if (tablet) 28.dp else 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Text("‹", color = EscalasPurple, style = MaterialTheme.typography.headlineLarge) }
                Column(Modifier.weight(1f)) {
                    Text("Escalas y tests", color = EscalasInk, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Biblioteca clínica de Enfermería", color = EscalasMuted, style = MaterialTheme.typography.bodySmall)
                }
                Text("iDreyK 360", color = EscalasPurple, style = MaterialTheme.typography.labelLarge)
            }
            Surface(color = Color(0xFFEFE5FA), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Valoración en el entorno hospitalario", color = Color(0xFF51366F), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Consulta una ficha transversal y encuentra los servicios donde se utiliza. Cada instrumento conserva su versión y estado de uso.", color = Color(0xFF69597A), style = MaterialTheme.typography.bodySmall)
                    }
                    Text("✚", color = EscalasPurple, style = MaterialTheme.typography.headlineLarge)
                }
            }
            OutlinedTextField(
                value = search, onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Buscar escala o test…") },
                shape = RoundedCornerShape(18.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Todas","UCI","Emergencia","Neonatología","Geriatría").forEach { label ->
                    FilterChip(selected = filter == label, onClick = { filter = label }, label = { Text(label) })
                }
            }
            Text("Prototipos de referencia", color = EscalasInk, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (tablet) 3 else 2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered, key = { it.id }) { item ->
                    ScaleLibraryNativeCard(item = item, onClick = { selectedId = item.id })
                }
            }
        }
    }
}

@Composable
private fun ScaleLibraryNativeCard(item: ScaleLibraryItem, onClick: () -> Unit) {
    val accent = accentFor(item.id)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Box(Modifier.fillMaxWidth().background(accent).padding(horizontal = 12.dp, vertical = 10.dp)) {
                Text(item.name, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            }
            ScaleHeroArt(artFor(item.id), accent, Modifier.fillMaxWidth().height(138.dp), "Ilustración clínica de ${item.name}")
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(item.shortDescription, color = EscalasInk, style = MaterialTheme.typography.bodySmall, minLines = 2)
                Text(item.services.take(3).joinToString(" · "), color = EscalasMuted, style = MaterialTheme.typography.labelSmall)
                Surface(color = accent.copy(alpha = .12f), shape = RoundedCornerShape(12.dp)) {
                    Text(labelForMode(item.contentMode), color = accent, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp))
                }
                Text("Ver ficha  →", color = accent, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun ScaleGuideDetailScreen(
    guide: ScaleGuideUi,
    onBack: () -> Unit,
    onOpenExistingRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = accentFor(guide.id)
    val uriHandler = LocalUriHandler.current
    val modeLabel = when (guide.appMode) {
        "EXTERNAL_RECORD" -> "Registro externo"
        "EDUCATIONAL_SIMULATION" -> "Simulación educativa"
        "EDUCATIONAL_PRACTICE" -> "Práctica educativa"
        else -> "Ficha educativa"
    }
    BoxWithConstraints(modifier.fillMaxSize().background(EscalasBg)) {
        val tablet = maxWidth >= 720.dp
        Column(Modifier.fillMaxSize().padding(horizontal = if (tablet) 28.dp else 18.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Text("‹", color = accent, style = MaterialTheme.typography.headlineLarge) }
                Column(Modifier.weight(1f)) {
                    Text(guide.name, color = EscalasInk, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(guide.abbreviation, color = accent, style = MaterialTheme.typography.labelMedium)
                }
                Surface(color = accent.copy(alpha = .12f), shape = RoundedCornerShape(12.dp)) {
                    Text(modeLabel, color = accent, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(8.dp))
                }
            }
            Column(
                Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (tablet) {
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
                        ScaleHeroArt(artFor(guide.id), accent, Modifier.weight(1f).height(250.dp), guide.scene)
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                            DetailPanel("¿Qué es y para qué sirve?", guide.purpose, accent)
                            DetailPanel("Población", guide.population, accent)
                            if (guide.services.isNotEmpty()) DetailPanel("Servicios", guide.services.joinToString(" · "), accent)
                        }
                    }
                } else {
                    ScaleHeroArt(artFor(guide.id), accent, Modifier.fillMaxWidth().height(210.dp), guide.scene)
                    DetailPanel("¿Qué es y para qué sirve?", guide.purpose, accent)
                    DetailPanel("Población", guide.population, accent)
                    if (guide.services.isNotEmpty()) DetailPanel("Servicios donde se utiliza", guide.services.joinToString(" · "), accent)
                }
                if (guide.components.isNotEmpty()) {
                    Text("Componentes de valoración", color = EscalasInk, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    guide.components.forEachIndexed { index, component ->
                        DetailPanel("${index + 1}. ${component.name}", component.description, accent)
                    }
                }
                if (guide.levels.isNotEmpty()) {
                    Text("Niveles de referencia", color = EscalasInk, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    guide.levels.chunked(2).forEach { pair ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            pair.forEach { (value, label) ->
                                Surface(color = accent.copy(alpha = .10f), shape = RoundedCornerShape(14.dp), modifier = Modifier.weight(1f)) {
                                    Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Text(value, color = accent, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                                        Text(label, color = EscalasInk, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                            if (pair.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
                if (guide.variants.isNotEmpty()) {
                    Text("Variantes", color = EscalasInk, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    guide.variants.forEach { v ->
                        DetailPanel(v.name, listOf(v.localReference, v.components.joinToString(" · "), v.formula, v.correction).filter { it.isNotBlank() }.joinToString("\n"), accent)
                    }
                }
                if (guide.scoringSummary.isNotBlank()) DetailPanel("Puntuación", guide.scoringSummary, accent)
                if (guide.timing.isNotBlank()) DetailPanel("Momento de evaluación", guide.timing, accent)
                DetailPanel("Aplicación en Enfermería", guide.nursingContext, accent)
                if (guide.recording.isNotBlank()) DetailPanel("Registro y seguridad", guide.recording, accent)
                DetailPanel("Interpretación y límites", guide.interpretation, accent)
                if (guide.limitations.isNotEmpty()) {
                    DetailPanel("Precauciones", guide.limitations.joinToString("\n• ", prefix = "• "), accent)
                }
                if (guide.references.isNotEmpty()) {
                    Text("Fuentes y referencias", color = EscalasInk, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    guide.references.forEach { ref ->
                        Card(onClick = { runCatching { uriHandler.openUri(ref.url) } }, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(15.dp)) {
                            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                                Text(ref.title, color = accent, style = MaterialTheme.typography.labelLarge)
                                Text(ref.organization, color = EscalasMuted, style = MaterialTheme.typography.bodySmall)
                                Text("Abrir referencia ↗", color = accent, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
                DetailPanel("Estado de revisión", "${guide.reviewStatus}. Contenido educativo: requiere validación profesional y protocolo local antes de atención real.", accent)
            }
            Spacer(Modifier.height(10.dp))
            val canPractice = guide.appMode == "EDUCATIONAL_PRACTICE" || guide.appMode == "EDUCATIONAL_SIMULATION" || guide.appMode == "EXTERNAL_RECORD"
            if (canPractice) {
                Button(
                    onClick = { onOpenExistingRoute(guide.currentRoute) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accent),
                    shape = RoundedCornerShape(17.dp)
                ) {
                    Text(if (guide.appMode == "EXTERNAL_RECORD") "Abrir registro externo" else "Abrir práctica educativa", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DetailPanel(title: String, body: String, accent: Color) {
    Surface(color = Color.White, shape = RoundedCornerShape(18.dp), tonalElevation = 1.dp) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, color = accent, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
            Text(body, color = EscalasInk, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
