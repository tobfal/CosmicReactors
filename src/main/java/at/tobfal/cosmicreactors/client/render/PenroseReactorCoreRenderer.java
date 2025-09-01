package at.tobfal.cosmicreactors.client.render;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.entity.PenroseReactorCoreEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class PenroseReactorCoreRenderer extends EntityRenderer<PenroseReactorCoreEntity, EntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CosmicReactors.getResourceLocation("penrose_reactor_core"), "main");
    private static final ResourceLocation TEXTURE = CosmicReactors.getResourceLocation("textures/entity/penrose_reactor_core.png");

    private final PenroseReactorCoreEntityModel model;

    public PenroseReactorCoreRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PenroseReactorCoreEntityModel(context.bakeLayer(LAYER_LOCATION));
        this.shadowRadius = 0.0F;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void render(EntityRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0, 0.05f * Math.sin(renderState.ageInTicks / 10f), 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.ageInTicks * 20f));

        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        this.model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
        super.render(renderState, poseStack, bufferSource, packedLight);
    }
}
