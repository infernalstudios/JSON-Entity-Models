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

package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class HumanoidAnimatedGeoModel extends HeadTurningAnimatedGeoModel {
    public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
    public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;

    public HumanoidAnimatedGeoModel(String entityName) {
        super(entityName);
    }

    @Override
    public void setCustomAnimations(ReplacedEntityBase animatable, int instanceId, AnimationEvent animationEvent) {
        if (this.getAnimationResource(animatable) == null) {
            LivingEntity livingEntity = this.getCurrentEntity();

            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

            IBone head = this.getAnimationProcessor().getBone("head");
            IBone body = this.getAnimationProcessor().getBone("body");
            IBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            IBone leftArm = this.getAnimationProcessor().getBone("leftarm");
            IBone rightLeg = this.getAnimationProcessor().getBone("rightleg");
            IBone leftLeg = this.getAnimationProcessor().getBone("leftleg");

            boolean flag = livingEntity.getFallFlyingTicks() > 4;
            boolean flag1 = livingEntity.isVisuallySwimming();

            float swimAmount = livingEntity.getSwimAmount(animationEvent.getPartialTick());

            head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
            if (flag) {
                head.setRotationX((-(float)Math.PI / 4F));
            } else if (swimAmount > 0.0F) {
                if (flag1) {
                    head.setRotationX(this.rotlerpRad(swimAmount, head.getRotationX(), (-(float)Math.PI / 4F)));
                } else {
                    head.setRotationX(this.rotlerpRad(swimAmount, head.getRotationX(), extraData.headPitch) * ((float)Math.PI / 180F));
                }
            } else {
                head.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));
            }

            body.setRotationY(0.0F);
            float f = 1.0F;
            if (flag) {
                f = (float)livingEntity.getDeltaMovement().lengthSqr();
                f /= 0.2F;
                f *= f * f;
            }

            if (f < 1.0F) {
                f = 1.0F;
            }

            rightArm.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F + (float)Math.PI) * 2.0F * animationEvent.getLimbSwingAmount() * 0.5F / f);
            leftArm.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F) * 2.0F * animationEvent.getLimbSwingAmount() * 0.5F / f);
            rightArm.setRotationZ(0.0F);
            leftArm.setRotationZ(0.0F);
            rightLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F) * 1.4F * animationEvent.getLimbSwingAmount() / f);
            leftLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F + (float)Math.PI) * 1.4F * animationEvent.getLimbSwingAmount() / f);
            rightLeg.setRotationY(0.0F);
            leftLeg.setRotationY(0.0F);
            rightLeg.setRotationZ(0.0F);
            leftLeg.setRotationZ(0.0F);

            boolean riding = livingEntity.isPassenger() && (livingEntity.getVehicle() != null && livingEntity.getVehicle().shouldRiderSit());

            if (riding) {
                rightArm.setRotationX(rightArm.getRotationX() - (float)Math.PI / 5F);
                leftArm.setRotationX(leftArm.getRotationX() - (float)Math.PI / 5F);
                rightLeg.setRotationX(-1.4137167F);
                rightLeg.setRotationY(((float)Math.PI / 10F));
                rightLeg.setRotationZ(0.07853982F);
                leftLeg.setRotationX(-1.4137167F);
                leftLeg.setRotationY((-(float)Math.PI / 10F));
                leftLeg.setRotationZ(-0.07853982F);
            }

            rightArm.setRotationY(0.0F);
            leftArm.setRotationY(0.0F);
            boolean flag2 = livingEntity.getMainArm() == HumanoidArm.RIGHT;
            if (livingEntity.isUsingItem()) {
                boolean flag3 = livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND;
                if (flag3 == flag2) {
                    this.poseRightArm(livingEntity, rightArm, leftArm, head);
                } else {
                    this.poseLeftArm(livingEntity, rightArm, leftArm, head);
                }
            } else {
                boolean flag4 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
                if (flag2 != flag4) {
                    this.poseLeftArm(livingEntity, rightArm, leftArm, head);
                    this.poseRightArm(livingEntity, rightArm, leftArm, head);
                } else {
                    this.poseRightArm(livingEntity, rightArm, leftArm, head);
                    this.poseLeftArm(livingEntity, rightArm, leftArm, head);
                }
            }

            this.setupAttackAnimation(livingEntity, animationEvent.getPartialTick(), rightArm, leftArm, body, head, livingEntity.getAttackAnim(animationEvent.getPartialTick()));

            boolean crouching = livingEntity.isCrouching();
            if (crouching) {
                body.setRotationX(0.5F);
                rightArm.setRotationX(rightArm.getRotationX() + 0.4F);
                leftArm.setRotationX(leftArm.getRotationX() + 0.4F);
            } else {
                body.setRotationX(0.0F);
            }

            if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
                this.bobBone(rightArm, animationEvent.getPartialTick(), 1.0F);
            }

            if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
                this.bobBone(leftArm, animationEvent.getPartialTick(), -1.0F);
            }

            if (swimAmount > 0.0F) {
                float attackTime = livingEntity.getAttackAnim(animationEvent.getPartialTick());

                float f5 = animationEvent.getLimbSwing() % 26.0F;
                HumanoidArm humanoidarm = this.getAttackArm(livingEntity);
                float f1 = humanoidarm == HumanoidArm.RIGHT && attackTime > 0.0F ? 0.0F : swimAmount;
                float f2 = humanoidarm == HumanoidArm.LEFT && attackTime > 0.0F ? 0.0F : swimAmount;
                if (!livingEntity.isUsingItem()) {
                    if (f5 < 14.0F) {
                        leftArm.setRotationX(this.rotlerpRad(f2, leftArm.getRotationX(), 0.0F));
                        rightArm.setRotationX(Mth.lerp(f1, rightArm.getRotationX(), 0.0F));
                        leftArm.setRotationY(this.rotlerpRad(f2, leftArm.getRotationY(), (float)Math.PI));
                        rightArm.setRotationY(Mth.lerp(f1, rightArm.getRotationY(), (float)Math.PI));
                        leftArm.setRotationZ(this.rotlerpRad(f2, leftArm.getRotationZ(), (float)Math.PI + 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F)));
                        rightArm.setRotationZ(Mth.lerp(f1, rightArm.getRotationZ(), (float)Math.PI - 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F)));
                    } else if (f5 >= 14.0F && f5 < 22.0F) {
                        float f6 = (f5 - 14.0F) / 8.0F;
                        leftArm.setRotationX(this.rotlerpRad(f2, leftArm.getRotationX(), ((float)Math.PI / 2F) * f6));
                        rightArm.setRotationX(Mth.lerp(f1, rightArm.getRotationX(), ((float)Math.PI / 2F) * f6));
                        leftArm.setRotationY(this.rotlerpRad(f2, leftArm.getRotationY(), (float)Math.PI));
                        rightArm.setRotationY(Mth.lerp(f1, rightArm.getRotationY(), (float)Math.PI));
                        leftArm.setRotationZ(this.rotlerpRad(f2, leftArm.getRotationZ(), 5.012389F - 1.8707964F * f6));
                        rightArm.setRotationZ(Mth.lerp(f1, rightArm.getRotationZ(), 1.2707963F + 1.8707964F * f6));
                    } else if (f5 >= 22.0F && f5 < 26.0F) {
                        float f3 = (f5 - 22.0F) / 4.0F;
                        leftArm.setRotationX(this.rotlerpRad(f2, leftArm.getRotationX(), ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f3));
                        rightArm.setRotationX(Mth.lerp(f1, rightArm.getRotationX(), ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f3));
                        leftArm.setRotationY(this.rotlerpRad(f2, leftArm.getRotationY(), (float)Math.PI));
                        rightArm.setRotationY(Mth.lerp(f1, rightArm.getRotationY(), (float)Math.PI));
                        leftArm.setRotationZ(this.rotlerpRad(f2, leftArm.getRotationZ(), (float)Math.PI));
                        rightArm.setRotationZ(Mth.lerp(f1, rightArm.getRotationZ(), (float)Math.PI));
                    }
                }

                leftLeg.setRotationX(Mth.lerp(swimAmount, leftLeg.getRotationX(), 0.3F * Mth.cos(animationEvent.getLimbSwing() * 0.33333334F + (float)Math.PI)));
                rightLeg.setRotationX(Mth.lerp(swimAmount, rightLeg.getRotationX(), 0.3F * Mth.cos(animationEvent.getLimbSwing() * 0.33333334F)));
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationEvent);
        }
    }

    public void bobBone(IBone bone, float partialTick, float multiplier) {
        bone.setRotationZ(bone.getRotationZ() + multiplier * (Mth.cos(partialTick * 0.09F) * 0.05F + 0.05F));
        bone.setRotationX(bone.getRotationX() + multiplier * Mth.sin(partialTick * 0.067F) * 0.05F);
    }

    public void bobArms(IBone rightArm, IBone leftArm, float partialTicks) {
        this.bobBone(rightArm, partialTicks, 1.0F);
        this.bobBone(leftArm, partialTicks, -1.0F);
    }

    private HumanoidArm getAttackArm(LivingEntity entity) {
        HumanoidArm humanoidarm = entity.getMainArm();
        return entity.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }

    private float quadraticArmUpdate(float limbSwingFactor) {
        return -65.0F * limbSwingFactor + limbSwingFactor * limbSwingFactor;
    }

    private void poseRightArm(LivingEntity livingEntity, IBone rightArm, IBone leftArm, IBone head) {
        switch (this.rightArmPose) {
            case EMPTY:
                rightArm.setRotationY(0.0F);
                break;
            case BLOCK:
                rightArm.setRotationX(rightArm.getRotationX() * 0.5F - 0.9424779F);
                rightArm.setRotationY(-(float)Math.PI / 6F);
                break;
            case ITEM:
                rightArm.setRotationX(rightArm.getRotationX() * 0.5F - ((float)Math.PI / 10F));
                rightArm.setRotationY(0.0F);
                break;
            case THROW_SPEAR:
                rightArm.setRotationX(rightArm.getRotationX() * 0.5F - (float)Math.PI);
                rightArm.setRotationY(0.0F);
                break;
            case BOW_AND_ARROW:
                rightArm.setRotationY(0.1F + head.getRotationY());
                leftArm.setRotationY(-0.1F - head.getRotationY() - 0.4F);
                rightArm.setRotationX((float)Math.PI / 2F + head.getRotationX());
                leftArm.setRotationX((float)Math.PI / 2F + head.getRotationX());
                break;
            case CROSSBOW_CHARGE:
                this.animateCrossbowCharge(rightArm, leftArm, livingEntity, true);
                break;
            case CROSSBOW_HOLD:
                this.animateCrossbowHold(rightArm, leftArm, head, true);
                break;
            case SPYGLASS:
                rightArm.setRotationX(Mth.clamp(head.getRotationX() - 1.9198622F - (livingEntity.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F));
                rightArm.setRotationY(head.getRotationY() - 0.2617994F);
                break;
            case TOOT_HORN:
                rightArm.setRotationX(Mth.clamp(head.getRotationX(), -1.2F, 1.2F) - 1.4835298F);
                rightArm.setRotationY(head.getRotationY() - ((float)Math.PI / 6F));
            default:
        }

    }

    private void poseLeftArm(LivingEntity livingEntity, IBone rightArm, IBone leftArm, IBone head) {
        switch (this.leftArmPose) {
            case EMPTY:
                break;
            case BLOCK:
                leftArm.setRotationX(leftArm.getRotationX() * 0.5F - 0.9424779F);
                leftArm.setRotationY(((float)Math.PI / 6F));
                break;
            case ITEM:
                leftArm.setRotationX(leftArm.getRotationX() * 0.5F - ((float)Math.PI / 10F));
                leftArm.setRotationY(0.0F);
                break;
            case THROW_SPEAR:
                leftArm.setRotationX(leftArm.getRotationX() * 0.5F - (float)Math.PI);
                leftArm.setRotationY(0.0F);
                break;
            case BOW_AND_ARROW:
                rightArm.setRotationY(0.1F + head.getRotationY() - 0.4F);
                leftArm.setRotationY(0.1F + head.getRotationY());
                rightArm.setRotationX((float)Math.PI / 2F + head.getRotationX());
                leftArm.setRotationX((float)Math.PI / 2F + head.getRotationX());
                break;
            case CROSSBOW_CHARGE:
                this.animateCrossbowCharge(rightArm, leftArm, livingEntity, false);
                break;
            case CROSSBOW_HOLD:
                this.animateCrossbowHold(rightArm, leftArm, head, false);
                break;
            case SPYGLASS:
                leftArm.setRotationX(Mth.clamp(head.getRotationX() - 1.9198622F - (livingEntity.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F));
                leftArm.setRotationY(head.getRotationY() + 0.2617994F);
                break;
            case TOOT_HORN:
                leftArm.setRotationX(Mth.clamp(head.getRotationX(), -1.2F, 1.2F) - 1.4835298F);
                leftArm.setRotationY(head.getRotationY() + ((float)Math.PI / 6F));
            default:
        }

    }

    protected void setupAttackAnimation(LivingEntity livingEntity, float partialTick, IBone rightArm, IBone leftArm, IBone body, IBone head, float attackTime) {
        if (!(attackTime <= 0.0F)) {
            HumanoidArm humanoidarm = this.getAttackArm(livingEntity);
            IBone arm = this.getArm(humanoidarm, rightArm, leftArm);
            float f = attackTime;
            body.setRotationY(Mth.sin(Mth.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F);
            if (humanoidarm == HumanoidArm.LEFT) {
                body.setRotationY(-body.getRotationY());
            }

            rightArm.setRotationY(rightArm.getRotationY() + body.getRotationY());
            leftArm.setRotationY(leftArm.getRotationY() + body.getRotationY());
            leftArm.setRotationX(leftArm.getRotationX() + body.getRotationY());
            f = 1.0F - attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float)Math.PI);
            float f2 = Mth.sin(attackTime * (float)Math.PI) * -(head.getRotationX() - 0.7F) * 0.75F;
            arm.setRotationX(arm.getRotationX() - f1 * 1.2F + f2);
            arm.setRotationY(arm.getRotationY() + body.getRotationY() * 2.0F);
            arm.setRotationZ(arm.getRotationZ() + Mth.sin(attackTime * (float)Math.PI) * -0.4F);
        }
    }

    protected float rotlerpRad(float p_102836_, float p_102837_, float p_102838_) {
        float f = (p_102838_ - p_102837_) % ((float)Math.PI * 2F);
        if (f < -(float)Math.PI) {
            f += ((float)Math.PI * 2F);
        }

        if (f >= (float)Math.PI) {
            f -= ((float)Math.PI * 2F);
        }

        return p_102837_ + p_102836_ * f;
    }

    protected IBone getArm(HumanoidArm arm, IBone rightArm, IBone leftArm) {
        return arm == HumanoidArm.LEFT ? leftArm : rightArm;
    }

    public void animateCrossbowHold(IBone rightArm, IBone leftArm, IBone head, boolean rightHanded) {
        IBone mainhand = rightHanded ? rightArm : leftArm;
        IBone offhand = rightHanded ? leftArm : rightArm;

        mainhand.setRotationY((rightHanded ? -0.3F : 0.3F) + head.getRotationY());
        offhand.setRotationY((rightHanded ? 0.6F : -0.6F) + head.getRotationY());
        mainhand.setRotationX((-(float)Math.PI / 2F) + head.getRotationX() + 0.1F);
        offhand.setRotationX(-1.5F + head.getRotationX());
    }

    public void animateCrossbowCharge(IBone rightArm, IBone leftArm, LivingEntity entity, boolean rightHanded) {
        IBone mainhand = rightHanded ? rightArm : leftArm;
        IBone offhand = rightHanded ? leftArm : rightArm;
        mainhand.setRotationX(-0.97079635F);
        offhand.setRotationX(mainhand.getRotationX());
        float f = (float) CrossbowItem.getChargeDuration(entity.getUseItem());
        float f1 = Mth.clamp((float)entity.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;
        offhand.setRotationX(Mth.lerp(f2, offhand.getRotationX(), (-(float)Math.PI / 2F)));
    }
}
