package com.appmascotasv2.smartpaws.presentation.feature.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.appmascotasv2.smartpaws.data.worker.NotificationWorker
import com.appmascotasv2.smartpaws.domain.model.mascota.EventoMascota
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.repository.EventoMascotaRepository
import com.appmascotasv2.smartpaws.domain.repository.MascotaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CalendarioViewModel(
    application: Application,
    private val eventoRepository: EventoMascotaRepository,
    private val mascotaRepository: MascotaRepository,
    private val userId: Int
) : AndroidViewModel(application) {

    val mascotas: StateFlow<List<Mascota>> = mascotaRepository.getMascotasByOwner(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val eventos: StateFlow<List<EventoMascota>> = eventoRepository.getAllEventos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDate = MutableStateFlow(startOfDay(System.currentTimeMillis()))
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    private val _visibleMonth = MutableStateFlow(
        Calendar.getInstance().let { it.get(Calendar.YEAR) to it.get(Calendar.MONTH) }
    )
    val visibleMonth: StateFlow<Pair<Int, Int>> = _visibleMonth.asStateFlow()

    fun selectDate(timestamp: Long) {
        _selectedDate.value = startOfDay(timestamp)
    }

    fun previousMonth() {
        val (year, month) = _visibleMonth.value
        val cal = Calendar.getInstance().apply { set(year, month, 1) }
        cal.add(Calendar.MONTH, -1)
        _visibleMonth.value = cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH)
    }

    fun nextMonth() {
        val (year, month) = _visibleMonth.value
        val cal = Calendar.getInstance().apply { set(year, month, 1) }
        cal.add(Calendar.MONTH, 1)
        _visibleMonth.value = cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH)
    }

    fun addEvento(evento: EventoMascota) {
        viewModelScope.launch {
            val id = eventoRepository.addEvento(evento)
            scheduleNotification(evento.copy(id = id.toInt()))
        }
    }

    private fun scheduleNotification(evento: EventoMascota) {
        val notificationTime = evento.fecha - TimeUnit.HOURS.toMillis(1)
        val delay = notificationTime - System.currentTimeMillis()
        
        if (delay > 0) {
            val data = Data.Builder()
                .putString("titulo", "Recordatorio: ${evento.titulo}")
                .putString("descripcion", "Turno de tu mascota en 1 hora")
                .putInt("eventoId", evento.id)
                .build()

            val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("evento_${evento.id}")
                .build()

            WorkManager.getInstance(getApplication()).enqueue(notificationWork)
        }
    }

    fun deleteEvento(evento: EventoMascota) {
        viewModelScope.launch {
            eventoRepository.deleteEvento(evento)
            WorkManager.getInstance(getApplication()).cancelAllWorkByTag("evento_${evento.id}")
        }
    }

    private fun startOfDay(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}

class CalendarioViewModelFactory(
    private val application: Application,
    private val eventoRepository: EventoMascotaRepository,
    private val mascotaRepository: MascotaRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarioViewModel(application, eventoRepository, mascotaRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}