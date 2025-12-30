package com.the_number_two.trainerattributeslib.attributes

import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.StatTypeAdapter
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.the_number_two.trainerattributeslib.TrainerAttributesLib
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class StatAttributesContainer(val attributeId: String, val fallbackValue: Double = 0.0, val minValue: Double = 0.0, val maxValue: Double = 1.0) {
    private var statAttributes: MutableMap<Stat, EntityAttribute>

    companion object {
        fun of(baseAttribute: EntityAttribute, minValue: Double = 0.0, maxValue: Double = 1.0): StatAttributesContainer {
            return StatAttributesContainer(
                baseAttribute.translationKey.split(".").last(), // quick and dirty gonna kms later
                baseAttribute.defaultValue,
                minValue,
                maxValue
            )
        }
    }

    init {
        this.statAttributes = this.generateStatAttributes()
    }

    private fun generateStatAttributes(): MutableMap<Stat, EntityAttribute> {
        val baseMap: MutableMap<Stat, EntityAttribute> = mutableMapOf()

        Stats.PERMANENT.forEach { it ->
            baseMap[it] = Registry.register(
                Registries.ATTRIBUTE,
                TrainerAttributesLib.getIdentifier(this.getStatIdentifierString(it)),
                ClampedEntityAttribute(
                    "attribute.name.trainerattributeslib.${this.getStatIdentifierString(it)}",
                    this.fallbackValue,
                    this.minValue,
                    this.maxValue
                )
            )
        }

        return baseMap
    }

    private fun getStatIdentifierString(stat: Stat): String {
        return "${this.attributeId}_${stat.showdownId}"
    }

    fun all(): List<EntityAttribute> {
        return this.statAttributes.values.toList()
    }

    fun register(): StatAttributesContainer {
        this.statAttributes.forEach { it ->
            Registry.register(
                Registries.ATTRIBUTE,
                this.getIdentifierOfStatAttribute(it.key),
                it.value
            )
        }
        return this
    }

    fun getIdentifierOfStatAttribute(type: Stat): Identifier {
        return TrainerAttributesLib.getIdentifier(this.getStatIdentifierString(type))
    }

    fun getAttributeOfStat(type: Stat): EntityAttribute {
        return this.statAttributes[type]!!
    }
}