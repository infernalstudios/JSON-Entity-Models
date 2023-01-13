package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.lang3.StringUtils;
import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import org.infernalstudios.jsonentitymodels.client.render.layer.AutomatedGlowLayer;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoQuad;
import software.bernie.geckolib3.geo.render.built.GeoVertex;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoReplacedEntityRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Adapted from:
 *
 * @author DerToaster98 Copyright (c) 30.03.2022 Developed by DerToaster98
 *         GitHub: https://github.com/DerToaster98
 *
 *         Purpose of this class: This class is a extended version of
 *         {@link GeoReplacedEntityRenderer}. It automates the process of rendering
 *         items at hand bones as well as standard armor at certain bones. The
 *         model must feature a few special bones for this to work.
 */
@OnlyIn(Dist.CLIENT)
public abstract class ExtendedGeoReplacedEntityRenderer<T extends IAnimatable, U extends Mob> extends GeoReplacedEntityRenderer<T> {
    protected static Map<ResourceLocation, IntIntPair> TEXTURE_DIMENSIONS_CACHE = new Object2ObjectOpenHashMap<>();
    protected static Map<ResourceLocation, Tuple<Integer, Integer>> TEXTURE_SIZE_CACHE = new Object2ObjectOpenHashMap<>();
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = new Object2ObjectOpenHashMap<>();
    protected static final HumanoidModel<LivingEntity> DEFAULT_BIPED_ARMOR_MODEL_INNER = new HumanoidModel<>(
            Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
    protected static final HumanoidModel<LivingEntity> DEFAULT_BIPED_ARMOR_MODEL_OUTER = new HumanoidModel<>(
            Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));

    protected float widthScale;
    protected float heightScale;

    protected U currentEntityBeingRendered;
    private float currentPartialTicks;
    protected ResourceLocation textureForBone = null;

    protected final Queue<Tuple<GeoBone, ItemStack>> HEAD_QUEUE = new ArrayDeque<>();

    protected ExtendedGeoReplacedEntityRenderer(EntityRendererProvider.Context renderManager,
                                        AnimatedGeoModel<IAnimatable> modelProvider, T animatable) {
        this(renderManager, modelProvider, animatable, 1, 1, 0);
    }

    protected ExtendedGeoReplacedEntityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<IAnimatable> modelProvider,
                                        T animatable, float widthScale, float heightScale, float shadowSize) {
        super(renderManager, modelProvider, animatable);

        this.addLayer(new AutomatedGlowLayer(this));

        this.shadowRadius = shadowSize;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }

    // Yes, this is necessary to be done after everything else, otherwise it will
    // mess up the texture cause the rendertypebuffer will be modified
    protected void renderHeads(PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        while (!this.HEAD_QUEUE.isEmpty()) {
            Tuple<GeoBone, ItemStack> entry = this.HEAD_QUEUE.poll();

            GeoBone bone = entry.getA();
            ItemStack itemStack = entry.getB();
            GameProfile skullOwnerProfile = null;

            poseStack.pushPose();
            RenderUtils.translateAndRotateMatrixForBone(poseStack, bone);

            if (itemStack.hasTag()) {
                Tag skullOwnerTag = itemStack.getTag().get(PlayerHeadItem.TAG_SKULL_OWNER);

                if (skullOwnerTag != null) {
                    if (skullOwnerTag instanceof CompoundTag tag) {
                        skullOwnerProfile = NbtUtils.readGameProfile(tag);
                    }
                    else if (skullOwnerTag instanceof StringTag tag) {
                        String skullOwner = tag.getAsString();

                        if (!StringUtils.isBlank(skullOwner)) {
                            SkullBlockEntity.updateGameprofile(new GameProfile(null, skullOwner), name ->
                                    itemStack.getTag().put(PlayerHeadItem.TAG_SKULL_OWNER, NbtUtils.writeGameProfile(new CompoundTag(), name)));
                        }
                    }
                }
            }

            float relativeScaleX = 1.1875F;
            float relativeScaleY = 1.1875F;
            float relativeScaleZ = 1.1875F;

            // Calculate scale in relation to a vanilla head (8x8x8 units)
            if (bone.childCubes.size() > 0) {
                GeoCube firstCube = bone.childCubes.get(0);
                relativeScaleX *= firstCube.size.x() / 8f;
                relativeScaleY *= firstCube.size.y() / 8f;
                relativeScaleZ *= firstCube.size.z() / 8f;
            }

            poseStack.scale(relativeScaleX, relativeScaleY, relativeScaleZ);
            poseStack.translate(-0.5, 0, -0.5);

            SkullBlock.Type skullBlockType = ((AbstractSkullBlock)((BlockItem)itemStack.getItem()).getBlock()).getType();
            SkullModelBase skullmodelbase = SkullBlockRenderer
                    .createSkullRenderers(Minecraft.getInstance().getEntityModels()).get(skullBlockType);
            RenderType rendertype = SkullBlockRenderer.getRenderType(skullBlockType, skullOwnerProfile);

            SkullBlockRenderer.renderSkull(null, 0, 0, poseStack, buffer, packedLight, skullmodelbase, rendertype);
            poseStack.popPose();
        }
    }

    // Rendercall to render the model itself
    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        if (entity instanceof LivingEntity livingEntity && animatable instanceof ReplacedEntityBase replacedEntityBase) {
            replacedEntityBase.setHurt(livingEntity.hurtTime > 0);
            replacedEntityBase.setBaby(livingEntity.isBaby());
            replacedEntityBase.setDead(livingEntity.isDeadOrDying());

            if (this.modelProvider instanceof HeadTurningAnimatedGeoModel headTurningAnimatedGeoModel) {
                headTurningAnimatedGeoModel.setCurrentEntity(livingEntity);

                if (livingEntity.isBaby() && !headTurningAnimatedGeoModel.getModelResource((ReplacedEntityBase) animatable).toString().contains("/baby/")) {
                    poseStack.scale(0.5F, 0.5F, 0.5F);
                }
            }


        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        // Now, render the heads
        renderHeads(poseStack, bufferSource, packedLight);

        // Since we rendered at least once at this point, let's set the cycle to
        // repeated
        setCurrentModelRenderCycle(EModelRenderCycle.REPEATED);
    }

    @Override
    public void renderLate(Object animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderLate(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.currentEntityBeingRendered = (U) animatable;
        this.currentPartialTicks = partialTick;
    }

    protected boolean isArmorBone(final GeoBone bone) {
        return bone.getName().startsWith("armor");
    }


    protected void handleArmorRenderingForBone(GeoBone bone, PoseStack stack, VertexConsumer bufferIn,
                                               int packedLightIn, int packedOverlayIn, ResourceLocation currentTexture) {
        final ItemStack armorForBone = this.getArmorForBone(bone.getName(), currentEntityBeingRendered);
        final EquipmentSlot boneSlot = this.getEquipmentSlotForArmorBone(bone.getName(),
                currentEntityBeingRendered);
        // Armor and geo armor
        if (armorForBone != null && boneSlot != null) {
            // Geo armor
            if (armorForBone.getItem() instanceof ArmorItem) {
                final ArmorItem armorItem = (ArmorItem) armorForBone.getItem();
                if (armorForBone.getItem() instanceof IAnimatable) {
                    final GeoArmorRenderer<? extends GeoArmorItem> geoArmorRenderer = GeoArmorRenderer
                            .getRenderer(armorItem.getClass(), this.currentEntityBeingRendered);
                    final HumanoidModel<?> armorModel = (HumanoidModel<?>) geoArmorRenderer;

                    if (armorModel != null) {
                        ModelPart sourceLimb = this.getArmorPartForBone(bone.getName(), armorModel);
                        if (sourceLimb != null) {
                            List<Cube> cubeList = sourceLimb.cubes;
                            if (cubeList != null && !cubeList.isEmpty()) {
                                // IMPORTANT: The first cube is used to define the armor part!!
                                stack.scale(-1, -1, 1);
                                stack.pushPose();

                                this.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, true,
                                        boneSlot == EquipmentSlot.CHEST);

                                geoArmorRenderer.setCurrentItem(this.currentEntityBeingRendered, armorForBone,
                                        boneSlot);
                                // Just to be safe, it does some modelprovider stuff in there too
                                geoArmorRenderer.applySlot(boneSlot);
                                this.handleGeoArmorBoneVisibility(geoArmorRenderer, sourceLimb, armorModel, boneSlot);

                                VertexConsumer ivb = ItemRenderer.getArmorFoilBuffer(rtb,
                                        RenderType.armorCutoutNoCull(GeoArmorRenderer
                                                .getRenderer(armorItem.getClass(), this.currentEntityBeingRendered)
                                                .getTextureLocation(armorItem)),
                                        false, armorForBone.hasFoil());

                                geoArmorRenderer.render(this.currentPartialTicks, stack, ivb, packedLightIn);

                                stack.popPose();
                            }
                        }
                    }
                }
                // Normal Armor
                else {
                    final HumanoidModel<?> armorModel = (HumanoidModel<?>) ForgeHooksClient.getArmorModel(currentEntityBeingRendered,
                            armorForBone, boneSlot, boneSlot == EquipmentSlot.LEGS ? DEFAULT_BIPED_ARMOR_MODEL_INNER
                                    : DEFAULT_BIPED_ARMOR_MODEL_OUTER);
                    if (armorModel != null) {
                        ModelPart sourceLimb = this.getArmorPartForBone(bone.getName(), armorModel);
                        if (sourceLimb != null) {
                            List<ModelPart.Cube> cubeList = sourceLimb.cubes;
                            if (cubeList != null && !cubeList.isEmpty()) {
                                // IMPORTANT: The first cube is used to define the armor part!!
                                this.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack);
                                stack.scale(-1, -1, 1);

                                stack.pushPose();

                                ResourceLocation armorResource = this.getArmorResource(currentEntityBeingRendered,
                                        armorForBone, boneSlot, null);

                                this.renderArmorOfItem(armorItem, armorForBone, boneSlot, armorResource, sourceLimb,
                                        stack, packedLightIn, packedOverlayIn);

                                stack.popPose();
                            }
                        }
                    }
                }
            }
            // Head blocks
            else if (armorForBone.getItem() instanceof BlockItem
                    && ((BlockItem) armorForBone.getItem()).getBlock() instanceof AbstractSkullBlock) {
                this.HEAD_QUEUE.add(new Tuple<>(bone, armorForBone));
            }
        }
    }

    protected void setLimbBoneVisible(GeoArmorRenderer<? extends GeoArmorItem> armorRenderer,
                                      ModelPart limb, HumanoidModel<?> armorModel, EquipmentSlot slot) {
        IBone gbHead = armorRenderer.getGeoModelProvider().getBone(armorRenderer.headBone);
        gbHead.setHidden(true);
        IBone gbBody = armorRenderer.getGeoModelProvider().getBone(armorRenderer.bodyBone);
        gbBody.setHidden(true);
        IBone gbArmL = armorRenderer.getGeoModelProvider().getBone(armorRenderer.leftArmBone);
        gbArmL.setHidden(true);
        IBone gbArmR = armorRenderer.getGeoModelProvider().getBone(armorRenderer.rightArmBone);
        gbArmR.setHidden(true);
        IBone gbLegL = armorRenderer.getGeoModelProvider().getBone(armorRenderer.leftLegBone);
        gbLegL.setHidden(true);
        IBone gbLegR = armorRenderer.getGeoModelProvider().getBone(armorRenderer.rightLegBone);
        gbLegR.setHidden(true);
        IBone gbBootL = armorRenderer.getGeoModelProvider().getBone(armorRenderer.leftBootBone);
        gbBootL.setHidden(true);
        IBone gbBootR = armorRenderer.getGeoModelProvider().getBone(armorRenderer.rightBootBone);
        gbBootR.setHidden(true);

        if (limb == armorModel.head || limb == armorModel.hat) {
            gbHead.setHidden(false);
            return;
        }
        if (limb == armorModel.body) {
            gbBody.setHidden(false);
            return;
        }
        if (limb == armorModel.leftArm) {
            gbArmL.setHidden(false);
            return;
        }
        if (limb == armorModel.leftLeg) {
            if (slot == EquipmentSlot.FEET) {
                gbBootL.setHidden(false);
            } else {
                gbLegL.setHidden(false);
            }
            return;
        }
        if (limb == armorModel.rightArm) {
            gbArmR.setHidden(false);
            return;
        }
        if (limb == armorModel.rightLeg) {
            if (slot == EquipmentSlot.FEET) {
                gbBootR.setHidden(false);
            } else {
                gbLegR.setHidden(false);
            }
        }
    }

    @Deprecated(forRemoval = true)
    protected void handleGeoArmorBoneVisibility(GeoArmorRenderer<? extends GeoArmorItem> geoArmorRenderer,
                                                ModelPart sourceLimb, HumanoidModel<?> armorModel, EquipmentSlot slot) {
        setLimbBoneVisible(geoArmorRenderer, sourceLimb, armorModel, slot);
    }

    protected void renderArmorOfItem(ArmorItem armorItem, ItemStack armorForBone, EquipmentSlot boneSlot,
                                     ResourceLocation armorResource, ModelPart sourceLimb, PoseStack stack, int packedLightIn,
                                     int packedOverlayIn) {
        if (armorItem instanceof DyeableArmorItem) {
            int i = ((DyeableArmorItem) armorItem).getColor(armorForBone);
            float r = (float) (i >> 16 & 255) / 255.0F;
            float g = (float) (i >> 8 & 255) / 255.0F;
            float b = (float) (i & 255) / 255.0F;

            renderArmorPart(stack, sourceLimb, packedLightIn, packedOverlayIn, r, g, b, 1, armorForBone, armorResource);
            renderArmorPart(stack, sourceLimb, packedLightIn, packedOverlayIn, 1, 1, 1, 1, armorForBone,
                    getArmorResource(currentEntityBeingRendered, armorForBone, boneSlot, "overlay"));
        } else {
            renderArmorPart(stack, sourceLimb, packedLightIn, packedOverlayIn, 1, 1, 1, 1, armorForBone, armorResource);
        }
    }

    protected void prepareArmorPositionAndScale(GeoBone bone, List<Cube> cubeList, ModelPart sourceLimb,
                                                PoseStack stack) {
        prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, false, false);
    }

    protected void prepareArmorPositionAndScale(GeoBone bone, List<Cube> cubeList, ModelPart sourceLimb,
                                                PoseStack stack, boolean geoArmor, boolean modMatrixRot) {
        GeoCube firstCube = bone.childCubes.get(0);
        final Cube armorCube = cubeList.get(0);

        final float targetSizeX = firstCube.size.x();
        final float targetSizeY = firstCube.size.y();
        final float targetSizeZ = firstCube.size.z();

        final float sourceSizeX = Math.abs(armorCube.maxX - armorCube.minX);
        final float sourceSizeY = Math.abs(armorCube.maxY - armorCube.minY);
        final float sourceSizeZ = Math.abs(armorCube.maxZ - armorCube.minZ);

        float scaleX = targetSizeX / sourceSizeX;
        float scaleY = targetSizeY / sourceSizeY;
        float scaleZ = targetSizeZ / sourceSizeZ;

        // Modify position to move point to correct location, otherwise it will be off
        // when the sizes are different
        // Modifications of X and Z doon't seem to be necessary here, so let's ignore
        // them. For now.
        sourceLimb.setPos(-(bone.getPivotX() - ((bone.getPivotX() * scaleX) - bone.getPivotX()) / scaleX),
                -(bone.getPivotY() - ((bone.getPivotY() * scaleY) - bone.getPivotY()) / scaleY),
                (bone.getPivotZ() - ((bone.getPivotZ() * scaleZ) - bone.getPivotZ()) / scaleZ));

        if (!geoArmor) {
            sourceLimb.xRot = -bone.getRotationX();
            sourceLimb.yRot = -bone.getRotationY();
            sourceLimb.zRot = bone.getRotationZ();
        } else {
            // All those *= 2 calls ARE necessary, otherwise the geo armor will apply
            // rotations twice, so to have it only applied one time in the correct direction
            // we add 2x the negative rotation to it
            float xRot = -bone.getRotationX();
            // xRot *= 1;
            float yRot = -bone.getRotationY();
            // yRot *= 1;
            float zRot = bone.getRotationZ();
            // zRot *= 1;
            /*
             * GeoBone tmpBone = bone.parent; while (tmpBone != null) { xRot -=
             * tmpBone.getRotationX(); yRot -= tmpBone.getRotationY(); zRot +=
             * tmpBone.getRotationZ(); tmpBone = tmpBone.parent; }
             */

            /*
             * if (modMatrixRot) { xRot = (float) Math.toRadians(xRot); yRot = (float)
             * Math.toRadians(yRot); zRot = (float) Math.toRadians(zRot);
             *
             * stack.mulPose(new Quaternion(0, 0, zRot, false)); stack.mulPose(new
             * Quaternion(0, yRot, 0, false)); stack.mulPose(new Quaternion(xRot, 0, 0,
             * false));
             *
             * } else {
             */
            sourceLimb.xRot = xRot;
            sourceLimb.yRot = yRot;
            sourceLimb.zRot = zRot;
            // }
        }

        stack.scale(scaleX, scaleY, scaleZ);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn,
                                  int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.getCurrentRTB() == null) {
            throw new IllegalStateException("RenderTypeBuffer must never be null at this point!");
        }

        if (this.getCurrentModelRenderCycle() != EModelRenderCycle.INITIAL) {
            super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            return;
        }

        this.textureForBone = this.getCurrentModelRenderCycle() != EModelRenderCycle.INITIAL ? null
                : this.getTextureForBone(bone.getName(), this.currentEntityBeingRendered);
        boolean customTextureMarker = this.textureForBone != null;
        ResourceLocation currentTexture = this.getTextureLocation(this.currentEntityBeingRendered);

        final RenderType rt = customTextureMarker
                ? this.getRenderTypeForBone(bone, this.currentEntityBeingRendered, this.currentPartialTicks, stack,
                bufferIn, this.getCurrentRTB(), packedLightIn, this.textureForBone)
                : this.getRenderType(this.currentEntityBeingRendered, this.currentPartialTicks, stack,
                this.getCurrentRTB(), bufferIn, packedLightIn, currentTexture);
        bufferIn = this.getCurrentRTB().getBuffer(rt);

        if (this.getCurrentModelRenderCycle() == EModelRenderCycle.INITIAL) {
            stack.pushPose();

            // Render armor
            if (this.isArmorBone(bone)) {
                stack.pushPose();
                this.handleArmorRenderingForBone(bone, stack, bufferIn, packedLightIn, packedOverlayIn, currentTexture);
                stack.popPose();

                // Reset buffer...
                bufferIn = this.getCurrentRTB().getBuffer(rt);
            } else {
                ItemStack boneItem = this.getHeldItemForBone(bone.getName(), this.currentEntityBeingRendered);
                BlockState boneBlock = this.getHeldBlockForBone(bone.getName(), this.currentEntityBeingRendered);
                if (boneItem != null || boneBlock != null) {
                    stack.pushPose();
                    this.handleItemAndBlockBoneRendering(stack, bone, boneItem, boneBlock, packedLightIn, packedOverlayIn);
                    stack.popPose();

                    bufferIn = this.getCurrentRTB().getBuffer(rt);
                }
            }
            stack.popPose();
        }
        this.customBoneSpecificRenderingHook(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue,
                alpha, customTextureMarker, currentTexture);

        ////////////////////////////////////
        stack.pushPose();
        RenderUtils.prepMatrixForBone(stack, bone);
        super.renderCubesOfBone(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        //////////////////////////////////////
        // reset buffer
        if (customTextureMarker) {
            bufferIn = this.getCurrentRTB().getBuffer(this.getRenderType(currentEntityBeingRendered,
                    this.currentPartialTicks, stack, this.getCurrentRTB(), bufferIn, packedLightIn, currentTexture));
            // Reset the marker...
            this.textureForBone = null;
        }
        //////////////////////////////////////
        super.renderChildBones(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        stack.popPose();
        ////////////////////////////////////
    }

    /*
     * Gets called after armor and item rendering but in every render cycle. This
     * serves as a hook for modders to include their own bone specific rendering
     */
    protected void customBoneSpecificRenderingHook(GeoBone bone, PoseStack poseStack, VertexConsumer buffer,
                                                   int packedLight, int packedOverlay, float red, float green, float blue, float alpha,
                                                   boolean customTextureMarker, ResourceLocation currentTexture) {
    }

    protected void handleItemAndBlockBoneRendering(PoseStack poseStack, GeoBone bone, @Nullable ItemStack boneItem,
                                                   @Nullable BlockState boneBlock, int packedLight, int packedOverlay) {
        RenderUtils.prepMatrixForBone(poseStack, bone);
        RenderUtils.translateAndRotateMatrixForBone(poseStack, bone);

        if (boneItem != null) {

            poseStack.pushPose();

            if (bone.getName().equals("rightitem")) {
                poseStack.mulPose(Vector3f.XN.rotationDegrees(90.0F));
                poseStack.translate(0.025D, 0.105D, -0.125D);
            } else if (bone.getName().equals("leftitem")) {
                poseStack.mulPose(Vector3f.XN.rotationDegrees(90.0F));
                poseStack.translate(-0.025D, 0.105D, -0.125D);
            }

            preRenderItem(poseStack, boneItem, bone.getName(), this.currentEntityBeingRendered, bone);
            renderItemStack(poseStack, getCurrentRTB(), packedLight, boneItem, bone.getName());
            postRenderItem(poseStack, boneItem, bone.getName(), this.currentEntityBeingRendered, bone);

            poseStack.popPose();
        }

        if (boneBlock != null) {
            preRenderBlock(poseStack, boneBlock, bone.getName(), this.currentEntityBeingRendered);
            renderBlock(poseStack, getCurrentRTB(), packedLight, boneBlock);
            postRenderBlock(poseStack, boneBlock, bone.getName(), this.currentEntityBeingRendered);
        }
    }

    protected void renderItemStack(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack,
                                   String boneName) {
        Minecraft.getInstance().getItemRenderer().renderStatic(this.currentEntityBeingRendered, stack,
                getCameraTransformForItemAtBone(stack, boneName), boneName.equals("leftitem"), poseStack, bufferSource, null, packedLight,
                LivingEntityRenderer.getOverlayCoords(this.currentEntityBeingRendered, 0.0F),
                currentEntityBeingRendered.getId());
    }

    protected RenderType getRenderTypeForBone(GeoBone bone, U animatable, float partialTick,
                                              PoseStack poseStack, VertexConsumer buffer, MultiBufferSource bufferSource, int packedLight, ResourceLocation texture) {
        return getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }

    // Internal use only. Basically renders the passed "part" of the armor model on
    // a pre-setup location
    protected void renderArmorPart(PoseStack poseStack, ModelPart sourceLimb, int packedLight, int packedOverlay,
                                   float red, float green, float blue, float alpha, ItemStack armorStack, ResourceLocation texture) {
        VertexConsumer buffer = ItemRenderer.getArmorFoilBuffer(getCurrentRTB(), RenderType.armorCutoutNoCull(texture), false,
                armorStack.hasFoil());

        sourceLimb.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    /**
     * Return a specific texture for a given bone, or null to use the existing texture
     * @param boneName The name of the bone to be rendered
     * @param animatable The animatable instance
     * @return The specified texture path, or null if no override
     */
    @Nullable
    protected abstract ResourceLocation getTextureForBone(String boneName, U animatable);

    /*
     * Return null if there is no item
     */
    @Nullable
    protected ItemStack getHeldItemForBone(String boneName, U animatable) {
        if (boneName.equals("rightitem")) {
            return !animatable.isLeftHanded() ? animatable.getMainHandItem() : animatable.getOffhandItem();
        } else if (boneName.equals("leftitem")) {
            return animatable.isLeftHanded() ? animatable.getMainHandItem() : animatable.getOffhandItem();
        }

        return null;
    }

    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        if (boneName.equals("rightitem")) {
            return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
        } else if (boneName.equals("leftitem")) {
            return ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
        }

        return ItemTransforms.TransformType.NONE;
    }

    /*
     * Return null if there is no held block
     */
    @Nullable
    protected abstract BlockState getHeldBlockForBone(String boneName, U animatable);

    protected abstract void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, U animatable,
                                          IBone bone);

    protected abstract void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, U animatable);

    protected abstract void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, U animatable,
                                           IBone bone);

    protected abstract void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, U animatable);

    /*
     * Return null, if there is no armor on this bone
     *
     */
    @Nullable
    protected ItemStack getArmorForBone(String boneName, U animatable) {
        EquipmentSlot slot = this.getEquipmentSlotForArmorBone(boneName, animatable);

        if (boneName.startsWith("armor") && slot != null) {
            return animatable.getItemBySlot(slot);
        }

        return null;
    }

    @Nullable
    protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, U animatable) {
        return switch (boneName) {
            case "armorhead" -> EquipmentSlot.HEAD;
            case "armorbody", "armorrightarm", "armorleftarm" -> EquipmentSlot.CHEST;
            case "armorpelvis", "armorrightleg", "armorleftleg" -> EquipmentSlot.LEGS;
            case "armorrightfoot", "armorleftfoot" -> EquipmentSlot.FEET;
            default -> null;
        };
    }

    @Nullable
    protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
        return switch (name) {
            case "armorhead" -> armorModel.head;
            case "armorbody", "armorpelvis" -> armorModel.body;
            case "armorrightarm" -> armorModel.rightArm;
            case "armorleftarm" -> armorModel.leftArm;
            case "armorrightleg", "armorrightfoot" -> armorModel.rightLeg;
            case "armorleftleg", "armorleftfoot" -> armorModel.leftLeg;
            default -> null;
        };
    }

    protected ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nonnull String type) {
        String path = ((ArmorItem) stack.getItem()).getMaterial().getName();
        String domain = "minecraft";
        String[] materialNameSplit = path.split(":", 2);

        if (materialNameSplit.length > 1) {
            domain = materialNameSplit[0];
            path = materialNameSplit[1];
        }

        String texture = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, path,
                (slot == EquipmentSlot.LEGS ? 2 : 1), type == null ? "" : String.format("_%s", type));

        texture = ForgeHooksClient.getArmorTexture(entity, stack, texture, slot, type);

        return ARMOR_TEXTURE_RES_MAP.computeIfAbsent(texture, ResourceLocation::new);
    }

    // Auto UV recalculations for texturePerBone
    @Override
    public void createVerticesOfQuad(GeoQuad quad, Matrix4f matrix4f, Vector3f normal, VertexConsumer bufferIn,
                                     int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        // If no textureForBone is used we can proceed normally
        if (this.textureForBone == null) {
            super.createVerticesOfQuad(quad, matrix4f, normal, bufferIn, packedLightIn, packedOverlayIn, red, green,
                    blue, alpha);
        }
        Tuple<Integer, Integer> tfbSize = this.getOrCreateTextureSize(this.textureForBone);
        Tuple<Integer, Integer> textureSize = this
                .getOrCreateTextureSize(this.getTextureLocation(this.currentEntityBeingRendered));

        if (tfbSize == null || textureSize == null) {
            super.createVerticesOfQuad(quad, matrix4f, normal, bufferIn, packedLightIn, packedOverlayIn, red, green,
                    blue, alpha);
            // Exit here, cause texture sizes are null
            return;
        }

        for (GeoVertex vertex : quad.vertices) {
            Vector4f vector4f = new Vector4f(vertex.position.x(), vertex.position.y(), vertex.position.z(), 1.0F);
            vector4f.transform(matrix4f);

            // Recompute the UV coordinates to the texture override
            float texU = (vertex.textureU * textureSize.getA()) / tfbSize.getA();
            float texV = (vertex.textureV * textureSize.getB()) / tfbSize.getB();

            bufferIn.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, texU, texV,
                    packedOverlayIn, packedLightIn, normal.x(), normal.y(), normal.z());
        }
    }

    protected void renderBlock(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                               BlockState state) {
        if (state.getRenderShape() != RenderShape.MODEL)
            return;

        poseStack.pushPose();
        poseStack.translate(-0.25f, -0.25f, -0.25f);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, packedLight,
                OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    protected float getDeathMaxRotation(LivingEntity entity) {
        return 0;
    }

    /**
     * Use {@link RenderUtils#getTextureDimensions(ResourceLocation)}<br>
     * Remove in 1.20+
     */
    @Deprecated(forRemoval = true)
    protected Tuple<Integer, Integer> getSizeOfTexture(ResourceLocation texture) {
        IntIntPair dimensions = RenderUtils.getTextureDimensions(texture);

        return dimensions == null ? null : new Tuple<>(dimensions.firstInt(), dimensions.secondInt());
    }

    /**
     * Use {@link RenderUtils#translateAndRotateMatrixForBone(PoseStack, GeoBone)}<br>
     * Remove in 1.20+
     */
    @Deprecated(forRemoval = true)
    protected void moveAndRotateMatrixToMatchBone(PoseStack stack, GeoBone bone) {
        RenderUtils.translateAndRotateMatrixForBone(stack, bone);
    }

    @Deprecated(forRemoval = true)
    protected Tuple<Integer, Integer> getOrCreateTextureSize(ResourceLocation texture) {
        return TEXTURE_SIZE_CACHE.computeIfAbsent(texture, key -> getSizeOfTexture(texture));
    }
}
