package rocks.rdil.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
    modid = "YOUR MOD ID",
    name = "YOUR MODS NAME",
    version = "YOUR MODS VERSION",
    clientSideOnly = true
)
public class ModMain {
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void preInit(FMLInitializationEvent e) {
        LOGGER.info("Loaded mod!");
    }
}
