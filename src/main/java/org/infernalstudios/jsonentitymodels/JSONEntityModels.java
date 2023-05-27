/*
 * Copyright 2023 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infernalstudios.jsonentitymodels;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import software.bernie.example.GeckoLibMod;

@Mod(JSONEntityModels.MOD_ID)
public class JSONEntityModels {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "jsonentitymodels";
    public static final String CURR_VERSION = "0.2.1";

    public JSONEntityModels() {
        JSONEntityModels.registerReloadListeners();
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GeckoLibMod.DISABLE_IN_DEV = true;

        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    synchronized public static void registerReloadListeners() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ResourceUtil::registerReloadListener);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ResourceUtil::loadResourcePacks);
    }

}
