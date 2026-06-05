package com.multiplatform.kanoonify.data

import com.multiplatform.kanoonify.domain.model.SubCategory

object CategoryDataProvider {

    private val categoryMap: Map<String, List<SubCategory>> = mapOf(
        "Traffic Rules" to listOf(
            SubCategory(
                title = "Driving without licence",
                keywords = listOf("licence", "no license", "without licence", "expired driving licence")
            ),
            SubCategory(
                title = "Overspeeding",
                keywords = listOf("overspeed", "speed limit", "speed camera", "overspeeding")
            ),
            SubCategory(
                title = "Drunk driving",
                keywords = listOf("drunk driving", "alcohol", "drink and drive", "DUI", "breathalyzer")
            ),
            SubCategory(
                title = "Riding without helmet",
                keywords = listOf("helmet", "no helmet", "pillion rider helmet")
            ),
            SubCategory(
                title = "Using phone while driving",
                keywords = listOf("phone while driving", "mobile use while driving", "texting while driving")
            ),
            SubCategory(
                title = "Red light / signal jumping",
                keywords = listOf("red light", "signal", "traffic signal", "signal jump")
            ),
            SubCategory(
                title = "Wrong side driving",
                keywords = listOf("wrong side", "wrong way", "one way")
            ),
            SubCategory(
                title = "No seatbelt",
                keywords = listOf("seatbelt", "seat belt", "no seatbelt")
            ),
            SubCategory(
                title = "No insurance / RC",
                keywords = listOf("insurance", "RC", "registration", "no insurance")
            ),
            SubCategory(
                title = "Hit and run",
                keywords = listOf("hit and run", "accident", "fled", "accident report")
            )
        ),

        "Police Rights" to listOf(
            SubCategory(
                title = "Arrest without warrant",
                keywords = listOf("arrest", "without warrant", "arrested without reason")
            ),
            SubCategory(
                title = "FIR filing rights",
                keywords = listOf("FIR", "file FIR", "police refused FIR", "zero FIR")
            ),
            SubCategory(
                title = "Search & seizure rules",
                keywords = listOf("search warrant", "police search", "seizure", "police took my phone")
            ),
            SubCategory(
                title = "Right to bail",
                keywords = listOf("bail", "zamaanat", "bailable", "non bailable")
            ),
            SubCategory(
                title = "Right to lawyer",
                keywords = listOf("lawyer", "legal aid", "right to lawyer", "advocate")
            ),
            SubCategory(
                title = "Police cannot enter home without warrant",
                keywords = listOf("warrant", "home search", "police entry", "raid")
            ),
            SubCategory(
                title = "Right against forced confession",
                keywords = listOf("confession", "forced confession", "torture", "custodial")
            ),
            SubCategory(
                title = "Woman arrest rules",
                keywords = listOf("woman arrest", "female arrest", "sunset", "lady police")
            )
        ),

        "Women Safety" to listOf(
            SubCategory(
                title = "Sexual harassment",
                keywords = listOf("sexual harassment", "harassment", "eve teasing", "POSH")
            ),
            SubCategory(
                title = "Stalking",
                keywords = listOf("stalking", "following", "cyberstalking", "online stalking")
            ),
            SubCategory(
                title = "Domestic violence",
                keywords = listOf("domestic violence", "husband beating", "DV Act", "gharelu hinsa")
            ),
            SubCategory(
                title = "Dowry harassment",
                keywords = listOf("dowry", "dahej", "498A", "dowry death")
            ),
            SubCategory(
                title = "Workplace harassment",
                keywords = listOf("workplace harassment", "POSH Act", "office", "ICC complaint")
            ),
            SubCategory(
                title = "Voyeurism / secret recording",
                keywords = listOf("voyeurism", "recording", "hidden camera", "MMS")
            ),
            SubCategory(
                title = "Acid attack",
                keywords = listOf("acid attack", "tezaab", "acid")
            )
        ),

        "Public Safety" to listOf(
            SubCategory(
                title = "Public nuisance",
                keywords = listOf("nuisance", "public nuisance", "disturbance", "blocking road")
            ),
            SubCategory(
                title = "Drunk & disorderly in public",
                keywords = listOf("drunk in public", "drunk disorderly", "IPC 510")
            ),
            SubCategory(
                title = "Noise pollution / loud music",
                keywords = listOf("noise", "loud music", "loudspeaker", "noise pollution")
            ),
            SubCategory(
                title = "Smoking in public",
                keywords = listOf("smoking", "public smoking", "COTPA", "cigarette")
            ),
            SubCategory(
                title = "Obscene behaviour in public",
                keywords = listOf("obscene", "abusing", "IPC 294", "vulgar")
            ),
            SubCategory(
                title = "Rioting / mob violence",
                keywords = listOf("rioting", "mob", "violence", "stone pelting")
            ),
            SubCategory(
                title = "Consumer rights / defective products",
                keywords = listOf("consumer", "defective", "refund", "complaint", "overcharging")
            ),
            SubCategory(
                title = "Curfew / Section 144 violation",
                keywords = listOf("curfew", "Section 144", "prohibitory", "lockdown")
            )
        )
    )

    fun getSubcategories(category: String): List<SubCategory> {
        return categoryMap[category] ?: emptyList()
    }

    fun getAllCategoryNames(): List<String> {
        return categoryMap.keys.toList()
    }
}
