package com.the_number_two.trainerattributeslib.attributes


import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.drop.DropEntry
import com.cobblemon.mod.common.api.drop.ItemDropEntry
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.spawning.SpawnBucket
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.util.nextBetween
import com.cobblemon.mod.common.util.weightedSelection
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import org.w3c.dom.Attr
import kotlin.math.roundToInt
import kotlin.random.Random

object AttributeEffects {
    fun applyEffects() {
//        CobblemonEvents.SPAWN_BUCKET_CHOSEN.subscribe(Priority.LOWEST) { it ->
//            val newBucketWeights: MutableMap<SpawnBucket, Float> = mutableMapOf()
//            it.bucketWeights.forEach { entry ->
//                val bucketAttribute: EntityAttribute = TrainerAttributes.TIERED_SPAWN_BUCKETS[entry.key.name] ?: return@forEach
//                val entity: LivingEntity = it.spawnCause.entity as? LivingEntity ?: return@forEach
//
//                val bucketWeightModifier: Float = AttributeUtils.getAttributeValue(
//                    entity,
//                    bucketAttribute,
//                    1.0
//                ).toFloat()
//
//                newBucketWeights[entry.key] = entry.value * bucketWeightModifier
//            }
//
//            it.bucket = newBucketWeights.entries.weightedSelection { it.value }?.key ?: Cobblemon.bestSpawner.config.buckets.first()
//        }

        CobblemonEvents.SPAWN_BUCKET_CHOSEN.subscribe(Priority.LOWEST) { event ->
            val entity = event.spawnCause.entity as? LivingEntity ?: return@subscribe

            val weightedBuckets = buildMap<SpawnBucket, Float> {
                event.bucketWeights.forEach { (bucket, baseWeight) ->
                    val attribute = TrainerAttributes.TIERED_SPAWN_BUCKETS[bucket.name] ?: return@forEach

                    val modifier = AttributeUtils
                        .getAttributeValue(entity, attribute, 0.0)
                        .toFloat()

                    val scaledWeight = baseWeight + (baseWeight * modifier)
                    put(bucket, scaledWeight.coerceAtLeast(0f))
                }
            }

            event.bucket = weightedBuckets.entries.weightedSelection { it.value }?.key
                    ?: Cobblemon.bestSpawner.config.buckets.first()
        }

        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.LOWEST) { it ->
            if (it.entity !is PokemonEntity) return@subscribe

            val player = it.player ?: return@subscribe
            val pokemonEntity = it.entity as PokemonEntity

            val extraRolls: Int = AttributeUtils.getAttributeValue(
                pokemonEntity.pokemon,
                player,
                TrainerAttributes.EXTRA_POKEMON_LOOT_ROLLS,
                TrainerAttributes.TYPED_EXTRA_POKEMON_LOOT_ROLLS,
                0.0
            ).roundToInt()

            if (extraRolls <= 0.0) return@subscribe

            val extraDrops: List<DropEntry> =
                it.table.getDrops(extraRolls..extraRolls, pokemonEntity.pokemon)

            // Build count map of already-selected entries
            val counts = mutableMapOf<String, Int>()

            fun DropEntry.key(): String = when (this) {
                is ItemDropEntry -> "${this::class.qualifiedName}:${this.item}"
                else -> "${this::class.qualifiedName}:${this.hashCode()}"
            }

            // Count existing drops
            it.drops.forEach { entry ->
                counts.merge(entry.key(), 1, Int::plus)
            }

            // Attempt to merge extra drops
            extraDrops.forEach { entry ->
                val key = entry.key()
                val currentCount = counts.getOrDefault(key, 0)

                if (currentCount < entry.maxSelectableTimes) {
                    it.drops.add(entry)
                    counts[key] = currentCount + 1
                }
            }
        }

        CobblemonEvents.POKEMON_CAPTURED.subscribe { event ->
            val player = event.pokemon.getOwnerPlayer() ?: return@subscribe

            val bonusIVSModified: Double = AttributeUtils.getAttributeValue(
                event.pokemon,
                player,
                TrainerAttributes.BONUS_IVS_ATTRIBUTE,
                TrainerAttributes.TYPED_BONUS_IVS_ATTRIBUTE,
                0.0
            )

            event.pokemon.ivs.forEach { entry ->
                val specificStatIVBonus: Double = AttributeUtils.getAttributeValue(
                    player,
                    TrainerAttributes.STAT_BONUS_IVS_ATTRIBUTE.getAttributeOfStat(entry.key),
                    0.0
                )

                event.pokemon.ivs[entry.key] = (entry.value + bonusIVSModified.toInt() + specificStatIVBonus.toInt()).coerceIn(0, 31)
            }

            val haChance: Double = AttributeUtils.getAttributeValue(
                event.pokemon,
                player,
                TrainerAttributes.HIDDEN_ABILITY_CHANCE_BOOST_ATTRIBUTE,
                TrainerAttributes.TYPED_HIDDEN_ABILITY_CHANCE_ATTRIBUTE,
                1.0
            ) - 1.0

            val haRoll: Boolean = Random.nextDouble() < haChance
            println("HA CHANCE -> ${haChance}, (${haRoll})")

            if (haRoll) AttributeUtils.forceHiddenAbility(event.pokemon)
        }

        CobblemonEvents.POKEMON_CATCH_RATE.subscribe(Priority.LOW, { event ->
            if (event.thrower !is PlayerEntity) return@subscribe
            val catchRateModified: Double = AttributeUtils.getAttributeValue(
                event.pokemonEntity.pokemon,
                event.thrower,
                TrainerAttributes.CATCH_CHANCE_BOOST_ATTRIBUTE,
                TrainerAttributes.TYPED_CATCH_CHANCE_BOOST_ATTRIBUTE,
                event.catchRate.toDouble()
            )
//            println("BEFORE CATCH RATE -> ${event.pokemonEntity.pokemon.species.catchRate}/${event.catchRate} AFTER -> ${event.pokemonEntity.pokemon.species.catchRate}/${catchRateModified}")
            event.catchRate = catchRateModified.toFloat()
        })

//        CobblemonEvents.POKE_BALL_CAPTURE_CALCULATED.subscribe { event ->
//            if (event.thrower !is PlayerEntity) return@subscribe
//
//        }

        CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe { event ->
            if (!event.source.isBattle() && event.pokemon.getOwnerPlayer() != null) return@subscribe

            val player = event.pokemon.getOwnerPlayer()!!
            val experienceModified = AttributeUtils.getAttributeValue(
                event.pokemon,
                player,
                TrainerAttributes.POKEMON_EXPERIENCE_GAINED_ATTRIBUTE,
                TrainerAttributes.TYPED_POKEMON_EXPERIENCE_GAINED_ATTRIBUTE,
                event.experience.toDouble()
            )
//            println("OLD EXPERIENCE -> ${event.experience} NEW -> ${experienceModified.roundToInt()}")
            event.experience = experienceModified.roundToInt()
        }


        CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe { event ->
            event.addModificationFunction { shinyChance, player, pokemon ->
                if (player == null) return@addModificationFunction shinyChance

                // This returns shinyChance with modifiers ADDED
                val modified = (AttributeUtils.getAttributeValue(
                    pokemon,
                    player,
                    TrainerAttributes.SHINY_CHANCE_BOOST_ATTRIBUTE,
                    TrainerAttributes.TYPED_SHINY_CHANCE_BOOST_ATTRIBUTE,
                    1.0
                ) - 1).coerceAtLeast(1.0)

                val boost = (1.0 / modified)
                val newChance = (shinyChance * boost).roundToInt()
//                println("OLD SHINY CHANCE -> ${shinyChance} NEW SHINY CHANCE -> ${newChance} (boost=${boost}, modified=${modified})")

                return@addModificationFunction newChance.toFloat()
            }
        }
    }
}