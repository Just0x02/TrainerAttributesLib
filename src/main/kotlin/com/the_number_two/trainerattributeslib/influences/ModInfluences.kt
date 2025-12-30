package com.the_number_two.trainerattributeslib.influences

import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawnerFactory

object ModInfluences {
    fun register() {
        PlayerSpawnerFactory.influenceBuilders.add { TypedBucketMultiplyingInfluence() }
//        PlayerSpawnerFactory.influenceBuilders.add { TieredSpawnBucketInfluence() }
    }
}