package com.the_number_two.trainerattributeslib.attributes


import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier

object AttributeUtils {
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
            when(mod.modifier.operation) {
                EntityAttributeModifier.Operation.ADD_VALUE -> attributeValue += mod.modifier.value
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE -> attributeValue += attributeValue * mod.modifier.value
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL -> attributeValue *= mod.modifier.value
            }
        }

        return attributeValue
    }

    fun getAttributeValue(pokemon: Pokemon, entity: LivingEntity, attr: EntityAttribute, typedAttributes: TypedAttributesContainer, baseValue: Double = 0.0): Double {
        var attributeValue: Double = baseValue

        AttributeUtils.getModifiersOfAttribute(entity, attr).forEach { mod ->
            when(mod.modifier.operation) {
                EntityAttributeModifier.Operation.ADD_VALUE -> attributeValue += mod.modifier.value
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE -> attributeValue += attributeValue * mod.modifier.value
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL -> attributeValue *= mod.modifier.value
            }
        }

        return this.getTypedAttributeValue(pokemon, entity, typedAttributes, attributeValue)
    }

    fun getTypedAttributeValue(pokemon: Pokemon, entity: LivingEntity, typedAttributes: TypedAttributesContainer, baseValue: Double = 0.0): Double {
        var attributeValue: Double = baseValue

        pokemon.species.types.toMutableSet().forEach { type ->
            AttributeUtils.getModifiersOfAttribute(entity, typedAttributes.getAttributeOfType(type)).forEach { mod ->
                when(mod.modifier.operation) {
                    EntityAttributeModifier.Operation.ADD_VALUE -> attributeValue += mod.modifier.value
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE -> attributeValue += attributeValue * mod.modifier.value
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL -> attributeValue *= mod.modifier.value
                }
            }
        }

        return attributeValue
    }
}