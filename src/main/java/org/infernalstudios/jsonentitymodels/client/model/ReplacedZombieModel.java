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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedZombieModel extends HumanoidAnimatedGeoModel {
    public ReplacedZombieModel() {
        super("zombie");
    }

    @Override
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        if (this.getAnimationResource(animatable) == null) {
            IBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            IBone leftArm = this.getAnimationProcessor().getBone("leftarm");

            this.animateZombieArms(leftArm, rightArm, ((Zombie) this.getCurrentEntity()).isAggressive(),
                    this.getCurrentEntity().getAttackAnim(animationEvent.getPartialTick()), animationEvent.getPartialTick());
        }
    }

    public void animateZombieArms(IBone leftArm, IBone rightArm, boolean isAggressive, float attackTime, float partialTicks) {
        float f = Mth.sin(attackTime * (float)Math.PI);
        float f1 = Mth.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);
        float f2 = (float)Math.PI / (isAggressive ? 5.0F : 15.0F);
        rightArm.setRotationY(0.1F - f * 0.6F);
        leftArm.setRotationY(-0.1F + f * 0.6F);
        rightArm.setRotationX(f2);
        leftArm.setRotationX(f2);
        rightArm.setRotationX(rightArm.getRotationX() - f * 1.2F + f1 * 0.4F);
        leftArm.setRotationX(leftArm.getRotationX() - f * 1.2F + f1 * 0.4F);
        this.bobArms(rightArm, leftArm, partialTicks);
    }
}
