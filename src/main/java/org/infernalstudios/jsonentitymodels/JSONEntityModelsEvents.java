package org.infernalstudios.jsonentitymodels;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedCreeperRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedIronGolemRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSheepRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSkeletonRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSpiderRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedZombieRenderer;

import javax.annotation.Nullable;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = JSONEntityModels.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class JSONEntityModelsEvents {
    @Nullable
    private static ShaderInstance renderTypeEntityTranslucentEmissive;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityType.CREEPER, ReplacedCreeperRenderer::new);
            event.registerEntityRenderer(EntityType.SPIDER, ReplacedSpiderRenderer::new);
            event.registerEntityRenderer(EntityType.ZOMBIE, ReplacedZombieRenderer::new);
            event.registerEntityRenderer(EntityType.SKELETON, ReplacedSkeletonRenderer::new);
            event.registerEntityRenderer(EntityType.IRON_GOLEM, ReplacedIronGolemRenderer::new);
            event.registerEntityRenderer(EntityType.SHEEP, ReplacedSheepRenderer::new);

            // TODO: Add remaining 4 mobs here to actually replace the renderers
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
}
