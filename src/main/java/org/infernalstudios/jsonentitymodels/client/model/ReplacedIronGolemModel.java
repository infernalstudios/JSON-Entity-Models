package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.resources.ResourceLocation;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ReplacedIronGolemModel extends AnimatedGeoModel {
    private final ResourceLocation MODEL = new ResourceLocation(JSONEntityModels.MOD_ID, "geo/iron_golem.geo.json");
    private final ResourceLocation TEXTURE = new ResourceLocation(JSONEntityModels.MOD_ID, "textures/entity/iron_golem.png");
    private final ResourceLocation ANIMATION = new ResourceLocation(JSONEntityModels.MOD_ID, "animations/iron_golem.animation.json");

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
