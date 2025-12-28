package com.the_number_two.trainerattributeslib.items

import com.the_number_two.trainerattributeslib.TrainerAttributesLib
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModItems {
    val MUDKIP_CHARM = register("mudkip_charm", MudkipCharm())

    fun <T : Item> register(id: String, item: T): T {
        val identifier: Identifier = TrainerAttributesLib.getIdentifier(id)
        Registry.register(Registries.ITEM, identifier, item)
        return item
    }

    fun register() {

    }
}