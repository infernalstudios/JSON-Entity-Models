/*
 * Copyright 2023 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import org.infernalstudios.jsonentitymodels.util.RandomUtil;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class HeadTurningAnimatedGeoModel<T extends IAnimatable, U extends Mob> extends AnimatedGeoModel<T> {
    private final String namespace;
    private final String entityName;

    private LivingEntity currentEntity;

    public HeadTurningAnimatedGeoModel(String namespace, String entityName) {
        this.namespace = namespace;
        this.entityName = entityName;
    }

    public HeadTurningAnimatedGeoModel(String entityName) {
        this("minecraft", entityName);
    }

    @Override
    public ResourceLocation getModelResource(@Nullable T object) {
        List<ResourceLocation> models = ResourceUtil.fetchModelsForEntity(this.namespace, this.entityName, this.currentEntity != null && this.currentEntity.isBaby());

        if (models == null || models.isEmpty()) {
            return null;
        }

        return models.get(RandomUtil.getPseudoRandomInt(this.currentEntity != null ? this.currentEntity.getUUID().getMostSignificantBits() : UUID.randomUUID().getMostSignificantBits(), RandomUtil.modelUUID.getMostSignificantBits(), models.size()));
    }

    @Override
    public ResourceLocation getTextureResource(@Nullable T object) {
        String[] modelPath = this.getModelResource(object).getPath().split("/");
        String modelName = modelPath[modelPath.length - 1].replaceAll("(\\.geo)?\\.json$", "");;

        List<ResourceLocation> textures = ResourceUtil.fetchTexturesForModel(this.namespace, this.entityName, modelName, this.currentEntity != null && this.currentEntity.isBaby());

        if (textures == null || textures.isEmpty()) {
            return new ResourceLocation(JSONEntityModels.MOD_ID, "textures/entity/" + this.namespace + "/" + this.entityName + "/" + (this.currentEntity != null && this.currentEntity.isBaby() ? "baby/" : "adult/") + modelName + "/");
        }

        return textures.get(RandomUtil.getPseudoRandomInt(this.currentEntity != null ? this.currentEntity.getUUID().getLeastSignificantBits() : UUID.randomUUID().getLeastSignificantBits(), RandomUtil.textureUUID.getLeastSignificantBits(), textures.size()));
    }

    @Override
    public ResourceLocation getAnimationResource(@Nullable T animatable) {
        String[] modelPath = this.getModelResource(animatable).getPath().split("/");
        String modelName = modelPath[modelPath.length - 1].replaceAll("(\\.geo)?\\.json$", "");

        List<ResourceLocation> animations = ResourceUtil.fetchAnimationsForModel(this.namespace, this.entityName, modelName, this.currentEntity != null && this.currentEntity.isBaby());

        if (animations == null || animations.isEmpty()) {
            return null;
        }

        return animations.get(RandomUtil.getPseudoRandomInt(this.currentEntity != null ?
                        this.currentEntity.getUUID().getLeastSignificantBits() ^ this.currentEntity.getUUID().getMostSignificantBits() :
                        UUID.randomUUID().getLeastSignificantBits() ^ UUID.randomUUID().getMostSignificantBits(), RandomUtil.animationUUID.getMostSignificantBits(), animations.size()));
    }

    @Override
    public Animation getAnimation(String name, IAnimatable animatable) {
        AnimationFile animation = GeckoLibCache.getInstance().getAnimations().get(this.getAnimationResource((T) animatable));

        if (animation == null) {
            return null;
        }

        return animation.getAnimation(name);
    }

    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        if (animatable instanceof ReplacedEntityBase replacedEntityBase) {
            replacedEntityBase.setModelInstance(this);
        }
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        IBone head = this.getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

            AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
            int unpausedMultiplier = !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;

            head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) * unpausedMultiplier);
            head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) * unpausedMultiplier);
        }
    }

    public void setCurrentEntity(LivingEntity currentEntity) {
        this.currentEntity = currentEntity;
    }

    public LivingEntity getCurrentEntity() {
        return this.currentEntity;
    }
}
