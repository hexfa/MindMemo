package com.mindmemo.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmemo.presentation.notification.NotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

abstract class ViewModelBase : ViewModel() {

    fun launchWithState(action: suspend () -> Unit) =
        performLaunchWithState(
            action,
            {},
            {}
        )

    fun launchWithDispatchers(action: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            action()
        }

    private fun performLaunchWithState(
        action: suspend () -> Unit,
        failedAction: suspend () -> Unit,
        finallyAction: () -> Unit
    ) {
        viewModelScope.launch {
            val requestId = UUID.randomUUID()
            requestIdList.add(requestId)
            try {
                if (!_isLoading.value) _isLoading.value = true
                action()
            } catch (exc: Exception) {
                NotificationService.showError(""/*ExceptionHandler.getErrorMessage(exc)*/)
                failedAction()
            } finally {
                requestIdList.remove(requestId)
                if (requestIdList.isEmpty()) _isLoading.value = false
                finallyAction()
            }
        }
    }

    companion object {
        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

        val requestIdList = mutableListOf<UUID>()
    }
}