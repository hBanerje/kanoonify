package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.LawyerDataProvider
import com.multiplatform.kanoonify.domain.model.ChatAuthor
import com.multiplatform.kanoonify.domain.model.ChatMessage
import com.multiplatform.kanoonify.domain.model.Lawyer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LawyerChatState(
    val lawyer: Lawyer? = null,
    val draft: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val isLawyerTyping: Boolean = false
)

class LawyerChatViewModel(lawyerId: String) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(LawyerChatState())
    val state: StateFlow<LawyerChatState> = _state

    private var messageCounter = 0

    init {
        val lawyer = LawyerDataProvider.findById(lawyerId)
        val initial = if (lawyer != null) {
            listOf(
                ChatMessage(
                    id = nextId(),
                    author = ChatAuthor.Lawyer,
                    text = "Hello, I'm ${lawyer.name}. How can I help you today?",
                    timestamp = "Just now"
                )
            )
        } else emptyList()

        _state.update { it.copy(lawyer = lawyer, messages = initial) }
    }

    fun onDraftChange(value: String) {
        _state.update { it.copy(draft = value) }
    }

    fun onSend() {
        val text = _state.value.draft.trim()
        if (text.isBlank()) return

        val userMsg = ChatMessage(
            id = nextId(),
            author = ChatAuthor.User,
            text = text,
            timestamp = "Now"
        )
        _state.update {
            it.copy(
                draft = "",
                messages = it.messages + userMsg,
                isLawyerTyping = true
            )
        }

        scope.launch {
            delay(1200L)
            val reply = generateDummyReply(text)
            _state.update {
                it.copy(
                    isLawyerTyping = false,
                    messages = it.messages + ChatMessage(
                        id = nextId(),
                        author = ChatAuthor.Lawyer,
                        text = reply,
                        timestamp = "Now"
                    )
                )
            }
        }
    }

    private fun nextId(): String = "m_${++messageCounter}"

    private fun generateDummyReply(userText: String): String {
        val lower = userText.lowercase()
        return when {
            "hi" in lower || "hello" in lower ->
                "Hi! Please share a brief description of your matter so I can guide you better."
            "fee" in lower || "charge" in lower || "cost" in lower ->
                "My standard consultation fee is ₹${_state.value.lawyer?.feePerSession ?: 1500} per session."
            "thank" in lower ->
                "You're welcome. Feel free to reach out whenever you need further assistance."
            "?" in lower ->
                "That's a good question. Based on what you've shared, I'd recommend documenting the facts and gathering any supporting evidence first."
            else ->
                "Understood. Could you share a few more details — dates, parties involved, and any documents you have?"
        }
    }
}
