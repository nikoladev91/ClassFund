package pl.nikola.classfund.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nikola.classfund.model.Contribution
import pl.nikola.classfund.model.Payment

@Composable
fun ContributionDetailsScreen(
    contribution: Contribution,
    students: List<String>,
    payments: List<Payment>,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val contributionPayments = payments.filter {
        it.contributionName == contribution.name
    }

    val requiredTotal =
        students.size * contribution.amountPerStudent

    val collectedTotal =
        contributionPayments.sumOf { it.amount }

    val remainingTotal =
        (requiredTotal - collectedTotal).coerceAtLeast(0.0)

    val fullyPaidCount = students.count { student ->

        val paidByStudent = contributionPayments
            .filter { it.studentName == student }
            .sumOf { it.amount }

        paidByStudent >= contribution.amountPerStudent
    }

    BackHandler {
        onBackClick()
    }

    if (showDeleteDialog) {

        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Usuń składkę")
            },
            text = {
                Text(
                    "Czy na pewno chcesz usunąć tę składkę? " +
                            "Wpłaty pozostaną w historii klasy."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text("Usuń")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Anuluj")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 72.dp,
                bottom = 32.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = contribution.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = String.format(
                "%.2f zł / uczeń",
                contribution.amountPerStudent
            ).replace(".", ","),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Termin: ${contribution.dueDate}",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Podsumowanie",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Wymagane: " +
                            String.format("%.2f zł", requiredTotal)
                                .replace(".", ",")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Zebrano: " +
                            String.format("%.2f zł", collectedTotal)
                                .replace(".", ",")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Pozostało: " +
                            String.format("%.2f zł", remainingTotal)
                                .replace(".", ",")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Opłacone: $fullyPaidCount/${students.size}"
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Uczniowie",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        students
            .sortedBy { student ->
                student.trim()
                    .substringAfterLast(" ")
                    .lowercase()
            }
            .forEach { student ->

                val paidByStudent = contributionPayments
                    .filter { it.studentName == student }
                    .sumOf { it.amount }

                val status = when {
                    paidByStudent >= contribution.amountPerStudent ->
                        "Opłacone"

                    paidByStudent > 0.0 ->
                        "Częściowo"

                    else ->
                        "Brak wpłaty"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = student,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = status,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = String.format(
                                "%.2f / %.2f zł",
                                paidByStudent,
                                contribution.amountPerStudent
                            ).replace(".", ","),
                            fontSize = 14.sp
                        )
                    }
                }
            }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Edytuj składkę")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                showDeleteDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Usuń składkę")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "← Wróć",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                onBackClick()
            }
        )
    }
}