package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.entities.Plugin;
import com.discord.rtcconnection.audio.DiscordAudioManager;

import top.canyie.pine.callback.MethodReplacement;

@SuppressWarnings("unused")
@AliucordPlugin
public class FixBluetoothAudioQuality extends Plugin {
    @NonNull
    @Override
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{ new Manifest.Author("oSumAtrIX", 737323631117598811L) };
        manifest.description = "Reverts discords changes to audio settings to enable high quality audio streaming when in a call";
        manifest.version = "1.0.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/oSumAtrIX/aliucord-plugins/builds/updater.json";
        return manifest;
    }
    @Override
    public void start(Context context) throws NoSuchMethodException {
        Class<DiscordAudioManager> DiscordAudioManagerClass = com.discord.rtcconnection.audio.DiscordAudioManager.class;

        patcher.patch(DiscordAudioManagerClass.getDeclaredMethod("h", boolean.class), MethodReplacement.DO_NOTHING);
        patcher.patch(DiscordAudioManagerClass.getDeclaredMethod("j"), MethodReplacement.DO_NOTHING);
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }
}
