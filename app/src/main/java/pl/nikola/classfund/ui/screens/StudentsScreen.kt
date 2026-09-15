package pl.nikola.classfund.ui.screens

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

@Composable
fun StudentsScreen(
    students: List<String>,
    onStudentClick: (String) -> Unit,
    onAddStudentClick: () -> Unit,
    onBackClick: () -> Unit
) {
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
            text = "Uczniowie",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (students.isEmpty()) {

            Text(
                text = "Nie dodano jeszcze żadnych uczniów.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

        } else {

            students
                .sortedBy { student ->
                    student.trim()
                        .substringAfterLast(" ")
                        .lowercase()
                }
                .forEach { student ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                onStudentClick(student)
                            }
                    ) {
                        Text(
                            text = student,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onAddStudentClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "+ Dodaj ucznia")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Wróć")
        }
    }
}