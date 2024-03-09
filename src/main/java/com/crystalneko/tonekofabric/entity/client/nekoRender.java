package com.crystalneko.tonekofabric.entity.client;

import net.minecraft.client.render.item.HeldItemRenderer;
import com.crystalneko.tonekofabric.entity.nekoEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class nekoRender extends GeoEntityRenderer<nekoEntity> {
    public HeldItemRenderer heldItemRenderer;

    public nekoRender(EntityRendererFactory.Context renderManager) {
        super(renderManager, new nekoModel());
        this.heldItemRenderer = renderManager.getHeldItemRenderer();
    }
    @Override
    public Identifier getTextureLocation(nekoEntity entity){
        return new Identifier("toneko","textures/entity/neko.png");
    }
    @Override
    public void render(nekoEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider buffSource, int packedLight){
        if (entity.isBaby()){
            poseStack.scale(0.5F, 0.5F, 0.5F); // 将幼年实体的尺寸缩小为原来的一半
        }
        Vec3d scale = entity.getScale();
        poseStack.scale((float) scale.x, (float) scale.y, (float) scale.z);
        super.render(entity,entityYaw,partialTick,poseStack,buffSource,packedLight);
    }

}
