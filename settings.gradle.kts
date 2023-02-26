rootProject.name = "AliucordPlugins"

include(
    "FixBluetoothAudioQuality"
)

rootProject.children.forEach {
    it.projectDir = file("plugins/kotlin/${it.name}")
}
