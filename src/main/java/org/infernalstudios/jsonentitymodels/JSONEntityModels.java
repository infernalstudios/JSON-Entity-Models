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
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;

@Mod(JSONEntityModels.MOD_ID)
public class JSONEntityModels {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "jsonentitymodels";
    public static final String CURR_VERSION = "0.1.0";

    public JSONEntityModels() {
        JSONEntityModels.registerReloadListeners();
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    synchronized public static void registerReloadListeners() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ResourceUtil::registerReloadListener);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ResourceUtil::loadResourcePacks);
    }

}
