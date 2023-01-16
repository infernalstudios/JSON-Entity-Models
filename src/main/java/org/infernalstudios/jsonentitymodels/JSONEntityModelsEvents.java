package org.infernalstudios.jsonentitymodels;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedDefaultModel;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedCreeperRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedDefaultRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedIronGolemRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSheepRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSkeletonRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSpiderRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedZombieRenderer;
import org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
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

        // TODO: Add 4 remaining mobs for Beta 1
    }

    private static Map<EntityType<?>, EntityRendererProvider<?>> DEFAULT_RENDERERS = new HashMap<>();

    @Nullable
    private static ShaderInstance renderTypeEntityTranslucentEmissive;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        DEFAULT_RENDERERS = new HashMap<>(EntityRenderers.PROVIDERS);
        replaceRenderers();
    }

    @SubscribeEvent
    public static void registerShaders(final RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceManager(),
                new ResourceLocation(JSONEntityModels.MOD_ID, "rendertype_entity_translucent_emissive"),
                DefaultVertexFormat.NEW_ENTITY),
                shaderInstance -> renderTypeEntityTranslucentEmissive = shaderInstance);
    }

    @Nullable
    public static ShaderInstance getRenderTypeEntityTranslucentEmissive() {
        return renderTypeEntityTranslucentEmissive;
    }

    public static void replaceRenderers() {
        for (EntityType<?> entity : ForgeRegistries.ENTITIES) {
            if (entity.getRegistryName().toString().equals("minecraft:player")) continue;

            if (doesEntityHaveResource(entity)) {
                ResourceLocation registryName = entity.getRegistryName();

                EntityRenderers.register(entity, (EntityRendererProvider<Entity>) RENDERER_MAP.getOrDefault(entity, (context) -> new ReplacedDefaultRenderer(context,
                    new ReplacedDefaultModel(registryName.getNamespace(), registryName.getPath()), new ReplacedDefaultEntity())));
            } else {
                EntityRenderers.register(entity, (EntityRendererProvider<Entity>) DEFAULT_RENDERERS.get(entity));
            }

        }
    }

    private static boolean doesEntityHaveResource(EntityType<?> entityType) {
        ResourceLocation entityTypeRegistry = entityType.getRegistryName();
        String resourceType = "geo/" + entityTypeRegistry.getNamespace() + "/" + entityTypeRegistry.getPath();

        Collection<ResourceLocation> resources = Minecraft.getInstance().getResourceManager().listResources(resourceType,
            (filename) -> filename.endsWith(".json"));

        return resources.stream().anyMatch(resourceLocation -> resourceLocation.getNamespace().equals(JSONEntityModels.MOD_ID));
    }
}
