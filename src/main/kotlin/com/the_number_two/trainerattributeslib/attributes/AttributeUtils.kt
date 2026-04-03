package com.the_number_two.trainerattributeslib.attributes


import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.pokedex.CaughtCount
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.server.network.ServerPlayerEntity
import kotlin.math.roundToInt
import kotlin.random.Random

object AttributeUtils {
//    fun shouldHaveCriticalCapture(player: ServerPlayerEntity, pokemon: Pokemon, modifiedCatchRate: Float): Boolean {
//        val caughtCount = Cobblemon.playerDataManager.getPokedexData(player).getGlobalCalculatedValue(CaughtCount)
//        val caughtMultiplier = when {
//            caughtCount <= 30 -> 0F
//            // This one is exact
//            caughtCount <= 150 -> 0.5F
//            caughtCount <= 300 -> 1F
//            caughtCount <= 450 -> 1.5F
//            caughtCount <= 600 -> 2F
//            else -> 2.5F
//        }
//        val b = modifiedCatchRate * caughtMultiplier
//        // ToDo replace * 1F with * 2F when the Catching Charm is implemented and is active.
//        val c = ((b * 1F) / 6F).roundToInt()
//        val critRateModified: Double = AttributeUtils.getAttributeValue(
//            pokemon,
//            player,
//            TrainerAttributes.CRIT_CATCH_CHANCE_BOOST_ATTRIBUTE,
//            TrainerAttributes.TYPED_CRIT_CATCH_CHANCE_ATTRIBUTE,
//            ((b * 1F) / 6F).toDouble()
//        )
//        println("${c} -> ${critRateModified}")
//        return Random.nextInt(256) < critRateModified.roundToInt()
//    }

    fun forceHiddenAbility(pokemon: Pokemon): Boolean {
        val hasHidden = pokemon.ability.priority == Priority.LOW
        val priority = if (hasHidden) Priority.LOWEST else Priority.LOW
        val targetForm = pokemon.form
        val targetAbilityMapping = targetForm.abilities.mapping[priority]
        val potentialAbility = targetAbilityMapping?.get(0) ?: return false
        val newAbilityBuilder = potentialAbility.template.builder
        val newAbility = newAbilityBuilder.invoke(potentialAbility.template, false, priority)
        pokemon.updateAbility(newAbility)
        return true
    }


    fun getModifiersOfAttribute(entity: LivingEntity, attr: EntityAttribute) = iterator {
        val accessoryCapability = entity.accessoriesCapability()

        accessoryCapability?.allEquipped?.forEach { equipment ->
            val accessoryItem = equipment.stack
            val attributeComponent = accessoryItem.get(DataComponentTypes.ATTRIBUTE_MODIFIERS)
            attributeComponent?.modifiers?.forEach { mod ->
                if (TrainerAttributes.entryOf(attr).matchesId(mod.modifier.id))
                    yield(mod)
            }
        }

        entity.equippedItems.forEach { equipment ->
            val attributeComponent = equipment.get(DataComponentTypes.ATTRIBUTE_MODIFIERS)
            attributeComponent?.modifiers?.forEach { mod ->
                if (TrainerAttributes.entryOf(attr).matchesId(mod.modifier.id))
                    yield(mod)
            }
        }
    }

    fun getAttributeValue(entity: LivingEntity, attr: EntityAttribute, baseValue: Double = 0.0): Double {
        var attributeValue: Double = baseValue

        AttributeUtils.getModifiersOfAttribute(entity, attr).forEach { mod ->
            attributeValue += when(mod.modifier.operation) {
                EntityAttributeModifier.Operation.ADD_VALUE -> mod.modifier.value
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE -> baseValue * mod.modifier.value
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL -> attributeValue * mod.modifier.value
            }
        }

        return attributeValue
    }

    fun getAttributeValue(pokemon: Pokemon, entity: LivingEntity, attr: EntityAttribute, typedAttributes: TypedAttributesContainer, baseValue: Double = 0.0): Double {
        var attributeValue: Double = baseValue

        AttributeUtils.getModifiersOfAttribute(entity, attr).forEach { mod ->
            attributeValue += when(mod.modifier.operation) {
                EntityAttributeModifier.Operation.ADD_VALUE -> mod.modifier.value
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE -> (baseValue * mod.modifier.value)
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL -> attributeValue * mod.modifier.value
            }
        }

        return this.getTypedAttributeValue(pokemon, entity, typedAttributes, attributeValue)
    }

    fun getTypedAttributeValue(pokemon: Pokemon, entity: LivingEntity, typedAttributes: TypedAttributesContainer, baseValue: Double = 0.0): Double {
        var attributeValue: Double = baseValue

        pokemon.species.types.toMutableSet().forEach { type ->
            AttributeUtils.getModifiersOfAttribute(entity, typedAttributes.getAttributeOfType(type)).forEach { mod ->
                attributeValue += when(mod.modifier.operation) {
                    EntityAttributeModifier.Operation.ADD_VALUE -> mod.modifier.value
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE -> baseValue * mod.modifier.value
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL -> attributeValue * mod.modifier.value
                }
            }
        }

        return attributeValue
    }
}