package io.github.itsflicker.tools.module.command

import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.expansion.createHelper

/**
 * CommandMisc
 * io.github.itsflicker.tools.module.command
 *
 * @author wlys
 * @since 2021/8/3 15:20
 */
@CommandHeader("itstest", description = "ItsTools-Test", permission = "itstools.command.test")
object CommandTest {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

}