package toni.embeddiumextras.mixins.impl.language;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import toni.embeddiumextras.EmbyConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;

@Mixin(LanguageSelectScreen.class)
public class LanguageMixin extends OptionsSubScreen {
    public LanguageMixin(Screen screen, Options options, Component component) { super(screen, options, component); }

    @WrapOperation(method = "onDone", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;"))
    public CompletableFuture<Void> redirect$resourcesReload(Minecraft instance, Operation<CompletableFuture<Void>> original) {
        if (EmbyConfig.fastLanguageReloadCache) {
            this.minecraft.getLanguageManager().onResourceManagerReload(this.minecraft.getResourceManager());
            return null;
        } else {
            return original.call(instance);
        }
    }
}