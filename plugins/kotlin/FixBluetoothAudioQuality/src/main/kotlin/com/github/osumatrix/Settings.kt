import android.os.Bundle
import android.view.View
import com.aliucord.PluginManager
import com.aliucord.Utils
import com.aliucord.api.SettingsAPI
import com.aliucord.widgets.BottomSheet
import com.discord.views.CheckedSetting

class Settings(private val settings: SettingsAPI) : BottomSheet() {
    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        context?.let {
            addView(
                Utils.createCheckedSetting(
                    it,
                    CheckedSetting.ViewType.SWITCH,
                    "Compatibility mode",
                    "If the plugin does not work, turn this on"
                ).apply {
                    isChecked = settings.getBool("compatibilityMode", false)
                    setOnCheckedListener {
                        settings.setBool("compatibilityMode", it)
                        PluginManager.stopPlugin("FixBluetoothAudioQuality")
                        PluginManager.startPlugin("FixBluetoothAudioQuality")
                    }
                }
            )
        }
    }
}