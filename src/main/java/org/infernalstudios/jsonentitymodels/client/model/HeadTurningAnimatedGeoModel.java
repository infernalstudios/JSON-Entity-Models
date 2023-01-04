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

    private LivingEntity currentEntity;

    public HeadTurningAnimatedGeoModel(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public ResourceLocation getModelResource(T object) {
        LivingEntityData entityData = (LivingEntityData) this.currentEntity;

        if (entityData.getModelLocation() == null) {
            List<ResourceLocation> models = ResourceUtil.fetchModelsForEntity(this.entityName, this.currentEntity.isBaby());

            if (models.isEmpty() && this.currentEntity.isBaby()) {
                models = ResourceUtil.fetchModelsForEntity(this.entityName, false);
            }

            entityData.setModelLocation(models.get(rand.nextInt(models.size())));
        }

        return entityData.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        LivingEntityData entityData = (LivingEntityData) this.currentEntity;

        if (entityData.getModelLocation() != null && entityData.getTextureLocation() == null) {
            String[] modelPath = entityData.getModelLocation().getPath().split("/");
            String modelName = modelPath[modelPath.length - 1].replace(".geo.json", "");;

            List<ResourceLocation> textures = ResourceUtil.fetchTexturesForModel(this.entityName, modelName, this.currentEntity.isBaby());

            if (textures.isEmpty() && this.currentEntity.isBaby()) {
                textures = ResourceUtil.fetchTexturesForModel(this.entityName, modelName, false);
            }

            entityData.setTextureLocation(textures.get(rand.nextInt(textures.size())));
        }

        return entityData.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        LivingEntityData entityData = (LivingEntityData) this.currentEntity;

        if (entityData.getModelLocation() != null && entityData.getAnimationLocation() == null) {
            String[] modelPath = entityData.getModelLocation().getPath().split("/");
            String modelName = modelPath[modelPath.length - 1].replace(".geo.json", "");

            List<ResourceLocation> animations = ResourceUtil.fetchAnimationsForModel(this.entityName, modelName, this.currentEntity.isBaby());

            if (animations.isEmpty() && this.currentEntity.isBaby()) {
                animations = ResourceUtil.fetchAnimationsForModel(this.entityName, modelName, false);
            }

            entityData.setAnimationLocation(animations.get(rand.nextInt(animations.size())));
        }

        return entityData.getAnimationLocation();
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

    public void setCurrentEntity(LivingEntity currentEntity) {
        this.currentEntity = currentEntity;
    }
}
