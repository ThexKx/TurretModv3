package sanandreasp.mods.TurretMod3.registry.TurretInfo;

import java.util.Map;

import sanandreasp.mods.TurretMod3.registry.TM3ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class TurretInfoT1Arrow extends TurretInfo {

	public TurretInfoT1Arrow() {
		this.maxAmmo = 256;
		this.maxHealth = 20;
		this.maxEXP = 256;
		this.damage = 2;
//		this.item = new ItemStack(TM3ModRegistry.turretItem, 1, 0);
		this.lowerRangeY = 5.5F;
		this.upperRangeY = 5.5F;
		this.rangeX = 16.5F;
		this.desc = "turretmod3.turret.desct1a";
		this.name = "turretmod3.turret.namet1a";
		this.crafting = new Object[] {
				" D ", " W ", "CCC",
				'D', new ItemStack(Block.dispenser),
				'W', new ItemStack(Block.planks, 0, OreDictionary.WILDCARD_VALUE),
				'C', new ItemStack(Block.cobblestone)
		};
		this.itemIcon = "TurretMod3:turret_01";
		
		int i = this.registerNewAmmoType("turretmod3.turret.amtp0t1a");
		 this.addAmmo(i, new ItemStack(Item.arrow), 1);
		 this.addAmmo(i, new ItemStack(TM3ModRegistry.ammoItems, 1, 0), 8);
		
		this.healItems.put(new ItemStack(Block.cobblestoneMossy), 10);
		this.healItems.put(new ItemStack(Block.cobblestone), 5);
		this.healItems.put(new ItemStack(Block.wood, 1, OreDictionary.WILDCARD_VALUE), 2);
		this.healItems.put(new ItemStack(Block.planks, 1, OreDictionary.WILDCARD_VALUE), 1);
	}
}
