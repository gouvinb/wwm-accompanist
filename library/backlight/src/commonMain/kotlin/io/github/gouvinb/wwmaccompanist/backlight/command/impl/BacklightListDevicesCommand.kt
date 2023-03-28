package io.github.gouvinb.wwmaccompanist.backlight.command.impl

import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightSubCommand

class BacklightListDevicesCommand : BacklightSubCommand(
    name = "list",
    help = "List devices",
    helpTags = mapOf("version" to "0.1.0"),
) {
    override fun run() {
        echo(
            engine.listDevices()
                .ifEmpty { null }
                ?.toList()
                ?.joinToString("\n") { "${it.first.padEnd(24)}\t${it.second}" }
                ?: "No sink found."
        )
    }
}
