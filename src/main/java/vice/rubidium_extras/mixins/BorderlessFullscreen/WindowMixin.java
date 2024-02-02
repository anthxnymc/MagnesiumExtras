package vice.rubidium_extras.mixins.BorderlessFullscreen;

import com.mojang.blaze3d.platform.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vice.rubidium_extras.config.MagnesiumExtrasConfig;

@Mixin({Window.class})
public class WindowMixin
{
    @Redirect(method = {"setMode"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
    private void glfwSetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height, int refreshRate)
    {
        if (!MagnesiumExtrasConfig.ConfigSpec.isLoaded())
        {
            GLFW.glfwSetWindowMonitor(window, monitor, xpos, ypos, width, height, refreshRate);
            return;
        }

        if (MagnesiumExtrasConfig.fullScreenMode.get() == MagnesiumExtrasConfig.FullscreenMode.BORDERLESS)
        {
            if (monitor != 0L)
            {
                GLFW.glfwSetWindowSizeLimits(window, 0, 0, width, height);
            }

            GLFW.glfwSetWindowMonitor(window, 0L, xpos, ypos, width, height, refreshRate);
        }
        else
        {
            GLFW.glfwSetWindowMonitor(window, monitor, xpos, ypos, width, height, refreshRate);
        }
    }

    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwGetWindowMonitor(J)J"))
    private long redirect$glfwGetWindowMonitor(long window) {
        if (MagnesiumExtrasConfig.fullScreenMode.get() == MagnesiumExtrasConfig.FullscreenMode.BORDERLESS) {
            return 1L;
        }
        return window;
    }
}