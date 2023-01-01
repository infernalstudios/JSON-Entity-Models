package org.infernalstudios.jsonentitymodels;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedCreeperRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedIronGolemRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSkeletonRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSpiderRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedZombieRenderer;

@Mod.EventBusSubscriber(modid = JSONEntityModels.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class JSONEntityModelsEvents {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityType.CREEPER, ReplacedCreeperRenderer::new);
            event.registerEntityRenderer(EntityType.SPIDER, ReplacedSpiderRenderer::new);
            event.registerEntityRenderer(EntityType.ZOMBIE, ReplacedZombieRenderer::new);
            event.registerEntityRenderer(EntityType.SKELETON, ReplacedSkeletonRenderer::new);
            event.registerEntityRenderer(EntityType.IRON_GOLEM, ReplacedIronGolemRenderer::new);
    }
}
