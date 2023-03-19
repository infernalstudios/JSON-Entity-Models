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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ReplacedSkeletonModel extends HumanoidAnimatedGeoModel {
    public ReplacedSkeletonModel() {
        super("skeleton");
    }

    @Override
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        if (this.getAnimationFileLocation(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

            IBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            IBone leftArm = this.getAnimationProcessor().getBone("leftarm");

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
                float attackTime = this.getCurrentEntity().getAttackAnim(animationEvent.getPartialTick());

                float f = Mth.sin(attackTime * (float)Math.PI);
                float f1 = Mth.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);

                if (rightArm != null) {
                    rightArm.setRotationZ(0.0F);
                    rightArm.setRotationY(-(0.1F - f * 0.6F));
                    rightArm.setRotationX((-(float) Math.PI / 2F));
                    rightArm.setRotationX(rightArm.getRotationX() - f * 1.2F - f1 * 0.4F);
                }

                if (leftArm != null) {
                    leftArm.setRotationZ(0.0F);
                    leftArm.setRotationY(0.1F - f * 0.6F);
                    leftArm.setRotationX((-(float) Math.PI / 2F));
                    leftArm.setRotationX(leftArm.getRotationX() - f * 1.2F - f1 * 0.4F);
                }

                this.bobArms(rightArm, leftArm, animationEvent.getPartialTick());
            }
        }
    }
}
