package com.example.tugasakhirpamm.ui.viewmodel.Aktivitas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.tugasakhirpamm.model.Aktivitas
import com.example.tugasakhirpamm.repository.AktivitasRepository
import com.example.tugasakhirpamm.repository.PekerjaRepository
import com.example.tugasakhirpamm.repository.TanamanRepository
import com.example.tugasakhirpamm.ui.navigasi.DestinasiDetailAktivitas
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailAktivitasUiState {
    data class Success(val aktivitas: Aktivitas) : DetailAktivitasUiState()
    object Error : DetailAktivitasUiState()
    object Loading : DetailAktivitasUiState()
}

class DetailAktivitasViewModel(
    savedStateHandle: SavedStateHandle,
    private val aktivitas: AktivitasRepository,
    private val pekerja: PekerjaRepository,
    private val tanaman: TanamanRepository

) : ViewModel() {

    var aktivitasDetailState: DetailAktivitasUiState by mutableStateOf(DetailAktivitasUiState.Loading)
        private set

    private val _idAktivitas: String = checkNotNull(savedStateHandle[DestinasiDetailAktivitas.AKTIVITAS])

    init {
        getAktivitasById()
    }

    fun getAktivitasById() {
        viewModelScope.launch {
            aktivitasDetailState = DetailAktivitasUiState.Loading
            aktivitasDetailState = try {
                val fetchedAktivitas= aktivitas.getAktivitasById(_idAktivitas)
                DetailAktivitasUiState.Success(fetchedAktivitas)
            } catch (e: IOException) {
                DetailAktivitasUiState.Error
            } catch (e: HttpException) {
                DetailAktivitasUiState.Error
            }
        }
    }

    fun deleteAktivitas() {
        viewModelScope.launch {
            try {
                aktivitas.deleteAktivitas(_idAktivitas)
            } catch (e: IOException) {
                // Handle error
            } catch (e: HttpException) {
                // Handle error
            }
        }
    }
}
fun Aktivitas.toDetailAktivitas(): AktivitasEvent {
    return AktivitasEvent(
        id_aktivitas = id_aktivitas,
        id_pekerja = id_pekerja,
        tanggal_aktivitas = tanggal_aktivitas,
        deskripsi_aktivitas = deskripsi_aktivitas
    )
}
