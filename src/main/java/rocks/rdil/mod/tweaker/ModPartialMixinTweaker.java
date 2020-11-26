package rocks.rdil.mod.tweaker;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(1)
public class ModPartialMixinTweaker implements ITweaker {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        try {
            MixinBootstrap.init();

            MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
            Mixins.addConfiguration("mixins.mod.json");
            environment.setObfuscationContext("searge");
            environment.setSide(MixinEnvironment.Side.CLIENT);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Failed to initialize Mixin. Is the subsystem already booted? Taking the fallback route.");
            Mixins.addConfiguration("mixins.mod.json");
        }
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}