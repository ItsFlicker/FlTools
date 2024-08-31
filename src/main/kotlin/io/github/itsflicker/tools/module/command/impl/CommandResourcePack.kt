package io.github.itsflicker.tools.module.command.impl

import io.github.itsflicker.tools.module.resourcepack.COSUploader
import io.github.itsflicker.tools.module.resourcepack.OSSUploader
import io.github.itsflicker.tools.util.isItemsAdderHooked
import org.bukkit.command.CommandSender
import taboolib.common.io.newFile
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submitAsync

/**
 * CommandResourcePack
 * io.github.itsflicker.tools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/6 22:55
 */
object CommandResourcePack {

    @CommandBody(aliases = ["up"], permission = "itstools.command.resourcepack.upload")
    val upload = subCommand {
        dynamic("type") {
            suggest {
                listOf("cos", "oss")
            }
            dynamic("file") {
                suggest {
                    var array = newFile(getDataFolder(), "packs", folder = true).list()!!
                    if (isItemsAdderHooked) {
                        array += "itemsadder"
                    }
                    array.toList()
                }
                execute<CommandSender> { sender, ctx, argument ->
                    val file = when (argument) {
                        "itemsadder" -> {
                            getDataFolder()
                                .resolveSibling("ItemsAdder")
                                .resolve("output")
                                .resolve("generated.zip")
                        }
                        else -> {
                            getDataFolder().resolve("packs").resolve(argument)
                        }
                    }
                    submitAsync {
                        val succeed = when (ctx["type"]) {
                            "cos" -> COSUploader.upload(file)
                            "oss" -> OSSUploader.upload(file)
                            else -> error("out of case")
                        }
                        if (succeed) {
                            sender.sendMessage("§a上传成功!")
                        } else {
                            sender.sendMessage("§c上传失败!")
                        }
                    }
                }
            }
        }

    }

}