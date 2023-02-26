package com.github.osumatrix

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.PreHook
import com.discord.rtcconnection.audio.DiscordAudioManager

@Suppress("unused")
@AliucordPlugin
class FixBluetoothAudioQuality : Plugin() {
    override fun start(context: Context) {
        patcher.patch(DiscordAudioManager::class.java.methods.first { it.name == ACTIVATE_DEVICE_METHOD_NAME },
            PreHook { activateDevice ->
                val discordAudioManager = activateDevice.thisObject as DiscordAudioManager

                // Prevents DiscordAudioManager.activateDevice from being called.
                // The exact reason why this works is unknown.
                discordAudioManager.activateDeviceProxy(activateDevice.args[0] as DiscordAudioManager.DeviceTypes)
            })
    }

    override fun stop(context: Context) = patcher.unpatchAll()

    private companion object {
        const val ACTIVATE_DEVICE_METHOD_NAME = "b"

        fun DiscordAudioManager.activateDeviceProxy(deviceTypes: DiscordAudioManager.DeviceTypes) =
            i(deviceTypes != DiscordAudioManager.DeviceTypes.BLUETOOTH_HEADSET)
    }
}