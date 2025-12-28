package com.the_number_two.trainerattributeslib.items

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.the_number_two.trainerattributeslib.TrainerAttributesLib
import com.the_number_two.trainerattributeslib.attributes.TrainerAttributes
import io.wispforest.accessories.api.AccessoryItem
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class MudkipCharm() : AccessoryItem(
    Item.Settings().component(
        DataComponentTypes.ATTRIBUTE_MODIFIERS as ComponentType<AttributeModifiersComponent>,
        AttributeModifiersComponent.builder()
            .add(
                Registries.ATTRIBUTE.getEntry(TrainerAttributes.BONUS_IVS_ATTRIBUTE),
                EntityAttributeModifier(
                    TrainerAttributesLib.getIdentifier("bonus_ivs"),
                    5.0,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                AttributeModifierSlot.ANY
            )
            .add(
                Registries.ATTRIBUTE.getEntry(TrainerAttributes.CATCH_CHANCE_BOOST_ATTRIBUTE),
                EntityAttributeModifier(
                    TrainerAttributesLib.getIdentifier("catch_chance_boost"),
                    0.05,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                AttributeModifierSlot.ANY
            )
            .add(
                Registries.ATTRIBUTE.getEntry(
                    TrainerAttributes.TYPED_CATCH_CHANCE_BOOST_ATTRIBUTE.getAttributeOfType(ElementalTypes.WATER)
                ),
                EntityAttributeModifier(
                    TrainerAttributes.TYPED_CATCH_CHANCE_BOOST_ATTRIBUTE.getIdentifierOfTypedAttribute(ElementalTypes.WATER),
                    0.10,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                AttributeModifierSlot.ANY
            )
            .build()
    )
) {
}