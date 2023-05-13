package io.github.gouvinb.wwmaccompanist.screenshot.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotCheckCommand
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotCopyCommand
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotSaveCommand

/**
 * [ScreenshotSubBaseCommand] is a command to manage the sound of the Linux system. It contains the following subcommands:
 *
 * - [ScreenshotCheckCommand] to verify if required tools are installed and exit
 * - [ScreenshotCopyCommand] to copy the screenshot data into the clipboard
 * - [ScreenshotSaveCommand] to save the screenshot to a regular file or '-' to pipe to STDOUT."
 */
abstract class ScreenshotSubBaseCommand(
    help: String = "",
    epilog: String = "",
    name: String? = null,
    invokeWithoutSubcommand: Boolean = false,
    printHelpOnEmptyArgs: Boolean = false,
    helpTags: Map<String, String> = emptyMap(),
    autoCompleteEnvvar: String? = "",
    allowMultipleSubcommands: Boolean = false,
    treatUnknownOptionsAsArgs: Boolean = false,
    hidden: Boolean = false,
) : CliktCommand(
    help,
    epilog,
    name,
    invokeWithoutSubcommand,
    printHelpOnEmptyArgs,
    helpTags,
    autoCompleteEnvvar,
    allowMultipleSubcommands,
    treatUnknownOptionsAsArgs,
    hidden,
) {
    internal val notify by requireObject<Boolean>()
}
