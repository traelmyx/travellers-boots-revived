package dev.denismasterherobrine.travellersbootsreloaded.test;

import dev.denismasterherobrine.travellersbootsreloaded.TravellersBoots;
import net.minecraft.gametest.framework.GameTestHelper;
//? if >=1.21.11 {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class BootsTests {
	private BootsTests() {
	}

	//? if >=1.21.11 {
	/*private static final net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> SPEED = MobEffects.SPEED, JUMP = MobEffects.JUMP_BOOST;
	*///?} else {
	private static final net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> SPEED = MobEffects.MOVEMENT_SPEED, JUMP = MobEffects.JUMP;
	//?}

	private static int defense(ItemStack stack) {
		//? if >=1.21.11 {
		/*double armor = 0;
		for (var entry : stack.getOrDefault(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, net.minecraft.world.item.component.ItemAttributeModifiers.EMPTY).modifiers()) {
			if (entry.attribute().equals(Attributes.ARMOR)) armor += entry.modifier().amount();
		}
		return (int) armor;
		*///?} else {
		return ((net.minecraft.world.item.ArmorItem) stack.getItem()).getDefense();
		//?}
	}

	private static ItemStack boots(int tier) {
		return new ItemStack(TravellersBoots.BOOTS.get(tier - 1).get());
	}

	/** Each pair speeds you up, MK4 and MK5 add jump boost, and the effects stop when you take them off. */
	public static void effectsPerTier(GameTestHelper helper) {
		ServerPlayer player = helper.makeMockServerPlayerInLevel();
		for (int tier = 1; tier <= TravellersBoots.TIERS; tier++) {
			player.removeAllEffects();
			player.setItemSlot(EquipmentSlot.FEET, boots(tier));
			TravellersBoots.tick(player);
			var speed = player.getEffect(SPEED);
			helper.assertTrue(speed != null && speed.getAmplifier() == TravellersBoots.SPEED[tier - 1], "MK" + tier + " speed: " + speed);
			var jump = player.getEffect(JUMP);
			helper.assertTrue(jump != null && jump.getAmplifier() == TravellersBoots.JUMP[tier - 1], "MK" + tier + " jump: " + jump);
		}
		player.removeAllEffects();
		player.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
		TravellersBoots.tick(player);
		helper.assertTrue(player.getEffect(SPEED) == null, "plain boots gave speed");
		helper.succeed();
	}

	/** MK1 steps 0.75 blocks, the rest a full block and a bit; sneaking turns it off. */
	public static void stepHeight(GameTestHelper helper) {
		ServerPlayer player = helper.makeMockServerPlayerInLevel();
		player.setItemSlot(EquipmentSlot.FEET, boots(1));
		TravellersBoots.tick(player);
		helper.assertTrue(Math.abs(player.getAttributeValue(Attributes.STEP_HEIGHT) - 0.75) < 1e-6, "MK1 step " + player.getAttributeValue(Attributes.STEP_HEIGHT));
		player.setItemSlot(EquipmentSlot.FEET, boots(3));
		TravellersBoots.tick(player);
		helper.assertTrue(Math.abs(player.getAttributeValue(Attributes.STEP_HEIGHT) - 1.25) < 1e-6, "MK3 step " + player.getAttributeValue(Attributes.STEP_HEIGHT));
		player.setShiftKeyDown(true);
		TravellersBoots.tick(player);
		helper.assertTrue(Math.abs(player.getAttributeValue(Attributes.STEP_HEIGHT) - 0.6) < 1e-6, "sneaking step " + player.getAttributeValue(Attributes.STEP_HEIGHT));
		player.setShiftKeyDown(false);
		player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
		TravellersBoots.tick(player);
		helper.assertTrue(Math.abs(player.getAttributeValue(Attributes.STEP_HEIGHT) - 0.6) < 1e-6, "step height left over");
		helper.succeed();
	}

	/** All five recipes load and MK1 crafts from leather boots. */
	public static void recipes(GameTestHelper helper) {
		//? if >=1.21.11 {
		/*var manager = helper.getLevel().recipeAccess();
		*///?} else {
		var manager = helper.getLevel().getRecipeManager();
		//?}
		List<String> missing = new ArrayList<>();
		for (int tier = 1; tier <= TravellersBoots.TIERS; tier++) {
			//? if >=1.21.11 {
			/*ResourceKey<net.minecraft.world.item.crafting.Recipe<?>> id = ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(TravellersBoots.MOD_ID, TravellersBoots.name(tier - 1)));
			*///?} else {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(TravellersBoots.MOD_ID, TravellersBoots.name(tier - 1));
			//?}
			if (manager.byKey(id).isEmpty()) missing.add(id.toString());
		}
		helper.assertTrue(missing.isEmpty(), "missing recipes: " + missing);
		CraftingInput input = CraftingInput.of(3, 2, List.of(
			new ItemStack(Items.STRING), new ItemStack(Items.LEATHER_BOOTS), new ItemStack(Items.STRING),
			new ItemStack(Items.LEATHER), new ItemStack(Items.FEATHER), new ItemStack(Items.LEATHER)));
		var result = manager.getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel())
			//? if >=26 {
			/*.map(r -> r.value().assemble(input)).orElse(ItemStack.EMPTY);
			*///?} else {
			.map(r -> r.value().assemble(input, helper.getLevel().registryAccess())).orElse(ItemStack.EMPTY);
			//?}
		helper.assertTrue(result.is(TravellersBoots.BOOTS.get(0).get()), "MK1 recipe gave " + result);
		helper.succeed();
	}

	/** The armor value goes up with each pair. */
	public static void armorValues(GameTestHelper helper) {
		int last = -1;
		for (int tier = 1; tier <= TravellersBoots.TIERS; tier++) {
			int armor = defense(boots(tier));
			helper.assertTrue(armor > last, "MK" + tier + " armor " + armor + " not above " + last);
			last = armor;
		}
		helper.succeed();
	}
}
