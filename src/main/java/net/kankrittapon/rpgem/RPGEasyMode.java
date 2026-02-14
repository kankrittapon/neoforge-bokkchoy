package net.kankrittapon.rpgem;

import net.kankrittapon.rpgem.init.ModBlockEntities;
import net.kankrittapon.rpgem.init.ModBlocks;
import net.kankrittapon.rpgem.init.ModCreativeTabs;
import net.kankrittapon.rpgem.init.ModItems;
import net.kankrittapon.rpgem.init.ModMenuTypes;
import net.kankrittapon.rpgem.init.ModMobEffects;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RPGEasyMode.MODID)
public class RPGEasyMode {
    public static final String MODID = "rpgem";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RPGEasyMode(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModBlocks.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ModItems.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        ModCreativeTabs.register(modEventBus);
        // Register the Deferred Register to the mod event bus so block entities get
        // registered
        ModBlockEntities.register(modEventBus);
        // Register the Deferred Register to the mod event bus so menus get registered
        ModMenuTypes.register(modEventBus);
        // Register the Deferred Register to the mod event bus so effects get registered
        ModMobEffects.register(modEventBus);
        // Register the Deferred Register to the mod event bus so custom attributes get
        // registered
        net.kankrittapon.rpgem.init.ModAttributes.register(modEventBus);
        // Register Data Components
        net.kankrittapon.rpgem.init.ModDataComponents.register(modEventBus);
        // Register the Deferred Register to the mod event bus so entities get
        // registered
        net.kankrittapon.rpgem.init.ModEntities.ENTITIES.register(modEventBus);
        // Register Loot Modifiers
        net.kankrittapon.rpgem.loot.ModLootModifiers.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        NeoForge.EVENT_BUS.register(this);

        // Register Networking
        modEventBus.addListener(net.kankrittapon.rpgem.network.ModMessages::register);

        // Register our mod's ModConfigSpec so that FML can create and load the config
        // file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("RPG Easy Mode Common Setup Initialized");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
