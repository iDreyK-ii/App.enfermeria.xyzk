package com.idreyk.enfermeria360.scales360

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Primera capa de biblioteca nativa.
 *
 * Integración: llamar desde la navegación anfitriona y pasar el callback
 * para abrir la ficha existente. La escena ilustrada se añadirá como recurso
 * independiente; títulos, tarjetas, chips y navegación permanecen nativos.
 */
@Composable
fun Escalas360LibraryScreen(
    onOpenScale: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF6FF))
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Biblioteca clínica",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF392849),
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Escalas y tests de Enfermería",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF776580),
            )
        }

        Surface(
            color = Color(0xFFEFE4FA),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Valoración en el entorno hospitalario",
                        color = Color(0xFF50386C),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Consulta instrumentos por necesidad clínica o servicio. Una misma escala puede utilizarse en varias áreas.",
                        color = Color(0xFF665675),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text("✚", color = Color(0xFF9C6FC1), style = MaterialTheme.typography.headlineLarge)
            }
        }

        Text(
            text = "Prototipos de referencia",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF392849),
            fontWeight = FontWeight.Bold,
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 156.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(Escalas360ReferenceItems, key = { it.id }) { item ->
                ScalePrototypeCard(item = item, onClick = { onOpenScale(item.id) })
            }
        }
    }
}

@Composable
private fun ScalePrototypeCard(
    item: ScaleLibraryItem,
    onClick: () -> Unit,
) {
    val palette = when (item.prototype) {
        ScalePrototype.NEUROLOGICAL_BEDSIDE -> CardPalette(Color(0xFFE3F0FF), Color(0xFF4775A7), "🧠")
        ScalePrototype.PAIN_INTERVIEW -> CardPalette(Color(0xFFFFE5F0), Color(0xFFB84C83), "💗")
        ScalePrototype.ICU_SEDATION -> CardPalette(Color(0xFFDFF4F1), Color(0xFF347E79), "🩺")
        ScalePrototype.NEWBORN_APGAR -> CardPalette(Color(0xFFEDE4FF), Color(0xFF7655A6), "👶")
        ScalePrototype.NEWBORN_CAPURRO -> CardPalette(Color(0xFFE1F2F4), Color(0xFF397F88), "🍼")
        ScalePrototype.PRESSURE_INJURY_PREVENTION -> CardPalette(Color(0xFFFFF0DC), Color(0xFF9A6A2E), "🤍")
        ScalePrototype.GENERAL_CLINICAL -> CardPalette(Color(0xFFF0EAF5), Color(0xFF705C7F), "✚")
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = palette.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
                    .background(Color.White.copy(alpha = 0.72f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center,
            ) {
                // Sustituir el símbolo provisional por la ilustración original aprobada.
                Text(text = palette.symbol, style = MaterialTheme.typography.displayMedium)
            }
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = palette.ink,
            )
            Text(
                text = item.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF5E5368),
                minLines = 2,
            )
            Text(
                text = item.services.take(3).joinToString(" · "),
                style = MaterialTheme.typography.labelSmall,
                color = palette.ink,
            )
            Text(
                text = when (item.contentMode) {
                    ScaleContentMode.DOCUMENTAL -> "Ficha informativa"
                    ScaleContentMode.EDUCATIONAL_PRACTICE -> "Práctica educativa"
                    ScaleContentMode.EXTERNAL_RECORD -> "Registro externo"
                    ScaleContentMode.CALCULATOR_ALLOWED -> "Calculadora autorizada"
                    ScaleContentMode.PERMISSION_REQUIRED -> "Permiso pendiente"
                    ScaleContentMode.CLINICAL_REVIEW_REQUIRED -> "Revisión clínica pendiente"
                },
                style = MaterialTheme.typography.labelMedium,
                color = palette.ink,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private data class CardPalette(
    val background: Color,
    val ink: Color,
    val symbol: String,
)
