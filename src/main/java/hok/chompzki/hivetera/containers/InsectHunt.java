package hok.chompzki.hivetera.containers;

import hok.chompzki.hivetera.NBTHelper;
import hok.chompzki.hivetera.items.insects.ItemHivebag;
import hok.chompzki.hivetera.registrys.ConfigRegistry;

import java.awt.Color;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class InsectHunt implements IInventory {
	
	public class InsectHuntBase{
		public ItemStack slot = null;
	}
	
	ItemStack stack = null;
	public final int size = 1 + 5 * 5;
	public InsectHuntBase[] inventory = new InsectHuntBase[size];
	EntityPlayer player = null;
	int slot = -1;	
	
	private Random rand = new Random();
	
	public int points = 0;
	
	
	public InsectHunt(EntityPlayer player2, int slot) {
		this.player = player2;
		this.stack = player2.inventory.getStackInSlot(slot);
		this.slot = slot;
		this.readFrom();
	}

	public void readFrom(){
		this.points = NBTHelper.get(stack, "POINTS", 0);
		NBTTagCompound nbt;
		if(slot == -1)
			nbt = player.inventory.getCurrentItem().getTagCompound();
		else
			nbt = player.inventory.getStackInSlot(slot).getTagCompound();
		
		for(int i = 0; i < size; i++)
			this.inventory[i] = new InsectHuntBase();
		
		if(!nbt.hasKey("INVENTORY")){
			NBTTagList nbttaglist = new NBTTagList();
			stack.stackTagCompound.setTag("INVENTORY", nbttaglist);
		}
		
		NBTTagList list = nbt.getTagList("INVENTORY", NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = list.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;
            
            if (j >= 0 && j < this.inventory.length)
            {
            	this.inventory[j] = new InsectHuntBase();
            	this.inventory[j].slot = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
	}
	
	public void writeTo(){
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setInteger("POINTS", this.points);
		nbt.setBoolean("BEETLE", this.inventory[0].slot != null);
		
		NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i].slot != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].slot.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        
        nbt.setTag("INVENTORY", nbttaglist);
        if(slot == -1)
        	player.inventory.getCurrentItem().setTagCompound(nbt);
        else
        	player.inventory.getStackInSlot(slot).setTagCompound(nbt);
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return this.inventory[var1].slot;
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.inventory[par1] != null)
        {
            ItemStack itemstack;
            if (this.inventory[par1].slot.stackSize <= par2)
            {
                itemstack = this.inventory[par1].slot;
                this.inventory[par1].slot = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.inventory[par1].slot.splitStack(par2);

                if (this.inventory[par1].slot.stackSize == 0)
                {
                    this.inventory[par1].slot = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.inventory[par1] != null)
        {
            ItemStack itemstack = this.inventory[par1].slot;
            this.inventory[par1].slot = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.inventory[par1].slot = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "container.insect.hunt";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void markDirty() {
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0)
				inventory[i].slot = null;
		}
		writeTo();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openInventory() {	}
	
	@Override
	public void closeInventory() {	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return !(var2.getItem() instanceof ItemHivebag);
	}
	
	public void tickAll() {
		
		this.markDirty();
	}
	
	public int canInsertStack(ItemStack stack){
		if(stack == null)
			return -1;
		for(int i = 0; i < this.getSizeInventory(); i++){
			ItemStack temp = this.getStackInSlot(i);
			if(temp == null)
				return i;
			if(temp.isItemEqual(stack) && (temp.stackSize + stack.stackSize) <= this.getInventoryStackLimit())
				return i;
		}
		return -1;
	}

}

