Example -> 

```
package com.the_number_two.traineraccessories.items.accessories.pokemon.base

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.the_number_two.traineraccessories.items.accessories.TrainerAccessoryItem
import com.the_number_two.trainerattributeslib.attributes.TrainerAttributes
import com.the_number_two.trainerattributeslib.attributes.util.AttributeBuilderHelper.addTypedAttribute
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.attribute.EntityAttributeModifier

class AxewCharm() : TrainerAccessoryItem(
	Settings()
		.component(
		DataComponentTypes.ATTRIBUTE_MODIFIERS as ComponentType<AttributeModifiersComponent>,
		AttributeModifiersComponent.builder()
            .addTypedAttribute(
                TrainerAttributes.TYPED_BONUS_IVS_ATTRIBUTE,
                ElementalTypes.DRAGON,
                2.0,
                EntityAttributeModifier.Operation.ADD_VALUE
            )
            .addTypedAttribute(
                TrainerAttributes.TYPED_SPAWN_CHANCE_ATTRIBUTE,
                ElementalTypes.DRAGON,
                0.20,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
			.addTypedAttribute(
				TrainerAttributes.TYPED_CATCH_CHANCE_BOOST_ATTRIBUTE,
				ElementalTypes.DRAGON,
				0.20,
				EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
			)
			.build()
	)
)
```
