/*
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
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class ReplacedSkeletonModel extends HumanoidAnimatedGeoModel {
    public ReplacedSkeletonModel() {
        super("skeleton");
    }

    public ReplacedSkeletonModel(String entityTypeName) {
        super(entityTypeName);
    }

    @Override
    public void setCustomAnimations(GeoReplacedEntity animatable, long instanceId, AnimationState animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (this.getAnimationResource(animatable) == null) {
            CoreGeoBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            CoreGeoBone leftArm = this.getAnimationProcessor().getBone("leftarm");

            this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
            this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
            ItemStack itemstack = this.getCurrentEntity().getItemInHand(InteractionHand.MAIN_HAND);
            if (itemstack.is(Items.BOW) && ((Skeleton) this.getCurrentEntity()).isAggressive()) {
                if (this.getCurrentEntity().getMainArm() == HumanoidArm.RIGHT) {
                    this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
                } else {
                    this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
                }
            }

            if (((Skeleton) this.getCurrentEntity()).isAggressive() && (itemstack.isEmpty() || !itemstack.is(Items.BOW))) {
                float attackTime = this.getCurrentEntity().getAttackAnim(animationState.getPartialTick());

                float f = Mth.sin(attackTime * (float)Math.PI);
                float f1 = Mth.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);

                if (rightArm != null) {
                    rightArm.setRotZ(0.0F);
                    rightArm.setRotY(-(0.1F - f * 0.6F));
                    rightArm.setRotX((-(float) Math.PI / 2F));
                    rightArm.setRotX(rightArm.getRotX() - f * 1.2F - f1 * 0.4F);
                }

                if (leftArm != null) {
                    leftArm.setRotZ(0.0F);
                    leftArm.setRotY(0.1F - f * 0.6F);
                    leftArm.setRotX((-(float) Math.PI / 2F));
                    leftArm.setRotX(leftArm.getRotX() - f * 1.2F - f1 * 0.4F);
                }

                this.bobArms(rightArm, leftArm, animationState.getPartialTick());
            }
        }
    }
}
