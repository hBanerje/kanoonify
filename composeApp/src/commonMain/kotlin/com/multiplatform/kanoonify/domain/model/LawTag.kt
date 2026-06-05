package com.multiplatform.kanoonify.domain.model

enum class LawTag(val label: String) {
    FINE("Fine"),
    JAIL("Jail"),
    RIGHT("Right")
}

fun deriveLawTag(punishment: String): LawTag {
    val p = punishment.lowercase()
    return when {
        p.contains("jail") || p.contains("imprison") || p.contains("custody") ||
            p.contains("years") || p.contains("year ") || p.contains("month") -> LawTag.JAIL
        p.contains("fine") || p.contains("rs") || p.contains("₹") ||
            p.contains("challan") || p.contains("penalty") -> LawTag.FINE
        else -> LawTag.RIGHT
    }
}

fun deriveUserAction(law: LawItem): String {
    return when (deriveLawTag(law.punishment)) {
        LawTag.JAIL ->
            "1. Stay calm. Do not resist or self-incriminate.\n" +
            "2. Ask the officer for ID and reason for action.\n" +
            "3. Contact a lawyer or legal aid cell immediately.\n" +
            "4. Request bail where the offence is bailable."
        LawTag.FINE ->
            "1. Request a written challan with section reference.\n" +
            "2. Pay only via official e-Challan portals.\n" +
            "3. Keep the receipt safely.\n" +
            "4. Contest in court within the allowed window if unjust."
        LawTag.RIGHT ->
            "1. Politely assert your right and ask for written grounds.\n" +
            "2. Document the incident (time, place, witnesses).\n" +
            "3. File an FIR or written complaint if denied.\n" +
            "4. Reach out to free legal aid services for support."
    }
}
