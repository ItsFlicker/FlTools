package io.github.itsflicker.itstools.module.integrations.placeholderapi

import io.github.itsflicker.itstools.conf
import io.github.itsflicker.itstools.module.resourcepack.ResourcePack
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

/**
 * @author wlys
 * @since 2022/6/29 12:24
 */
object HookPlaceholderAPI : PlaceholderExpansion {

    override val identifier: String
        get() = "itstools"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player != null && player.isOnline) {
            val params = args.split('_')

            return when (params[0].lowercase()) {
                "resourcepack", "rp" -> {
                    if (params.size == 1) {
                        conf.resource_packs.entries.firstOrNull { it.value == ResourcePack.selected[player.uniqueId] }?.key.toString()
                    } else {
                        (conf.resource_packs.entries.firstOrNull { it.value == ResourcePack.selected[player.uniqueId] }?.key == params[1]).toString()
                    }
                }
                else -> "out of case"
            }
        }

        return "ERROR"
    }

}