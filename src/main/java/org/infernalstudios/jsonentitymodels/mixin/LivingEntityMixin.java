package org.infernalstudios.jsonentitymodels.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.infernalstudios.jsonentitymodels.data.LivingEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityData {

    @Unique
    private ResourceLocation modelLocation;

    @Unique
    private ResourceLocation textureLocation;

    @Unique
    private ResourceLocation animationLocation;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    @Override
    public void setModelLocation(ResourceLocation location) {
        this.modelLocation = location;
    }

    @Unique
    @Override
    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }

    @Unique
    @Override
    public void setTextureLocation(ResourceLocation location) {
        this.textureLocation = location;
    }

    @Unique
    @Override
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    @Unique
    @Override
    public void setAnimationLocation(ResourceLocation location) {
        this.animationLocation = location;
    }

    @Unique
    @Override
    public ResourceLocation getAnimationLocation() {
        return this.animationLocation;
    }
}
