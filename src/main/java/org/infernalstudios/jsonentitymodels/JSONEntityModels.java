/*
 * Copyright 2022 Infernal Studios
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
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import software.bernie.example.GeckoLibMod;

@Mod(JSONEntityModels.MOD_ID)
public class JSONEntityModels {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "jsonentitymodels";

    public JSONEntityModels() {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GeckoLibMod.DISABLE_IN_DEV = true;

        modEventBus.addListener(this::onClientSetup);
    }

    public void onClientSetup(final FMLClientSetupEvent event) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ResourceUtil::registerReloadListener);
    }

}
