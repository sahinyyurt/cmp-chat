sealed interface Action {
    data class SendMessage(val message: Message) : Action
    data class MarkSent(val id: Long) : Action
}

data class State(
    val messages: List<Message> = emptyList()
)

fun chatReducer(state: State, action: Action): State =
    when (action) {
        is Action.SendMessage -> {
            state.copy(
                messages = (state.messages + action.message).takeLast(100)
            )
        }
        is Action.MarkSent -> {
            state.copy(
                messages = state.messages.map {
                    if (it.id == action.id) it.copy(status = MessageStatus.Sent) else it
                }
            )
        }
    }
