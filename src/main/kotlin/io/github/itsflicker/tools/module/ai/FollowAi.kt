package io.github.itsflicker.tools.module.ai

import io.github.itsflicker.tools.util.nms
import org.bukkit.entity.LivingEntity
import taboolib.module.ai.SimpleAi
import taboolib.module.ai.navigationMove

class FollowAi(val entity: LivingEntity, val target: LivingEntity) : SimpleAi() {

    override fun shouldExecute(): Boolean {
        return !target.isDead && target.world.name == entity.world.name && entity.location.distanceSquared(target.location) > 12
    }

    override fun startTask() {
        entity.navigationMove(target, nms.getSpeed(entity).takeIf { it >= 0.1 } ?: 1.0)
    }

    override fun updateTask() = startTask()

}