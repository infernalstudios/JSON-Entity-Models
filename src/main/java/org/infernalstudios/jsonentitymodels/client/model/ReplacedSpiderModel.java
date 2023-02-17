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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ReplacedSpiderModel extends HeadTurningAnimatedGeoModel {
    public ReplacedSpiderModel() {
        super("spider");
    }

    @Override
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        if (this.getAnimationResource(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

            IBone head = this.getAnimationProcessor().getBone("head");
            IBone rightFrontLeg = this.getAnimationProcessor().getBone("rightleg1o");
            IBone leftFrontLeg = this.getAnimationProcessor().getBone("leftleg1o");
            IBone rightMiddleFrontLeg = this.getAnimationProcessor().getBone("rightleg2o");
            IBone leftMiddleFrontLeg = this.getAnimationProcessor().getBone("leftleg2o");
            IBone rightMiddleHindLeg = this.getAnimationProcessor().getBone("rightleg3o");
            IBone leftMiddleHindLeg = this.getAnimationProcessor().getBone("leftleg3o");
            IBone rightHindLeg = this.getAnimationProcessor().getBone("rightleg4o");
            IBone leftHindLeg = this.getAnimationProcessor().getBone("leftleg4o");

            head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
            head.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));

            float f3 = -(Mth.cos(animationEvent.getLimbSwing() * 0.6662F * 2.0F + 0.0F) * 0.4F) * animationEvent.getLimbSwingAmount();
            float f4 = -(Mth.cos(animationEvent.getLimbSwing() * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * animationEvent.getLimbSwingAmount();
            float f5 = -(Mth.cos(animationEvent.getLimbSwing() * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * animationEvent.getLimbSwingAmount();
            float f6 = -(Mth.cos(animationEvent.getLimbSwing() * 0.6662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * animationEvent.getLimbSwingAmount();
            float f7 = Math.abs(Mth.sin(animationEvent.getLimbSwing() * 0.6662F + 0.0F) * 0.4F) * animationEvent.getLimbSwingAmount();
            float f8 = Math.abs(Mth.sin(animationEvent.getLimbSwing() * 0.6662F + (float)Math.PI) * 0.4F) * animationEvent.getLimbSwingAmount();
            float f9 = Math.abs(Mth.sin(animationEvent.getLimbSwing() * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * animationEvent.getLimbSwingAmount();
            float f10 = Math.abs(Mth.sin(animationEvent.getLimbSwing() * 0.6662F + ((float)Math.PI * 1.5F)) * 0.4F) * animationEvent.getLimbSwingAmount();

            rightHindLeg.setRotationY(f3);
            leftHindLeg.setRotationY(-f3);
            rightMiddleHindLeg.setRotationY(f4);
            leftMiddleHindLeg.setRotationY(-f4);
            rightMiddleFrontLeg.setRotationY(f5);
            leftMiddleFrontLeg.setRotationY(-f5);
            rightFrontLeg.setRotationY(f6);
            leftFrontLeg.setRotationY(-f6);
            rightHindLeg.setRotationZ(f7);
            leftHindLeg.setRotationZ(-f7);
            rightMiddleHindLeg.setRotationZ(f8);
            leftMiddleHindLeg.setRotationZ(-f8);
            rightMiddleFrontLeg.setRotationZ(f9);
            leftMiddleFrontLeg.setRotationZ(-f9);
            rightFrontLeg.setRotationZ(f10);
            leftFrontLeg.setRotationZ(-f10);
        } else {
            super.setCustomAnimations(animatable, instanceId, animationEvent);
        }
    }
}
