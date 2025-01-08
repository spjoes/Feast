package dev.persn.feast;


import com.mojang.serialization.MapCodec;
import dev.persn.feast.Feast;
import dev.persn.feast.components.ModComponents;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record HasWaterProperty() implements BooleanProperty {

    public static final MapCodec<HasWaterProperty> CODEC = MapCodec.unit(HasWaterProperty::new);


    @Override
    public boolean getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ModelTransformationMode modelTransformationMode) {
        return stack.getOrDefault(ModComponents.HAS_WATER, false);
    }

    @Override
    public MapCodec<? extends BooleanProperty> getCodec() {
        return CODEC;
    }

    public static void register() {
        BooleanProperties.ID_MAPPER.put(Identifier.of(Feast.MOD_ID, "has_water"), CODEC);
    }

}

