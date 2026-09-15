package pl.nikola.classfund.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import pl.nikola.classfund.model.Expense
import pl.nikola.classfund.model.Payment

class LocalStorage(context: Context) {

    private val preferences = context.getSharedPreferences(
        "classfund_storage",
        Context.MODE_PRIVATE
    )

    fun saveClassName(className: String) {
        preferences.edit()
            .putString("class_name", className)
            .apply()
    }

    fun getClassName(): String {
        return preferences.getString(
            "class_name",
            "7A"
        ) ?: "7A"
    }

    fun saveSchoolYear(schoolYear: String) {
        preferences.edit()
            .putString("school_year", schoolYear)
            .apply()
    }

    fun getSchoolYear(): String {
        return preferences.getString(
            "school_year",
            "2026/2027"
        ) ?: "2026/2027"
    }

    fun saveStudents(students: List<String>) {

        val jsonArray = JSONArray()

        students.forEach { student ->
            jsonArray.put(student)
        }

        preferences.edit()
            .putString("students", jsonArray.toString())
            .apply()
    }

    fun getStudents(): List<String> {

        val savedStudents = preferences.getString(
            "students",
            null
        ) ?: return emptyList()

        val jsonArray = JSONArray(savedStudents)

        val students = mutableListOf<String>()

        for (index in 0 until jsonArray.length()) {
            students.add(
                jsonArray.getString(index)
            )
        }

        return students
    }

    fun savePayments(payments: List<Payment>) {

        val jsonArray = JSONArray()

        payments.forEach { payment ->

            val jsonObject = JSONObject()

            jsonObject.put(
                "studentName",
                payment.studentName
            )

            jsonObject.put(
                "amount",
                payment.amount
            )

            jsonObject.put(
                "purpose",
                payment.purpose
            )

            jsonObject.put(
                "date",
                payment.date
            )

            jsonArray.put(jsonObject)
        }

        preferences.edit()
            .putString("payments", jsonArray.toString())
            .apply()
    }

    fun getPayments(): List<Payment> {

        val savedPayments = preferences.getString(
            "payments",
            null
        ) ?: return emptyList()

        val jsonArray = JSONArray(savedPayments)

        val payments = mutableListOf<Payment>()

        for (index in 0 until jsonArray.length()) {

            val jsonObject = jsonArray.getJSONObject(index)

            payments.add(
                Payment(
                    studentName = jsonObject.getString("studentName"),
                    amount = jsonObject.getDouble("amount"),
                    purpose = jsonObject.getString("purpose"),
                    date = jsonObject.getString("date")
                )
            )
        }

        return payments
    }

    fun saveExpenses(expenses: List<Expense>) {

        val jsonArray = JSONArray()

        expenses.forEach { expense ->

            val jsonObject = JSONObject()

            jsonObject.put(
                "amount",
                expense.amount
            )

            jsonObject.put(
                "purpose",
                expense.purpose
            )

            jsonObject.put(
                "date",
                expense.date
            )

            jsonObject.put(
                "attachmentUri",
                expense.attachmentUri ?: JSONObject.NULL
            )

            jsonArray.put(jsonObject)
        }

        preferences.edit()
            .putString("expenses", jsonArray.toString())
            .apply()
    }

    fun getExpenses(): List<Expense> {

        val savedExpenses = preferences.getString(
            "expenses",
            null
        ) ?: return emptyList()

        val jsonArray = JSONArray(savedExpenses)

        val expenses = mutableListOf<Expense>()

        for (index in 0 until jsonArray.length()) {

            val jsonObject = jsonArray.getJSONObject(index)

            val attachmentUri =
                if (
                    jsonObject.has("attachmentUri") &&
                    !jsonObject.isNull("attachmentUri")
                ) {
                    jsonObject.getString("attachmentUri")
                } else {
                    null
                }

            expenses.add(
                Expense(
                    amount = jsonObject.getDouble("amount"),
                    purpose = jsonObject.getString("purpose"),
                    date = jsonObject.getString("date"),
                    attachmentUri = attachmentUri
                )
            )
        }

        return expenses
    }

    fun saveBalance(balance: Double) {
        preferences.edit()
            .putString(
                "class_balance",
                balance.toString()
            )
            .apply()
    }

    fun getBalance(): Double {
        return preferences.getString(
            "class_balance",
            "0.0"
        )?.toDoubleOrNull() ?: 0.0
    }
}