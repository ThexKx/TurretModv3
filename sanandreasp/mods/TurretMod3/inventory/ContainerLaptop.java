package sanandreasp.mods.TurretMod3.inventory;

import sanandreasp.mods.TurretMod3.item.ItemTurret;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLaptop extends Container {
	private IInventory invBlock;
	private IInventory invPlayer;

	public ContainerLaptop(IInventory block, IInventory player) {
		this.invBlock = block;
		this.invPlayer = player;
		this.invBlock.openChest();
        int var3 = 137;
        int var4;
        int var5;

        for (var4 = 0; var4 < 4; ++var4)
        {
        	this.addSlotToContainer(new Slot(this.invBlock, var4*2, 7, var3 + 18*var4) {
        		@Override
        		public boolean isItemValid(ItemStack par1ItemStack) {
        			return par1ItemStack.getItem() instanceof ItemTurret;
        		}
        		
        		@Override
        		public int getSlotStackLimit() {
        			return 1;
        		}
        	});
        	this.addSlotToContainer(new Slot(this.invBlock, var4*2 + 1, 233, var3 + 18*var4) {
        		@Override
        		public boolean isItemValid(ItemStack par1ItemStack) {
        			return par1ItemStack.getItem() instanceof ItemTurret;
        		}
        		
        		@Override
        		public int getSlotStackLimit() {
        			return 1;
        		}
        	});
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(this.invPlayer, var5 + var4 * 9 + 9, 48 + var5 * 18, var4 * 18 + var3));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(this.invPlayer, var4, 48 + var4 * 18, 58 + var3));
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.invBlock.isUseableByPlayer(entityplayer);
	}
    
    public String getInvName() {
    	return this.invBlock.getInvName();
    }
    
    public String getPInvName() {
    	return this.invBlock.getInvName();
    }

    @Override
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
        super.onCraftGuiClosed(par1EntityPlayer);
        this.invBlock.closeChest();
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < 8)
            {
                if (!this.mergeItemStack(itemstack1, 8, 44, true))
                {
                    return null;
                }
            }
            else if (!(itemstack1.getItem() instanceof ItemTurret && this.mergeOwnItemStack(itemstack1, 0, 8))) {
                return null;
            }
            
            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
    
    protected boolean mergeOwnItemStack(ItemStack par1ItemStack, int par2, int par3) {
        boolean flag1 = false;
        int k = par2;

        Slot slot;
        ItemStack itemstack1;
        
        if(par1ItemStack.stackSize < 1)
        	return false;

        while (k < par3)
        {
            slot = (Slot)this.inventorySlots.get(k);
            itemstack1 = slot.getStack();

            if (itemstack1 == null)
            {
            	ItemStack newIS = par1ItemStack.copy();
            	newIS.stackSize = 1;
                slot.putStack(newIS.copy());
                slot.onSlotChanged();
                par1ItemStack.stackSize -= 1;
                flag1 = true;
                break;
            }

            else
            {
                ++k;
            }
        }

        return flag1;
    }
}
