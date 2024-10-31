package com.example.oilcollectionv2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun signOut(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                FirebaseAuth.getInstance().signOut()
                onLogoutSuccess()
            } catch (e: Exception) {
//                TODO: handle error
            } finally {
                _loading.value = false
            }
        }
    }
}
