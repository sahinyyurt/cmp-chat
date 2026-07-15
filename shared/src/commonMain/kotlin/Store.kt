import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

// Simulated network latency before a sent message flips from Sending to Sent.
private const val SENT_DELAY_MS = 600L

interface Store {
    fun send(action: Action)
    val stateFlow: StateFlow<State>
    val state get() = stateFlow.value
}

fun CoroutineScope.createStore(): Store {
    val mutableStateFlow = MutableStateFlow(State())
    val channel: Channel<Action> = Channel(Channel.UNLIMITED)

    return object : Store {
        init {
            launch {
                channel.consumeAsFlow().collect { action ->
                    mutableStateFlow.value = chatReducer(mutableStateFlow.value, action)
                    // A message inserted as Sending flips to Sent after a simulated
                    // delay — there is no backend, so this stands in for the network.
                    if (action is Action.SendMessage &&
                        action.message.status == MessageStatus.Sending
                    ) {
                        val id = action.message.id
                        launch {
                            delay(SENT_DELAY_MS)
                            channel.send(Action.MarkSent(id))
                        }
                    }
                }
            }
        }

        override fun send(action: Action) {
            launch {
                channel.send(action)
            }
        }

        override val stateFlow: StateFlow<State> = mutableStateFlow
    }
}
