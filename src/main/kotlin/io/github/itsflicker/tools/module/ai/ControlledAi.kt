package io.github.itsflicker.tools.module.ai

import io.github.itsflicker.tools.util.nms
import org.bukkit.entity.LivingEntity
import taboolib.module.ai.SimpleAi
import taboolib.module.ai.navigationMove

class ControlledAi(val entity: LivingEntity) : SimpleAi() {

    override fun shouldExecute(): Boolean {
        if (entity.passengers.isEmpty()) return false
        val rider = entity.passengers[0] as? LivingEntity ?: return false
        return nms.getTargetLocation(rider) != null
    }

    override fun startTask() {
        val rider = entity.passengers[0] as LivingEntity
        entity.navigationMove(nms.getTargetLocation(rider)!!, nms.getSpeed(entity))
    }

    override fun updateTask() = startTask()

}