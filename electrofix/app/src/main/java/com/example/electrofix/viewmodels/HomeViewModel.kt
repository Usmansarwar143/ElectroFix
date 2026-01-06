package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val serviceCategories: List<String> = emptyList(),
    val nearbyTechnicians: List<String> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val serviceCategories = homeRepository.getServiceCategories()
            val nearbyTechnicians = homeRepository.getNearbyTechnicians()
            _uiState.value = HomeUiState(
                serviceCategories = serviceCategories,
                nearbyTechnicians = nearbyTechnicians
            )
        }
    }
}
