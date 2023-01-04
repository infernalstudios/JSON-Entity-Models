package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.infernalstudios.jsonentitymodels.data.LivingEntityData;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;
import java.util.Random;

public abstract class HeadTurningAnimatedGeoModel<T extends ReplacedEntityBase & IAnimatable, U extends Mob> extends AnimatedGeoModel<T> {
    private final String entityName;
    private final Random rand = new Random();
    private ResourceLocation MODEL;
    private ResourceLocation TEXTURE;
    private ResourceLocation ANIMATION;

    public HeadTurningAnimatedGeoModel(String entityName) {
        this.entityName = entityName;
    }


    @Override
    public ResourceLocation getModelResource(T object) {
        if (MODEL == null) {
            List<ResourceLocation> models = ResourceUtil.fetchModelsForEntity(this.entityName, object.getBaby());

            if (models.isEmpty() && object.getBaby()) {
                models = ResourceUtil.fetchModelsForEntity(this.entityName, false);
            }

            MODEL = models.get(rand.nextInt(models.size()));
        }

        return MODEL;
    }

    public ResourceLocation getModelResource(T replacedEntity, LivingEntity entity) {
        if (MODEL == null) {
            List<ResourceLocation> models = ResourceUtil.fetchModelsForEntity(this.entityName, entity.isBaby());

            if (models.isEmpty() && entity.isBaby()) {
                models = ResourceUtil.fetchModelsForEntity(this.entityName, false);
            }

            MODEL = models.get(rand.nextInt(models.size()));
        }

        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        if (MODEL != null && TEXTURE == null) {
            String[] modelPath = MODEL.getPath().split("/");
            String modelName = modelPath[modelPath.length - 1].replace(".geo.json", "");;

            List<ResourceLocation> textures = ResourceUtil.fetchTexturesForModel(this.entityName, modelName, object.getBaby());

            if (textures.isEmpty() && object.getBaby()) {
                textures = ResourceUtil.fetchTexturesForModel(this.entityName, modelName, false);
            }

            TEXTURE = textures.get(rand.nextInt(textures.size()));
        }

        return TEXTURE;
    }

    public ResourceLocation getTextureResource(T replacedEntity, LivingEntity entity) {
        if (MODEL != null && ((LivingEntityData) entity).getTextureLocation() == null) {
            String[] modelPath = MODEL.getPath().split("/");
            String modelName = modelPath[modelPath.length - 1].replace(".geo.json", "");;

            List<ResourceLocation> textures = ResourceUtil.fetchTexturesForModel(this.entityName, modelName, entity.isBaby());

            if (textures.isEmpty() && entity.isBaby()) {
                textures = ResourceUtil.fetchTexturesForModel(this.entityName, modelName, false);
            }

            ((LivingEntityData) entity).setTextureLocation(textures.get(rand.nextInt(textures.size())));
        }

        return ((LivingEntityData) entity).getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        if (MODEL != null && ANIMATION == null) {
            String[] modelPath = MODEL.getPath().split("/");
            String modelName = modelPath[modelPath.length - 1].replace(".geo.json", "");

            List<ResourceLocation> animations = ResourceUtil.fetchAnimationsForModel(this.entityName, modelName, animatable.getBaby());

            if (animations.isEmpty() && animatable.getBaby()) {
                animations = ResourceUtil.fetchAnimationsForModel(this.entityName, modelName, false);
            }

            ANIMATION = animations.get(rand.nextInt(animations.size()));
        }

        return ANIMATION;
    }

    public ResourceLocation getAnimationResource(T animatable, LivingEntity entity) {
        if (MODEL != null && ANIMATION == null) {
            String[] modelPath = MODEL.getPath().split("/");
            String modelName = modelPath[modelPath.length - 1].replace(".geo.json", "");

            List<ResourceLocation> animations = ResourceUtil.fetchAnimationsForModel(this.entityName, modelName, entity.isBaby());

            if (animations.isEmpty() && entity.isBaby()) {
                animations = ResourceUtil.fetchAnimationsForModel(this.entityName, modelName, false);
            }

            ANIMATION = animations.get(rand.nextInt(animations.size()));
        }

        return ANIMATION;
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
