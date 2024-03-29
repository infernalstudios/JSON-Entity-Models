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

package org.infernalstudios.jsonentitymodels.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedDefaultModel;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedChickenRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedCowRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedCreeperRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedDefaultRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedEnderManRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedIronGolemRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedPigRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSheepRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSkeletonRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSpiderRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedZombieRenderer;
import org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity;
import org.infernalstudios.jsonentitymodels.util.ResourceCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Shadow @Final private ItemRenderer itemRenderer;
    @Shadow @Final private EntityModelSet entityModels;
    @Shadow @Final private Font font;
    @Shadow @Final private BlockRenderDispatcher blockRenderDispatcher;
    @Shadow @Final private ItemInHandRenderer itemInHandRenderer;
    @Unique
    private static final Map<EntityType<? extends Entity>, EntityRendererProvider<? extends Entity>> RENDERER_PROVIDER_MAP = new HashMap<>();

    @Unique
    private static Map<EntityType<? extends Entity>, EntityRenderer<? extends Entity>> RENDERER_MAP = new HashMap<>();

    static {
        RENDERER_PROVIDER_MAP.put(EntityType.CREEPER, ReplacedCreeperRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.SPIDER, ReplacedSpiderRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.CAVE_SPIDER, (context) -> new ReplacedSpiderRenderer(context, "cave_spider"));
        RENDERER_PROVIDER_MAP.put(EntityType.ZOMBIE, ReplacedZombieRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.DROWNED, (context) -> new ReplacedZombieRenderer(context, "drowned"));
        RENDERER_PROVIDER_MAP.put(EntityType.HUSK, (context) -> new ReplacedZombieRenderer(context, "husk"));
        RENDERER_PROVIDER_MAP.put(EntityType.SKELETON, ReplacedSkeletonRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.STRAY, (context) -> new ReplacedSkeletonRenderer(context, "stray"));
        RENDERER_PROVIDER_MAP.put(EntityType.WITHER_SKELETON, (context) -> new ReplacedSkeletonRenderer(context, "wither_skeleton"));
        RENDERER_PROVIDER_MAP.put(EntityType.IRON_GOLEM, ReplacedIronGolemRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.SHEEP, ReplacedSheepRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.PIG, ReplacedPigRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.COW, ReplacedCowRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.MOOSHROOM, (context) -> new ReplacedCowRenderer(context, "mooshroom"));
        RENDERER_PROVIDER_MAP.put(EntityType.CHICKEN, ReplacedChickenRenderer::new);
        RENDERER_PROVIDER_MAP.put(EntityType.ENDERMAN, ReplacedEnderManRenderer::new);
    }

    @Inject(method = "getRenderer", at = @At("RETURN"), cancellable = true)
    private <T extends Entity> void jsonentitymodels_chooseRenderer(T entity, CallbackInfoReturnable<EntityRenderer<? super T>> cir) {
        if (!(entity instanceof AbstractClientPlayer) && entity instanceof LivingEntity livingEntity) {
            ResourceCache cache = ResourceCache.getInstance();

            Map<String, List<ResourceLocation>> modelMap = livingEntity.isBaby() ? cache.getBabyModels() : cache.getAdultModels();
            List<ResourceLocation> models = modelMap.get(ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType()).toString());

            if (models != null && !models.isEmpty()) {
                EntityRenderer<T> renderer = (EntityRenderer<T>) RENDERER_MAP.get(livingEntity.getType());
                cir.setReturnValue(renderer);
            }
        }
    }

    @Inject(method = "onResourceManagerReload", at = @At("RETURN"))
    private void jsonentitymodels_trackDefaultRenderers(ResourceManager manager, CallbackInfo ci) {
        boolean changedRenderer = false;

        for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entityEntry : ForgeRegistries.ENTITY_TYPES.getEntries()) {
            ResourceLocation entityResourceLocation = entityEntry.getKey().location();

            if (entityResourceLocation.toString().equals("minecraft:player")) continue;

            if (!RENDERER_PROVIDER_MAP.containsKey(entityEntry.getValue())) {
                RENDERER_PROVIDER_MAP.put(entityEntry.getValue(), RENDERER_PROVIDER_MAP.getOrDefault(entityEntry.getValue(), (context) -> new ReplacedDefaultRenderer(context,
                        new ReplacedDefaultModel(entityResourceLocation.getNamespace(), entityResourceLocation.getPath()), new ReplacedDefaultEntity(entityEntry.getValue()))));

                changedRenderer = true;
            }
        }

        if (changedRenderer) {
            createRenderers(manager);
        }
    }

    @Unique
    private void createRenderers(ResourceManager manager) {
        EntityRendererProvider.Context context = new EntityRendererProvider.Context((EntityRenderDispatcher) (Object) this, this.itemRenderer, this.blockRenderDispatcher, this.itemInHandRenderer, manager, this.entityModels, this.font);

        ImmutableMap.Builder<EntityType<?>, EntityRenderer<?>> builder = ImmutableMap.builder();
        RENDERER_PROVIDER_MAP.forEach((entityType, rendererProvider) -> {
            try {
                builder.put(entityType, rendererProvider.create(context));
            } catch (Exception exception) {
                throw new IllegalArgumentException("JEMs failed to create model for " + Registry.ENTITY_TYPE.getKey(entityType), exception);
            }
        });

        RENDERER_MAP = builder.build();
    }
}
