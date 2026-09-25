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
import pl.nikola.classfund.model.Expense

@Composable
fun EditExpenseScreen(
    expense: Expense,
    onSaveClick: (
        Double,
        String,
        String?
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var amount by remember {
        mutableStateOf(
            expense.amount
                .toString()
                .replace(".", ",")
        )
    }

    var purpose by remember {
        mutableStateOf(expense.purpose)
    }

    var attachmentUri by remember {
        mutableStateOf(expense.attachmentUri)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
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
                text = "Edytuj wydatek",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(32.dp)
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
                    Text("Cel wydatku")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (!attachmentUri.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Załącznik jest zapisany",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Usuń załącznik",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        attachmentUri = null
                    }
                )
            }

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
                                parsedAmount,
                                purpose.trim(),
                                attachmentUri
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