package dev.oreo.owplugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.io.File

/**
 * Runs the current .ow file with the installed `ow` command and shows its output.
 * Requires Ow on the PATH — see
 * https://github.com/OW-Programing-Language/ow-lang (INSTALL.md).
 *
 * Note: long-running programs (e.g. a `serve:` web app) block until the process
 * exits — run those from a terminal instead.
 */
class RunOwFileAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file != null && file.extension == "ow"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val path = file.path
        val name = file.name
        val workDir = file.parent?.path

        val cmd = GeneralCommandLine(resolveOw(), "run", path).apply {
            if (workDir != null) withWorkDirectory(workDir)
        }

        object : Task.Backgroundable(project, "Running $name with Ow", true) {
            override fun run(indicator: ProgressIndicator) {
                try {
                    val output = ExecUtil.execAndGetOutput(cmd)
                    val text = buildString {
                        append(output.stdout)
                        if (output.stderr.isNotBlank()) append("\n").append(output.stderr)
                    }.trim()
                    showInfo(project, "Ow — $name (exit ${output.exitCode})",
                        text.ifEmpty { "(no output)" })
                } catch (ex: Exception) {
                    showError(project,
                        "Could not run the 'ow' command. Make sure Ow is installed and on your PATH.\n\n${ex.message}")
                }
            }
        }.queue()
    }

    /**
     * Finds the Ow launcher on PATH, supporting both the native install
     * (`ow.exe`) and the portable JDK distribution (`ow.bat`). Falls back to a
     * bare name and lets the OS resolve it.
     */
    private fun resolveOw(): String {
        val win = System.getProperty("os.name").lowercase().contains("win")
        val candidates = if (win) listOf("ow.exe", "ow.cmd", "ow.bat", "ow") else listOf("ow")
        val path = System.getenv("PATH") ?: ""
        for (dir in path.split(File.pathSeparator)) {
            if (dir.isBlank()) continue
            for (c in candidates) {
                val f = File(dir, c)
                if (f.isFile) return f.absolutePath
            }
        }
        return if (win) "ow.exe" else "ow"
    }

    private fun showInfo(project: Project, title: String, message: String) {
        ApplicationManager.getApplication().invokeLater {
            Messages.showMessageDialog(project, message, title, Messages.getInformationIcon())
        }
    }

    private fun showError(project: Project, message: String) {
        ApplicationManager.getApplication().invokeLater {
            Messages.showErrorDialog(project, message, "Run Ow File")
        }
    }
}
