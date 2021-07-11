package dev.isxander.evergreenhud.installer

import co.uk.isxander.libinstaller.InstallerUtils
import org.apache.commons.io.FileUtils
import java.awt.Font
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.jar.JarFile
import javax.swing.*

object EvergreenHUDInstaller {

    private const val coreUrl = "https://static.isxander.co.uk/evergreenhud/core/%s.jar"
    private const val coreVersion = "https://raw.githubusercontent.com/isXander/EvergreenHUD/main/version.json"

    fun install(mcDir: File) {
        val dataDir = File(mcDir, "evergreenhud")
        if (!dataDir.exists()) dataDir.mkdirs()

        installCore(dataDir)
        updateAddons(dataDir)

    }

    private fun installCore(dataDir: File) {
        val config = InstallerUtils.readFile(File(dataDir, "config/global.json"))
        val releaseChannel = config.optString("release_channel", "release")!!

        val localFile = File(dataDir, "metadata.json")
        val localVersion = InstallerUtils.readFile(localFile)
        val remoteVersion = InstallerUtils.fetchJSON(coreVersion)

        val currentVersion = localVersion.optString(releaseChannel)
        val latestVersion = remoteVersion.optString(releaseChannel)!!

        val outputFile = File(dataDir, "core.jar")
        if (currentVersion == null
            || localVersion.optString("release_channel", "release") != releaseChannel
            || latestVersion != currentVersion) {
            if (InstallerUtils.download(String.format(coreUrl, latestVersion), outputFile, "EvergreenHUD", "${if (outputFile.exists()) "Updating" else "Downloading"} core...")) {
                localVersion.put("release_channel", releaseChannel)
                localVersion.put(releaseChannel, latestVersion)
                FileUtils.write(localFile, localVersion.toString(), Charset.defaultCharset())

                InstallerUtils.addToClasspath(outputFile)
            }
        }
    }

    private fun updateAddons(dataDir: File) {
        val addonsFolderFile = File(dataDir, "addons")
        if (!addonsFolderFile.exists()) addonsFolderFile.mkdirs()
        val jarFiles = addonsFolderFile.listFiles { _, name -> name.endsWith(".jar") }!!
        val remove = ArrayList<File>()

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        val frame = JFrame("EvergreenHUD")
        val bar = JProgressBar()
        val label = JLabel("Updating addons...", SwingConstants.CENTER)
        label.setSize(600, 120)
        frame.contentPane.add(label)
        frame.contentPane.add(bar)
        val layout = GroupLayout(frame.contentPane)
        frame.contentPane.layout = layout
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(label, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE.toInt())
                    .addComponent(bar, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt()))
                .addContainerGap()))
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bar, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())))
        frame.isResizable = false
        bar.isBorderPainted = true
        bar.minimum = 0
        bar.isStringPainted = true
        val font: Font = bar.font
        bar.font = Font(font.name, font.style, font.size * 2)
        label.font = Font(font.name, font.style, font.size * 2)
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        bar.maximum = jarFiles.size

        for (addonFile in jarFiles) {
            bar.value++

            JarFile(addonFile).use { jar ->
                val entry = jar.getEntry("evergreen.addon.json")
                val addonJson = InstallerUtils.readFile(File(entry.name))
                val addonName = addonJson.optString("name", "Unknown")!!

                label.name = "Updating $addonName addon..."
                val updateUrl = addonJson.optString("updateUrl", null)
                if (updateUrl != null) {
                    val updateJson = InstallerUtils.fetchJSON(updateUrl)
                    val currentVersion = addonJson.optString("version", "1.0")!!
                    val latestVersion = updateJson.optString("version", "1.0")!!

                    if (currentVersion != latestVersion) {
                        FileOutputStream(File(addonsFolderFile, "$addonName ($latestVersion).jar")).use { outputStream ->
                            val u = URL(updateJson.optString(updateJson.optString("download", null)!!))
                            val connection = u.openConnection() as HttpURLConnection
                            connection.requestMethod = "GET"
                            connection.useCaches = true
                            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (EvergreenHUD)")
                            connection.readTimeout = 15000
                            connection.connectTimeout = 15000
                            connection.doOutput = true
                            val instream = connection.inputStream
                            val buffer = ByteArray(1024)
                            var read: Int
                            while (instream.read(buffer).also { read = it } > 0) {
                                outputStream.write(buffer, 0, read)
                            }
                        }

                        remove.add(addonFile)
                    }
                }
            }
        }

        frame.dispose()
    }

}