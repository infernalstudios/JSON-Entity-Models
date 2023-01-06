package org.infernalstudios.jsonentitymodels.entity;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ReplacedSkeletonEntity extends ReplacedEntityBase implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean isAiming;

    private boolean isAggressive;

    private boolean hasBow;

    public void setAiming(boolean isAiming) {
        this.isAiming = isAiming;
    }

    public void setAggressive(boolean isAggressive) {
        this.isAggressive = isAggressive;
    }

    public void setHasBow(boolean hasBow) {
        this.hasBow = hasBow;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.isHurt) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAiming || this.isAggressive ? "aim_hurt" : "hurt", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAiming || this.isAggressive ? "aim_walk" : "walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAiming || this.isAggressive ? "aim_idle" : "idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private <P extends IAnimatable> PlayState aimPredicate(AnimationEvent<P> event) {
        if (this.isAiming) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("aim", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isAggressive) {
            if (this.hasBow) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("aggressivebow", ILoopType.EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("aggressivemelee", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "main_controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "aim_controller", 0, this::aimPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
