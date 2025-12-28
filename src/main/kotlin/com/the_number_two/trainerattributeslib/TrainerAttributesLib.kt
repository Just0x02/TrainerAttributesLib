package com.the_number_two.trainerattributeslib

import com.the_number_two.trainerattributeslib.attributes.TrainerAttributes
import com.the_number_two.trainerattributeslib.items.ModItems
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

class TrainerAttributesLib : ModInitializer {
    companion object {
        val MOD_ID: String = "trainerattributeslib"

        fun getIdentifier(subId: String): Identifier = Identifier.of(TrainerAttributesLib.MOD_ID, subId)
    }

    override fun onInitialize() {
        TrainerAttributes.register()
        ModItems.register()
    }
}
