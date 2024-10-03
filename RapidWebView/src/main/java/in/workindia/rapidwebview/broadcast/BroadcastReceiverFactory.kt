package `in`.workindia.rapidwebview.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

object BroadcastReceiverFactory {

    fun createReceiver(
        action: String,
        onReceive: (context: Context?, intent: Intent, receiver: BroadcastReceiver) -> Unit,
    ): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == action) {
                    onReceive(context, intent, this)
                }
            }
        }
    }
}