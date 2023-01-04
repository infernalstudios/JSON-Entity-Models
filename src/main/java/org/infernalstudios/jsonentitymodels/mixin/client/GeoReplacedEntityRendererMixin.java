package org.infernalstudios.jsonentitymodels.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoReplacedEntityRenderer;

@Mixin(GeoReplacedEntityRenderer.class)
public class GeoReplacedEntityRendererMixin {

    @Shadow @Final protected AnimatedGeoModel<IAnimatable> modelProvider;

    @Shadow protected IAnimatable currentAnimatable;

    @Unique
    private Entity entity;

    @Inject(
            method = "render(Lnet/minecraft/world/entity/Entity;Lsoftware/bernie/geckolib3/core/IAnimatable;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"),
            remap = false
    )
    private void jsonentitymodels_saveEntityInRender(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        this.entity = entity;
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/world/entity/Entity;Lsoftware/bernie/geckolib3/core/IAnimatable;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lsoftware/bernie/geckolib3/model/AnimatedGeoModel;getModel(Lnet/minecraft/resources/ResourceLocation;)Lsoftware/bernie/geckolib3/geo/render/built/GeoModel;"),
            remap = false
    )
    private GeoModel jsonentitymodels_allowModelVariants(GeoModel original) {
        if (this.modelProvider instanceof HeadTurningAnimatedGeoModel headTurningAnimatedGeoModel && this.entity instanceof LivingEntity livingEntity) {
            return this.modelProvider.getModel(headTurningAnimatedGeoModel.getModelResource((ReplacedEntityBase) this.currentAnimatable, livingEntity));
        }

        return original;
    }
}
