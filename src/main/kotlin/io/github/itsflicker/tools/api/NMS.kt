package io.github.itsflicker.tools.api

import org.bukkit.Location
import org.bukkit.entity.LivingEntity

/**
 * NMS
 * io.github.itsflicker.tools.api
 *
 * @author wlys
 * @since 2021/8/3 14:56
 */
abstract class NMS {

    abstract fun getTargetEntity(entity: LivingEntity): LivingEntity?

    abstract fun setTargetEntity(entity: LivingEntity, target: LivingEntity?)

    abstract fun makeMeleeHostile(
        entity: LivingEntity,
        damage: Double? = null,
        speed: Double = 1.0,
        priority: Int = 2,
        type: String = "EntityHuman",
        followingTargetEvenIfNotSeen: Boolean = false
    )

    abstract fun getSpeed(entity: LivingEntity): Double

    abstract fun getTargetLocation(entity: LivingEntity): Location?

}