package pl.nikola.classfund.model

import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val purpose: String,
    val date: String,
    val attachmentUri: String? = null
)