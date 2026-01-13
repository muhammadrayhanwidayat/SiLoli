package com.example.siloli.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.siloli.model.Laporan
import com.example.siloli.repository.LaporanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaporanViewModel @Inject constructor(
    private val repository: LaporanRepository
) : ViewModel() {

    // Auth State
    private val _isLoggedIn = MutableStateFlow(repository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun checkLoginStatus() {
        _isLoggedIn.value = repository.isLoggedIn()
    }

    fun logout() {
        repository.logout()
        _isLoggedIn.value = false
    }

    fun getUserData() = repository.getUserData()

    // Login
    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.login(com.example.siloli.model.LoginRequest(username, password))
            if (result.isSuccess) {
                _loginState.value = UiState.Success(Unit)
                _isLoggedIn.value = true
            } else {
                _loginState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }
    fun resetLoginState() { _loginState.value = UiState.Idle }

    // Register
    private val _registerState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val registerState: StateFlow<UiState<Unit>> = _registerState.asStateFlow()

    fun register(username: String, password: String) {
         _registerState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.register(com.example.siloli.model.RegisterRequest(username, password))
            if (result.isSuccess) {
                _registerState.value = UiState.Success(Unit)
            } else {
                _registerState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }
    fun resetRegisterState() { _registerState.value = UiState.Idle }

    // Laporan List
    private val _laporanListState = MutableStateFlow<UiState<List<Laporan>>>(UiState.Loading)
    val laporanListState: StateFlow<UiState<List<Laporan>>> = _laporanListState.asStateFlow()

    fun loadLaporan() {
        _laporanListState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.getLaporanSaya()
            if (result.isSuccess) {
                val list = result.getOrNull()?.laporan ?: emptyList()
                // Default sort: Newest first (by createdAt or ID could work too)
                val sortedList = list.sortedByDescending { it.createdAt ?: "" } 
                _laporanListState.value = UiState.Success(sortedList)
            } else {
                _laporanListState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to load reports")
            }
        }
    }

    // Create Laporan
    private val _createLaporanState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val createLaporanState: StateFlow<UiState<Unit>> = _createLaporanState.asStateFlow()

    fun buatLaporan(wilayah: String, aduan: String) {
        _createLaporanState.value = UiState.Loading
        viewModelScope.launch {
             val result = repository.buatLaporan(com.example.siloli.model.BuatLaporanRequest(wilayah, aduan))
             if (result.isSuccess) {
                 _createLaporanState.value = UiState.Success(Unit)
                 loadLaporan() // Refresh list
             } else {
                 _createLaporanState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to create report")
             }
        }
    }
    fun resetCreateLaporanState() { _createLaporanState.value = UiState.Idle }

    // Edit Laporan
    private val _editLaporanState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val editLaporanState: StateFlow<UiState<Unit>> = _editLaporanState.asStateFlow()

    fun editLaporan(id: Int, wilayah: String, aduan: String) {
        _editLaporanState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.editLaporan(com.example.siloli.model.EditLaporanRequest(id, wilayah, aduan))
            if (result.isSuccess) {
                _editLaporanState.value = UiState.Success(Unit)
                loadLaporan() // Refresh list
            } else {
                _editLaporanState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to edit report")
            }
        }
    }
    fun resetEditLaporanState() { _editLaporanState.value = UiState.Idle }

    // Delete Laporan
    private val _deleteLaporanState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteLaporanState: StateFlow<UiState<Unit>> = _deleteLaporanState.asStateFlow()

    fun hapusLaporan(id: Int) {
        _deleteLaporanState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.hapusLaporan(id)
            if (result.isSuccess) {
                _deleteLaporanState.value = UiState.Success(Unit)
                loadLaporan() // Refresh list
            } else {
                _deleteLaporanState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to delete report")
            }
        }
    }
    fun resetDeleteLaporanState() { _deleteLaporanState.value = UiState.Idle }
}

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
