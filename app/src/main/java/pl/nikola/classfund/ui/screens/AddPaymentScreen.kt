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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nikola.classfund.R

@Composable
fun AddPaymentScreen(
    onSavePaymentClick: (String, Double, String) -> Unit,
    onBackClick: () -> Unit
) {
    var studentName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

            OutlinedTextField(
                value = studentName,
                onValueChange = {
                    studentName = it
                    errorMessage = null
                },
                label = {
                    Text(stringResource(R.string.student_name))
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

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
                        studentName.isBlank() ||
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
                                studentName.trim(),
                                parsedAmount,
                                purpose.trim()
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