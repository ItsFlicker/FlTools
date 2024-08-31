package io.github.itsflicker.tools.module.integration.placeholderapi

import io.github.itsflicker.tools.module.feature.IPInfo
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

/**
 * @author wlys
 * @since 2022/6/29 12:24
 */
@Suppress("unused")
object HookPlaceholderAPI : PlaceholderExpansion {

    override val identifier: String
        get() = "itstools"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player != null && player.isOnline) {
            val params = args.split('_')

            return when (params[0].lowercase()) {
                "ip" -> {
                    val default = params.getOrElse(2) { "" }
                    val info = IPInfo.caches[player.uniqueId] ?: return default
                    when (params.getOrElse(1) { "province" }.lowercase()) {
                        "country" -> info.country
                        "shortname" -> info.short_name
                        "province" -> info.province
                        "city" -> info.city
                        "area" -> info.area
                        "isp" -> info.isp
                        else -> "out of case"
                    }
                }
                else -> "out of case"
            }
        }

        return "ERROR"
    }

}