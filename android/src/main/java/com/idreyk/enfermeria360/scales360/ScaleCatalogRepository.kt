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

data class CatalogReference(val title: String, val organization: String, val url: String)
data class CatalogInstrument(
    val id: String,
    val name: String,
    val category: String,
    val populations: List<String>,
    val services: List<String>,
    val purpose: String,
    val summary: String,
    val nursingContext: String,
    val contentMode: String,
    val visualPrototype: String,
    val limits: String,
    val rights: String,
    val references: List<CatalogReference>
)

private val HospitalServices = mapOf(
    "emergency" to "Emergencia","icu-adult" to "UCI adulto","medicine" to "Medicina",
    "surgery" to "Cirugía","operating-room" to "Centro quirúrgico","recovery" to "URPA",
    "cardiology" to "Cardiología","respiratory" to "Neumología","neurology" to "Neurología",
    "nephrology" to "Nefrología","obstetrics" to "Obstetricia","postpartum" to "Puerperio",
    "neonatology" to "Neonatología","pediatrics" to "Pediatría","picu" to "UCI pediátrica",
    "geriatrics" to "Geriatría","mental-health" to "Salud mental","oncology" to "Oncología",
    "burns-wounds" to "Quemados/heridas","ostomy" to "Ostomías","rehab" to "Rehabilitación",
    "infection" to "Infecciones/sepsis","vte" to "Prevención TEV"
)
private fun JSONArray.toStrings(): List<String> = buildList { for (i in 0 until length()) add(optString(i)) }
private fun baseAreaServices(area: String): List<String> = when (area) {
    "Neurología" -> listOf("emergency","icu-adult","medicine","neurology","surgery")
    "Dolor" -> listOf("emergency","medicine","surgery","icu-adult","recovery","pediatrics","picu","neonatology","oncology")
    "Delirium y sedación" -> listOf("icu-adult","picu","medicine","emergency","recovery")
    "Piel y caídas" -> listOf("medicine","surgery","icu-adult","pediatrics","picu","geriatrics","rehab")
    "Neonatología" -> listOf("neonatology","obstetrics","postpartum","picu")
    "Deterioro clínico" -> listOf("emergency","medicine","icu-adult","pediatrics","picu","obstetrics")
    "Nutrición" -> listOf("medicine","surgery","geriatrics","nephrology","oncology","pediatrics")
    "Funcionalidad" -> listOf("geriatrics","medicine","rehab","neurology","surgery")
    "Cognición y salud mental" -> listOf("mental-health","geriatrics","medicine","neurology")
    else -> listOf("medicine")
}
private fun parseReferences(sourceIds: List<String>, map: Map<String, CatalogReference>): List<CatalogReference> =
    sourceIds.mapNotNull { map[it] }

private fun loadCatalog(context: Context): List<CatalogInstrument> {
    val byId = linkedMapOf<String, CatalogInstrument>()
    val refs = linkedMapOf<String, CatalogReference>()

    fun readJson(path: String): JSONObject =
        context.assets.open(path).bufferedReader(Charsets.UTF_8).use { JSONObject(it.readText()) }

    val base = readJson("source/catalogo_fuente_Enfermer-a-app_2026-11-02.json")
    val baseRefs = base.optJSONArray("fuentes") ?: JSONArray()
    for (i in 0 until baseRefs.length()) {
        val r = baseRefs.getJSONObject(i)
        refs[r.optString("id")] = CatalogReference(r.optString("titulo"),r.optString("editor"),r.optString("url"))
    }
    val baseItems = base.optJSONArray("instrumentos") ?: JSONArray()
    for (i in 0 until baseItems.length()) {
        val item = baseItems.getJSONObject(i)
        val sourceIds = item.optJSONArray("fuentes")?.toStrings().orEmpty()
        val id = item.optString("id")
        byId[id] = CatalogInstrument(
            id=id,name=item.optString("nombre"),category=item.optString("area"),
            populations=item.optJSONArray("poblaciones")?.toStrings().orEmpty(),
            services=baseAreaServices(item.optString("area")).mapNotNull { HospitalServices[it] },
            purpose=item.optString("objetivo"),summary=item.optString("metodo"),
            nursingContext=item.optString("contexto_enfermeria","Aplicar/registrar solo la versión prevista por el protocolo local."),
            contentMode=when(item.optString("estado")) {
                "calculo" -> "Práctica educativa"
                "simulacion" -> "Simulación educativa"
                "registro" -> "Registro externo"
                "permiso" -> "Permiso pendiente"
                else -> "Ficha documental"
            },
            visualPrototype=item.optString("prototipo_visual","clinico-general"),
            limits=item.optString("limites"),rights=item.optString("derechos"),
            references=parseReferences(sourceIds,refs)
        )
    }

    val expansionFiles = context.assets.list("expansiones").orEmpty().filter { it.endsWith(".json") }.sorted()
    for (file in expansionFiles) {
        val expansion = readJson("expansiones/$file")
        val expansionRefs = expansion.optJSONArray("references") ?: JSONArray()
        for (i in 0 until expansionRefs.length()) {
            val r = expansionRefs.getJSONObject(i)
            refs[r.optString("id")] = CatalogReference(r.optString("title"),r.optString("organization"),r.optString("url"))
        }
        val items = expansion.optJSONArray("instruments") ?: JSONArray()
        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)
            val ids = item.optJSONArray("source_ids")?.toStrings().orEmpty()
            val services = item.optJSONArray("services")?.toStrings().orEmpty().map { HospitalServices[it] ?: it }
            val id = item.optString("id")
            byId[id] = CatalogInstrument(
                id=id,name=item.optString("name"),category=item.optString("category"),
                populations=item.optJSONArray("populations")?.toStrings().orEmpty(),
                services=services,purpose=item.optString("purpose"),summary=item.optString("what_it_assesses"),
                nursingContext=item.optString("nursing_context"),
                contentMode=item.optString("content_mode","DOCUMENTAL"),
                visualPrototype=item.optString("visual_prototype","general"),
                limits=item.optString("rights_status"),rights=item.optString("local_adoption"),
                references=parseReferences(ids,refs)
            )
        }
    }

    return byId.values.sortedWith(
        compareBy<CatalogInstrument> { when(it.id) {
            "glasgow" -> 0; "eva" -> 1; "rass" -> 2; "apgar" -> 3; "capurro-b" -> 4; "norton" -> 5; else -> 10
        }}.thenBy { it.name }
    )
}

private fun loadGuideObject(context: Context, id: String): JSONObject? = runCatching {
    val root = context.assets.open("guias_seis_prototipos_clinicos_v1.json")
        .bufferedReader(Charsets.UTF_8).use { JSONObject(it.readText()) }
    val a = root.getJSONArray("instruments")
    for (i in 0 until a.length()) {
        val g = a.getJSONObject(i)
        if (g.optString("id") == id) return@runCatching g
    }
    null
}.getOrNull()

private fun modeLabel(mode: String): String = when (mode) {
    "EDUCATIONAL_PRACTICE" -> "Práctica educativa"
    "EDUCATIONAL_SIMULATION" -> "Simulación educativa"
    "EXTERNAL_RECORD" -> "Registro externo"
    "PERMISSION_REQUIRED" -> "Permiso pendiente"
    "CLINICAL_REVIEW_REQUIRED" -> "Revisión clínica pendiente"
    else -> "Ficha documental"
}
private fun accentForScale(id: String): Color = when (id) {
    "glasgow" -> Color(0xFF7850B5)
    "eva" -> Color(0xFFD64792)
    "rass" -> Color(0xFF23AEB3)
    "apgar" -> Color(0xFFB34195)
    "capurro" ,"capurro-a","capurro-b" -> Color(0xFF299EAA)
    "norton" -> Color(0xFFCC873E)
    else -> Color(0xFFA271CD)
}
private fun artPath(id: String): String = when (id) {
    "glasgow" -> "scales360_art/scale_glasgow.webp"
    "eva" -> "scales360_art/scale_eva.webp"
    "rass" -> "scales360_art/scale_rass.webp"
    "apgar" -> "scales360_art/scale_apgar.webp"
    "capurro","capurro-a","capurro-b" -> "scales360_art/scale_capurro.webp"
    "norton" -> "scales360_art/scale_norton.webp"
    else -> "scales360_art/scale_general.webp"
}
private fun detailRoute(id: String): String = when (id) {
    "glasgow" -> "glasgow"
    "eva" -> "eva"
    "rass" -> "registro-escala/rass"
    "apgar" -> "escala/apgar"
    "capurro","capurro-a","capurro-b" -> "capurro"
    "norton" -> "registro-escala/norton"
    "braden" -> "registro-escala/braden"
    "morse" -> "registro-escala/morse"
    "4at" -> "test-4at"
    "silverman" -> "escala/silverman"
    else -> ""
}

@Composable
private fun CatalogArt(id: String, accent: Color, modifier: Modifier, description: String) {
    val context = LocalContext.current
    val bitmap by produceState<ImageBitmap?>(null, id) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                context.assets.open(artPath(id)).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
            }.getOrNull()
        }
    }
    Box(modifier.clip(RoundedCornerShape(18.dp)).background(Brush.linearGradient(listOf(accent.copy(alpha=.24f),Color.White,accent.copy(alpha=.10f))))) {
        if(bitmap!=null) Image(bitmap!!, description, Modifier.fillMaxSize(), contentScale=ContentScale.Crop)
        else Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center) {
            Text("✚",color=accent,style=MaterialTheme.typography.headlineLarge)
        }
    }
}

/**
 * Biblioteca completa: carga las 45 fichas fuente y las expansiones por especialidad.
 * El catálogo mantiene una ficha por instrumento y enlaza servicios, evitando duplicados por área.
 */
@Composable
fun Escalas360CompleteCatalogScreen(
    onBack: () -> Unit,
    onOpenExistingRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context=LocalContext.current
    var catalog by remember { mutableStateOf<List<CatalogInstrument>>(emptyList()) }
    var selected by remember { mutableStateOf<CatalogInstrument?>(null) }
    var search by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("Todas") }
    LaunchedEffect(Unit) { catalog=withContext(Dispatchers.IO){runCatching{loadCatalog(context)}.getOrDefault(emptyList())} }

    if(selected!=null) {
        CatalogInstrumentDetail(selected!!, onBack={selected=null}, onOpenExistingRoute=onOpenExistingRoute)
        return
    }
    val areas=catalog.map{it.category}.distinct().sorted()
    val visible=catalog.filter {
        val q=search.isBlank() || it.name.contains(search,true) || it.purpose.contains(search,true) ||
            it.category.contains(search,true) || it.services.any { s->s.contains(search,true) }
        q && (area=="Todas" || it.category==area)
    }
    BoxWithConstraints(modifier.fillMaxSize().background(Color(0xFFFAF6FF))) {
        val tablet=maxWidth>=680.dp
        Column(Modifier.fillMaxSize().padding(horizontal=if(tablet)28.dp else 16.dp,vertical=12.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically) {
                IconButton(onClick=onBack){Text("‹",style=MaterialTheme.typography.headlineLarge,color=Color(0xFFA271CD))}
                Column(Modifier.weight(1f)) {
                    Text("Escalas y tests",style=MaterialTheme.typography.headlineSmall,color=Color(0xFF30253E),fontWeight=FontWeight.Bold)
                    Text("${catalog.size} instrumentos · biblioteca transversal",style=MaterialTheme.typography.bodySmall,color=Color(0xFF7A6785))
                }
            }
            OutlinedTextField(search,{search=it},Modifier.fillMaxWidth(),singleLine=true,shape=RoundedCornerShape(18.dp),placeholder={Text("Buscar por escala, área o servicio…")})
            Row(horizontalArrangement=Arrangement.spacedBy(7.dp)) {
                listOf("Todas","UCI y carga asistencial","Neonatología","Piel y caídas").forEach { f ->
                    FilterChip(selected=area==f,onClick={area=f},label={Text(f)})
                }
            }
            Text(if(visible.isEmpty()) "No se encontraron instrumentos" else "Resultados: ${visible.size}",style=MaterialTheme.typography.titleMedium,color=Color(0xFF30253E),fontWeight=FontWeight.Bold)
            LazyVerticalGrid(
                columns=GridCells.Fixed(if(tablet)3 else 2),
                modifier=Modifier.fillMaxSize(),
                contentPadding=PaddingValues(bottom=26.dp),
                horizontalArrangement=Arrangement.spacedBy(10.dp),
                verticalArrangement=Arrangement.spacedBy(10.dp)
            ) {
                items(visible,key={it.id}) { item -> CatalogCard(item){selected=item} }
            }
        }
    }
}

@Composable
private fun CatalogCard(item: CatalogInstrument,onClick:()->Unit) {
    val accent=accentForScale(item.id)
    Card(onClick=onClick,shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Color.White),elevation=CardDefaults.cardElevation(2.dp)) {
        Column {
            Box(Modifier.fillMaxWidth().background(accent).padding(horizontal=11.dp,vertical=9.dp)) {
                Text(item.name,color=Color.White,style=MaterialTheme.typography.titleSmall,fontWeight=FontWeight.ExtraBold)
            }
            CatalogArt(item.id,accent,Modifier.fillMaxWidth().height(116.dp),"Ilustración de ${item.name}")
            Column(Modifier.padding(11.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                Text(item.purpose,color=Color(0xFF30253E),style=MaterialTheme.typography.bodySmall,maxLines=3)
                Text(item.category,color=accent,style=MaterialTheme.typography.labelSmall,fontWeight=FontWeight.Bold)
                Text(item.services.take(3).joinToString(" · "),color=Color(0xFF7A6785),style=MaterialTheme.typography.labelSmall,maxLines=2)
                Text(modeLabel(item.contentMode),color=accent,style=MaterialTheme.typography.labelSmall,fontWeight=FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CatalogInstrumentDetail(item:CatalogInstrument,onBack:()->Unit,onOpenExistingRoute:(String)->Unit) {
    val context=LocalContext.current
    val guide by produceState<JSONObject?>(null,item.id){value=withContext(Dispatchers.IO){loadGuideObject(context,item.id)}}
    val accent=accentForScale(item.id)
    val uriHandler=LocalUriHandler.current
    BoxWithConstraints(Modifier.fillMaxSize().background(Color(0xFFFAF6FF))) {
        val tablet=maxWidth>=720.dp
        Column(Modifier.fillMaxSize().padding(horizontal=if(tablet)28.dp else 16.dp,vertical=12.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically) {
                IconButton(onClick=onBack){Text("‹",style=MaterialTheme.typography.headlineLarge,color=accent)}
                Column(Modifier.weight(1f)) {
                    Text(item.name,style=MaterialTheme.typography.titleLarge,color=Color(0xFF30253E),fontWeight=FontWeight.Bold)
                    Text(item.category,style=MaterialTheme.typography.bodySmall,color=accent)
                }
            }
            Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(10.dp)) {
                if(tablet) {
                    Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
                        CatalogArt(item.id,accent,Modifier.weight(1f).height(235.dp),item.name)
                        Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                            InfoPanel("¿Para qué sirve?",item.purpose,accent)
                            InfoPanel("Población",item.populations.joinToString(" · "),accent)
                            InfoPanel("Servicios",item.services.joinToString(" · "),accent)
                        }
                    }
                } else {
                    CatalogArt(item.id,accent,Modifier.fillMaxWidth().height(205.dp),item.name)
                    InfoPanel("¿Para qué sirve?",item.purpose,accent)
                    InfoPanel("Población",item.populations.joinToString(" · "),accent)
                    InfoPanel("Servicios",item.services.joinToString(" · "),accent)
                }
                InfoPanel("¿Qué evalúa?",item.summary,accent)
                InfoPanel("Rol de Enfermería",item.nursingContext,accent)
                if(item.limits.isNotBlank())InfoPanel("Limitaciones / derechos",item.limits,accent)
                if(item.rights.isNotBlank())InfoPanel("Adopción y contexto local",item.rights,accent)
                if(guide!=null) {
                    val components=guide!!.optJSONArray("components")
                    if(components!=null && components.length()>0) {
                        Text("Componentes detallados",style=MaterialTheme.typography.titleMedium,color=Color(0xFF30253E),fontWeight=FontWeight.Bold)
                        for(i in 0 until components.length()) {
                            val c=components.getJSONObject(i)
                            InfoPanel(c.optString("name"),c.optString("description"),accent)
                        }
                    }
                    val limits=guide!!.optJSONArray("limitations")
                    if(limits!=null && limits.length()>0) InfoPanel("Precauciones", (0 until limits.length()).joinToString("\n• ",prefix="• "){limits.optString(it)},accent)
                }
                if(item.references.isNotEmpty()) {
                    Text("Fuentes",style=MaterialTheme.typography.titleMedium,color=Color(0xFF30253E),fontWeight=FontWeight.Bold)
                    item.references.forEach { ref ->
                        Card(onClick={runCatching{uriHandler.openUri(ref.url)}},colors=CardDefaults.cardColors(containerColor=Color.White),shape=RoundedCornerShape(15.dp)) {
                            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                                Text(ref.title,color=accent,style=MaterialTheme.typography.labelLarge)
                                Text(ref.organization,color=Color(0xFF7A6785),style=MaterialTheme.typography.bodySmall)
                                Text("Abrir fuente ↗",color=accent,style=MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
                InfoPanel("Estado de la ficha",modeLabel(item.contentMode)+" · revisión clínica pendiente cuando se indica. No utilizar para tomar decisiones sobre pacientes reales hasta aprobación institucional.",accent)
            }
            val route=detailRoute(item.id)
            if(route.isNotBlank()) {
                Button(onClick={onOpenExistingRoute(route)},modifier=Modifier.fillMaxWidth().heightIn(min=52.dp),shape=RoundedCornerShape(17.dp),colors=ButtonDefaults.buttonColors(containerColor=accent)) {
                    Text(if(item.contentMode=="EXTERNAL_RECORD"||item.contentMode=="registro") "Abrir registro externo" else "Abrir práctica / registro",color=Color.White,fontWeight=FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun InfoPanel(title:String,body:String,accent:Color) {
    Surface(color=Color.White,shape=RoundedCornerShape(17.dp),tonalElevation=1.dp) {
        Column(Modifier.fillMaxWidth().padding(13.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
            Text(title,color=accent,style=MaterialTheme.typography.titleSmall,fontWeight=FontWeight.ExtraBold)
            Text(body,color=Color(0xFF30253E),style=MaterialTheme.typography.bodyMedium)
        }
    }
}
