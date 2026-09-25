package pl.nikola.classfund.model

import java.util.UUID

data class Payment(
    val id: String = UUID.randomUUID().toString(),
    val studentName: String,
    val amount: Double,
    val purpose: String,
    val date: String,
    val contributionName: String? = null
)