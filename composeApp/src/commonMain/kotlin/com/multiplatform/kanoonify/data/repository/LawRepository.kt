package com.multiplatform.kanoonify.data.repository

import com.multiplatform.kanoonify.domain.model.AskAnswer
import com.multiplatform.kanoonify.domain.model.LawItem

object LawRepository {

    fun findLaw(query: String, laws: List<LawItem>): String {
        val match = laws.find {
            it.keywords.any { keyword ->
                query.contains(keyword, ignoreCase = true)
            }
        }

        return match?.let {
            """
        📋 ${it.title}
        📂 Category: ${it.category}
        📝 ${it.description}
        ⚖️ Punishment: ${it.punishment}
            """.trimIndent()
        } ?: "No relevant law found for your query. Try describing your situation differently."
    }

    /** Structured, deterministic answer used by the redesigned Ask screen. */
    fun findAnswer(query: String, laws: List<LawItem>): AskAnswer {
        val match = laws.find {
            it.keywords.any { keyword -> query.contains(keyword, ignoreCase = true) }
        } ?: return AskAnswer.NotFound

        return AskAnswer.Found(
            law = match,
            rights = buildRights(match),
            applicableLaw = buildApplicableLaw(match),
            whatToDo = buildWhatToDo(match)
        )
    }

    private fun buildRights(law: LawItem): String {
        val category = law.category.lowercase()
        return when {
            "police" in category || "right" in category ->
                "You are entitled to exercise this right. Police or any authority cannot deny it without due process."
            "women" in category ->
                "You have the right to file a complaint at any police station (Zero FIR), seek free legal aid, and request a woman officer."
            "traffic" in category ->
                "You have the right to ask for the officer's ID, the reason for the stop, and to request a written challan."
            else ->
                "You have the right to be informed of the alleged offence and to seek legal representation before any statement."
        }
    }

    private fun buildApplicableLaw(law: LawItem): String =
        "${law.title}\nCategory: ${law.category.ifBlank { "General" }}\n\n${law.description}"

    private fun buildWhatToDo(law: LawItem): String {
        val p = law.punishment.lowercase()
        return when {
            "jail" in p || "imprison" in p ->
                "1. Stay calm and avoid self-incrimination.\n2. Contact a lawyer immediately.\n3. Request the right to bail where applicable.\n4. Note officer details and any witnesses."
            "fine" in p || "rs" in p || "challan" in p ->
                "1. Request a written challan with section reference.\n2. Pay only through official portals (e-Challan).\n3. Keep the receipt for your records.\n4. Contest in court if you believe it is unjust."
            else ->
                "1. Document the incident (date, time, location).\n2. Save any evidence (photos, messages, witnesses).\n3. File an FIR or written complaint at the nearest police station.\n4. Consult a lawyer or free legal aid cell for next steps."
        }
    }
}

