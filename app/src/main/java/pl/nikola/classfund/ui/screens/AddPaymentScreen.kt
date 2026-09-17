package pl.nikola.classfund.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nikola.classfund.R
import pl.nikola.classfund.model.Contribution

@Composable
fun AddPaymentScreen(
    students: List<String>,
    contributions: List<Contribution>,
    onSavePaymentClick: (String, Double, String, String?) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedStudent by remember { mutableStateOf("") }
    var selectedContribution by remember { mutableStateOf<String?>(null) }

    var amount by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    var studentMenuExpanded by remember { mutableStateOf(false) }
    var contributionMenuExpanded by remember { mutableStateOf(false) }

    val errorEmptyFields = stringResource(R.string.error_payment_empty_fields)
    val errorInvalidAmount = stringResource(R.string.error_invalid_amount)

    BackHandler {
        onBackClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {

        Text(
            text = "←",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 24.dp)
                .clickable {
                    onBackClick()
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.add_payment_title),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedButton(
                    onClick = {
                        studentMenuExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (selectedStudent.isBlank()) {
                            "Wybierz ucznia"
                        } else {
                            selectedStudent
                        }
                    )
                }

                DropdownMenu(
                    expanded = studentMenuExpanded,
                    onDismissRequest = {
                        studentMenuExpanded = false
                    }
                ) {

                    if (students.isEmpty()) {

                        DropdownMenuItem(
                            text = {
                                Text("Brak uczniów")
                            },
                            onClick = {
                                studentMenuExpanded = false
                            }
                        )

                    } else {

                        students
                            .sortedBy { student ->
                                student.trim()
                                    .substringAfterLast(" ")
                                    .lowercase()
                            }
                            .forEach { student ->

                                DropdownMenuItem(
                                    text = {
                                        Text(student)
                                    },
                                    onClick = {
                                        selectedStudent = student
                                        studentMenuExpanded = false
                                        errorMessage = null
                                    }
                                )
                            }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedButton(
                    onClick = {
                        contributionMenuExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = selectedContribution ?: "Wybierz składkę"
                    )
                }

                DropdownMenu(
                    expanded = contributionMenuExpanded,
                    onDismissRequest = {
                        contributionMenuExpanded = false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Bez przypisania do składki")
                        },
                        onClick = {
                            selectedContribution = null
                            contributionMenuExpanded = false
                            errorMessage = null
                        }
                    )

                    contributions.forEach { contribution ->

                        DropdownMenuItem(
                            text = {
                                Text(
                                    "${contribution.name} - " +
                                            String.format(
                                                "%.2f zł",
                                                contribution.amountPerStudent
                                            ).replace(".", ",")
                                )
                            },
                            onClick = {
                                selectedContribution = contribution.name
                                contributionMenuExpanded = false
                                errorMessage = null

                                purpose = contribution.name

                                if (amount.isBlank()) {
                                    amount = contribution.amountPerStudent
                                        .toString()
                                        .replace(".", ",")
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    errorMessage = null
                },
                label = {
                    Text(stringResource(R.string.payment_amount))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = purpose,
                onValueChange = {
                    purpose = it
                    errorMessage = null
                },
                label = {
                    Text(stringResource(R.string.payment_purpose))
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = errorMessage!!,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {

                    if (
                        selectedStudent.isBlank() ||
                        amount.isBlank() ||
                        purpose.isBlank()
                    ) {

                        errorMessage = errorEmptyFields

                    } else {

                        val parsedAmount = amount
                            .replace(",", ".")
                            .toDoubleOrNull()

                        if (parsedAmount == null || parsedAmount <= 0.0) {

                            errorMessage = errorInvalidAmount

                        } else {

                            onSavePaymentClick(
                                selectedStudent,
                                parsedAmount,
                                purpose.trim(),
                                selectedContribution
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = stringResource(R.string.save_payment)
                )
            }
        }
    }
}