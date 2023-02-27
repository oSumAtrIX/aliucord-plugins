package com.github.osumatrix

import Settings
import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.PreHook
import com.discord.rtcconnection.audio.DiscordAudioManager
import de.robv.android.xposed.XC_MethodReplacement
import java.lang.Boolean
import kotlin.Suppress
import kotlin.apply
import kotlin.arrayOfNulls
import kotlin.let


@Suppress("unused")
@AliucordPlugin
class FixBluetoothAudioQuality : Plugin() {
    init {
        settingsTab = SettingsTab(Settings::class.java, SettingsTab.Type.BOTTOM_SHEET).withArgs(settings)
    }

    override fun start(context: Context) {
        patcher.apply {
            if (settings.getBool("compatibilityMode", false)) {
                // Side effect includes not being able to output from device speaker
                DiscordAudioManager::class.java.let {
                    listOf(
                        it.getDeclaredMethod(SET_COMMUNICATION_MODE_ON_METHOD_NAME, Boolean.TYPE),
                        it.getDeclaredMethod(START_BLUETOOTH_SCO, *arrayOfNulls(0))
                    )
                }.forEach { patch(it, XC_MethodReplacement.DO_NOTHING) }
            } else {
                patch(DiscordAudioManager::class.java.methods.first { it.name == ACTIVATE_DEVICE_METHOD_NAME },
                    PreHook { activateDevice ->
                        val discordAudioManager = activateDevice.thisObject as DiscordAudioManager

                        // Prevents DiscordAudioManager.activateDevice from being called when audio mode is normal.
                        // The exact reason why this works is unknown.
                        discordAudioManager.activateDeviceProxy(activateDevice.args[0] as DiscordAudioManager.DeviceTypes)
                    })
            }
        }
    }

    override fun stop(context: Context) = patcher.unpatchAll()

    private companion object {
        const val ACTIVATE_DEVICE_METHOD_NAME = "b"

        // Hooked on compatibility mode
        const val SET_COMMUNICATION_MODE_ON_METHOD_NAME = "h"

        // Hooked on compatibility mode
        const val START_BLUETOOTH_SCO = "j"

        fun DiscordAudioManager.activateDeviceProxy(deviceTypes: DiscordAudioManager.DeviceTypes) =
            i(deviceTypes != DiscordAudioManager.DeviceTypes.BLUETOOTH_HEADSET)
    }
}
