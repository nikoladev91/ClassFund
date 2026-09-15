package pl.nikola.classfund.model

data class Expense(
    val amount: Double,
    val purpose: String,
    val date: String,
    val attachmentUri: String? = null
)