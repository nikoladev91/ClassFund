package pl.nikola.classfund

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pl.nikola.classfund.ui.screens.WelcomeScreen
import pl.nikola.classfund.ui.theme.ClassFundTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClassFundTheme {
                WelcomeScreen(
                    onLoginClick = {
                        Toast.makeText(
                            this,
                            "Tutaj będzie logowanie",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onRegisterClick = {
                        Toast.makeText(
                            this,
                            "Tutaj będzie rejestracja",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}