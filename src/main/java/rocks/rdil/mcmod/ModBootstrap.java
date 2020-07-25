package rocks.rdil.mcmod;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(1)
public class ModBootstrap implements ITweaker {
    @Override
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {}

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        MixinBootstrap.init();

        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
        Mixins.addConfiguration("mixins.MinecraftMod.json");
        environment.setObfuscationContext("searge");
        environment.setSide(MixinEnvironment.Side.CLIENT);

        // thanks replay mod <3
        CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
        try {
            Class<?> aClass = Class.forName("net.minecraftforge.fml.relauncher.CoreModManager");
            Method getIgnoredMods = null;
            try {
                getIgnoredMods = aClass.getDeclaredMethod("getIgnoredMods");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                if (getIgnoredMods == null) {
                    getIgnoredMods = aClass.getDeclaredMethod("getLoadedCoremods");
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (codeSource != null) {
                URL location = codeSource.getLocation();
                try {
                    File file = new File(location.toURI());
                    if (file.isFile()) {
                        try {
                            if (getIgnoredMods != null)
                                ((List<String>) getIgnoredMods.invoke(null)).remove(file.getName());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No CodeSource, if this is not a development environment we might run into problems!");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}