package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import me.srrapero720.embeddiumplus.foundation.entitydistance.IEntityTypeAccess;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class MaxDistanceEntity {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir) {
        if (!EmbyConfig.entityDistanceCullingCache) return;

        if (entity instanceof EnderDragon) return;
        if (entity instanceof Ghast) return;

        String name = entity.getClass().getName().toLowerCase();
        if (name.startsWith("com.simibubi.create.content.contraptions")) return;
        if (name.startsWith("com.github.alexthe666.iceandfire.entity") && name.contains("dragon")) return;

        if (!((IEntityTypeAccess) entity.getType()).embPlus$isWhitelisted() && !EmbyTools.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                EmbyConfig.entityCullingDistanceYCache,
                EmbyConfig.entityCullingDistanceXCache
        )) {
            cir.cancel();
        }
    }
}