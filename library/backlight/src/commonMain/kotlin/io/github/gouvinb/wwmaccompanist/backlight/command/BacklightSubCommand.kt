package io.github.gouvinb.wwmaccompanist.backlight.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import io.github.gouvinb.wwmaccompanist.backlight.engine.BacklightEngine
import io.github.gouvinb.wwmaccompanist.logger.presentation.log

/**
 * This class provides the ability to define subcommands for audio management.
 *
 * @param help A string to provide help information about the command.
 * @param epilog A string to provide additional information at the end of the help.
 * @param name A string to set the name of the command. If null, the name of the class will be used.
 * @param invokeWithoutSubcommand A boolean to indicate if the command should be invoked without a subcommand.
 * @param printHelpOnEmptyArgs A boolean to indicate if help should be printed if no arguments are provided.
 * @param helpTags A map of strings to add tags to the help.
 * @param autoCompleteEnvvar A string to set the name of the environment variable that will be used for autocomplete.
 * @param allowMultipleSubcommands A boolean to indicate if multiple subcommands can be used.
 * @param treatUnknownOptionsAsArgs A boolean to indicate if unknown options should be treated as arguments.
 * @param hidden A boolean to indicate if the command should be hidden.
 *
 * @property engine defines the module used by wwma for update sound configuration.
 */
abstract class BacklightSubCommand(
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
    internal val engine by requireObject<BacklightEngine>()

    override fun run() {
        if (!engine.isEngineAvailable()) {
            log.println {
                """
                |The command ${bold("`${engine.command.command}`")} was not found.
                |You can install this command if it is not already done and check your ${bold(italic("`PATH`"))} as well as your selected engine to solve this problem.
                """.trimMargin()
            }
            throw Abort()
        }
    }
}
