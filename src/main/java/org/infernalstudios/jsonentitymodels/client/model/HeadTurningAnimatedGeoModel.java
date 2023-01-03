package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public abstract class HeadTurningAnimatedGeoModel<T extends ReplacedEntityBase & IAnimatable> extends AnimatedGeoModel<T> {
    private final ResourceLocation MODEL;
    private final ResourceLocation BABY_MODEL;
    private final ResourceLocation TEXTURE;
    private final ResourceLocation BABY_TEXTURE;
    private final ResourceLocation ANIMATION;
    private final ResourceLocation BABY_ANIMATION;

    public HeadTurningAnimatedGeoModel(String entityName) {
        this.MODEL = new ResourceLocation(JSONEntityModels.MOD_ID, "geo/" + entityName + ".geo.json");
        this.TEXTURE = new ResourceLocation(JSONEntityModels.MOD_ID, "textures/entity/" + entityName + ".png");
        this.ANIMATION = new ResourceLocation(JSONEntityModels.MOD_ID, "animations/" + entityName + ".animation.json");

        this.BABY_MODEL = new ResourceLocation(JSONEntityModels.MOD_ID, "geo/" + entityName + "_baby.geo.json");
        this.BABY_TEXTURE = new ResourceLocation(JSONEntityModels.MOD_ID, "textures/entity/" + entityName + "_baby.png");
        this.BABY_ANIMATION = new ResourceLocation(JSONEntityModels.MOD_ID, "animations/" + entityName + "_baby.animation.json");
    }


    @Override
    public ResourceLocation getModelResource(T object) {
        return object.getBaby() ? BABY_MODEL : MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return object.getBaby() ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animatable.getBaby() ? BABY_ANIMATION : ANIMATION;
    }

    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

        AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
        int unpausedMultiplier = !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;

        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) * unpausedMultiplier);
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) * unpausedMultiplier);
    }
}
