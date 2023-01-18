package org.infernalstudios.jsonentitymodels;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedDefaultModel;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedCreeperRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedDefaultRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedIronGolemRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedPigRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSheepRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSkeletonRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSpiderRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedZombieRenderer;
import org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = JSONEntityModels.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class JSONEntityModelsEvents {
    private static final Map<EntityType<? extends Entity>, EntityRendererProvider<? extends Entity>> RENDERER_MAP = new HashMap();

    static {
        RENDERER_MAP.put(EntityType.CREEPER, ReplacedCreeperRenderer::new);
        RENDERER_MAP.put(EntityType.SPIDER, ReplacedSpiderRenderer::new);
        RENDERER_MAP.put(EntityType.ZOMBIE, ReplacedZombieRenderer::new);
        RENDERER_MAP.put(EntityType.SKELETON, ReplacedSkeletonRenderer::new);
        RENDERER_MAP.put(EntityType.IRON_GOLEM, ReplacedIronGolemRenderer::new);
        RENDERER_MAP.put(EntityType.SHEEP, ReplacedSheepRenderer::new);
        RENDERER_MAP.put(EntityType.PIG, ReplacedPigRenderer::new);

        // TODO: Add 3 remaining mobs for Beta 1
    }

    private static Map<EntityType<?>, EntityRendererProvider<?>> DEFAULT_RENDERERS = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        DEFAULT_RENDERERS = new HashMap<>(EntityRenderers.PROVIDERS);
        replaceRenderers();
    }

    public static void replaceRenderers() {
        for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entityEntry : ForgeRegistries.ENTITY_TYPES.getEntries()) {
            ResourceLocation entityResourceLocation = entityEntry.getKey().location();

            if (entityResourceLocation.toString().equals("minecraft:player")) continue;

            if (doesEntityHaveResource(entityResourceLocation)) {
                EntityRenderers.register(entityEntry.getValue(), (EntityRendererProvider<Entity>) RENDERER_MAP.getOrDefault(entityEntry.getValue(), (context) -> new ReplacedDefaultRenderer(context,
                    new ReplacedDefaultModel(entityResourceLocation.getNamespace(), entityResourceLocation.getPath()), new ReplacedDefaultEntity())));
            } else {
                EntityRenderers.register(entityEntry.getValue(), (EntityRendererProvider<Entity>) DEFAULT_RENDERERS.get(entityEntry.getValue()));
            }

        }
    }

    private static boolean doesEntityHaveResource(ResourceLocation entityResourceLocation) {
        String resourceType = "geo/" + entityResourceLocation.getNamespace() + "/" + entityResourceLocation.getPath();

        return Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(JSONEntityModels.MOD_ID, resourceType)).isPresent();
    }
}
