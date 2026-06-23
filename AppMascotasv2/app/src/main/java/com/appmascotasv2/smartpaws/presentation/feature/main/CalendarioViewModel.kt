package com.appmascotasv2.smartpaws.presentation.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appmascotasv2.smartpaws.domain.model.mascota.EventoMascota
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.repository.EventoMascotaRepository
import com.appmascotasv2.smartpaws.domain.repository.MascotaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
class CalendarioViewModel(
    private val eventoRepository: EventoMascotaRepository,
    private val mascotaRepository: MascotaRepository,
    private val userId: Int
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = mascotaRepository.getMascotasByOwner(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val eventos: StateFlow<List<EventoMascota>> = eventoRepository.getAllEventos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEvento(evento: EventoMascota) {
        viewModelScope.launch {
            eventoRepository.addEvento(evento)
        }
    }

    fun deleteEvento(evento: EventoMascota) {
        viewModelScope.launch {
            eventoRepository.deleteEvento(evento)
        }
    }

    // Fecha seleccionada — por defecto hoy al inicio del día
    private val _selectedDate = MutableStateFlow(startOfDay(System.currentTimeMillis()))
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    // Mes visible — año y mes como par
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
    private val eventoRepository: EventoMascotaRepository,
    private val mascotaRepository: MascotaRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarioViewModel(eventoRepository, mascotaRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}