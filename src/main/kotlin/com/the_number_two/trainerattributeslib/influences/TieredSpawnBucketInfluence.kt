package com.the_number_two.trainerattributeslib.influences

import com.cobblemon.mod.common.api.spawning.SpawnBucket
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence
import com.the_number_two.trainerattributeslib.attributes.AttributeUtils
import com.the_number_two.trainerattributeslib.attributes.TrainerAttributes
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.server.network.ServerPlayerEntity

class TieredSpawnBucketInfluence : AbstractWeightInfluenceBooster() {
    override fun boostWeight(detail: PokemonSpawnDetail, player: ServerPlayerEntity, weight: Float): Float {
        if (detail.bucket.name == "common") return weight
        val bucketAttribute: EntityAttribute = TrainerAttributes.TIERED_SPAWN_BUCKETS[detail.bucket.name]!!
        val chanceModifier: Double = AttributeUtils.getAttributeValue(
            player,
            bucketAttribute,
            0.01
        ) * 100.0

        println("APPLYING MODIFIER ${chanceModifier} TO BUCKET ${detail.bucket.name}!!! (${weight} -> ${weight * chanceModifier})")

        return (weight * chanceModifier).toFloat()
    }
}