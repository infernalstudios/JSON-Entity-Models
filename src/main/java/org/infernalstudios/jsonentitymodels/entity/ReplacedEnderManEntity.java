package org.infernalstudios.jsonentitymodels.entity;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class ReplacedEnderManEntity extends ReplacedEntityBase {
    private boolean isScreaming;

    private boolean holdingBlock;

    public void setScreaming(boolean isScreaming) {
        this.isScreaming = isScreaming;
    }

    public void setHoldingBlock(boolean holdingBlock) {
        this.holdingBlock = holdingBlock;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.isHurt) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hurt", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.inWater) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", ILoopType.EDefaultLoopTypes.LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.holdingBlock ? (this.isScreaming ? "hold_block_scream_run" : "hold_block_walk") : (this.isScreaming ? "scream_run" : "walk"), ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.holdingBlock ? "hold_block_idle" : "idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    protected <P extends IAnimatable> PlayState screamPredicate(AnimationEvent<P> event) {
        if (this.isScreaming) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("scream_start", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("scream_loop", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    protected <P extends IAnimatable> PlayState holdBlockPredicate(AnimationEvent<P> event) {
        if (this.holdingBlock) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hold_block", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        data.addAnimationController(new AnimationController(this, "scream_controller", 0, this::screamPredicate));
        data.addAnimationController(new AnimationController(this, "hold_block_controller", 0, this::holdBlockPredicate));
    }
}
