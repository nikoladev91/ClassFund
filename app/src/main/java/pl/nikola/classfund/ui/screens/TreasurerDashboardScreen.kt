package pl.nikola.classfund.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nikola.classfund.R
import pl.nikola.classfund.model.Contribution
import pl.nikola.classfund.model.Payment

@Composable
fun TreasurerDashboardScreen(
    className: String,
    schoolYear: String,
    balance: Double,
    totalPayments: Double,
    totalExpenses: Double,
    students: List<String>,
    contributions: List<Contribution>,
    payments: List<Payment>,
    onAddPaymentClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onExpensesClick: () -> Unit,
    onContributionsClick: () -> Unit,
    onContributionClick: (Contribution) -> Unit,
    onStudentsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 72.dp,
                bottom = 32.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = className,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = schoolYear,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = stringResource(R.string.class_balance),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = String.format(
                        "%.2f zł",
                        balance
                    ).replace(".", ","),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Wpłaty",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = String.format(
                            "%.2f zł",
                            totalPayments
                        ).replace(".", ","),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Wydatki",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = String.format(
                            "%.2f zł",
                            totalExpenses
                        ).replace(".", ","),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Uczniowie",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = students.size.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Składki",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = contributions.size.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = stringResource(R.string.payments_tab),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onPaymentsClick()
                }
            )

            Text(
                text = stringResource(R.string.expenses_tab),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onExpensesClick()
                }
            )

            Text(
                text = "Składki",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onContributionsClick()
                }
            )

            Text(
                text = stringResource(R.string.students_tab),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onStudentsClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onAddPaymentClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.add_payment)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        if (contributions.isNotEmpty()) {

            Text(
                text = "Aktywne składki",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(
                    Alignment.Start
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            contributions.forEach { contribution ->

                val contributionPayments =
                    payments.filter { payment ->
                        payment.contributionName ==
                                contribution.name
                    }

                val requiredTotal =
                    students.size *
                            contribution.amountPerStudent

                val collectedTotal =
                    contributionPayments.sumOf {
                        it.amount
                    }

                val fullyPaidCount =
                    students.count { student ->

                        val paidByStudent =
                            contributionPayments
                                .filter {
                                    it.studentName ==
                                            student
                                }
                                .sumOf {
                                    it.amount
                                }

                        paidByStudent >=
                                contribution.amountPerStudent
                    }

                val progress =
                    if (requiredTotal > 0.0) {
                        (
                                collectedTotal /
                                        requiredTotal
                                )
                            .toFloat()
                            .coerceIn(
                                0f,
                                1f
                            )
                    } else {
                        0f
                    }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 6.dp
                        )
                        .clickable {
                            onContributionClick(
                                contribution
                            )
                        },
                    shape = RoundedCornerShape(20.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(
                            18.dp
                        )
                    ) {

                        Text(
                            text = contribution.name,
                            fontSize = 19.sp,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                String.format(
                                    "%.2f zł / uczeń",
                                    contribution
                                        .amountPerStudent
                                ).replace(
                                    ".",
                                    ","
                                ),
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(14.dp)
                        )

                        Text(
                            text =
                                "Zebrano " +
                                        String.format(
                                            "%.2f zł",
                                            collectedTotal
                                        ).replace(
                                            ".",
                                            ","
                                        ) +
                                        " z " +
                                        String.format(
                                            "%.2f zł",
                                            requiredTotal
                                        ).replace(
                                            ".",
                                            ","
                                        ),
                            fontSize = 15.sp,
                            fontWeight =
                                FontWeight.Medium
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        LinearProgressIndicator(
                            progress = {
                                progress
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                "Opłaciło: " +
                                        "$fullyPaidCount/" +
                                        "${students.size}",
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Termin: " +
                                        contribution.dueDate,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}