package sanandreasp.mods.TurretMod3.entity.turret;

import java.util.AbstractMap.SimpleEntry;

import sanandreasp.mods.TurretMod3.entity.projectile.TurretProj_Arrow;
import sanandreasp.mods.TurretMod3.entity.projectile.TurretProj_Bullet;
import sanandreasp.mods.TurretMod3.entity.projectile.TurretProj_Flame;
import sanandreasp.mods.TurretMod3.entity.projectile.TurretProj_Laser;
import sanandreasp.mods.TurretMod3.entity.projectile.TurretProj_Pebble;
import sanandreasp.mods.TurretMod3.entity.projectile.TurretProjectile;
import sanandreasp.mods.TurretMod3.registry.AchievementPageTM;
import sanandreasp.mods.TurretMod3.registry.TM3ModRegistry;
import sanandreasp.mods.TurretMod3.registry.TurretUpgrades.TUpgInfAmmo;
import sanandreasp.mods.TurretMod3.registry.TurretUpgrades.TUpgPurify;
import sanandreasp.mods.TurretMod3.registry.TurretUpgrades.TurretUpgrades;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTurret_T3Flamethrower extends EntityTurret_Base {
	
	private int soundTicks = 0;
	private boolean shouldWasteAmmo = false;

	public EntityTurret_T3Flamethrower(World par1World) {
		super(par1World);
		this.wdtRange = 8.5D;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTexture() {
		return TM3ModRegistry.TEX_TURRETDIR + "t3Flamethrower.png";
	}
	
	@Override
	public int getMaxHealth() {
		return 60;
	}
	
	@Override
	protected void decrAmmo() {
		if(shouldWasteAmmo)
			super.decrAmmo();
		this.shouldWasteAmmo = !this.shouldWasteAmmo;
	}

	@Override
	public TurretProjectile getProjectile() {
		return new TurretProj_Flame(this.worldObj);
	}
	
	@Override
	public void shootProjectile(boolean isRidden) {
		for(int i = 0; i < 5; i++) {
			TurretProj_Flame var2 = (TurretProj_Flame) this.getProjectile();
	        var2.isPickupable = false;
	        if(TurretUpgrades.hasUpgrade(TUpgPurify.class, this.upgrades))
	        	var2.setPurified();
	        if(isRidden) {
	        	EntityPlayer player = (EntityPlayer) this.riddenByEntity;
	        	var2.hasNoTarget = true;
	        	var2.setLocationAndAngles(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, player.rotationYaw, player.rotationPitch);
	        	var2.posX -= (double)(MathHelper.cos(var2.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
	        	var2.posY -= 0.10000000149011612D;
	        	var2.posZ -= (double)(MathHelper.sin(var2.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
	        	var2.setPosition(var2.posX, var2.posY, var2.posZ);
	        	var2.yOffset = 0.0F;
	        	var2.motionX = (double)(-MathHelper.sin(var2.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(var2.rotationPitch / 180.0F * (float)Math.PI));
	        	var2.motionZ = (double)(MathHelper.cos(var2.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(var2.rotationPitch / 180.0F * (float)Math.PI));
	        	var2.motionY = (double)(-MathHelper.sin(var2.rotationPitch / 180.0F * (float)Math.PI));
	        	var2.setHeading(var2.motionX, var2.motionY, var2.motionZ, 1F * 1.5F, 6.0F);
	        	var2.shootingEntity = this;
	        } else {
	        	var2.setTarget(this, this.currentTarget, 1.4F, 0.0F);
	        	var2.motionX /= var2.getSpeedVal();
	        	var2.motionY /= var2.getSpeedVal();
	        	var2.motionZ /= var2.getSpeedVal();
	        	var2.setHeading(var2.motionX, var2.motionY, var2.motionZ, 1F * 1.0F, 6.0F);
	        }
	        this.worldObj.spawnEntityInWorld(var2);
	        var2.isMoving = true;
		}
		double rotYawX = Math.sin((this.rotationYawHead / 180) * Math.PI);
		double rotYawZ = Math.cos((this.rotationYawHead / 180) * Math.PI);
		double partX = this.posX - rotYawX * (Math.cos(this.rotationPitch / (180F / (float)Math.PI))) * 0.7D;
		double partY = this.posY + this.getEyeHeight() - Math.sin(this.rotationPitch / (180F / (float)Math.PI)) * 0.7D;
		double partZ = this.posZ + rotYawZ * (Math.cos(this.rotationPitch / (180F / (float)Math.PI))) * 0.7D;
		
		TM3ModRegistry.proxy.spawnParticle(2, partX, partY, partZ, 64, this.worldObj.getWorldInfo().getDimension(), this);
		
	    this.worldObj.playSoundAtEntity(this, this.getShootSound(), 1.5F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
	}
	
	@Override
	public int getMaxShootTicks() {
		return 1;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		this.soundTicks++;
		if(this.soundTicks == 4)
			this.soundTicks = 0;
	}
	
	@Override
	public int getMaxAmmo() {
		return 512;
	}
	
	@Override
	public boolean hasFireImmunity() {
		return true;
	}

	@Override
	public String getGlowTexture() {
		return TM3ModRegistry.TEX_TURRETDIR + "t3FlamethrowerG.png";
	}
	
	@Override
	public String getShootSound() {
		return this.soundTicks == 0 ? "turretmod3.shoot.flamethrower" : "";
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		this.shouldWasteAmmo = par1nbtTagCompound.getBoolean("shouldWasteAmmo");
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setBoolean("shouldWasteAmmo", this.shouldWasteAmmo);
	}
}
