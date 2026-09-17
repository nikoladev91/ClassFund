package pl.nikola.classfund.model

data class Contribution(
    val name: String,
    val amountPerStudent: Double,
    val createdDate: String,
    val dueDate: String
)