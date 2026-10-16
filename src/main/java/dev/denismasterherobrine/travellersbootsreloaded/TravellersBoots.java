package dev.denismasterherobrine.travellersbootsreloaded;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

public final class TravellersBoots {
	public static final String MOD_ID = "travellersbootsreloaded";
	public static final int TIERS = 5;

	private static final int[] DEFENSE = {1, 2, 4, 6, 8};
	private static final int[] DURABILITY = {16, 32, 64, 128, 256};
	private static final ResourceLocation STEP_HEIGHT_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "step_height");

	/** Filled by the loader entry point once the boots are registered, lowest tier first. */
	public static final List<Supplier<Item>> BOOTS = new ArrayList<>();

	public static boolean stepHeightEnabled = true;
	public static final int[] SPEED = {1, 2, 3, 4, 5};
	public static final int[] JUMP = {0, 0, 0, 1, 2};

	private TravellersBoots() {
	}

	public static String name(int tier) {
		return "travellers_boots_tier" + (tier + 1);
	}

	public static ArmorMaterial material(int tier) {
		Holder<SoundEvent> sound = switch (tier) {
			case 0 -> SoundEvents.ARMOR_EQUIP_LEATHER;
			case 1 -> SoundEvents.ARMOR_EQUIP_GOLD;
			case 2 -> SoundEvents.ARMOR_EQUIP_DIAMOND;
			case 3 -> SoundEvents.ARMOR_EQUIP_CHAIN;
			default -> SoundEvents.ARMOR_EQUIP_NETHERITE;
		};
		Item repair = switch (tier) {
			case 0 -> Items.LEATHER;
			case 1 -> Items.GOLD_INGOT;
			case 2 -> Items.DIAMOND;
			case 3 -> Items.EMERALD_BLOCK;
			default -> Items.NETHERITE_SCRAP;
		};
		EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
		for (ArmorItem.Type type : ArmorItem.Type.values()) defense.put(type, type == ArmorItem.Type.BOOTS ? DEFENSE[tier] : 0);
		return new ArmorMaterial(defense, 25, sound, () -> Ingredient.of(repair),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MOD_ID, name(tier)))), 1.0F, 0.0F);
	}

	public static Item boots(Holder<ArmorMaterial> material, int tier) {
		return new ArmorItem(material, ArmorItem.Type.BOOTS, new Item.Properties().durability(DURABILITY[tier]));
	}

	/** 1 to 5 for the boots on the player's feet, 0 for none. */
	public static int tier(Player player) {
		ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
		for (int i = BOOTS.size() - 1; i >= 0; i--) {
			if (feet.is(BOOTS.get(i).get())) return i + 1;
		}
		return 0;
	}

	/** Runs every player tick on both sides: step height everywhere, speed and jump on the server. */
	public static void tick(Player player) {
		int tier = tier(player);
		AttributeInstance step = player.getAttribute(Attributes.STEP_HEIGHT);
		if (step != null) {
			step.removeModifier(STEP_HEIGHT_ID);
			if (stepHeightEnabled && tier > 0 && !player.isShiftKeyDown()) {
				double target = tier > 1 ? 1.25 : 0.75;
				step.addTransientModifier(new AttributeModifier(STEP_HEIGHT_ID, target - step.getBaseValue(), AttributeModifier.Operation.ADD_VALUE));
			}
		}
		if (tier > 0 && !player.level().isClientSide()) {
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, SPEED[tier - 1]));
			player.addEffect(new MobEffectInstance(MobEffects.JUMP, 20, JUMP[tier - 1]));
		}
	}

	/** Same file and keys as the original mod, so existing configs keep working. */
	public static void loadConfig(Path configDir) {
		Path file = configDir.resolve(MOD_ID + ".properties");
		Properties props = new Properties();
		props.setProperty("isStepHeightEnabled", "true");
		for (int i = 0; i < TIERS; i++) props.setProperty("speedModifierTier" + (i + 1), Integer.toString(SPEED[i]));
		props.setProperty("jumpModifierTier4", "1");
		props.setProperty("jumpModifierTier5", "2");
		try {
			if (Files.exists(file)) {
				try (Reader reader = Files.newBufferedReader(file)) {
					props.load(reader);
				}
			} else {
				Files.createDirectories(configDir);
				try (Writer writer = Files.newBufferedWriter(file)) {
					props.store(writer, " Traveller's Boots configuration.\n isStepHeightEnabled: let the boots step up full blocks (hold sneak to turn it off).\n speedModifierTierN: Speed effect level for each pair (0 = Speed I).\n jumpModifierTier4/5: Jump Boost level for the top two pairs.");
				}
			}
		} catch (IOException e) {
			// fall back to the defaults above
		}
		stepHeightEnabled = Boolean.parseBoolean(props.getProperty("isStepHeightEnabled", "true"));
		for (int i = 0; i < TIERS; i++) SPEED[i] = parse(props.getProperty("speedModifierTier" + (i + 1)), SPEED[i]);
		JUMP[3] = parse(props.getProperty("jumpModifierTier4"), JUMP[3]);
		JUMP[4] = parse(props.getProperty("jumpModifierTier5"), JUMP[4]);
	}

	private static int parse(String value, int fallback) {
		try {
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			return fallback;
		}
	}
}
