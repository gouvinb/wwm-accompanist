package io.github.gouvinb.wwmaccompanist.screenshot.command

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotCheckCommand
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotCopyCommand
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotSaveCommand
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotSwappyCommand
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.ActiveScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.AreaScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.OutputScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.ScreenScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.WindowScreenshotTarget

/**
 * [ScreenshotSubCommand] is a command to manage the sound of the Linux system. It contains the following subcommands:
 *
 * - [ScreenshotCheckCommand] to verify if required tools are installed and exit
 * - [ScreenshotCopyCommand] to copy the screenshot data into the clipboard
 * - [ScreenshotSaveCommand] to save the screenshot to a regular file or '-' to pipe to STDOUT.
 * - [ScreenshotSwappyCommand] to redirect the screenshot to swappy.
 */
abstract class ScreenshotSubCommand(
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
) : ScreenshotSubBaseCommand(
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
    internal val target by option("-t", "--target", help = "Selected device")
        .choice(
            "active" to ActiveScreenshotTarget(),
            "screen" to ScreenScreenshotTarget(),
            "output" to OutputScreenshotTarget(),
            "area" to AreaScreenshotTarget(),
            "window" to WindowScreenshotTarget(),
        ).required()
}
