package dev.denismasterherobrine.travellersbootsreloaded.fabric;

//? if fabric {
/*import dev.denismasterherobrine.travellersbootsreloaded.TravellersBoots;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

public class TravellersBootsFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		TravellersBoots.loadConfig(FabricLoader.getInstance().getConfigDir());
		for (int i = 0; i < TravellersBoots.TIERS; i++) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(TravellersBoots.MOD_ID, TravellersBoots.name(i));
			Holder<ArmorMaterial> material = Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, id, TravellersBoots.material(i));
			Item boots = Registry.register(BuiltInRegistries.ITEM, id, TravellersBoots.boots(material, i));
			TravellersBoots.BOOTS.add(() -> boots);
		}
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(TravellersBoots.MOD_ID, "travellers_boots"),
			FabricItemGroup.builder()
				.title(Component.translatable("travellersbootsreloaded.creative_tab"))
				.icon(() -> TravellersBoots.BOOTS.get(4).get().getDefaultInstance())
				.displayItems((params, output) -> TravellersBoots.BOOTS.forEach(boots -> output.accept(boots.get())))
				.build());
		ServerTickEvents.END_SERVER_TICK.register(server -> server.getPlayerList().getPlayers().forEach(TravellersBoots::tick));
	}
}
*///?}
