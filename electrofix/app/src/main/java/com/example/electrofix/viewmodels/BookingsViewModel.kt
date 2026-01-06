package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.Booking
import com.example.electrofix.data.BookingStatus
import com.example.electrofix.data.BookingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookingsUiState(
    val ongoingBookings: List<Booking> = emptyList(),
    val completedBookings: List<Booking> = emptyList(),
    val cancelledBookings: List<Booking> = emptyList(),
    val selectedTabIndex: Int = 0
)

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val bookingsRepository: BookingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingsUiState())
    val uiState: StateFlow<BookingsUiState> = _uiState

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            val bookings = bookingsRepository.getBookings()
            _uiState.value = BookingsUiState(
                ongoingBookings = bookings.filter { it.status == BookingStatus.ONGOING },
                completedBookings = bookings.filter { it.status == BookingStatus.COMPLETED },
                cancelledBookings = bookings.filter { it.status == BookingStatus.CANCELLED }
            )
        }
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = tabIndex)
    }
}
