package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.resources.ResourceLocation;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ReplacedSkeletonModel extends AnimatedGeoModel {
    private final ResourceLocation MODEL = new ResourceLocation(JSONEntityModels.MOD_ID, "geo/skeleton.geo.json");
    private final ResourceLocation TEXTURE = new ResourceLocation(JSONEntityModels.MOD_ID, "textures/entity/skeleton.png");
    private final ResourceLocation ANIMATION = new ResourceLocation(JSONEntityModels.MOD_ID, "animations/skeleton.animation.json");

    @Override
    public ResourceLocation getModelResource(Object object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Object object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Object animatable) {
        return ANIMATION;
    }
}
