package com.multiplatform.kanoonify.domain.model

sealed class AskAnswer {
    data class Found(
        val law: LawItem,
        val rights: String,
        val applicableLaw: String,
        val whatToDo: String
    ) : AskAnswer()

    data object NotFound : AskAnswer()
}
