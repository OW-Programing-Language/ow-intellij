package dev.oreo.owplugin

import com.intellij.openapi.application.PathManager
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Resolves how to launch the Ow toolchain so users don't have to install it:
 *
 *  1. If the user has a real `ow` on PATH, use it (their install always wins).
 *  2. Otherwise run the **bundled** Ow runtime (`OwIDE.jar`, shipped inside the
 *     plugin) with the IDE's own Java (the JetBrains Runtime), which is a full
 *     JDK — so neither Ow nor Java needs to be installed separately.
 *
 * The bundled jar lives in plugin resources at `/ow/OwIDE.jar`; it is extracted
 * once to a versioned file under the IDE system directory and reused thereafter.
 */
object OwRuntime {
    /** Keep in sync with the OwIDE.jar downloaded by build.gradle.kts. */
    const val VERSION = "1.2.3"

    private val isWindows = System.getProperty("os.name").orEmpty().lowercase().contains("win")

    /** The command prefix to run Ow, e.g. ["/path/ow"] or ["/jbr/bin/java","-jar","/cache/OwIDE.jar"]. */
    fun command(): List<String> {
        pathOw()?.let { return listOf(it) }
        bundledJar()?.let { return listOf(javaExe(), "-jar", it.absolutePath) }
        return listOf(if (isWindows) "ow.exe" else "ow")
    }

    /** True when a usable Ow launcher is available (always true once bundled). */
    fun isAvailable(): Boolean = pathOw() != null || bundledJar() != null

    private fun javaExe(): String {
        val home = System.getProperty("java.home") ?: return "java"
        val exe = File(home, "bin/" + if (isWindows) "java.exe" else "java")
        return if (exe.isFile) exe.absolutePath else "java"
    }

    private fun pathOw(): String? {
        val names = if (isWindows) listOf("ow.exe", "ow.cmd", "ow.bat", "ow") else listOf("ow")
        val path = System.getenv("PATH") ?: return null
        for (dir in path.split(File.pathSeparator)) {
            if (dir.isBlank()) continue
            for (n in names) {
                val f = File(dir, n)
                if (f.isFile) return f.absolutePath
            }
        }
        return null
    }

    private fun bundledJar(): File? {
        val cacheDir = File(PathManager.getSystemPath(), "ow-runtime")
        val target = File(cacheDir, "OwIDE-$VERSION.jar")
        if (target.isFile && target.length() > 0) return target
        val stream = OwRuntime::class.java.getResourceAsStream("/ow/OwIDE.jar") ?: return null
        cacheDir.mkdirs()
        val tmp = File(cacheDir, "OwIDE-$VERSION.jar.tmp")
        stream.use { input -> tmp.outputStream().use { input.copyTo(it) } }
        Files.move(tmp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING)
        return if (target.isFile) target else null
    }
}
