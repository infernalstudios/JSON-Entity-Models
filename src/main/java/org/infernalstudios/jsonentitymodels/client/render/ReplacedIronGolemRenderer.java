package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedIronGolemModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedIronGolemEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoReplacedEntityRenderer;

public class ReplacedIronGolemRenderer extends GeoReplacedEntityRenderer<ReplacedIronGolemEntity> {

    public ReplacedIronGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedIronGolemModel(), new ReplacedIronGolemEntity());
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof IronGolem ironGolem && animatable instanceof ReplacedIronGolemEntity replacedIronGolem) {
            replacedIronGolem.setAttacking(ironGolem.getAttackAnimationTick() > 0);
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
