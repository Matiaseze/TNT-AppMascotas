package com.appmascotasv2.smartpaws.presentation.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.rotate
import com.appmascotasv2.smartpaws.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CustomCalendar(
    year: Int,
    month: Int,                         // 0-based (Calendar.MONTH)
    selectedDate: Long,
    eventCountByDay: Map<Int, Int>,     // día del mes → cantidad de eventos
    onDaySelected: (Long) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthFormatter  = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val headerCal       = Calendar.getInstance().apply { set(year, month, 1) }
    val headerText      = monthFormatter.format(headerCal.time)
        .replaceFirstChar { it.uppercase() }

    // Primer día de semana del mes (0=Dom, 1=Lun … ajustado para Lun=0)
    val firstDayOfWeek  = ((headerCal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7)
    val daysInMonth     = headerCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Día seleccionado dentro de este mes (0 si no aplica)
    val selectedCal     = Calendar.getInstance().apply { timeInMillis = selectedDate }
    val selectedDay     = if (
        selectedCal.get(Calendar.YEAR) == year &&
        selectedCal.get(Calendar.MONTH) == month
    ) selectedCal.get(Calendar.DAY_OF_MONTH) else 0

    // Hoy
    val todayCal = Calendar.getInstance()
    val todayDay = if (
        todayCal.get(Calendar.YEAR) == year &&
        todayCal.get(Calendar.MONTH) == month
    ) todayCal.get(Calendar.DAY_OF_MONTH) else 0

    val maxCount = (eventCountByDay.values.maxOrNull() ?: 1).coerceAtLeast(1)

    Column(modifier = modifier) {

        // ── Header: mes + flechas ────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    painter            = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Mes anterior",
                    tint               = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text       = headerText,
                style      = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onNextMonth) {
                Icon(
                    painter            = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Mes siguiente",
                    modifier           = Modifier
                        .size(20.dp)
                        .rotate(180f),
                    tint               = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // ── Cabecera dias de semana ──────────────────────────────────────
        val dayLabels = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
        Row(modifier = Modifier.fillMaxWidth()) {
            dayLabels.forEach { label ->
                Text(
                    text      = label,
                    modifier  = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style     = MaterialTheme.typography.labelSmall,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // ── Grid de días ─────────────────────────────────────────────────
        val totalCells = firstDayOfWeek + daysInMonth
        val rows       = (totalCells + 6) / 7

        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val day       = cellIndex - firstDayOfWeek + 1

                    if (day < 1 || day > daysInMonth) {
                        // Celda vacía
                        Box(modifier = Modifier.weight(1f).height(48.dp))
                    } else {
                        val count      = eventCountByDay[day] ?: 0
                        val isSelected = day == selectedDay
                        val isToday    = day == todayDay

                        // Intensidad del fondo proporcional a eventos
                        val bgAlpha    = if (count > 0)
                            (count.toFloat() / maxCount * 0.35f + 0.10f).coerceIn(0.10f, 0.45f)
                        else 0f

                        DayCell(
                            day        = day,
                            count      = count,
                            isSelected = isSelected,
                            isToday    = isToday,
                            bgAlpha    = bgAlpha,
                            modifier   = Modifier.weight(1f),
                            onClick    = {
                                val cal = Calendar.getInstance().apply {
                                    set(year, month, day, 0, 0, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                onDaySelected(cal.timeInMillis)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    count: Int,
    isSelected: Boolean,
    isToday: Boolean,
    bgAlpha: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier         = modifier
            .height(48.dp)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> primary
                    bgAlpha > 0f -> primary.copy(alpha = bgAlpha)
                    else -> Color.Transparent
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text  = day.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    isToday    -> primary
                    else       -> MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
            )

            // Punto proporcional a la cantidad
            if (count > 0 && !isSelected) {
                val dotSize = (3 + (count * 1.5f)).coerceIn(3f, 8f)
                Spacer(Modifier.height(1.dp))
                Box(
                    modifier = Modifier
                        .size(dotSize.dp)
                        .clip(CircleShape)
                        .background(primary)
                )
            }
        }
    }
}