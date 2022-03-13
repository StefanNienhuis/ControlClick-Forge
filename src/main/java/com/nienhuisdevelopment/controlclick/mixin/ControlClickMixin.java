package com.nienhuisdevelopment.controlclick.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;

@Mixin(MouseHandler.class)
public abstract class ControlClickMixin {

    @Shadow @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void onPress(long window, int button, int action, int mods);

    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    private void onPress(long window, int button, int action, int mods, CallbackInfo ci) {
        // Ignore if not current window
        if (window != this.minecraft.getWindow().getWindow()) {
            return;
        }

        // If the control modifier is active, disable it and run the function again.
        if (Minecraft.ON_OSX && button == 0 && (mods & GLFW_MOD_CONTROL) == GLFW_MOD_CONTROL) {
            this.onPress(window, button, action, mods ^ GLFW_MOD_CONTROL);
            ci.cancel();
        }
    }
}
