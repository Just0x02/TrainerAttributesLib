package com.the_number_two.trainerattributeslib.influences

import com.cobblemon.mod.common.api.spawning.SpawnBucket
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail
import com.cobblemon.mod.common.api.spawning.influence.BucketMultiplyingInfluence
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition
import com.the_number_two.trainerattributeslib.attributes.AttributeUtils
import com.the_number_two.trainerattributeslib.attributes.TrainerAttributes
import net.minecraft.server.network.ServerPlayerEntity

// say that 10 times fast jeez
class TypedBucketMultiplyingInfluence : AbstractWeightInfluenceBooster() {
    override fun boostWeight(detail: PokemonSpawnDetail, player: ServerPlayerEntity, weight: Float): Float {
        val weightBoostMultiplier: Double = AttributeUtils.getAttributeValue(
            detail.pokemon.create(),
            player,
            TrainerAttributes.SPAWN_CHANCE_ATTRIBUTE,
            TrainerAttributes.TYPED_SPAWN_CHANCE_ATTRIBUTE,
            0.01
        ) * 100.0

//        println("WEIGHT BOOST MULT: ${weightBoostMultiplier} (${weight * weightBoostMultiplier}) FOR ${detail.pokemon.species}")

        return (weight * weightBoostMultiplier).toFloat()
    }
}