package com.appmascotasv2.smartpaws.data.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.appmascotasv2.smartpaws.domain.model.mascota.EventoMascota
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfReportGenerator {

    fun generateEventReport(
        context: Context,
        mascotaNombre: String,
        mes: Int,
        anio: Int,
        eventos: List<EventoMascota>
    ): File? {
        val pdfDocument = PdfDocument()
        val titlePaint = Paint().apply { textSize = 20f; isFakeBoldText = true }
        val headerPaint = Paint().apply { textSize = 16f; isFakeBoldText = true }
        val textPaint = Paint().apply { textSize = 14f }
        val linePaint = Paint().apply { color = Color.BLACK; strokeWidth = 2f }

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        var yPos = 40f
        
        canvas.drawText("Informe de Eventos - $mascotaNombre", 40f, yPos, titlePaint)
        yPos += 30f
        
        val cal = Calendar.getInstance().apply { 
            set(Calendar.MONTH, mes)
            set(Calendar.YEAR, anio)
        }
        val mesNombre = SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.time).replaceFirstChar { it.uppercase() }
        
        canvas.drawText("Periodo: $mesNombre $anio", 40f, yPos, headerPaint)
        yPos += 40f

        canvas.drawText("Fecha", 40f, yPos, headerPaint)
        canvas.drawText("Título", 150f, yPos, headerPaint)
        canvas.drawText("Tipo", 400f, yPos, headerPaint)
        yPos += 10f
        canvas.drawLine(40f, yPos, 550f, yPos, linePaint)
        yPos += 25f

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        val eventosFiltrados = eventos.filter {
            val evCal = Calendar.getInstance().apply { timeInMillis = it.fecha }
            evCal.get(Calendar.MONTH) == mes && evCal.get(Calendar.YEAR) == anio
        }.sortedBy { it.fecha }

        if (eventosFiltrados.isEmpty()) {
            canvas.drawText("No hay eventos registrados en este periodo.", 40f, yPos, textPaint)
        } else {
            for (evento in eventosFiltrados) {
                if (yPos > 800) break 
                canvas.drawText(dateFormatter.format(Date(evento.fecha)), 40f, yPos, textPaint)
                canvas.drawText(evento.titulo, 150f, yPos, textPaint)
                canvas.drawText(evento.tipo.name, 400f, yPos, textPaint)
                yPos += 20f
                if (evento.descripcion.isNotBlank()) {
                    canvas.drawText("Desc: ${evento.descripcion}", 60f, yPos, Paint().apply { textSize = 12f; color = Color.GRAY })
                    yPos += 20f
                }
                yPos += 10f
            }
        }

        pdfDocument.finishPage(page)

        val reportsDir = File(context.filesDir, "reports")
        if (!reportsDir.exists()) reportsDir.mkdirs()

        val fileName = "Informe_${mascotaNombre}_${mesNombre}_$anio.pdf"
        val file = File(reportsDir, fileName)

        return try {
            pdfDocument.writeTo(FileOutputStream(file))
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            pdfDocument.close()
        }
    }

    fun sharePdf(context: Context, file: File) {
        try {
            val authority = "${context.packageName}.fileprovider"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            context.startActivity(Intent.createChooser(intent, "Compartir Informe PDF"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error al compartir: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
