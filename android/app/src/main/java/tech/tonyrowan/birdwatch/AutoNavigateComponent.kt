package tech.tonyrowan.birdwatch

import android.util.Log
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import kotlinx.serialization.Serializable

class AutoNavigateComponent(
    name: String,
    private val delegate: BridgeDelegate<HotwireDestination>
) : BridgeComponent<HotwireDestination>(name, delegate) {

    override fun onReceive(message: Message) {
        Log.d("AutoNavigateComponent", "Message=${message}")
        when (message.event) {
            "connect" -> handleConnectEvent(message)
            else -> Log.w("AutoNavigateComponent", "Unknown event for message: $message")
        }
    }

    private fun handleConnectEvent(message: Message) {
        val data = message.data<MessageData>() ?: return
        delegate.destination.navigator.route("http://localhost:3000${data.destination}")
    }

    @Serializable
    data class MessageData(val destination: String)
}