package com.the_number_two.trainerattributeslib.attributes.util

import com.cobblemon.mod.common.api.types.ElementalType
import com.the_number_two.trainerattributeslib.attributes.StatAttributesContainer
import com.the_number_two.trainerattributeslib.attributes.TypedAttributesContainer
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier

object AttributeBuilderHelper {

    /*
     * Core add method for raw EntityAttribute
     */
    fun AttributeModifiersComponent.Builder.addAttribute(
        attribute: EntityAttribute,
        value: Double,
        operation: EntityAttributeModifier.Operation,
        slot: AttributeModifierSlot = AttributeModifierSlot.ANY,
        id: Identifier? = null
    ): AttributeModifiersComponent.Builder {

        val entry: RegistryEntry<EntityAttribute> =
            Registries.ATTRIBUTE.getEntry(attribute)

        val identifier = id ?: Identifier.of(entry.idAsString)

        return this.add(
            entry,
            EntityAttributeModifier(identifier, value, operation),
            slot
        )
    }

    /*
     * Typed attribute helper
     */
    fun AttributeModifiersComponent.Builder.addTypedAttribute(
        container: TypedAttributesContainer,
        type: ElementalType,
        value: Double,
        operation: EntityAttributeModifier.Operation,
        slot: AttributeModifierSlot = AttributeModifierSlot.ANY
    ): AttributeModifiersComponent.Builder {

        val attr = container.getAttributeOfType(type)
        val id = container.getIdentifierOfTypedAttribute(type)

        return addAttribute(attr, value, operation, slot, id)
    }

    /*
     * Stat attribute helper
     */
    fun AttributeModifiersComponent.Builder.addStatAttribute(
        container: StatAttributesContainer,
        stat: com.cobblemon.mod.common.api.pokemon.stats.Stat,
        value: Double,
        operation: EntityAttributeModifier.Operation,
        slot: AttributeModifierSlot = AttributeModifierSlot.ANY
    ): AttributeModifiersComponent.Builder {

        val attr = container.getAttributeOfStat(stat)
        val id = container.getIdentifierOfStatAttribute(stat)

        return addAttribute(attr, value, operation, slot, id)
    }
}