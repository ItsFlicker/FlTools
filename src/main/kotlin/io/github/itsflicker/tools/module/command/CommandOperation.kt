package io.github.itsflicker.tools.module.command

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import io.github.itsflicker.tools.module.feature.DebugItem
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.command.*
import taboolib.common5.Demand
import taboolib.expansion.createHelper
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * CommandMisc
 * io.github.itsflicker.tools.module.command
 *
 * @author wlys
 * @since 2021/8/3 15:20
 */
@CommandHeader("itsoperation", ["io"], description = "ItsTools-Operations", permission = "itstools.command.operation")
object CommandOperation {

    val cacheOperations: Cache<UUID, Consumer<LivingEntity>> = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.SECONDS)
        .build()

    @CommandBody(optional = true)
    val addpotion = subCommand {
        dynamic("potion") {
            suggest {
                PotionEffectType.values().map { it.key.key }
            }
            execute<Player> { sender, _, arg ->
                val key = NamespacedKey.minecraft(arg)
                cacheOperations.put(sender.uniqueId) {
                    it.addPotionEffect(PotionEffect(PotionEffectType.getByKey(key)!!, 30 * 20, 0))
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
            dynamic("args", optional = true) {
                suggest {
                    listOf("-d", "-a", "--ambient", "--p", "--i")
                }
                execute<Player> { sender, ctx, arg ->
                    val key = NamespacedKey.minecraft(ctx["potion"])
                    val de = Demand("potion $arg")
                    val duration = de.get(listOf("duration", "d"), "30")!!.toInt() * 20
                    val amplifier = de.get(listOf("amplifier", "a"), "1")!!.toInt().minus(1)
                    val ambient = de.tags.contains("ambient")
                    val particles = de.tags.contains("p")
                    val icon = de.tags.contains("i")
                    cacheOperations.put(sender.uniqueId) {
                        it.addPotionEffect(PotionEffect(PotionEffectType.getByKey(key)!!, duration, amplifier, ambient, particles, icon))
                    }
                    sender.sendMessage("§cClick an entity in the next 10 seconds.")
                }
            }
        }
    }

    @CommandBody(optional = true)
    val mount = subCommand {
        execute<Player> { sender, _, _ ->
            cacheOperations.put(sender.uniqueId) { target ->
                val entity = DebugItem.cache[sender.name]?.let { Bukkit.getEntity(it) as? LivingEntity } ?: return@put
                target.addPassenger(entity)
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(optional = true)
    val togglegravity = subCommand {
        execute<Player> { sender, _, _ ->
            cacheOperations.put(sender.uniqueId) {
                it.setGravity(!it.hasGravity())
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(optional = true)
    val getentityuuid = subCommand {
        execute<Player> { sender, _, _ ->
            val location = sender.eyeLocation
            val direction = location.direction
            location.add(direction)
            sender.sendMessage(sender.world.rayTraceEntities(location, direction, 25.0)?.hitEntity?.uniqueId.toString())
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

}