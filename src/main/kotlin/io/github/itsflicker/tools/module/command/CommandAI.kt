package io.github.itsflicker.tools.module.command

import io.github.itsflicker.tools.module.ai.ControlledAi
import io.github.itsflicker.tools.module.ai.EncircleAi
import io.github.itsflicker.tools.module.ai.FollowAi
import io.github.itsflicker.tools.module.feature.DebugItem
import io.github.itsflicker.tools.util.nms
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.common5.Demand
import taboolib.expansion.createHelper
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.ai.*

/**
 * CommandMisc
 * io.github.itsflicker.tools.module.command
 *
 * @author wlys
 * @since 2021/8/3 15:20
 */
@CommandHeader("itsai", description = "ItsTools-AI", permission = "itstools.command.ai")
object CommandAI {

    @CommandBody(optional = true)
    val makemeleehostile = subCommand {
        execute<Player> { sender, _, _ ->
            CommandOperation.cacheOperations.put(sender.uniqueId) {
                nms.makeMeleeHostile(it)
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
        dynamic("args", optional = true) {
            suggestUncheck {
                listOf("-d", "-s", "-t", "-p", "--f")
            }
            execute<Player> { sender, _, arg ->
                val de = Demand("0 $arg")
                val damage = de.get(listOf("damage", "d"))?.toDouble()
                val speed = de.get(listOf("speed", "s"), "1.0")!!.toDouble()
                val priority = de.get(listOf("priority", "p"), "2")!!.toInt()
                val type = de.get(listOf("type", "t"), "EntityHuman")!!
                val followingTargetEvenIfNotSeen = de.tags.contains("followingTargetEvenIfNotSeen") || de.tags.contains("f")
                CommandOperation.cacheOperations.put(sender.uniqueId) {
                    nms.makeMeleeHostile(it, damage, speed, priority, type, followingTargetEvenIfNotSeen)
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
        }
    }

    @CommandBody(optional = true)
    val removegoal = subCommand {
        dynamic("goal") {
            execute<Player> { sender, _, arg ->
                CommandOperation.cacheOperations.put(sender.uniqueId) {
                    it.removeGoalAi(arg)
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
        }
    }

    @CommandBody(optional = true)
    val removetarget = subCommand {
        dynamic("target") {
            execute<Player> { sender, _, arg ->
                CommandOperation.cacheOperations.put(sender.uniqueId) {
                    it.removeTargetAi(arg)
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
        }
    }

    @CommandBody(optional = true)
    val cleargoal = subCommand {
        execute<Player> { sender, _, _ ->
            CommandOperation.cacheOperations.put(sender.uniqueId) {
                it.clearGoalAi()
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(optional = true)
    val cleartarget = subCommand {
        execute<Player> { sender, _, _ ->
            CommandOperation.cacheOperations.put(sender.uniqueId) {
                it.clearTargetAi()
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(optional = true)
    val getgoal = subCommand {
        execute<Player> { sender, _, _ ->
            CommandOperation.cacheOperations.put(sender.uniqueId) {
                sender.sendMessage(it.getGoalAi()
                    .sortedBy { ai -> ai!!.invokeMethod<Int>("getPriority") }
                    .joinToString("\n") { ai -> ai!!.invokeMethod<Any>("getGoal")!!.javaClass.simpleName }
                )
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(optional = true)
    val gettarget = subCommand {
        execute<Player> { sender, _, _ ->
            CommandOperation.cacheOperations.put(sender.uniqueId) {
                sender.sendMessage(it.getTargetAi()
                    .sortedBy { target -> target!!.invokeMethod<Int>("getPriority") }
                    .joinToString("\n") { target -> target!!.invokeMethod<Any>("getGoal")!!.javaClass.simpleName }
                )
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(optional = true)
    val settarget = subCommand {
        execute<Player> { sender, _, _ ->
            CommandOperation.cacheOperations.put(sender.uniqueId) { target ->
                val entity = DebugItem.cache[sender.name]?.let { Bukkit.getEntity(it) as? LivingEntity } ?: return@put
                nms.setTargetEntity(entity, target)
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(optional = true)
    val follow = subCommand {
        execute<Player> { sender, _, _ ->
            CommandOperation.cacheOperations.put(sender.uniqueId) { target ->
                val entity = DebugItem.cache[sender.name]?.let { Bukkit.getEntity(it) as? LivingEntity } ?: return@put
                entity.removeGoalAi("CreatorImpl")
                entity.addGoalAi(FollowAi(entity, target), 1)
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(optional = true)
    val encircle = subCommand {
        dynamic("distance") {
            dynamic("speed") {
                execute<Player> { sender, ctx, _ ->
                    CommandOperation.cacheOperations.put(sender.uniqueId) {
                        it.addGoalAi(EncircleAi(it, ctx.double("distance"), ctx.double("speed")), 1)
                    }
                    sender.sendMessage("§cClick an entity in the next 10 seconds.")
                }
            }
        }
    }

    @CommandBody(optional = true)
    val controlled = subCommand {
        execute<Player> { sender, _, _ ->
            CommandOperation.cacheOperations.put(sender.uniqueId) {
                it.addGoalAi(ControlledAi(it), 0)
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

}