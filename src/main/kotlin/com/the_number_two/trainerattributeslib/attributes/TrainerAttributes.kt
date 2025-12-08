package com.the_number_two.trainerattributeslib.attributes


import com.the_number_two.trainerattributeslib.TrainerAttributesLib
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import java.util.function.BiConsumer

object TrainerAttributes {
    val HIDDEN_ABILITY_CHANCE_BOOST_ATTRIBUTE: EntityAttribute = Registry.register(
        Registries.ATTRIBUTE,
        TrainerAttributesLib.getIdentifier("hidden_ability_chance_boost"),
        ClampedEntityAttribute(
            "attribute.name.trainerattributeslib.hidden_ability_chance_boost",
            0.0,
            0.0,
            1.0
        )
    )

    val SHINY_CHANCE_BOOST_ATTRIBUTE: EntityAttribute = Registry.register(
        Registries.ATTRIBUTE,
        TrainerAttributesLib.getIdentifier("shiny_chance_boost"),
        ClampedEntityAttribute(
            "attribute.name.trainerattributeslib.shiny_chance_boost",
            0.0,
            0.0,
            100.0
        )
    )

    val CRIT_CATCH_CHANCE_BOOST_ATTRIBUTE: EntityAttribute = Registry.register(
        Registries.ATTRIBUTE,
        TrainerAttributesLib.getIdentifier("crit_catch_chance_boost"),
        ClampedEntityAttribute(
            "attribute.name.trainerattributeslib.crit_catch_chance_boost",
            0.0,
            0.0,
            1.0
        )
    )

    val CATCH_CHANCE_BOOST_ATTRIBUTE: EntityAttribute = Registry.register(
        Registries.ATTRIBUTE,
        TrainerAttributesLib.getIdentifier("catch_chance_boost"),
        ClampedEntityAttribute(
            "attribute.name.trainerattributeslib.catch_chance_boost",
            0.0,
            0.0,
            1.0
        )
    )

    val POKEMON_EXPERIENCE_GAINED_ATTRIBUTE: EntityAttribute = Registry.register(
        Registries.ATTRIBUTE,
        TrainerAttributesLib.getIdentifier("pokemon_experience_gain_boost"),
        ClampedEntityAttribute(
            "attribute.name.trainerattributeslib.pokemon_experience_gain_boost",
            0.0,
            0.0,
            100.0
        )
    )

    val TYPED_POKEMON_EXPERIENCE_GAINED_ATTRIBUTE: TypedAttributesContainer = TypedAttributesContainer.of(POKEMON_EXPERIENCE_GAINED_ATTRIBUTE).register()
    val TYPED_CATCH_CHANCE_BOOST_ATTRIBUTE: TypedAttributesContainer = TypedAttributesContainer.of(CATCH_CHANCE_BOOST_ATTRIBUTE).register()
    val TYPED_SHINY_CHANCE_BOOST_ATTRIBUTE: TypedAttributesContainer = TypedAttributesContainer.of(SHINY_CHANCE_BOOST_ATTRIBUTE).register()


    fun applyOnto(item: ItemStack, slot: AttributeModifierSlot, attr: EntityAttribute, n: Double, operation: EntityAttributeModifier.Operation) {
        var modifiersComponent: AttributeModifiersComponent? = item.get(DataComponentTypes.ATTRIBUTE_MODIFIERS)

        if (modifiersComponent == null) {
            modifiersComponent = item.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent(
                listOf(), true
            ))
        }

        modifiersComponent?.modifiers()?.add(
            AttributeModifiersComponent.Entry(
                this.entryOf(attr),
                EntityAttributeModifier(this.idOfAttribute(attr), n, operation),
                slot
            )
        )

//        modifiersComponent?.applyModifiers(slot, BiConsumer {
//            _, _ -> EntityAttributeModifier(this.idOfAttribute(attr), n, operation)
//        })
    }

    fun entryOf(attr: EntityAttribute): RegistryEntry<EntityAttribute> = Registries.ATTRIBUTE.getEntry(
        attr
    )

    fun idOfAttribute(attr: EntityAttribute): Identifier = Identifier.of(this.entryOf(attr).idAsString)

    fun register() {
        AttributeEffects.applyEffects()
    }

}