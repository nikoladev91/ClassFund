package pl.nikola.classfund

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pl.nikola.classfund.ui.screens.AddPaymentScreen
import pl.nikola.classfund.ui.screens.CreateClassScreen
import pl.nikola.classfund.ui.screens.JoinClassScreen
import pl.nikola.classfund.ui.screens.ParentRegisterScreen
import pl.nikola.classfund.ui.screens.RoleSelectionScreen
import pl.nikola.classfund.ui.screens.TreasurerDashboardScreen
import pl.nikola.classfund.ui.screens.TreasurerRegisterScreen
import pl.nikola.classfund.ui.screens.WelcomeScreen
import pl.nikola.classfund.ui.theme.ClassFundTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClassFundTheme {

                var currentScreen by remember {
                    mutableStateOf("welcome")
                }

                var createdClassName by remember {
                    mutableStateOf("")
                }

                var createdSchoolYear by remember {
                    mutableStateOf("")
                }

                var classBalance by remember {
                    mutableStateOf(0.0)
                }

                when (currentScreen) {

                    "welcome" -> {
                        WelcomeScreen(
                            onLoginClick = {
                                Toast.makeText(
                                    this,
                                    "Logowanie zrobimy w kolejnym etapie",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onRegisterClick = {
                                currentScreen = "role_selection"
                            }
                        )
                    }

                    "role_selection" -> {
                        RoleSelectionScreen(
                            onTreasurerClick = {
                                currentScreen = "treasurer_register"
                            },
                            onParentClick = {
                                currentScreen = "parent_register"
                            }
                        )
                    }

                    "treasurer_register" -> {
                        TreasurerRegisterScreen(
                            onCreateAccountClick = {
                                currentScreen = "create_class"
                            },
                            onBackClick = {
                                currentScreen = "role_selection"
                            }
                        )
                    }

                    "create_class" -> {
                        CreateClassScreen(
                            onCreateClassClick = { className, schoolYear ->
                                createdClassName = className
                                createdSchoolYear = schoolYear
                                currentScreen = "treasurer_dashboard"
                            },
                            onBackClick = {
                                currentScreen = "treasurer_register"
                            }
                        )
                    }

                    "treasurer_dashboard" -> {
                        TreasurerDashboardScreen(
                            className = createdClassName,
                            schoolYear = createdSchoolYear,
                            balance = classBalance,
                            onAddPaymentClick = {
                                currentScreen = "add_payment"
                            }
                        )
                    }

                    "add_payment" -> {
                        AddPaymentScreen(
                            onSavePaymentClick = { studentName, amount, purpose ->
                                classBalance += amount

                                Toast.makeText(
                                    this,
                                    "Dodano wpłatę: $studentName - $amount zł",
                                    Toast.LENGTH_SHORT
                                ).show()

                                currentScreen = "treasurer_dashboard"
                            },
                            onBackClick = {
                                currentScreen = "treasurer_dashboard"
                            }
                        )
                    }

                    "parent_register" -> {
                        ParentRegisterScreen(
                            onCreateAccountClick = {
                                currentScreen = "join_class"
                            },
                            onBackClick = {
                                currentScreen = "role_selection"
                            }
                        )
                    }

                    "join_class" -> {
                        JoinClassScreen(
                            onJoinClassClick = {
                                Toast.makeText(
                                    this,
                                    "Dołączono do klasy",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onBackClick = {
                                currentScreen = "parent_register"
                            }
                        )
                    }
                }
            }
        }
    }
}