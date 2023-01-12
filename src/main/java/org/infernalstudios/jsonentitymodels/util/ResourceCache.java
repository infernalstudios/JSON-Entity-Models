package org.infernalstudios.jsonentitymodels.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.geo.render.built.GeoModel;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ResourceCache {
    private static ResourceCache INSTANCE;

    private Map<String, List<ResourceLocation>> babyModels = Collections.emptyMap();
    private Map<String, List<ResourceLocation>> adultModels = Collections.emptyMap();
    private Map<ResourceLocation, List<ResourceLocation>> babyTextures = Collections.emptyMap();
    private Map<ResourceLocation, List<ResourceLocation>> adultTextures = Collections.emptyMap();
    private Map<ResourceLocation, List<ResourceLocation>> babyAnimations = Collections.emptyMap();
    private Map<ResourceLocation, List<ResourceLocation>> adultAnimations = Collections.emptyMap();

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
        Map<ResourceLocation, List<ResourceLocation>> babyTextures = new Object2ObjectOpenHashMap<>();
        Map<ResourceLocation, List<ResourceLocation>> adultTextures = new Object2ObjectOpenHashMap<>();
        Map<ResourceLocation, List<ResourceLocation>> babyAnimations = new Object2ObjectOpenHashMap<>();
        Map<ResourceLocation, List<ResourceLocation>> adultAnimations = new Object2ObjectOpenHashMap<>();

        return CompletableFuture.allOf(
                        loadBabyResources(backgroundExecutor, resourceManager, "geo", babyModels::put),
                        loadAdultResources(backgroundExecutor, resourceManager, "geo", adultModels::put),
                        loadBabyResources(backgroundExecutor, resourceManager, "textures/entity", babyTextures::put),
                        loadAdultResources(backgroundExecutor, resourceManager, "textures/entity", adultTextures::put),
                        loadBabyResources(backgroundExecutor, resourceManager, "animations", babyAnimations::put),
                        loadAdultResources(backgroundExecutor, resourceManager, "animations", adultAnimations::put))
                .thenCompose(stage::wait).thenAcceptAsync(empty -> {
                    this.babyAnimations = babyAnimations;
                    this.babyModels = babyModels;
                }, gameExecutor);
    }

    private static <T> CompletableFuture<Void> loadBabyResources(Executor executor, ResourceManager resourceManager,
                                                             String type, BiConsumer<ResourceLocation, T> map) {
        return CompletableFuture.supplyAsync(
                        () -> resourceManager.listResources(type, fileName -> fileName.toString().endsWith(".json") || fileName.toString().endsWith(".png")), executor)
                .thenApplyAsync(resources -> {
                    Map<String, List<ResourceLocation>> tasks = new Object2ObjectOpenHashMap<>();

                    for (ResourceLocation resource : resources.keySet()) {
                        resource.getPath().

                        if (existing != null) {// Possibly if this matters, the last one will win
                            System.err.println("Duplicate resource for " + resource);
                            existing.cancel(false);
                        }
                    }

                    return tasks;
                }, executor).thenAcceptAsync(tasks -> {
                    for (Map.Entry<ResourceLocation, CompletableFuture<T>> entry : tasks.entrySet()) {
                        // Shouldn't be any duplicates as they are caught above
                        // Skips moreplayermodels and customnpc namespaces as they use an animation
                        // folder as well
                        if (entry.getKey().getNamespace().equals("jsonentitymodels") && entry.getKey().getPath().contains("/baby/"))
                            map.accept(entry.getKey(), entry.getValue().join());
                    }
                }, executor);
    }

    private static <T> CompletableFuture<Void> loadAdultResources(Executor executor, ResourceManager resourceManager,
                                                                 String type, BiConsumer<ResourceLocation, T> map) {
        return CompletableFuture.supplyAsync(
                        () -> resourceManager.listResources(type, fileName -> fileName.toString().endsWith(".json") || fileName.toString().endsWith(".png")), executor)
                .thenApplyAsync(resources -> {
                    Map<ResourceLocation, CompletableFuture<T>> tasks = new Object2ObjectOpenHashMap<>();

                    for (ResourceLocation resource : resources.keySet()) {
                        CompletableFuture<T> existing = tasks.put(resource,
                                CompletableFuture.supplyAsync(() -> loader.apply(resource), executor));

                        if (existing != null) {// Possibly if this matters, the last one will win
                            System.err.println("Duplicate resource for " + resource);
                            existing.cancel(false);
                        }
                    }

                    return tasks;
                }, executor).thenAcceptAsync(tasks -> {
                    for (Map.Entry<ResourceLocation, CompletableFuture<T>> entry : tasks.entrySet()) {
                        // Shouldn't be any duplicates as they are caught above
                        if (entry.getKey().getNamespace().equals("jsonentitymodels") && entry.getKey().getPath().contains("/adult/"))
                            map.accept(entry.getKey(), entry.getValue().join());
                    }
                }, executor);
    }
}
