package hok.chompzki.hivetera.items.armor.insects;

import hok.chompzki.hivetera.HiveteraMod;
import hok.chompzki.hivetera.BioHelper;
import hok.chompzki.hivetera.api.IArmorInsect;
import hok.chompzki.hivetera.api.IInsect;
import hok.chompzki.hivetera.api.INestInsect;
import hok.chompzki.hivetera.containers.BioArmor;
import hok.chompzki.hivetera.hunger.logic.EnumResource;
import hok.chompzki.hivetera.items.armor.SocketType;
import hok.chompzki.hivetera.items.insects.ItemInsect;
import hok.chompzki.hivetera.research.events.GameEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

public class ItemFireSprinter extends ItemInsect implements IArmorInsect {
	
	private final static Random rand = new Random();
	public final static String NAME = "itemFireSprinter";
	
	public ItemFireSprinter(){
		super(EnumResource.NURITMENT, 10.0D, 10.0D, true);
		setUnlocalizedName(HiveteraMod.MODID + "_" + NAME);
		setCreativeTab(HiveteraMod.insectTab);
		setTextureName(HiveteraMod.MODID + ":" + NAME);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player)
    {
		if(!par2World.isRemote)
			this.feed(player, stack, false);
		
		player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 600, 2));
		
		return stack;
    }
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
		if(!world.isRemote)
			this.feed(player, stack, false);
		
		player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 600, 2));
		
		return true;
    }

	@Override
	public int addMaxDamage(int currentDmg, BioArmor armor, int slot) {
		return 0;
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player,
		BioArmor[] armors, int type, int slot) {
		
		if(!world.isRemote && player.isBurning() && GameEvents.tickServer % 20 == 0){
			double chance = Math.max(0.1D, 1.0D - ((double)player.getHealth() / player.getMaxHealth()));	
			double roll = rand.nextDouble();
			
			if(roll < chance)
				player.extinguish();
		}
		
		if(player.isSneaking())
			return;
		
		if (world.handleMaterialAcceleration(player.boundingBox.expand(0.0D, 0.0D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.lava, player))
        {
			player.motionY += 0.1D;
        }else if (world.handleMaterialAcceleration(player.boundingBox.expand(0.0D, 2.0D/16.0D, 0.0D), Material.lava, player))
        {
			player.motionY = 0.0D;
			player.isAirBorne = false;
			player.onGround = true;
        }
		
	}
	
	@Override
	public void addProperties(ArmorProperties prop, BioArmor[] armors,
			EntityPlayer player, DamageSource source, double damage,
			int type, int slot) {
		
		
		
	}

	@Override
	public boolean shouldWork(World world, EntityPlayer player,
			BioArmor[] armors, int type, int slot) {
		BioArmor armor = armors[type];
		if(armor == null)
			return false;
		ItemStack stack = armor.stack;
		return stack.getItemDamage() < stack.getMaxDamage();
	}
	
	@Override
	public void damageArmor(World worldObj, EntityPlayer player,
			BioArmor[] armors, int armorType, int i, DamageSource source,
			int damage) {
		
	}
	
	@Override
	public SocketType getType() {
		return SocketType.PASSIVE;
	}
	
	@Override
	public boolean shouldMod(World worldObj, EntityPlayer player) {
		return player.isInWater();
	}

	@Override
	public void applyModifier(IAttributeInstance attribute, AttributeModifier value, EntityPlayer player) {
		
	}

	@Override
	public void removeModifier(IAttributeInstance attribute, AttributeModifier value, EntityPlayer player) {
		
	}

	@Override
	public double getBaseModValue() {
		return 0;
	}

	@Override
	public int getDamageReduction(int dmg, BioArmor armor, int i) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
