package pl.nikola.classfund.model

data class Payment(
    val studentName: String,
    val amount: Double,
    val purpose: String,
    val date: String
)