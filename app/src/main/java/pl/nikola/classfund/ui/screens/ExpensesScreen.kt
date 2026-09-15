package pl.nikola.classfund.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nikola.classfund.model.Expense

@Composable
fun ExpensesScreen(
    expenses: List<Expense>,
    onAddExpenseClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onBackClick()
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
            text = "Wydatki",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (expenses.isEmpty()) {

            Text(
                text = "Nie dodano jeszcze żadnych wydatków.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

        } else {

            expenses.forEach { expense ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = String.format("%.2f zł", expense.amount)
                                .replace(".", ","),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = expense.date,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = expense.purpose,
                            fontSize = 14.sp
                        )

                        if (!expense.attachmentUri.isNullOrBlank()) {

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "📎 Zobacz załącznik",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {

                                    val uri = Uri.parse(expense.attachmentUri)

                                    val intent = Intent(
                                        Intent.ACTION_VIEW
                                    ).apply {
                                        setDataAndType(
                                            uri,
                                            "*/*"
                                        )

                                        addFlags(
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        )
                                    }

                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onAddExpenseClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "+ Dodaj wydatek")
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