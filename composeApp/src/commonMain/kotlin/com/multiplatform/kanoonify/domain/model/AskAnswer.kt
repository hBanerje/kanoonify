package com.multiplatform.kanoonify.domain.model

/**
 * A structured deterministic answer used by the Ask flow.
 * No AI — content is composed from a matched [LawItem] using simple rules.
 */
sealed class AskAnswer {
    data class Found(
        val law: LawItem,
        val rights: String,
        val applicableLaw: String,
        val whatToDo: String
    ) : AskAnswer()

    data object NotFound : AskAnswer()
}

