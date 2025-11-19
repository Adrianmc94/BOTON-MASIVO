package com.example.boton_masivo

// MyViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    // StateFlow para el estado actual del botón (Observado por la UI)
    private val _currentState = MutableStateFlow(ButtonState.TEXT_DISPLAY)
    val currentState: StateFlow<ButtonState> = _currentState

    // StateFlow para el texto que se muestra en el Composable (Observado por la UI)
    private val _displayMessage = MutableStateFlow("Estado 1: Pulsa el botón de acción.")
    val displayMessage: StateFlow<String> = _displayMessage

    private var countdownJob: Job? = null

    // Función llamada por el botón "Avanza Estado"
    fun advanceState() {
        // Cancelar corrutina si estaba activa antes de cambiar
        cancelCountdown()

        _currentState.value = when (_currentState.value) {
            ButtonState.TEXT_DISPLAY -> {
                _displayMessage.value = "Estado 2: Listo para la cuenta atrás."
                ButtonState.COUNTDOWN
            }
            ButtonState.COUNTDOWN -> {
                _displayMessage.value = "Estado 3: Pulsa para sonido."
                ButtonState.SOUND_NOTE
            }
            ButtonState.SOUND_NOTE -> {
                _displayMessage.value = "Estado 1: Pulsa el botón de acción."
                ButtonState.TEXT_DISPLAY
            }
        }
    }

    // Función llamada por el botón de Acción. La LÓGICA interna cambia según el estado.
    fun performAction() {
        // La ejecución cambia con la expresión 'when'
        when (_currentState.value) {
            ButtonState.TEXT_DISPLAY -> {
                _displayMessage.value = "Acción de texto completada en Estado 1."
            }
            ButtonState.COUNTDOWN -> {
                startCountdown()
            }
            ButtonState.SOUND_NOTE -> {
                _displayMessage.value = "Sonido o Nota Reproducida en Estado 3."
                // Aquí iría la llamada a la función de sonido real
            }
        }
    }

    // Implementación del Estado 2: Cuenta atrás con Corrutinas
    private fun startCountdown() {
        cancelCountdown()

        countdownJob = viewModelScope.launch {
            var count = 3
            while (count >= 0) {
                _displayMessage.value = "Cuenta atrás: $count..."
                delay(1000L) // Espera 1 segundo sin bloquear la UI
                count--
            }
            _displayMessage.value = "Cuenta atrás finalizada."
        }
    }

    // Función de limpieza de corrutina
    private fun cancelCountdown() {
        countdownJob?.cancel()
        countdownJob = null
    }

    // Asegura que las corrutinas se cancelan cuando el ViewModel se destruye
    override fun onCleared() {
        super.onCleared()
        cancelCountdown()
    }
}