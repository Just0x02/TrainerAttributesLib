package com.the_number_two.trainerattributeslib.influences

import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition
import net.minecraft.server.network.ServerPlayerEntity

// Tim you are simply my goat
// https://github.com/timinc-cobble/cobblemon-unchained-1.6-fabric/blob/v1.3.0/src/main/kotlin/us/timinc/mc/cobblemon/unchained/api/AbstractWeightInfluenceBooster.kt
abstract class AbstractWeightInfluenceBooster : SpawningInfluence {
    override fun affectWeight(detail: SpawnDetail, ctx: SpawnablePosition, weight: Float): Float {
        val player = ctx.cause.entity as? ServerPlayerEntity
        if (detail !is PokemonSpawnDetail || player === null) return super.affectWeight(detail, ctx, weight)
        return super.affectWeight(detail, ctx, boostWeight(detail, player, weight))
    }

    abstract fun boostWeight(detail: PokemonSpawnDetail, player: ServerPlayerEntity, weight: Float): Float
}