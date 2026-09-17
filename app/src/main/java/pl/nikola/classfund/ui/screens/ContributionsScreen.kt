package pl.nikola.classfund.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nikola.classfund.model.Contribution

@Composable
fun ContributionsScreen(
    contributions: List<Contribution>,
    onContributionClick: (Contribution) -> Unit,
    onAddContributionClick: () -> Unit,
    onBackClick: () -> Unit
) {
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
            text = "Składki",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (contributions.isEmpty()) {

            Text(
                text = "Nie dodano jeszcze żadnych składek.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

        } else {

            contributions.forEach { contribution ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            onContributionClick(contribution)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = contribution.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = String.format(
                                "%.2f zł / uczeń",
                                contribution.amountPerStudent
                            ).replace(".", ","),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Termin: ${contribution.dueDate}",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Utworzono: ${contribution.createdDate}",
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onAddContributionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "+ Dodaj składkę")
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