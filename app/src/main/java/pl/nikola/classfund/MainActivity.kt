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
import pl.nikola.classfund.model.Contribution
import pl.nikola.classfund.model.Expense
import pl.nikola.classfund.model.Payment
import pl.nikola.classfund.ui.screens.AddContributionScreen
import pl.nikola.classfund.ui.screens.AddExpenseScreen
import pl.nikola.classfund.ui.screens.AddPaymentScreen
import pl.nikola.classfund.ui.screens.AddStudentScreen
import pl.nikola.classfund.ui.screens.ContributionDetailsScreen
import pl.nikola.classfund.ui.screens.ContributionsScreen
import pl.nikola.classfund.ui.screens.CreateClassScreen
import pl.nikola.classfund.ui.screens.EditContributionScreen
import pl.nikola.classfund.ui.screens.EditPaymentScreen
import pl.nikola.classfund.ui.screens.ExpenseDetailsScreen
import pl.nikola.classfund.ui.screens.ExpensesScreen
import pl.nikola.classfund.ui.screens.JoinClassScreen
import pl.nikola.classfund.ui.screens.ParentRegisterScreen
import pl.nikola.classfund.ui.screens.PaymentDetailsScreen
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
import pl.nikola.classfund.ui.screens.EditExpenseScreen

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

                var contributions by remember {
                    mutableStateOf(localStorage.getContributions())
                }

                var selectedStudent by remember {
                    mutableStateOf("")
                }

                var selectedContribution by remember {
                    mutableStateOf<Contribution?>(null)
                }

                var selectedPayment by remember {
                    mutableStateOf<Payment?>(null)
                }
                var selectedExpense by remember {
                    mutableStateOf<Expense?>(null)
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
                            onContributionsClick = {
                                currentScreen = "contributions"
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
                            contributions = contributions,
                            onSavePaymentClick = {
                                    studentName,
                                    amount,
                                    purpose,
                                    contributionName ->

                                val today = SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    Locale.getDefault()
                                ).format(Date())

                                val newPayment = Payment(
                                    studentName = studentName,
                                    amount = amount,
                                    purpose = purpose,
                                    date = today,
                                    contributionName = contributionName
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

                            onPaymentClick = { payment ->
                                selectedPayment = payment
                                currentScreen = "payment_details"
                            },

                            onBackClick = {
                                currentScreen = "treasurer_dashboard"
                            }
                        )
                    }

                    "payment_details" -> {

                        selectedPayment?.let { payment ->

                            PaymentDetailsScreen(
                                payment = payment,

                                onEditClick = {
                                    currentScreen = "edit_payment"
                                },

                                onDeleteClick = {

                                    payments = payments.filter { existingPayment ->
                                        existingPayment.id != payment.id
                                    }

                                    classBalance -= payment.amount

                                    localStorage.savePayments(payments)
                                    localStorage.saveBalance(classBalance)

                                    selectedPayment = null

                                    Toast.makeText(
                                        this,
                                        "Usunięto wpłatę",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    currentScreen = "payments"
                                },

                                onBackClick = {
                                    currentScreen = "payments"
                                }
                            )
                        }
                    }

                    "edit_payment" -> {

                        selectedPayment?.let { payment ->

                            EditPaymentScreen(
                                payment = payment,
                                students = students,
                                contributions = contributions,

                                onSaveClick = {
                                        newStudentName,
                                        newAmount,
                                        newPurpose,
                                        newContributionName ->

                                    val oldAmount = payment.amount

                                    val updatedPayment = payment.copy(
                                        studentName = newStudentName,
                                        amount = newAmount,
                                        purpose = newPurpose,
                                        contributionName = newContributionName
                                    )

                                    payments = payments.map { existingPayment ->

                                        if (existingPayment.id == payment.id) {
                                            updatedPayment
                                        } else {
                                            existingPayment
                                        }
                                    }

                                    val amountDifference =
                                        newAmount - oldAmount

                                    classBalance += amountDifference

                                    localStorage.savePayments(payments)
                                    localStorage.saveBalance(classBalance)

                                    selectedPayment = updatedPayment

                                    Toast.makeText(
                                        this,
                                        "Zapisano zmiany wpłaty",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    currentScreen = "payment_details"
                                },

                                onBackClick = {
                                    currentScreen = "payment_details"
                                }
                            )
                        }
                    }

                    "expenses" -> {
                        ExpensesScreen(
                            expenses = expenses,

                            onExpenseClick = { expense ->
                                selectedExpense = expense
                                currentScreen = "expense_details"
                            },

                            onAddExpenseClick = {
                                currentScreen = "add_expense"
                            },

                            onBackClick = {
                                currentScreen = "treasurer_dashboard"
                            }
                        )
                    }

                    "expense_details" -> {

                        selectedExpense?.let { expense ->

                            ExpenseDetailsScreen(
                                expense = expense,

                                onEditClick = {
                                    currentScreen = "edit_expense"
                                },

                                onDeleteClick = {

                                    expenses = expenses.filter { existingExpense ->
                                        existingExpense.id != expense.id
                                    }

                                    classBalance += expense.amount

                                    localStorage.saveExpenses(expenses)
                                    localStorage.saveBalance(classBalance)

                                    selectedExpense = null

                                    Toast.makeText(
                                        this,
                                        "Usunięto wydatek",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    currentScreen = "expenses"
                                },
                                onBackClick = {
                                    currentScreen = "expenses"
                                }
                            )
                        }
                    }
                    "edit_expense" -> {

                        selectedExpense?.let { expense ->

                            EditExpenseScreen(
                                expense = expense,

                                onSaveClick = {
                                        newAmount,
                                        newPurpose,
                                        newAttachmentUri ->

                                    val oldAmount = expense.amount

                                    val updatedExpense = expense.copy(
                                        amount = newAmount,
                                        purpose = newPurpose,
                                        attachmentUri = newAttachmentUri
                                    )

                                    expenses = expenses.map { existingExpense ->

                                        if (existingExpense.id == expense.id) {
                                            updatedExpense
                                        } else {
                                            existingExpense
                                        }
                                    }

                                    val amountDifference =
                                        oldAmount - newAmount

                                    classBalance += amountDifference

                                    localStorage.saveExpenses(expenses)
                                    localStorage.saveBalance(classBalance)

                                    selectedExpense = updatedExpense

                                    Toast.makeText(
                                        this,
                                        "Zapisano zmiany wydatku",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    currentScreen = "expense_details"
                                },

                                onBackClick = {
                                    currentScreen = "expense_details"
                                }
                            )
                        }
                    }
                    "add_expense" -> {
                        AddExpenseScreen(
                            onSaveExpenseClick = {
                                    amount,
                                    purpose,
                                    attachmentUri ->

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

                    "contributions" -> {
                        ContributionsScreen(
                            contributions = contributions,
                            onContributionClick = { contribution ->
                                selectedContribution = contribution
                                currentScreen = "contribution_details"
                            },
                            onAddContributionClick = {
                                currentScreen = "add_contribution"
                            },
                            onBackClick = {
                                currentScreen = "treasurer_dashboard"
                            }
                        )
                    }

                    "contribution_details" -> {

                        selectedContribution?.let { contribution ->

                            ContributionDetailsScreen(
                                contribution = contribution,
                                students = students,
                                payments = payments,

                                onEditClick = {
                                    currentScreen = "edit_contribution"
                                },

                                onDeleteClick = {

                                    val deletedContributionName =
                                        contribution.name

                                    contributions = contributions.filter {
                                        it != contribution
                                    }

                                    payments = payments.map { payment ->

                                        if (
                                            payment.contributionName ==
                                            deletedContributionName
                                        ) {
                                            payment.copy(
                                                contributionName = null
                                            )
                                        } else {
                                            payment
                                        }
                                    }

                                    localStorage.saveContributions(
                                        contributions
                                    )

                                    localStorage.savePayments(
                                        payments
                                    )

                                    selectedContribution = null

                                    Toast.makeText(
                                        this,
                                        "Usunięto składkę",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    currentScreen = "contributions"
                                },

                                onBackClick = {
                                    currentScreen = "contributions"
                                }
                            )
                        }
                    }

                    "edit_contribution" -> {

                        selectedContribution?.let { contribution ->

                            EditContributionScreen(
                                contribution = contribution,
                                onSaveClick = {
                                        newName,
                                        newAmount,
                                        newDueDate ->

                                    val oldName = contribution.name

                                    val updatedContribution =
                                        contribution.copy(
                                            name = newName,
                                            amountPerStudent = newAmount,
                                            dueDate = newDueDate
                                        )

                                    contributions = contributions.map {
                                        if (it == contribution) {
                                            updatedContribution
                                        } else {
                                            it
                                        }
                                    }

                                    if (oldName != newName) {

                                        payments = payments.map { payment ->

                                            if (
                                                payment.contributionName ==
                                                oldName
                                            ) {
                                                payment.copy(
                                                    contributionName = newName,
                                                    purpose = if (
                                                        payment.purpose ==
                                                        oldName
                                                    ) {
                                                        newName
                                                    } else {
                                                        payment.purpose
                                                    }
                                                )
                                            } else {
                                                payment
                                            }
                                        }

                                        localStorage.savePayments(
                                            payments
                                        )
                                    }

                                    localStorage.saveContributions(
                                        contributions
                                    )

                                    selectedContribution =
                                        updatedContribution

                                    Toast.makeText(
                                        this,
                                        "Zapisano zmiany składki",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    currentScreen =
                                        "contribution_details"
                                },
                                onBackClick = {
                                    currentScreen =
                                        "contribution_details"
                                }
                            )
                        }
                    }

                    "add_contribution" -> {
                        AddContributionScreen(
                            onSaveContributionClick = {
                                    name,
                                    amount,
                                    dueDate ->

                                val today = SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    Locale.getDefault()
                                ).format(Date())

                                val newContribution = Contribution(
                                    name = name,
                                    amountPerStudent = amount,
                                    createdDate = today,
                                    dueDate = dueDate
                                )

                                contributions =
                                    contributions + newContribution

                                localStorage.saveContributions(
                                    contributions
                                )

                                Toast.makeText(
                                    this,
                                    "Dodano składkę: $name",
                                    Toast.LENGTH_SHORT
                                ).show()

                                currentScreen = "contributions"
                            },
                            onBackClick = {
                                currentScreen = "contributions"
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