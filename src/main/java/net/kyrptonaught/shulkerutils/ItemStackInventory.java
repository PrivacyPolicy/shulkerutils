package net.kyrptonaught.shulkerutils;


import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;


public class ItemStackInventory extends SimpleInventory {
    protected final ItemStack itemStack;
    protected final int SIZE;

    public ItemStackInventory(ItemStack stack, int SIZE) {
        super(getStacks(stack, SIZE).toArray(new ItemStack[SIZE]));
        itemStack = stack;
        this.SIZE = SIZE;
    }

    public static DefaultedList<ItemStack> getStacks(ItemStack usedStack, int SIZE) {
        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
        if (!usedStack.contains(DataComponentTypes.CONTAINER)) return itemStacks;
        ContainerComponent containerComponent = usedStack.get(DataComponentTypes.CONTAINER);
        containerComponent.copyTo(itemStacks);
        return itemStacks;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!isEmpty()) {
            DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
            for (int i = 0; i < size(); i++) {
                itemStacks.set(i, getStack(i));
            }
            itemStack.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(itemStacks));
        } else {
            itemStack.remove(DataComponentTypes.CONTAINER);
        }
    }

    @Override
    public void onClose(PlayerEntity playerEntity_1) {
        if (itemStack.getCount() > 1) {
            int count = itemStack.getCount();
            itemStack.setCount(1);
            playerEntity_1.giveItemStack(new ItemStack(itemStack.getItem(), count - 1));
        }
        markDirty();
    }
}