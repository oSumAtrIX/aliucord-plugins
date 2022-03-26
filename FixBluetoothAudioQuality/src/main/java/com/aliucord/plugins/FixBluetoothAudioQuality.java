package com.aliucord.plugins;

import android.content.Context;
import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.entities.Plugin;
import com.discord.rtcconnection.audio.DiscordAudioManager;

import de.robv.android.xposed.XC_MethodReplacement;

@SuppressWarnings("unused")
@AliucordPlugin
public class FixBluetoothAudioQuality extends Plugin {

    @Override
    public void start(Context context) throws NoSuchMethodException {
        Class<DiscordAudioManager> DiscordAudioManagerClass = com.discord.rtcconnection.audio.DiscordAudioManager.class;
        patcher.patch(DiscordAudioManagerClass.getDeclaredMethod("h", boolean.class), XC_MethodReplacement.DO_NOTHING);
        patcher.patch(DiscordAudioManagerClass.getDeclaredMethod("j"), XC_MethodReplacement.DO_NOTHING);
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }
}
