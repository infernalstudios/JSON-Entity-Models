package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.resources.ResourceLocation;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;

public class ReplacedSpiderModel extends HeadTurningAnimatedGeoModel {
    private final ResourceLocation MODEL = new ResourceLocation(JSONEntityModels.MOD_ID, "geo/spider.geo.json");
    private final ResourceLocation TEXTURE = new ResourceLocation(JSONEntityModels.MOD_ID, "textures/entity/spider.png");
    private final ResourceLocation ANIMATION = new ResourceLocation(JSONEntityModels.MOD_ID, "animations/spider.animation.json");

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
