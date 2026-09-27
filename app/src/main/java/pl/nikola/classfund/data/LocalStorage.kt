package pl.nikola.classfund.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import pl.nikola.classfund.model.Contribution
import pl.nikola.classfund.model.Expense
import pl.nikola.classfund.model.Payment
import pl.nikola.classfund.model.SchoolClass
import java.util.UUID

class LocalStorage(context: Context) {

    private val preferences = context.getSharedPreferences(
        "classfund_storage",
        Context.MODE_PRIVATE
    )

    // ----------------------------------------------------
    // KLASY
    // ----------------------------------------------------

    fun saveClasses(classes: List<SchoolClass>) {

        val jsonArray = JSONArray()

        classes.forEach { schoolClass ->

            val jsonObject = JSONObject()

            jsonObject.put("id", schoolClass.id)
            jsonObject.put("name", schoolClass.name)
            jsonObject.put("schoolName", schoolClass.schoolName)
            jsonObject.put("schoolYear", schoolClass.schoolYear)
            jsonObject.put("classCode", schoolClass.classCode)

            jsonArray.put(jsonObject)
        }

        preferences.edit()
            .putString("school_classes", jsonArray.toString())
            .apply()
    }

    fun getClasses(): List<SchoolClass> {

        val savedClasses = preferences.getString(
            "school_classes",
            null
        ) ?: return emptyList()

        val jsonArray = JSONArray(savedClasses)

        val classes = mutableListOf<SchoolClass>()

        for (index in 0 until jsonArray.length()) {

            val jsonObject = jsonArray.getJSONObject(index)

            classes.add(
                SchoolClass(
                    id = jsonObject.getString("id"),
                    name = jsonObject.getString("name"),
                    schoolName = jsonObject.optString(
                        "schoolName",
                        ""
                    ),
                    schoolYear = jsonObject.getString("schoolYear"),
                    classCode = jsonObject.getString("classCode")
                )
            )
        }

        return classes
    }

    fun saveActiveClassId(classId: String) {

        preferences.edit()
            .putString(
                "active_class_id",
                classId
            )
            .apply()
    }

    fun getActiveClassId(): String {

        return preferences.getString(
            "active_class_id",
            ""
        ) ?: ""
    }

    fun getActiveClass(): SchoolClass? {

        val activeClassId = getActiveClassId()

        if (activeClassId.isBlank()) {
            return null
        }

        return getClasses().firstOrNull {
            it.id == activeClassId
        }
    }

    fun createSchoolClass(
        name: String,
        schoolName: String,
        schoolYear: String
    ): SchoolClass {

        val newClass = SchoolClass(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            schoolName = schoolName.trim(),
            schoolYear = schoolYear.trim(),
            classCode = generateClassCodeValue(name)
        )

        val updatedClasses =
            getClasses() + newClass

        saveClasses(updatedClasses)
        saveActiveClassId(newClass.id)

        return newClass
    }

    private fun generateClassCodeValue(
        className: String
    ): String {

        val cleanClassName = className
            .trim()
            .uppercase()
            .replace(" ", "")
            .replace(
                Regex("[^A-Z0-9]"),
                ""
            )
            .take(4)
            .ifBlank {
                "KLASA"
            }

        val randomPart = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .take(4)
            .uppercase()

        return "$cleanClassName-$randomPart"
    }

    // ----------------------------------------------------
    // STARE POLA KLASY
    // Na razie zostawiamy je dla zgodności z MainActivity
    // ----------------------------------------------------

    fun saveClassName(className: String) {

        preferences.edit()
            .putString(
                "class_name",
                className
            )
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
            .putString(
                "school_year",
                schoolYear
            )
            .apply()
    }

    fun getSchoolYear(): String {

        return preferences.getString(
            "school_year",
            "2026/2027"
        ) ?: "2026/2027"
    }

    fun saveClassCode(classCode: String) {

        preferences.edit()
            .putString(
                "class_code",
                classCode
            )
            .apply()
    }

    fun getClassCode(): String {

        return preferences.getString(
            "class_code",
            ""
        ) ?: ""
    }

    fun generateClassCode(
        className: String
    ): String {

        val classCode =
            generateClassCodeValue(className)

        saveClassCode(classCode)

        return classCode
    }

    // ----------------------------------------------------
    // KLUCZE DANYCH DLA AKTYWNEJ KLASY
    // ----------------------------------------------------

    private fun classKey(
        key: String
    ): String {

        val activeClassId =
            getActiveClassId()

        return if (
            activeClassId.isBlank()
        ) {
            key
        } else {
            "${activeClassId}_$key"
        }
    }

    // ----------------------------------------------------
    // UCZNIOWIE
    // ----------------------------------------------------

    fun saveStudents(
        students: List<String>
    ) {

        val jsonArray = JSONArray()

        students.forEach { student ->
            jsonArray.put(student)
        }

        preferences.edit()
            .putString(
                classKey("students"),
                jsonArray.toString()
            )
            .apply()
    }

    fun getStudents(): List<String> {

        val savedStudents =
            preferences.getString(
                classKey("students"),
                null
            ) ?: return emptyList()

        val jsonArray =
            JSONArray(savedStudents)

        val students =
            mutableListOf<String>()

        for (
        index in
        0 until jsonArray.length()
        ) {
            students.add(
                jsonArray.getString(index)
            )
        }

        return students
    }

    // ----------------------------------------------------
    // WPŁATY
    // ----------------------------------------------------

    fun savePayments(
        payments: List<Payment>
    ) {

        val jsonArray = JSONArray()

        payments.forEach { payment ->

            val jsonObject =
                JSONObject()

            jsonObject.put(
                "id",
                payment.id
            )

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

            jsonObject.put(
                "contributionName",
                payment.contributionName
                    ?: JSONObject.NULL
            )

            jsonArray.put(
                jsonObject
            )
        }

        preferences.edit()
            .putString(
                classKey("payments"),
                jsonArray.toString()
            )
            .apply()
    }

    fun getPayments(): List<Payment> {

        val savedPayments =
            preferences.getString(
                classKey("payments"),
                null
            ) ?: return emptyList()

        val jsonArray =
            JSONArray(savedPayments)

        val payments =
            mutableListOf<Payment>()

        var needsMigration = false

        for (
        index in
        0 until jsonArray.length()
        ) {

            val jsonObject =
                jsonArray.getJSONObject(index)

            val paymentId =
                if (
                    jsonObject.has("id") &&
                    !jsonObject.isNull("id") &&
                    jsonObject
                        .getString("id")
                        .isNotBlank()
                ) {

                    jsonObject
                        .getString("id")

                } else {

                    needsMigration = true

                    UUID.randomUUID()
                        .toString()
                }

            val contributionName =
                if (
                    jsonObject.has(
                        "contributionName"
                    ) &&
                    !jsonObject.isNull(
                        "contributionName"
                    )
                ) {

                    jsonObject.getString(
                        "contributionName"
                    )

                } else {

                    null
                }

            payments.add(
                Payment(
                    id = paymentId,
                    studentName =
                        jsonObject.getString(
                            "studentName"
                        ),
                    amount =
                        jsonObject.getDouble(
                            "amount"
                        ),
                    purpose =
                        jsonObject.getString(
                            "purpose"
                        ),
                    date =
                        jsonObject.getString(
                            "date"
                        ),
                    contributionName =
                        contributionName
                )
            )
        }

        if (needsMigration) {
            savePayments(payments)
        }

        return payments
    }

    // ----------------------------------------------------
    // WYDATKI
    // ----------------------------------------------------

    fun saveExpenses(
        expenses: List<Expense>
    ) {

        val jsonArray = JSONArray()

        expenses.forEach { expense ->

            val jsonObject =
                JSONObject()

            jsonObject.put(
                "id",
                expense.id
            )

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
                expense.attachmentUri
                    ?: JSONObject.NULL
            )

            jsonArray.put(
                jsonObject
            )
        }

        preferences.edit()
            .putString(
                classKey("expenses"),
                jsonArray.toString()
            )
            .apply()
    }

    fun getExpenses(): List<Expense> {

        val savedExpenses =
            preferences.getString(
                classKey("expenses"),
                null
            ) ?: return emptyList()

        val jsonArray =
            JSONArray(savedExpenses)

        val expenses =
            mutableListOf<Expense>()

        var needsMigration = false

        for (
        index in
        0 until jsonArray.length()
        ) {

            val jsonObject =
                jsonArray.getJSONObject(index)

            val expenseId =
                if (
                    jsonObject.has("id") &&
                    !jsonObject.isNull("id") &&
                    jsonObject
                        .getString("id")
                        .isNotBlank()
                ) {

                    jsonObject
                        .getString("id")

                } else {

                    needsMigration = true

                    UUID.randomUUID()
                        .toString()
                }

            val attachmentUri =
                if (
                    jsonObject.has(
                        "attachmentUri"
                    ) &&
                    !jsonObject.isNull(
                        "attachmentUri"
                    )
                ) {

                    jsonObject.getString(
                        "attachmentUri"
                    )

                } else {

                    null
                }

            expenses.add(
                Expense(
                    id = expenseId,
                    amount =
                        jsonObject.getDouble(
                            "amount"
                        ),
                    purpose =
                        jsonObject.getString(
                            "purpose"
                        ),
                    date =
                        jsonObject.getString(
                            "date"
                        ),
                    attachmentUri =
                        attachmentUri
                )
            )
        }

        if (needsMigration) {
            saveExpenses(expenses)
        }

        return expenses
    }

    // ----------------------------------------------------
    // SKŁADKI
    // ----------------------------------------------------

    fun saveContributions(
        contributions: List<Contribution>
    ) {

        val jsonArray = JSONArray()

        contributions.forEach {
                contribution ->

            val jsonObject =
                JSONObject()

            jsonObject.put(
                "name",
                contribution.name
            )

            jsonObject.put(
                "amountPerStudent",
                contribution
                    .amountPerStudent
            )

            jsonObject.put(
                "createdDate",
                contribution.createdDate
            )

            jsonObject.put(
                "dueDate",
                contribution.dueDate
            )

            jsonArray.put(
                jsonObject
            )
        }

        preferences.edit()
            .putString(
                classKey(
                    "contributions"
                ),
                jsonArray.toString()
            )
            .apply()
    }

    fun getContributions():
            List<Contribution> {

        val savedContributions =
            preferences.getString(
                classKey(
                    "contributions"
                ),
                null
            ) ?: return emptyList()

        val jsonArray =
            JSONArray(
                savedContributions
            )

        val contributions =
            mutableListOf<Contribution>()

        for (
        index in
        0 until jsonArray.length()
        ) {

            val jsonObject =
                jsonArray
                    .getJSONObject(index)

            contributions.add(
                Contribution(
                    name =
                        jsonObject.getString(
                            "name"
                        ),
                    amountPerStudent =
                        jsonObject.getDouble(
                            "amountPerStudent"
                        ),
                    createdDate =
                        jsonObject.getString(
                            "createdDate"
                        ),
                    dueDate =
                        jsonObject.getString(
                            "dueDate"
                        )
                )
            )
        }

        return contributions
    }

    // ----------------------------------------------------
    // SALDO
    // ----------------------------------------------------

    fun saveBalance(
        balance: Double
    ) {

        preferences.edit()
            .putString(
                classKey(
                    "class_balance"
                ),
                balance.toString()
            )
            .apply()
    }

    fun getBalance(): Double {

        return preferences.getString(
            classKey(
                "class_balance"
            ),
            "0.0"
        )?.toDoubleOrNull()
            ?: 0.0
    }
    fun migrateOldDataIfNeeded(): SchoolClass? {

        // Jeżeli mamy już klasy w nowym systemie,
        // migracji drugi raz nie wykonujemy.
        if (getClasses().isNotEmpty()) {
            return getActiveClass()
                ?: getClasses().firstOrNull()
        }

        // Pobieramy stare dane zapisane przed systemem wielu klas.
        val oldClassName = preferences.getString(
            "class_name",
            "7A"
        ) ?: "7A"

        val oldSchoolYear = preferences.getString(
            "school_year",
            "2026/2027"
        ) ?: "2026/2027"

        val oldClassCode = preferences.getString(
            "class_code",
            ""
        ) ?: ""

        val oldStudents = preferences.getString(
            "students",
            null
        )

        val oldPayments = preferences.getString(
            "payments",
            null
        )

        val oldExpenses = preferences.getString(
            "expenses",
            null
        )

        val oldContributions = preferences.getString(
            "contributions",
            null
        )

        val oldBalance = preferences.getString(
            "class_balance",
            "0.0"
        ) ?: "0.0"

        val migratedClass = SchoolClass(
            id = UUID.randomUUID().toString(),
            name = oldClassName,
            schoolName = "",
            schoolYear = oldSchoolYear,
            classCode = if (oldClassCode.isNotBlank()) {
                oldClassCode
            } else {
                generateClassCodeValue(oldClassName)
            }
        )

        saveClasses(
            listOf(migratedClass)
        )

        saveActiveClassId(
            migratedClass.id
        )

        val classId = migratedClass.id

        val editor = preferences.edit()

        if (oldStudents != null) {
            editor.putString(
                "${classId}_students",
                oldStudents
            )
        }

        if (oldPayments != null) {
            editor.putString(
                "${classId}_payments",
                oldPayments
            )
        }

        if (oldExpenses != null) {
            editor.putString(
                "${classId}_expenses",
                oldExpenses
            )
        }

        if (oldContributions != null) {
            editor.putString(
                "${classId}_contributions",
                oldContributions
            )
        }

        editor.putString(
            "${classId}_class_balance",
            oldBalance
        )

        editor.apply()

        return migratedClass
    }
}