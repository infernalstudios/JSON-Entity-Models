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
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import org.infernalstudios.jsonentitymodels.util.RandomUtil;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import software.bernie.geckolib.GeckoLibException;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class HeadTurningAnimatedGeoModel<T extends GeoReplacedEntity, U extends Mob> extends GeoModel<T> {
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
        ResourceLocation modelResource = this.getModelResource(object);

        if (modelResource == null) return null;

        String[] modelPath = modelResource.getPath().split("/");
        String modelName = modelPath[modelPath.length - 1].replaceAll("(\\.geo)?\\.json$", "");;

        List<ResourceLocation> textures = ResourceUtil.fetchTexturesForModel(this.namespace, this.entityName, modelName, this.currentEntity != null && this.currentEntity.isBaby());

        if (textures == null || textures.isEmpty()) {
            return new ResourceLocation(JSONEntityModels.MOD_ID, "textures/entity/" + this.namespace + "/" + this.entityName + "/" + (this.currentEntity != null && this.currentEntity.isBaby() ? "baby/" : "adult/") + modelName + "/");
        }

        return textures.get(RandomUtil.getPseudoRandomInt(this.currentEntity != null ? this.currentEntity.getUUID().getLeastSignificantBits() : UUID.randomUUID().getLeastSignificantBits(), RandomUtil.textureUUID.getLeastSignificantBits(), textures.size()));
    }

    @Override
    public ResourceLocation getAnimationResource(@Nullable T animatable) {
        ResourceLocation modelResource = this.getModelResource(animatable);

        if (modelResource == null) return null;

        String[] modelPath = modelResource.getPath().split("/");
        String modelName = modelPath[modelPath.length - 1].replaceAll("(\\.geo)?\\.json$", "");

        List<ResourceLocation> animations = ResourceUtil.fetchAnimationsForModel(this.namespace, this.entityName, modelName, this.currentEntity != null && this.currentEntity.isBaby());

        if (animations == null || animations.isEmpty()) {

            if (this.currentEntity != null) {
                EntityRenderer<?> originalRenderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(this.currentEntity.getType());

                if (this.currentEntity instanceof GeoEntity geoEntity && originalRenderer instanceof GeoEntityRenderer<?> geoEntityRenderer && geoEntityRenderer.getGeoModel() != null) {
                    return geoEntityRenderer.getGeoModel().getAnimationResource(geoEntity);
                }
            }

            return null;
        }

        return animations.get(RandomUtil.getPseudoRandomInt(this.currentEntity != null ?
                        this.currentEntity.getUUID().getLeastSignificantBits() ^ this.currentEntity.getUUID().getMostSignificantBits() :
                        UUID.randomUUID().getLeastSignificantBits() ^ UUID.randomUUID().getMostSignificantBits(), RandomUtil.animationUUID.getMostSignificantBits(), animations.size()));
    }

    @Override
    public Animation getAnimation(T animatable, String name) {
        ResourceLocation location = getAnimationResource(animatable);
        BakedAnimations bakedAnimations = GeckoLibCache.getBakedAnimations().get(location);

        if (bakedAnimations == null)
            return null;

        return bakedAnimations.getAnimation(name);
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        if (animatable instanceof ReplacedEntityBase replacedEntityBase) {
            replacedEntityBase.setModelInstance(this);
        }
        super.setCustomAnimations(animatable, instanceId, animationState);

        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

    public void setCurrentEntity(LivingEntity currentEntity) {
        this.currentEntity = currentEntity;
    }

    public LivingEntity getCurrentEntity() {
        return this.currentEntity;
    }
}
