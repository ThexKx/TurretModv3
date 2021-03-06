package sanandreasp.mods.TurretMod3.block;

import sanandreasp.mods.TurretMod3.registry.TM3ModRegistry;
import sanandreasp.mods.TurretMod3.tileentity.TileEntityLaptop;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLaptop extends BlockContainer {
	
	@SideOnly(Side.CLIENT)
	public Icon whiteIcon;
	@SideOnly(Side.CLIENT)
	public Icon blackIcon;
	
	public BlockLaptop(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityLaptop();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
		int i = getType(par1);
		switch(i) {
			case 1: return this.blackIcon;
			case 0:
			default:
				return this.whiteIcon;
		}
	}
	
	public static int getType(World par1World, int par2X, int par3Y, int par4Z) {
		return getType(par1World.getBlockMetadata(par2X, par3Y, par4Z));
	}
	
	public static int getType(int i) {
		return (i & 8) >> 3;
	}
	
	public static int getRotation(World par1World, int par2X, int par3Y, int par4Z) {
		return getRotation(par1World.getBlockMetadata(par2X, par3Y, par4Z));
	}
	
	public static int getRotation(int i) {
		return i & 7;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
	{
        byte b0 = 0;
        int l1 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l1 == 0)
            b0 = 2;

        if (l1 == 1)
            b0 = 5;
            
        if (l1 == 2)
            b0 = 3;

        if (l1 == 3)
            b0 = 4;

        par1World.setBlockMetadataWithNotify(par2, par3, par4, b0 | (getType(par6ItemStack.getItemDamage()) << 3), 3);

        if (par6ItemStack.hasDisplayName())
        {
//            ((TileEntityLaptop)par1World.getBlockTileEntity(par2, par3, par4)).func_94043_a(par6ItemStack.getDisplayName());
        }
    }
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
		if(te instanceof TileEntityLaptop) {
			TileEntityLaptop lap = (TileEntityLaptop)te;
			if(lap.isUsedByPlayer)
				return false;
			if(par5EntityPlayer.isSneaking() || !lap.isOpen) {
				par1World.addBlockEvent(par2, par3, par4, this.blockID, 2, lap.isOpen ? 0 : 1);
				return true;
			} else if(lap.isOpen) {
				par5EntityPlayer.openGui(TM3ModRegistry.instance, 3, par1World, par2, par3, par4);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return AxisAlignedBB.getBoundingBox(par2, par3, par4, par2+1F, par3+0.1F, par4+1F);
	}
	
	@Override
	public int getRenderType() {
		return 22;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.whiteIcon = Block.blockNetherQuartz.getBlockTextureFromSideAndMetadata(0,0);
		this.blackIcon = Block.obsidian.getBlockTextureFromSideAndMetadata(0,0);
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityLaptop) {
			TileEntityLaptop lap = (TileEntityLaptop)te;
			if(lap.isOpen && lap.screenAngle >= 0.999F)
				return 5;
		}
		return 0;
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if(tile != null && tile instanceof TileEntityLaptop && !par1World.isRemote) {
			TileEntityLaptop telap = (TileEntityLaptop)tile;
			for(int i = 0; i < telap.getSizeInventory(); i++) {
				ItemStack var3Stack = telap.getStackInSlot(i);
				if(var3Stack != null && var3Stack.stackSize > 0) {
					par1World.spawnEntityInWorld(new EntityItem(par1World, par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, var3Stack));
				}
			}
		}
		if(tile != null)
			tile.invalidate();
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public int damageDropped(int par1) {
		return getType(par1) << 3;
	}
}
