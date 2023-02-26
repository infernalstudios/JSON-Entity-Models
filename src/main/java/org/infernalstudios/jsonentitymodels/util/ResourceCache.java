package org.infernalstudios.jsonentitymodels.util;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

public class ResourceCache {
    private static ResourceCache INSTANCE;

    public Map<String, List<ResourceLocation>> getBabyModels() {
        return this.babyModels;
    }

    public Map<String, List<ResourceLocation>> getAdultModels() {
        return this.adultModels;
    }

    public Map<String, List<ResourceLocation>> getBabyTextures() {
        return this.babyTextures;
    }

    public Map<String, List<ResourceLocation>> getAdultTextures() {
        return this.adultTextures;
    }

    public Map<String, List<ResourceLocation>> getBabyAnimations() {
        return this.babyAnimations;
    }

    public Map<String, List<ResourceLocation>> getAdultAnimations() {
        return this.adultAnimations;
    }

    private Map<String, List<ResourceLocation>> babyModels = Collections.emptyMap();
    private Map<String, List<ResourceLocation>> adultModels = Collections.emptyMap();
    private Map<String, List<ResourceLocation>> babyTextures = Collections.emptyMap();
    private Map<String, List<ResourceLocation>> adultTextures = Collections.emptyMap();
    private Map<String, List<ResourceLocation>> babyAnimations = Collections.emptyMap();
    private Map<String, List<ResourceLocation>> adultAnimations = Collections.emptyMap();

    public static ResourceCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResourceCache();
            return INSTANCE;
        }
        return INSTANCE;
    }

    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager,
                                          ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor,
                                          Executor gameExecutor) {

        Map<String, List<ResourceLocation>> babyModels = new Object2ObjectOpenHashMap<>();
        Map<String, List<ResourceLocation>> adultModels = new Object2ObjectOpenHashMap<>();
        Map<String, List<ResourceLocation>> babyTextures = new Object2ObjectOpenHashMap<>();
        Map<String, List<ResourceLocation>> adultTextures = new Object2ObjectOpenHashMap<>();
        Map<String, List<ResourceLocation>> babyAnimations = new Object2ObjectOpenHashMap<>();
        Map<String, List<ResourceLocation>> adultAnimations = new Object2ObjectOpenHashMap<>();

        return CompletableFuture.allOf(
                        loadModels(backgroundExecutor, resourceManager, "geo", adultModels::put, babyModels::put),
                        loadTextures(backgroundExecutor, resourceManager, "textures/entity", adultTextures::put, babyTextures::put),
                        loadAnimations(backgroundExecutor, resourceManager, "animations", adultAnimations::put, babyAnimations::put))
                .thenCompose(stage::wait).thenAcceptAsync(empty -> {
                    this.adultModels = adultModels;
                    this.babyModels = babyModels;
                    this.adultTextures = adultTextures;
                    this.babyTextures = babyTextures;
                    this.adultAnimations = adultAnimations;
                    this.babyAnimations = babyAnimations;
            }, gameExecutor);
    }

    private static CompletableFuture<Void> loadModels(Executor executor, ResourceManager resourceManager,
                                                      String type, BiConsumer<String, List<ResourceLocation>> adults, BiConsumer<String, List<ResourceLocation>> babies) {
        return CompletableFuture.supplyAsync(
                        () -> resourceManager.listResources(type, fileName -> fileName.getNamespace().equals("jsonentitymodels")
                                && fileName.toString().endsWith(".json")), executor)
                .thenApplyAsync(resources -> {
                    Map<String, List<ResourceLocation>> adultModels = new Object2ObjectOpenHashMap<>();
                    Map<String, List<ResourceLocation>> babyModels = new Object2ObjectOpenHashMap<>();

                    for (ResourceLocation resource : resources.keySet()) {
                        String[] splitPath = resource.getPath().split("/");

                        String entityIdentifier = splitPath[1] + ":" + splitPath[2];

                        if (resource.getPath().contains("/adult/")) {
                            if (adultModels.containsKey(entityIdentifier)) {
                                adultModels.get(entityIdentifier).add(resource);
                            } else {
                                ArrayList<ResourceLocation> initialList = new ArrayList<>();
                                initialList.add(resource);
                                adultModels.put(entityIdentifier, initialList);
                            }
                        } else if (resource.getPath().contains("/baby/")) {
                            if (babyModels.containsKey(entityIdentifier)) {
                                babyModels.get(entityIdentifier).add(resource);
                            } else {
                                ArrayList<ResourceLocation> initialList = new ArrayList<>();
                                initialList.add(resource);
                                babyModels.put(entityIdentifier, initialList);
                            }
                        }
                    }

                    return Pair.of(adultModels, babyModels);
                }, executor).thenAcceptAsync(models -> {
                    Map<String, List<ResourceLocation>> adultModels = models.first();
                    Map<String, List<ResourceLocation>> babyModels = models.second();

                    for (Map.Entry<String, List<ResourceLocation>> entry : adultModels.entrySet()) {
                        adults.accept(entry.getKey(), entry.getValue());
                    }

                    for (Map.Entry<String, List<ResourceLocation>> entry : babyModels.entrySet()) {
                        babies.accept(entry.getKey(), entry.getValue());
                    }

                }, executor);
    }

    private static CompletableFuture<Void> loadTextures(Executor executor, ResourceManager resourceManager,
                                                      String type, BiConsumer<String, List<ResourceLocation>> adults, BiConsumer<String, List<ResourceLocation>> babies) {
        return CompletableFuture.supplyAsync(
                        () -> resourceManager.listResources(type, fileName -> fileName.getNamespace().equals("jsonentitymodels")
                                && fileName.toString().endsWith(".png") && !fileName.toString().contains("crackiness")), executor)
                .thenApplyAsync(resources -> {
                    Map<String, List<ResourceLocation>> adultTextures = new Object2ObjectOpenHashMap<>();
                    Map<String, List<ResourceLocation>> babyTextures = new Object2ObjectOpenHashMap<>();

                    for (ResourceLocation resource : resources.keySet()) {
                        String[] splitPath = resource.getPath().split("/");

                        String modelIdentifier = splitPath[2] + ":" + splitPath[3] + "/" + splitPath[5];

                        if (resource.toString().endsWith("_glow.png")) {
                            modelIdentifier += "/" + splitPath[splitPath.length - 1].replace(".png", "");
                        } else if (resource.toString().endsWith("_powered.png")) {
                            modelIdentifier += "/" + splitPath[splitPath.length - 1].replace(".png", "");
                        }

                        if (resource.getPath().contains("/adult/")) {
                            if (adultTextures.containsKey(modelIdentifier)) {
                                adultTextures.get(modelIdentifier).add(resource);
                            } else {
                                ArrayList<ResourceLocation> initialList = new ArrayList<>();
                                initialList.add(resource);
                                adultTextures.put(modelIdentifier, initialList);
                            }
                        } else if (resource.getPath().contains("/baby/")) {
                            if (babyTextures.containsKey(modelIdentifier)) {
                                babyTextures.get(modelIdentifier).add(resource);
                            } else {
                                ArrayList<ResourceLocation> initialList = new ArrayList<>();
                                initialList.add(resource);
                                babyTextures.put(modelIdentifier, initialList);
                            }
                        }
                    }

                    return Pair.of(adultTextures, babyTextures);
                }, executor).thenAcceptAsync(models -> {
                    Map<String, List<ResourceLocation>> adultTextures = models.first();
                    Map<String, List<ResourceLocation>> babyTextures = models.second();

                    for (Map.Entry<String, List<ResourceLocation>> entry : adultTextures.entrySet()) {
                        adults.accept(entry.getKey(), entry.getValue());
                    }

                    for (Map.Entry<String, List<ResourceLocation>> entry : babyTextures.entrySet()) {
                        babies.accept(entry.getKey(), entry.getValue());
                    }

                }, executor);
    }

    private static CompletableFuture<Void> loadAnimations(Executor executor, ResourceManager resourceManager,
                                                        String type, BiConsumer<String, List<ResourceLocation>> adults, BiConsumer<String, List<ResourceLocation>> babies) {
        return CompletableFuture.supplyAsync(
                        () -> resourceManager.listResources(type, fileName -> fileName.getNamespace().equals("jsonentitymodels")
                                && fileName.toString().endsWith(".json")), executor)
                .thenApplyAsync(resources -> {
                    Map<String, List<ResourceLocation>> adultAnimations = new Object2ObjectOpenHashMap<>();
                    Map<String, List<ResourceLocation>> babyAnimations = new Object2ObjectOpenHashMap<>();

                    for (ResourceLocation resource : resources.keySet()) {
                        String[] splitPath = resource.getPath().split("/");

                        String modelIdentifier = splitPath[1] + ":" + splitPath[2] + "/" + splitPath[4];

                        if (resource.getPath().contains("/adult/")) {
                            if (adultAnimations.containsKey(modelIdentifier)) {
                                adultAnimations.get(modelIdentifier).add(resource);
                            } else {
                                ArrayList<ResourceLocation> initialList = new ArrayList<>();
                                initialList.add(resource);
                                adultAnimations.put(modelIdentifier, initialList);
                            }
                        } else if (resource.getPath().contains("/baby/")) {
                            if (babyAnimations.containsKey(modelIdentifier)) {
                                babyAnimations.get(modelIdentifier).add(resource);
                            } else {
                                ArrayList<ResourceLocation> initialList = new ArrayList<>();
                                initialList.add(resource);
                                babyAnimations.put(modelIdentifier, initialList);
                            }
                        }
                    }

                    return Pair.of(adultAnimations, babyAnimations);
                }, executor).thenAcceptAsync(models -> {
                    Map<String, List<ResourceLocation>> adultAnimations = models.first();
                    Map<String, List<ResourceLocation>> babyAnimations = models.second();

                    for (Map.Entry<String, List<ResourceLocation>> entry : adultAnimations.entrySet()) {
                        adults.accept(entry.getKey(), entry.getValue());
                    }

                    for (Map.Entry<String, List<ResourceLocation>> entry : babyAnimations.entrySet()) {
                        babies.accept(entry.getKey(), entry.getValue());
                    }

                }, executor);
    }
}