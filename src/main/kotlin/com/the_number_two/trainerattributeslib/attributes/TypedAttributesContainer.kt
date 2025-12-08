package com.the_number_two.trainerattributeslib.attributes


import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.the_number_two.trainerattributeslib.TrainerAttributesLib
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class TypedAttributesContainer(val attributeId: String, val fallbackValue: Double = 0.0, val minValue: Double = 0.0, val maxValue: Double = 1.0) {
    private var typedAttributes: MutableMap<ElementalType, EntityAttribute>

    companion object {
        fun of(baseAttribute: EntityAttribute, minValue: Double = 0.0, maxValue: Double = 1.0): TypedAttributesContainer {
            return TypedAttributesContainer(
                baseAttribute.translationKey.split(".").last(), // quick and dirty gonna kms later
                baseAttribute.defaultValue,
                minValue,
                maxValue
            )
        }
    }

    init {
        this.typedAttributes = this.generateTypedAttributes()
    }

    private fun generateTypedAttributes(): MutableMap<ElementalType, EntityAttribute> {
        val baseMap: MutableMap<ElementalType, EntityAttribute> = mutableMapOf()

        ElementalTypes.all().forEach { it ->
            baseMap[it] = Registry.register(
                Registries.ATTRIBUTE,
                TrainerAttributesLib.getIdentifier(this.getTypedIdentifierString(it)),
                ClampedEntityAttribute(
                    "attribute.name.trainerattributeslib.${this.getTypedIdentifierString(it)}",
                    this.fallbackValue,
                    this.minValue,
                    this.maxValue
                )
            )
        }

        return baseMap
    }

    private fun getTypedIdentifierString(type: ElementalType): String {
        return "${this.attributeId}_${type.name.lowercase()}"
    }

    fun all(): List<EntityAttribute> {
        return this.typedAttributes.values.toList()
    }

    fun register(): TypedAttributesContainer {
        this.typedAttributes.forEach { it ->
            Registry.register(
                Registries.ATTRIBUTE,
                this.getIdentifierOfTypedAttribute(it.key),
                it.value
            )
        }
        return this
    }

    fun getIdentifierOfTypedAttribute(type: ElementalType): Identifier {
        return TrainerAttributesLib.getIdentifier(this.getTypedIdentifierString(type))
    }

    fun getAttributeOfType(type: ElementalType): EntityAttribute {
        return this.typedAttributes[type]!!
    }
}