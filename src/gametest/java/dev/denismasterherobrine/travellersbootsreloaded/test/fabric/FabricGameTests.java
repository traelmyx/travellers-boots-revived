package dev.denismasterherobrine.travellersbootsreloaded.test.fabric;

//? if fabric {
/*import dev.denismasterherobrine.travellersbootsreloaded.test.BootsTests;
import net.minecraft.gametest.framework.GameTestHelper;
//? if >=1.21.5 {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//?} else {
/^import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
^///?}

//? if >=1.21.5 {
public class FabricGameTests {
	private static final String EMPTY = "travellersbootsreloaded_gametest:empty";

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void effectsPerTier(GameTestHelper helper) {
		BootsTests.effectsPerTier(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void stepHeight(GameTestHelper helper) {
		BootsTests.stepHeight(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void recipes(GameTestHelper helper) {
		BootsTests.recipes(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void armorValues(GameTestHelper helper) {
		BootsTests.armorValues(helper);
	}
}
//?} else {
/^public class FabricGameTests implements FabricGameTest {
	private static final String EMPTY = "travellersbootsreloaded_gametest:empty";

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void effectsPerTier(GameTestHelper helper) {
		BootsTests.effectsPerTier(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void stepHeight(GameTestHelper helper) {
		BootsTests.stepHeight(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void recipes(GameTestHelper helper) {
		BootsTests.recipes(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void armorValues(GameTestHelper helper) {
		BootsTests.armorValues(helper);
	}
}
^///?}
*///?}
