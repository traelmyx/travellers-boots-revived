package dev.denismasterherobrine.travellersbootsreloaded.test.neoforge;

//? if neoforge {
import dev.denismasterherobrine.travellersbootsreloaded.test.ClientSmoke;
import dev.denismasterherobrine.travellersbootsreloaded.test.BootsTests;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(NeoForgeGameTests.MOD_ID)
@PrefixGameTestTemplate(false)
@Mod(NeoForgeGameTests.MOD_ID)
public class NeoForgeGameTests {
	public static final String MOD_ID = "travellersbootsreloaded_gametest";

	public NeoForgeGameTests(Dist dist) {
		if (dist.isClient() && ClientSmoke.start()) {
			NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> ClientSmoke.tick(net.minecraft.client.Minecraft.getInstance()));
		}
	}

	@GameTest(template = "empty")
	public static void effectsPerTier(GameTestHelper helper) {
		BootsTests.effectsPerTier(helper);
	}

	@GameTest(template = "empty")
	public static void stepHeight(GameTestHelper helper) {
		BootsTests.stepHeight(helper);
	}

	@GameTest(template = "empty")
	public static void recipes(GameTestHelper helper) {
		BootsTests.recipes(helper);
	}

	@GameTest(template = "empty")
	public static void armorValues(GameTestHelper helper) {
		BootsTests.armorValues(helper);
	}
}
//?}
