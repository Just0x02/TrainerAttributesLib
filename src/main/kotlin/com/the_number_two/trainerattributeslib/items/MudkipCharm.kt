package com.the_number_two.trainerattributeslib.items

import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.the_number_two.trainerattributeslib.TrainerAttributesLib
import com.the_number_two.trainerattributeslib.attributes.TrainerAttributes
import com.the_number_two.trainerattributeslib.attributes.util.AttributeBuilderHelper.addAttribute
import com.the_number_two.trainerattributeslib.attributes.util.AttributeBuilderHelper.addStatAttribute
import com.the_number_two.trainerattributeslib.attributes.util.AttributeBuilderHelper.addTypedAttribute
import io.wispforest.accessories.api.AccessoryItem
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class MudkipCharm : AccessoryItem(
    Item.Settings().component(
        DataComponentTypes.ATTRIBUTE_MODIFIERS as ComponentType<AttributeModifiersComponent>,
        AttributeModifiersComponent.builder()
            .addAttribute(
                TrainerAttributes.HIDDEN_ABILITY_CHANCE_BOOST_ATTRIBUTE,
                0.25,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
            .build()
    )
)