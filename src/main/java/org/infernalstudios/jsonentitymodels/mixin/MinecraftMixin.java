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
package org.infernalstudios.jsonentitymodels.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.repository.PackRepository;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public abstract PackRepository getResourcePackRepository();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void jsonentitymodels_enableDefaultPacks(GameConfig config, CallbackInfo ci) {
        if (ResourceUtil.packLoaded) {
            PackRepository repository = this.getResourcePackRepository();
            Collection<String> selectedPacks = new ArrayList<>(repository.getSelectedIds());
            if (!ResourceUtil.packDeleted && !selectedPacks.contains("file/JEMs Base Resources-" + JSONEntityModels.CURR_VERSION + ".zip")) {
                selectedPacks.add("file/JEMs Base Resources-" + JSONEntityModels.CURR_VERSION + ".zip");
                repository.setSelected(selectedPacks);
                repository.openAllSelected();
            }
        }
    }
}
