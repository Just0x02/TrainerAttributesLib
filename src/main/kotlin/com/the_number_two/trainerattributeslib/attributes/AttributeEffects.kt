package com.the_number_two.trainerattributeslib.attributes


import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.entity.player.PlayerEntity
import kotlin.math.roundToInt

object AttributeEffects {
    fun applyEffects() {
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
                val modified = AttributeUtils.getAttributeValue(
                    pokemon,
                    player,
                    TrainerAttributes.SHINY_CHANCE_BOOST_ATTRIBUTE,
                    TrainerAttributes.TYPED_SHINY_CHANCE_BOOST_ATTRIBUTE,
                    shinyChance.toDouble()
                )

                // Convert "added" modifiers into a reduction toward better odds
                // Example: shinyChance = 4096, modified = 4300 → boost = 204
                // New chance = 4096 - 204 = 3892 (better chance)
                val boost = modified - shinyChance
                val newChance = (shinyChance - boost).coerceAtLeast(1.0)

//                println("OLD SHINY CHANCE -> ${shinyChance} NEW SHINY CHANCE -> ${newChance}")

                return@addModificationFunction newChance.toFloat()
            }
        }
    }
}