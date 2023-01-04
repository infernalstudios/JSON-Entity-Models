package org.infernalstudios.jsonentitymodels.data;

import net.minecraft.resources.ResourceLocation;

public interface LivingEntityData {

    void setModelLocation(ResourceLocation location);

    ResourceLocation getModelLocation();

    void setTextureLocation(ResourceLocation location);

    ResourceLocation getTextureLocation();

    void setAnimationLocation(ResourceLocation location);

    ResourceLocation getAnimationLocation();
}
