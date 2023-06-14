/*
 *
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

package org.infernalstudios.jsonentitymodels.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import org.infernalstudios.jsonentitymodels.util.RandomUtil;
import org.infernalstudios.jsonentitymodels.util.ResourceCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Map;

public class CreeperPoweredLayer extends GeoRenderLayer {

    public CreeperPoweredLayer(GeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, GeoAnimatable animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable instanceof Creeper creeper && creeper.isPowered() && this.getGeoModel() instanceof HeadTurningAnimatedGeoModel headTurningAnimatedGeoModel) {
            Map<String, List<ResourceLocation>> textures = ((LivingEntity) animatable).isBaby() ? ResourceCache.getInstance().getBabyTextures() : ResourceCache.getInstance().getAdultTextures();

            String[] splitPath = headTurningAnimatedGeoModel.getTextureResource(null).getPath().split("/");

            String poweredKey = splitPath[2] + ":" + splitPath[3] + "/" + splitPath[5] + "/" + splitPath[splitPath.length - 1].replace(".png", "") + "_glow";

            List<ResourceLocation> poweredResources = textures.get(poweredKey);

            ResourceLocation poweredResource;

            if (poweredResources == null || poweredResources.isEmpty()) {
                poweredResource = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
            } else {
                poweredResource = poweredResources.get(RandomUtil.getPseudoRandomInt(((LivingEntity) animatable).getUUID().getMostSignificantBits(), RandomUtil.textureUUID.getLeastSignificantBits(), poweredResources.size()));
            }

            float f = (float)creeper.tickCount + partialTick;

            RenderType newRenderType = RenderType.energySwirl(poweredResource, this.xOffset(f) % 1.0F, f * 0.01F % 1.0F);

            this.getRenderer().actuallyRender(
                    poseStack,
                    animatable,
                    bakedModel,
                    newRenderType,
                    bufferSource,
                    bufferSource.getBuffer(newRenderType),
                    true,
                    partialTick,
                    packedLight,
                    LivingEntityRenderer.getOverlayCoords(creeper, 0.0F),
                    1f, 1f, 1f, 1f);
        }
    }

    protected float xOffset(float creeperLife) {
        return creeperLife * 0.01F;
    }
}
