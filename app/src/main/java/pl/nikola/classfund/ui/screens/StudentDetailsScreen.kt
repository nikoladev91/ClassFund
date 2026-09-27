package pl.nikola.classfund.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nikola.classfund.model.Contribution
import pl.nikola.classfund.model.Payment

@Composable
fun StudentDetailsScreen(
    studentName: String,
    payments: List<Payment>,
    contributions: List<Contribution>,
    onBackClick: () -> Unit
) {
    val studentPayments = payments.filter {
        it.studentName == studentName
    }

    val totalPaid = studentPayments.sumOf {
        it.amount
    }

    val fullyPaidContributionsCount =
        contributions.count { contribution ->

            val paidForContribution =
                studentPayments
                    .filter {
                        it.contributionName == contribution.name
                    }
                    .sumOf {
                        it.amount
                    }

            paidForContribution >=
                    contribution.amountPerStudent
        }

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 72.dp,
                bottom = 32.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = studentName,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Podsumowanie",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Column {

                        Text(
                            text = "Wpłacono łącznie",
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text = String.format(
                                "%.2f zł",
                                totalPaid
                            ).replace(".", ","),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        horizontalAlignment =
                            Alignment.End
                    ) {

                        Text(
                            text = "Opłacone składki",
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "$fullyPaidContributionsCount/" +
                                        "${contributions.size}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "Składki",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(
                Alignment.Start
            )
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        if (contributions.isEmpty()) {

            Text(
                text = "Nie dodano jeszcze żadnych składek.",
                fontSize = 15.sp
            )

        } else {

            contributions.forEach { contribution ->

                val paidForContribution =
                    studentPayments
                        .filter {
                            it.contributionName ==
                                    contribution.name
                        }
                        .sumOf {
                            it.amount
                        }

                val status = when {

                    paidForContribution >=
                            contribution.amountPerStudent -> {
                        "✓ Opłacone"
                    }

                    paidForContribution > 0.0 -> {
                        "Częściowo"
                    }

                    else -> {
                        "Brak wpłaty"
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 6.dp
                        ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(
                            16.dp
                        )
                    ) {

                        Text(
                            text = contribution.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text = status,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text = String.format(
                                "%.2f / %.2f zł",
                                paidForContribution,
                                contribution.amountPerStudent
                            ).replace(".", ","),
                            fontSize = 15.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Termin: ${contribution.dueDate}",
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "Historia wpłat",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(
                Alignment.Start
            )
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        if (studentPayments.isEmpty()) {

            Text(
                text = "Brak wpłat dla tego ucznia.",
                fontSize = 16.sp
            )

        } else {

            studentPayments.forEach { payment ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 6.dp
                        ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(
                            16.dp
                        )
                    ) {

                        Text(
                            text = String.format(
                                "%.2f zł",
                                payment.amount
                            ).replace(".", ","),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text = payment.date,
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text = payment.purpose,
                            fontSize = 14.sp
                        )

                        if (
                            !payment.contributionName
                                .isNullOrBlank()
                        ) {

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    "Składka: " +
                                            payment.contributionName,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "← Wróć",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                onBackClick()
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}