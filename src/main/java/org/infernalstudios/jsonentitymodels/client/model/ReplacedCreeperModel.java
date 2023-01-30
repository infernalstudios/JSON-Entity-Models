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

public class ReplacedCreeperModel extends HeadTurningAnimatedGeoModel {
    public ReplacedCreeperModel() {
        super("creeper");
    }

    @Override
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        if (this.getAnimationResource(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

            IBone head = this.getAnimationProcessor().getBone("head");
            IBone rightHindLeg = this.getAnimationProcessor().getBone("rightlegback");
            IBone leftHindLeg = this.getAnimationProcessor().getBone("leftlegback");
            IBone rightFrontLeg = this.getAnimationProcessor().getBone("rightlegfront");
            IBone leftFrontLeg = this.getAnimationProcessor().getBone("leftlegfront");


            head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
            head.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));
            rightHindLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F) * 1.4F * animationEvent.getLimbSwingAmount());
            leftHindLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F + (float)Math.PI) * 1.4F * animationEvent.getLimbSwingAmount());
            rightFrontLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F + (float)Math.PI) * 1.4F * animationEvent.getLimbSwingAmount());
            leftFrontLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F) * 1.4F * animationEvent.getLimbSwingAmount());
        } else {
            super.setCustomAnimations(animatable, instanceId, animationEvent);
        }
    }
}
