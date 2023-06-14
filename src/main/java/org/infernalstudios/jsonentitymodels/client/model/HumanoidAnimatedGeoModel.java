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
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class HumanoidAnimatedGeoModel extends HeadTurningAnimatedGeoModel {
    public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
    public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;

    public HumanoidAnimatedGeoModel(String entityName) {
        super(entityName);
    }

    public HumanoidAnimatedGeoModel(String namespace, String entityName) {
        super(namespace, entityName);
    }

    @Override
    public void setCustomAnimations(GeoReplacedEntity animatable, long instanceId, AnimationState animationState) {
        if (this.getAnimationResource(animatable) == null) {
            LivingEntity livingEntity = this.getCurrentEntity();

            EntityModelData extraData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            CoreGeoBone head = this.getAnimationProcessor().getBone("head");
            CoreGeoBone body = this.getAnimationProcessor().getBone("body");
            CoreGeoBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            CoreGeoBone leftArm = this.getAnimationProcessor().getBone("leftarm");
            CoreGeoBone rightLeg = this.getAnimationProcessor().getBone("rightleg");
            CoreGeoBone leftLeg = this.getAnimationProcessor().getBone("leftleg");

            boolean flag = livingEntity.getFallFlyingTicks() > 4;
            boolean flag1 = livingEntity.isVisuallySwimming();

            float swimAmount = livingEntity.getSwimAmount(animationState.getPartialTick());

            if (head != null) {
                head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
                if (flag) {
                    head.setRotX((-(float) Math.PI / 4F));
                } else if (swimAmount > 0.0F) {
                    if (flag1) {
                        head.setRotX(this.rotlerpRad(swimAmount, head.getRotX(), (-(float) Math.PI / 4F)));
                    } else {
                        head.setRotX(this.rotlerpRad(swimAmount, head.getRotX(), extraData.headPitch()) * ((float) Math.PI / 180F));
                    }
                } else {
                    head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
                }
            }

            if (body != null) {
                body.setRotY(0.0F);
            }

            float f = 1.0F;
            if (flag) {
                f = (float)livingEntity.getDeltaMovement().lengthSqr();
                f /= 0.2F;
                f *= f * f;
            }

            if (f < 1.0F) {
                f = 1.0F;
            }

            if (rightArm != null) {
                rightArm.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F + (float) Math.PI) * 2.0F * animationState.getLimbSwingAmount() * 0.5F / f);
                rightArm.setRotZ(0.0F);
            }

            if (leftArm != null) {
                leftArm.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F) * 2.0F * animationState.getLimbSwingAmount() * 0.5F / f);
                leftArm.setRotZ(0.0F);
            }

            if (rightLeg != null) {
                rightLeg.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F) * 1.4F * animationState.getLimbSwingAmount() / f);
                rightLeg.setRotY(0.0F);
                rightLeg.setRotZ(0.0F);
            }

            if (leftLeg != null) {
                leftLeg.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F + (float) Math.PI) * 1.4F * animationState.getLimbSwingAmount() / f);
                leftLeg.setRotY(0.0F);
                leftLeg.setRotZ(0.0F);
            }

            boolean riding = livingEntity.isPassenger() && (livingEntity.getVehicle() != null && livingEntity.getVehicle().shouldRiderSit());

            if (riding) {
                if (rightArm != null) {
                    rightArm.setRotX(rightArm.getRotX() - (float) Math.PI / 5F);
                }

                if (leftArm != null) {
                    leftArm.setRotX(leftArm.getRotX() - (float) Math.PI / 5F);
                }

                if (rightLeg != null) {
                    rightLeg.setRotX(-1.4137167F);
                    rightLeg.setRotY(((float) Math.PI / 10F));
                    rightLeg.setRotZ(0.07853982F);
                }

                if (leftLeg != null) {
                    leftLeg.setRotX(-1.4137167F);
                    leftLeg.setRotY((-(float) Math.PI / 10F));
                    leftLeg.setRotZ(-0.07853982F);
                }
            }

            if (rightArm != null) {
                rightArm.setRotY(0.0F);
            }

            if (leftArm != null) {
                leftArm.setRotY(0.0F);
            }

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

            this.setupAttackAnimation(livingEntity, animationState.getPartialTick(), rightArm, leftArm, body, head, livingEntity.getAttackAnim(animationState.getPartialTick()));

            boolean crouching = livingEntity.isCrouching();
            if (crouching) {
                if (body != null) {
                    body.setRotX(0.5F);
                }

                if (rightArm != null) {
                    rightArm.setRotX(rightArm.getRotX() + 0.4F);
                }

                if (leftArm != null) {
                    leftArm.setRotX(leftArm.getRotX() + 0.4F);
                }
            } else if (body != null) {
                body.setRotX(0.0F);
            }

            if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
                this.bobBone(rightArm, animationState.getPartialTick(), 1.0F);
            }

            if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
                this.bobBone(leftArm, animationState.getPartialTick(), -1.0F);
            }

            if (swimAmount > 0.0F) {
                float attackTime = livingEntity.getAttackAnim(animationState.getPartialTick());

                float f5 = animationState.getLimbSwing() % 26.0F;
                HumanoidArm humanoidarm = this.getAttackArm(livingEntity);
                float f1 = humanoidarm == HumanoidArm.RIGHT && attackTime > 0.0F ? 0.0F : swimAmount;
                float f2 = humanoidarm == HumanoidArm.LEFT && attackTime > 0.0F ? 0.0F : swimAmount;
                if (!livingEntity.isUsingItem()) {
                    if (f5 < 14.0F) {
                        if (leftArm != null) {
                            leftArm.setRotX(this.rotlerpRad(f2, leftArm.getRotX(), 0.0F));
                            leftArm.setRotY(this.rotlerpRad(f2, leftArm.getRotY(), (float) Math.PI));
                            leftArm.setRotZ(this.rotlerpRad(f2, leftArm.getRotZ(), (float) Math.PI + 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F)));
                        }

                        if (rightArm != null) {
                            rightArm.setRotX(Mth.lerp(f1, rightArm.getRotX(), 0.0F));
                            rightArm.setRotY(Mth.lerp(f1, rightArm.getRotY(), (float) Math.PI));
                            rightArm.setRotZ(Mth.lerp(f1, rightArm.getRotZ(), (float) Math.PI - 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F)));
                        }
                    } else if (f5 >= 14.0F && f5 < 22.0F) {
                        float f6 = (f5 - 14.0F) / 8.0F;

                        if (leftArm != null) {
                            leftArm.setRotX(this.rotlerpRad(f2, leftArm.getRotX(), ((float) Math.PI / 2F) * f6));
                            leftArm.setRotY(this.rotlerpRad(f2, leftArm.getRotY(), (float) Math.PI));
                            leftArm.setRotZ(this.rotlerpRad(f2, leftArm.getRotZ(), 5.012389F - 1.8707964F * f6));
                        }

                        if (rightArm != null) {
                            rightArm.setRotX(Mth.lerp(f1, rightArm.getRotX(), ((float) Math.PI / 2F) * f6));
                            rightArm.setRotY(Mth.lerp(f1, rightArm.getRotY(), (float) Math.PI));
                            rightArm.setRotZ(Mth.lerp(f1, rightArm.getRotZ(), 1.2707963F + 1.8707964F * f6));
                        }
                    } else if (f5 >= 22.0F && f5 < 26.0F) {
                        float f3 = (f5 - 22.0F) / 4.0F;
                        if (leftArm != null) {
                            leftArm.setRotX(this.rotlerpRad(f2, leftArm.getRotX(), ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f3));
                            leftArm.setRotY(this.rotlerpRad(f2, leftArm.getRotY(), (float) Math.PI));
                            leftArm.setRotZ(this.rotlerpRad(f2, leftArm.getRotZ(), (float) Math.PI));
                        }

                        if (rightArm != null) {
                            rightArm.setRotX(Mth.lerp(f1, rightArm.getRotX(), ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f3));
                            rightArm.setRotY(Mth.lerp(f1, rightArm.getRotY(), (float) Math.PI));
                            rightArm.setRotZ(Mth.lerp(f1, rightArm.getRotZ(), (float) Math.PI));
                        }
                    }
                }

                if (leftLeg != null) {
                    leftLeg.setRotX(Mth.lerp(swimAmount, leftLeg.getRotX(), 0.3F * Mth.cos(animationState.getLimbSwing() * 0.33333334F + (float) Math.PI)));
                }

                if (rightLeg != null) {
                    rightLeg.setRotX(Mth.lerp(swimAmount, rightLeg.getRotX(), 0.3F * Mth.cos(animationState.getLimbSwing() * 0.33333334F)));
                }
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationState);
        }
    }

    public void bobBone(CoreGeoBone bone, float partialTick, float multiplier) {
        if (bone != null) {
            bone.setRotZ(bone.getRotZ() + multiplier * (Mth.cos(partialTick * 0.09F) * 0.05F + 0.05F));
            bone.setRotX(bone.getRotX() + multiplier * Mth.sin(partialTick * 0.067F) * 0.05F);
        }
    }

    public void bobArms(CoreGeoBone rightArm, CoreGeoBone leftArm, float partialTicks) {
        if (rightArm != null) {
            this.bobBone(rightArm, partialTicks, 1.0F);
        }

        if (leftArm != null) {
            this.bobBone(leftArm, partialTicks, -1.0F);
        }
    }

    private HumanoidArm getAttackArm(LivingEntity entity) {
        HumanoidArm humanoidarm = entity.getMainArm();
        return entity.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }

    private float quadraticArmUpdate(float limbSwingFactor) {
        return -65.0F * limbSwingFactor + limbSwingFactor * limbSwingFactor;
    }

    private void poseRightArm(LivingEntity livingEntity, CoreGeoBone rightArm, CoreGeoBone leftArm, CoreGeoBone head) {
        switch (this.rightArmPose) {
            case EMPTY:
                if (rightArm != null) {
                    rightArm.setRotY(0.0F);
                }
                break;
            case BLOCK:
                if (rightArm != null) {
                    rightArm.setRotX(rightArm.getRotX() * 0.5F - 0.9424779F);
                    rightArm.setRotY(-(float) Math.PI / 6F);
                }
                break;
            case ITEM:
                if (rightArm != null) {
                    rightArm.setRotX(rightArm.getRotX() * 0.5F - ((float) Math.PI / 10F));
                    rightArm.setRotY(0.0F);
                }
                break;
            case THROW_SPEAR:
                if (rightArm != null) {
                    rightArm.setRotX(rightArm.getRotX() * 0.5F - (float) Math.PI);
                    rightArm.setRotY(0.0F);
                }
                break;
            case BOW_AND_ARROW:
                if (rightArm != null) {
                    rightArm.setRotY(0.1F + head.getRotY());
                    rightArm.setRotX((float) Math.PI / 2F + head.getRotX());
                }

                if (leftArm != null) {
                    leftArm.setRotY(-0.1F - head.getRotY() - 0.4F);
                    leftArm.setRotX((float) Math.PI / 2F + head.getRotX());
                }
                break;
            case CROSSBOW_CHARGE:
                this.animateCrossbowCharge(rightArm, leftArm, livingEntity, true);
                break;
            case CROSSBOW_HOLD:
                this.animateCrossbowHold(rightArm, leftArm, head, true);
                break;
            case SPYGLASS:
                if (rightArm != null) {
                    rightArm.setRotX(Mth.clamp(head.getRotX() - 1.9198622F - (livingEntity.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F));
                    rightArm.setRotY(head.getRotY() - 0.2617994F);
                }
                break;
            case TOOT_HORN:
                if (rightArm != null) {
                    rightArm.setRotX(Mth.clamp(head.getRotX(), -1.2F, 1.2F) - 1.4835298F);
                    rightArm.setRotY(head.getRotY() - ((float) Math.PI / 6F));
                }
            default:
        }

    }

    private void poseLeftArm(LivingEntity livingEntity, CoreGeoBone rightArm, CoreGeoBone leftArm, CoreGeoBone head) {
        switch (this.leftArmPose) {
            case EMPTY:
                break;
            case BLOCK:
                if (leftArm != null) {
                    leftArm.setRotX(leftArm.getRotX() * 0.5F - 0.9424779F);
                    leftArm.setRotY(((float) Math.PI / 6F));
                }
                break;
            case ITEM:
                if (leftArm != null) {
                    leftArm.setRotX(leftArm.getRotX() * 0.5F - ((float) Math.PI / 10F));
                    leftArm.setRotY(0.0F);
                }
                break;
            case THROW_SPEAR:
                if (leftArm != null) {
                    leftArm.setRotX(leftArm.getRotX() * 0.5F - (float) Math.PI);
                    leftArm.setRotY(0.0F);
                }
                break;
            case BOW_AND_ARROW:
                if (rightArm != null) {
                    rightArm.setRotY(0.1F + head.getRotY() - 0.4F);
                    rightArm.setRotX((float) Math.PI / 2F + head.getRotX());
                }

                if (leftArm != null) {
                    leftArm.setRotY(0.1F + head.getRotY());
                    leftArm.setRotX((float) Math.PI / 2F + head.getRotX());
                }
                break;
            case CROSSBOW_CHARGE:
                this.animateCrossbowCharge(rightArm, leftArm, livingEntity, false);
                break;
            case CROSSBOW_HOLD:
                this.animateCrossbowHold(rightArm, leftArm, head, false);
                break;
            case SPYGLASS:
                if (leftArm != null) {
                    leftArm.setRotX(Mth.clamp(head.getRotX() - 1.9198622F - (livingEntity.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F));
                    leftArm.setRotY(head.getRotY() + 0.2617994F);
                }
                break;
            case TOOT_HORN:
                if (leftArm != null) {
                    leftArm.setRotX(Mth.clamp(head.getRotX(), -1.2F, 1.2F) - 1.4835298F);
                    leftArm.setRotY(head.getRotY() + ((float) Math.PI / 6F));
                }
            default:
        }

    }

    protected void setupAttackAnimation(LivingEntity livingEntity, float partialTick, CoreGeoBone rightArm, CoreGeoBone leftArm, CoreGeoBone body, CoreGeoBone head, float attackTime) {
        if (!(attackTime <= 0.0F)) {
            HumanoidArm humanoidarm = this.getAttackArm(livingEntity);
            CoreGeoBone arm = this.getArm(humanoidarm, rightArm, leftArm);
            float f = attackTime;

            if (body != null) {
                body.setRotY(Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F);
            }

            if (humanoidarm == HumanoidArm.LEFT && body != null) {
                body.setRotY(-body.getRotY());
            }

            if (rightArm != null && body != null) {
                rightArm.setRotY(rightArm.getRotY() + body.getRotY());
            }

            if (leftArm != null && body != null) {
                leftArm.setRotY(leftArm.getRotY() + body.getRotY());
                leftArm.setRotX(leftArm.getRotX() + body.getRotY());
            }

            f = 1.0F - attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float)Math.PI);

            float f2 = Mth.sin(attackTime * (float)Math.PI) * -((head != null ? head.getRotX() : 0.0F) - 0.7F) * 0.75F;

            if (arm != null) {
                arm.setRotX(arm.getRotX() - f1 * 1.2F + f2);
                arm.setRotY(arm.getRotY() + (body != null ? body.getRotY() : 0.0F) * 2.0F);
                arm.setRotZ(arm.getRotZ() + Mth.sin(attackTime * (float) Math.PI) * -0.4F);
            }
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

    protected CoreGeoBone getArm(HumanoidArm arm, CoreGeoBone rightArm, CoreGeoBone leftArm) {
        return arm == HumanoidArm.LEFT ? leftArm : rightArm;
    }

    public void animateCrossbowHold(CoreGeoBone rightArm, CoreGeoBone leftArm, CoreGeoBone head, boolean rightHanded) {
        CoreGeoBone mainhand = rightHanded ? rightArm : leftArm;
        CoreGeoBone offhand = rightHanded ? leftArm : rightArm;

        if (mainhand != null) {
            mainhand.setRotY((rightHanded ? -0.3F : 0.3F) + (head != null ? head.getRotY() : 0.0F));
            mainhand.setRotX((-(float)Math.PI / 2F) + (head != null ? head.getRotX() : 0.0F) + 0.1F);
        }

        if (offhand != null) {
            offhand.setRotY((rightHanded ? 0.6F : -0.6F) + (head != null ? head.getRotY() : 0.0F));
            offhand.setRotX(-1.5F + (head != null ? head.getRotX() : 0.0F));
        }
    }

    public void animateCrossbowCharge(CoreGeoBone rightArm, CoreGeoBone leftArm, LivingEntity entity, boolean rightHanded) {
        CoreGeoBone mainhand = rightHanded ? rightArm : leftArm;
        CoreGeoBone offhand = rightHanded ? leftArm : rightArm;

        if (mainhand != null) {
            mainhand.setRotX(-0.97079635F);

            if (offhand != null) {
                offhand.setRotX(mainhand.getRotX());
            }
        }

        float f = (float) CrossbowItem.getChargeDuration(entity.getUseItem());
        float f1 = Mth.clamp((float)entity.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;

        if (offhand != null) {
            offhand.setRotX(Mth.lerp(f2, offhand.getRotX(), (-(float) Math.PI / 2F)));
        }
    }
}
