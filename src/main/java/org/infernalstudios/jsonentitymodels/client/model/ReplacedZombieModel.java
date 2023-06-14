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

import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Zombie;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class ReplacedZombieModel extends HumanoidAnimatedGeoModel {
    public ReplacedZombieModel() {
        super("zombie");
    }

    public ReplacedZombieModel(String entityTypeName) {
        super(entityTypeName);
    }

    @Override
    public void setCustomAnimations(GeoReplacedEntity animatable, long instanceId, AnimationState animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (this.getAnimationResource(animatable) == null) {
            CoreGeoBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            CoreGeoBone leftArm = this.getAnimationProcessor().getBone("leftarm");

            this.animateZombieArms(leftArm, rightArm, ((Zombie) this.getCurrentEntity()).isAggressive(),
                    this.getCurrentEntity().getAttackAnim(animationState.getPartialTick()), animationState.getPartialTick());
        }
    }

    public void animateZombieArms(CoreGeoBone leftArm, CoreGeoBone rightArm, boolean isAggressive, float attackTime, float partialTicks) {
        float f = Mth.sin(attackTime * (float)Math.PI);
        float f1 = Mth.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);
        float f2 = (float)Math.PI / (isAggressive ? 5.0F : 15.0F);

        if (rightArm != null) {
            rightArm.setRotY(0.1F - f * 0.6F);
            rightArm.setRotX(f2);
            rightArm.setRotX(rightArm.getRotX() - f * 1.2F + f1 * 0.4F);
        }

        if (leftArm != null) {
            leftArm.setRotY(-0.1F + f * 0.6F);
            leftArm.setRotX(f2);
            leftArm.setRotX(leftArm.getRotX() - f * 1.2F + f1 * 0.4F);
        }

        this.bobArms(rightArm, leftArm, partialTicks);
    }
}
