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
import pl.nikola.classfund.data.LocalStorage
import pl.nikola.classfund.model.Expense
import pl.nikola.classfund.model.Payment
import pl.nikola.classfund.ui.screens.AddExpenseScreen
import pl.nikola.classfund.ui.screens.AddPaymentScreen
import pl.nikola.classfund.ui.screens.AddStudentScreen
import pl.nikola.classfund.ui.screens.CreateClassScreen
import pl.nikola.classfund.ui.screens.ExpensesScreen
import pl.nikola.classfund.ui.screens.JoinClassScreen
import pl.nikola.classfund.ui.screens.ParentRegisterScreen
import pl.nikola.classfund.ui.screens.PaymentsScreen
import pl.nikola.classfund.ui.screens.RoleSelectionScreen
import pl.nikola.classfund.ui.screens.StudentDetailsScreen
import pl.nikola.classfund.ui.screens.StudentsScreen
import pl.nikola.classfund.ui.screens.TreasurerDashboardScreen
import pl.nikola.classfund.ui.screens.TreasurerRegisterScreen
import pl.nikola.classfund.ui.screens.WelcomeScreen
import pl.nikola.classfund.ui.theme.ClassFundTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClassFundTheme {

                val localStorage = remember {
                    LocalStorage(this)
                }

                var currentScreen by remember {
                    mutableStateOf("treasurer_dashboard")
                }

                var createdClassName by remember {
                    mutableStateOf(localStorage.getClassName())
                }

                var createdSchoolYear by remember {
                    mutableStateOf(localStorage.getSchoolYear())
                }

                var classBalance by remember {
                    mutableStateOf(localStorage.getBalance())
                }

                var students by remember {
                    mutableStateOf(localStorage.getStudents())
                }

                var payments by remember {
                    mutableStateOf(localStorage.getPayments())
                }

                var expenses by remember {
                    mutableStateOf(localStorage.getExpenses())
                }

                var selectedStudent by remember {
                    mutableStateOf("")
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

                                localStorage.saveClassName(className)
                                localStorage.saveSchoolYear(schoolYear)

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
                            },
                            onPaymentsClick = {
                                currentScreen = "payments"
                            },
                            onExpensesClick = {
                                currentScreen = "expenses"
                            },
                            onStudentsClick = {
                                currentScreen = "students"
                            }
                        )
                    }

                    "students" -> {
                        StudentsScreen(
                            students = students,
                            onStudentClick = { studentName ->
                                selectedStudent = studentName
                                currentScreen = "student_details"
                            },
                            onAddStudentClick = {
                                currentScreen = "add_student"
                            },
                            onBackClick = {
                                currentScreen = "treasurer_dashboard"
                            }
                        )
                    }

                    "student_details" -> {
                        StudentDetailsScreen(
                            studentName = selectedStudent,
                            payments = payments,
                            onBackClick = {
                                currentScreen = "students"
                            }
                        )
                    }

                    "add_student" -> {
                        AddStudentScreen(
                            onSaveStudentClick = { studentName ->

                                students = students + studentName
                                localStorage.saveStudents(students)

                                Toast.makeText(
                                    this,
                                    "Dodano ucznia: $studentName",
                                    Toast.LENGTH_SHORT
                                ).show()

                                currentScreen = "students"
                            },
                            onBackClick = {
                                currentScreen = "students"
                            }
                        )
                    }

                    "add_payment" -> {
                        AddPaymentScreen(
                            students = students,
                            onSavePaymentClick = { studentName, amount, purpose ->

                                val today = SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    Locale.getDefault()
                                ).format(Date())

                                val newPayment = Payment(
                                    studentName = studentName,
                                    amount = amount,
                                    purpose = purpose,
                                    date = today
                                )

                                payments = payments + newPayment
                                classBalance += amount

                                localStorage.savePayments(payments)
                                localStorage.saveBalance(classBalance)

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

                    "payments" -> {
                        PaymentsScreen(
                            payments = payments,
                            onBackClick = {
                                currentScreen = "treasurer_dashboard"
                            }
                        )
                    }

                    "expenses" -> {
                        ExpensesScreen(
                            expenses = expenses,
                            onAddExpenseClick = {
                                currentScreen = "add_expense"
                            },
                            onBackClick = {
                                currentScreen = "treasurer_dashboard"
                            }
                        )
                    }

                    "add_expense" -> {
                        AddExpenseScreen(
                            onSaveExpenseClick = { amount, purpose, attachmentUri ->
                                val today = SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    Locale.getDefault()
                                ).format(Date())

                                val newExpense = Expense(
                                    amount = amount,
                                    purpose = purpose,
                                    date = today,
                                    attachmentUri = attachmentUri
                                )

                                expenses = expenses + newExpense
                                classBalance -= amount

                                localStorage.saveExpenses(expenses)
                                localStorage.saveBalance(classBalance)

                                Toast.makeText(
                                    this,
                                    "Dodano wydatek: $amount zł",
                                    Toast.LENGTH_SHORT
                                ).show()

                                currentScreen = "expenses"
                            },
                            onBackClick = {
                                currentScreen = "expenses"
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