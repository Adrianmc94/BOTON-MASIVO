// MainActivity.kt
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Reemplaza 'YourAppTheme' con el nombre real de tu tema
            MaterialTheme {
                StateScreen()
            }
        }
    }
}

@Composable
fun StateScreen(viewModel: MyViewModel = viewModel()) {

    // Observa el StateFlow del ViewModel. La UI se redibuja (recompone) al cambiar.
    val currentMessage by viewModel.displayMessage.collectAsStateWithLifecycle()
    val currentState by viewModel.currentState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Muestra el estado actual del sistema
        Text(
            text = "ESTADO: ${currentState.name.replace("_", " ")}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))

        // El cuadro de texto que muestra el mensaje y el progreso de la cuenta atrás
        Text(
            text = currentMessage,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        // BOTÓN 1: AVANZA DE ESTADO
        Button(
            onClick = { viewModel.advanceState() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AVANZA ESTADO")
        }

        Spacer(Modifier.height(20.dp))

        // BOTÓN 2: BOTÓN DE ACCIÓN

        // Define el texto del botón basado en el estado actual
        val buttonText = when (currentState) {
            ButtonState.TEXT_DISPLAY -> "Ejecutar Acción (Texto)"
            ButtonState.COUNTDOWN -> "INICIAR Cuenta Atrás"
            ButtonState.SOUND_NOTE -> "Reproducir Nota (Sonido)"
        }

        Button(
            // El onClick SIEMPRE llama a la misma función del ViewModel.
            // La lógica que se ejecuta es lo que cambia dentro de 'performAction()'.
            onClick = { viewModel.performAction() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(buttonText)
        }
    }
}