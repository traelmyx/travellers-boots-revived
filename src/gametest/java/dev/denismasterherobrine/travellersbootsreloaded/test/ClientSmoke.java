package dev.denismasterherobrine.travellersbootsreloaded.test;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.GameType;

import dev.denismasterherobrine.travellersbootsreloaded.TravellersBoots;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.nio.file.Files;

// Run with -Dsmoketest=true: creates a flat world, takes a screenshot and quits. Exit code 1 on failure.
public final class ClientSmoke {

    public static final boolean ENABLED = Boolean.getBoolean("smoketest");
    private static final long TIMEOUT_MS = 5 * 60 * 1000;

    private static int stage;
    private static int ticks;

    private ClientSmoke() {
    }

    public static boolean start() {
        if (!ENABLED) {
            return false;
        }
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_MS);
                fail("timed out");
            } catch (InterruptedException ignored) {
            }
        }, "smoketest-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
        return true;
    }

    public static void tick(Minecraft mc) {
        try {
            switch (stage) {
                case 0 -> {
                    Screen screen = SmokeCompat.screen(mc);
                    if (screen != null && SmokeCompat.overlayHidden(mc) && ++ticks >= 20) {
                        if (screen.getClass().getSimpleName().equals("LoadingErrorScreen")) {
                            fail("mod loading errors");
                        }
                        ticks = 0;
                        mc.options.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.NONE;
                        SmokeCompat.createFlatWorld(mc, GameType.SURVIVAL);
                        stage = 1;
                    }
                }
                case 1 -> {
                    if (mc.level != null && mc.player != null && SmokeCompat.screen(mc) == null) {
                        stage = 2;
                    }
                }
                case 2 -> {
                    ticks++;
                    if (ticks == 40) {
                        setup(mc);
                    }
                    if (ticks == 60) {
                        mc.options.setCameraType(net.minecraft.client.CameraType.THIRD_PERSON_FRONT);
                        SmokeCompat.hideGui(mc, true);
                    }
                    if (ticks == 140) {
                        SmokeCompat.screenshot(mc, "wearing.png");
                        mc.options.setCameraType(net.minecraft.client.CameraType.FIRST_PERSON);
                        SmokeCompat.hideGui(mc, false);
                    }
                    if (ticks >= 160) {
                        if (TravellersBoots.tier(mc.player) != 5) {
                            fail("client player is not wearing MK5 boots");
                        }
                        Files.writeString(mc.gameDirectory.toPath().resolve("smoke-ok"), "ok\n");
                        stage = 3;
                        mc.stop();
                    }
                }
                default -> {
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.toString());
        }
    }

    // the player wears the top-tier boots, and an armor stand next to them wears the other four in turn
    private static void setup(Minecraft mc) {
        var server = mc.getSingleplayerServer();
        server.execute(() -> {
            var player = server.getPlayerList().getPlayers().get(0);
            player.setItemSlot(EquipmentSlot.FEET, new ItemStack(TravellersBoots.BOOTS.get(4).get()));
            for (int i = 0; i < 4; i++) {
                double sx = player.getX() + (i < 2 ? -3 - i * 1.5 : 3 + (i - 2) * 1.5);
                server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), String.format(java.util.Locale.ROOT,
                    "summon minecraft:armor_stand %.2f %.2f %.2f {Rotation:[%.1ff,0f]}", sx, player.getY(), player.getZ(), player.getYRot()));
            }
            var stands = player.level().getEntitiesOfClass(net.minecraft.world.entity.decoration.ArmorStand.class, player.getBoundingBox().inflate(8));
            stands.sort(java.util.Comparator.comparingDouble(net.minecraft.world.entity.Entity::getX));
            for (int i = 0; i < stands.size() && i < 4; i++) {
                stands.get(i).setItemSlot(EquipmentSlot.FEET, new ItemStack(TravellersBoots.BOOTS.get(i).get()));
            }
        });
    }

    public static void fail(String why) {
        System.err.println("Smoke test failed: " + why);
        System.out.println("[clientsmoke] FAILED: " + why);
        System.err.flush();
        Runtime.getRuntime().halt(1);
    }
}
