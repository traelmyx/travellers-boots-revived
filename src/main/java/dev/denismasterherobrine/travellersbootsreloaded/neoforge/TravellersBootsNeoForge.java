package dev.denismasterherobrine.travellersbootsreloaded.neoforge;

//? if neoforge {
import dev.denismasterherobrine.travellersbootsreloaded.TravellersBoots;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
//? if <1.21.11 {
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredHolder;
//?} else {
/*import net.minecraft.resources.ResourceKey;
*///?}
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(TravellersBoots.MOD_ID)
public class TravellersBootsNeoForge {
	//? if <1.21.11
	private static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, TravellersBoots.MOD_ID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TravellersBoots.MOD_ID);
	private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TravellersBoots.MOD_ID);

	static {
		for (int i = 0; i < TravellersBoots.TIERS; i++) {
			int tier = i;
			//? if >=1.21.11 {
			/*TravellersBoots.BOOTS.add(ITEMS.register(TravellersBoots.name(tier), id -> TravellersBoots.boots(ResourceKey.create(Registries.ITEM, id), tier)));
			*///?} else {
			DeferredHolder<ArmorMaterial, ArmorMaterial> material = MATERIALS.register(TravellersBoots.name(tier), () -> TravellersBoots.material(tier));
			TravellersBoots.BOOTS.add(ITEMS.register(TravellersBoots.name(tier), () -> TravellersBoots.boots(material, tier)));
			//?}
		}
		TABS.register("travellers_boots", () -> CreativeModeTab.builder()
			.title(Component.translatable("travellersbootsreloaded.creative_tab"))
			.icon(() -> TravellersBoots.BOOTS.get(4).get().getDefaultInstance())
			.displayItems((params, output) -> TravellersBoots.BOOTS.forEach(boots -> output.accept(boots.get())))
			.build());
	}

	public TravellersBootsNeoForge(IEventBus modBus) {
		TravellersBoots.loadConfig(FMLPaths.CONFIGDIR.get());
		//? if <1.21.11
		MATERIALS.register(modBus);
		ITEMS.register(modBus);
		TABS.register(modBus);
		NeoForge.EVENT_BUS.addListener((PlayerTickEvent.Pre e) -> TravellersBoots.tick(e.getEntity()));
	}
}
//?}
