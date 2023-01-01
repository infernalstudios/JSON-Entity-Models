package org.infernalstudios.jsonentitymodels.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedSpiderModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedSpiderEntity;
import software.bernie.geckolib3.renderers.geo.GeoReplacedEntityRenderer;

public class ReplacedSpiderRenderer extends GeoReplacedEntityRenderer<ReplacedSpiderEntity> {

    public ReplacedSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedSpiderModel(), new ReplacedSpiderEntity());
    }

}
