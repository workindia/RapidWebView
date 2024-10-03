package `in`.workindia.rapidwebview.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi

object GlobalBroadcastReceiverRegistrar {

    fun registerReceiver(
        context: Context,
        broadcastReceiver: BroadcastReceiver,
        intentFilter: IntentFilter,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            registerReceiverApi34(
                context,
                broadcastReceiver,
                intentFilter
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiverApi26(
                context,
                broadcastReceiver,
                intentFilter
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun registerReceiverApi34(
        context: Context,
        broadcastReceiver: BroadcastReceiver,
        intentFilter: IntentFilter,
    ) {
        context.registerReceiver(
            broadcastReceiver,
            intentFilter,
            RECEIVER_EXPORTED
        )
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerReceiverApi26(
        context: Context,
        broadcastReceiver: BroadcastReceiver,
        intentFilter: IntentFilter,
    ) {
        context.registerReceiver(
            broadcastReceiver,
            intentFilter
        )
    }

}