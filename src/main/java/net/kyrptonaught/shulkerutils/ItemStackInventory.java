package net.kyrptonaught.shulkerutils;


import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;


public class ItemStackInventory extends SimpleContainer {
    protected final ItemStack itemStack;
    protected final int SIZE;

    public ItemStackInventory(ItemStack stack, int SIZE) {
        super(getStacks(stack, SIZE).toArray(new ItemStack[SIZE]));
        itemStack = stack;
        this.SIZE = SIZE;
    }

    public static NonNullList<ItemStack> getStacks(ItemStack usedStack, int SIZE) {
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        if (!usedStack.has(DataComponents.CONTAINER)) return itemStacks;
        ItemContainerContents containerComponent = usedStack.get(DataComponents.CONTAINER);
        containerComponent.copyInto(itemStacks);
        return itemStacks;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (!isEmpty()) {
            NonNullList<ItemStack> itemStacks = NonNullList.withSize(SIZE, ItemStack.EMPTY);
            for (int i = 0; i < getContainerSize(); i++) {
                itemStacks.set(i, getItem(i));
            }
            itemStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(itemStacks));
        } else {
            itemStack.remove(DataComponents.CONTAINER);
        }
    }

    @Override
    public void stopOpen(ContainerUser user) {
        if (itemStack.getCount() > 1) {
            int count = itemStack.getCount();
            itemStack.setCount(1);
            user.getLivingEntity().handleExtraItemsCreatedOnUse(new ItemStack(itemStack.getItem(), count - 1));
        }
        setChanged();
    }
}