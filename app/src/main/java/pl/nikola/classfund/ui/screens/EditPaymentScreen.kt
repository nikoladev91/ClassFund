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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nikola.classfund.model.Contribution
import pl.nikola.classfund.model.Payment

@Composable
fun EditPaymentScreen(
    payment: Payment,
    students: List<String>,
    contributions: List<Contribution>,
    onSaveClick: (
        String,
        Double,
        String,
        String?
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedStudent by remember {
        mutableStateOf(payment.studentName)
    }

    var selectedContribution by remember {
        mutableStateOf(payment.contributionName)
    }

    var amount by remember {
        mutableStateOf(
            payment.amount
                .toString()
                .replace(".", ",")
        )
    }

    var purpose by remember {
        mutableStateOf(payment.purpose)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var studentMenuExpanded by remember {
        mutableStateOf(false)
    }

    var contributionMenuExpanded by remember {
        mutableStateOf(false)
    }

    BackHandler {
        onBackClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 24.dp,
                vertical = 24.dp
            )
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
                text = "Edytuj wpłatę",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

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
                        text = selectedStudent
                    )
                }

                DropdownMenu(
                    expanded = studentMenuExpanded,
                    onDismissRequest = {
                        studentMenuExpanded = false
                    }
                ) {

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

            Spacer(
                modifier = Modifier.height(12.dp)
            )

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
                        text =
                            selectedContribution
                                ?: "Bez przypisania do składki"
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
                            Text(
                                "Bez przypisania do składki"
                            )
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
                                selectedContribution =
                                    contribution.name

                                contributionMenuExpanded =
                                    false

                                errorMessage = null
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    errorMessage = null
                },
                label = {
                    Text("Kwota")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = purpose,
                onValueChange = {
                    purpose = it
                    errorMessage = null
                },
                label = {
                    Text("Cel wpłaty")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = errorMessage!!,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Button(
                onClick = {

                    if (
                        selectedStudent.isBlank() ||
                        amount.isBlank() ||
                        purpose.isBlank()
                    ) {

                        errorMessage =
                            "Uzupełnij wszystkie pola"

                    } else {

                        val parsedAmount =
                            amount
                                .replace(",", ".")
                                .toDoubleOrNull()

                        if (
                            parsedAmount == null ||
                            parsedAmount <= 0.0
                        ) {

                            errorMessage =
                                "Podaj prawidłową kwotę"

                        } else {

                            onSaveClick(
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
                    text = "Zapisz zmiany"
                )
            }
        }
    }
}