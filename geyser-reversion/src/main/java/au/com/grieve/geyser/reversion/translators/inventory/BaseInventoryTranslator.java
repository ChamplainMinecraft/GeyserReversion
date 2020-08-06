/*
 * EduSupport - Minecraft Protocol Support for MultiVersion in Geyser
 * Copyright (C) 2020 EduSupport Developers
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.com.grieve.geyser.reversion.translators.inventory;

import au.com.grieve.geyser.reversion.translators.inventory.action.Click;
import au.com.grieve.geyser.reversion.translators.inventory.action.Drop;
import au.com.grieve.geyser.reversion.translators.inventory.action.Execute;
import au.com.grieve.geyser.reversion.translators.inventory.action.Refresh;
import au.com.grieve.geyser.reversion.translators.inventory.action.Transaction;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.InventoryActionData;
import com.nukkitx.protocol.bedrock.data.inventory.InventorySource;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import lombok.NonNull;
import lombok.ToString;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.InventoryTranslator;
import org.geysermc.connector.network.translators.inventory.SlotType;
import org.geysermc.connector.network.translators.item.ItemTranslator;
import org.geysermc.connector.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseInventoryTranslator extends InventoryTranslator {
    protected BaseInventoryTranslator(int size) {
        super(size);
    }

    @Override
    public void updateProperty(GeyserSession session, Inventory inventory, int key, int value) {
        //
    }

    @Override
    public int bedrockSlotToJava(InventoryActionData action) {
        int slotnum = action.getSlot();
        switch (action.getSource().getContainerId()) {
            case ContainerId.INVENTORY:
                //hotbar
                if (slotnum >= 9) {
                    return slotnum + this.size - 9;
                }
                return slotnum + this.size + 27;
            case ContainerId.UI:
                if (action.getSlot() == 0) {
                    return -1;
                }
                break;
        }

        return slotnum;
    }

    @Override
    public int javaSlotToBedrock(int slot) {
        if (slot >= this.size) {
            final int tmp = slot - this.size;
            if (tmp < 27) {
                return tmp + 9;
            } else {
                return tmp - 27;
            }
        }
        return slot;
    }

    /**
     * Return true if the action represents the temporary cursor slot
     *
     * @return boolean true if is the cursor
     */
    public boolean isCursor(InventoryActionData action) {
        return (action.getSource().getContainerId() == ContainerId.UI && action.getSlot() == 0);
    }

    /**
     * Return true if action represents an output slot
     *
     * @return boolean true if an output slot
     */
    public boolean isOutput(InventoryActionData action) {
        return false;
    }


    @Override
    public void translateActions(GeyserSession session, Inventory inventory, List<InventoryActionData> actions) {
        Transaction transaction = Transaction.of(session, BaseInventoryTranslator.this, inventory);
        transaction.add(new Execute(() -> {
            List<ActionData> actionDataList = new ArrayList<>();
            ActionData cursor = null;

            for (InventoryActionData action : actions) {
                ActionData actionData = new ActionData(BaseInventoryTranslator.this, action);

                if (isCursor(action)) {
                    cursor = actionData;
                }
                actionDataList.add(actionData);
            }

            if (cursor == null) {
                // Create a fake cursor action based upon current known cursor
                ItemStack playerCursor = session.getInventory().getCursor();
                if (playerCursor != null) {
                    cursor = new ActionData(BaseInventoryTranslator.this, new InventoryActionData(
                            InventorySource.fromContainerWindowId(124),
                            -1,
                            ItemTranslator.translateToBedrock(session, playerCursor),
                            ItemTranslator.translateToBedrock(session, playerCursor)
                    ));
                } else {
                    cursor = new ActionData(BaseInventoryTranslator.this, new InventoryActionData(
                            InventorySource.fromContainerWindowId(124),
                            -1,
                            ItemData.AIR,
                            ItemData.AIR
                    ));
                }
                actionDataList.add(cursor);
            }

            while (actionDataList.size() > 0) {
                ActionData a1 = actionDataList.remove(0);

                for (ActionData a2 : actionDataList) {

                    // Check if a1 is already fulfilled
                    if (a1.isResolved() || a2.isResolved()) {
                        continue;
                    }

                    // Directions have to be opposite or equal
                    if ((a1.currentCount > a1.toCount && a2.currentCount > a2.toCount)
                            || (a1.currentCount < a1.toCount && a2.currentCount < a2.toCount)) {
                        continue;
                    }

                    // Work out direction
                    ActionData from;
                    ActionData to;
                    if (a1.currentCount > a1.toCount) {
                        from = a1;
                        to = a2;
                    } else {
                        from = a2;
                        to = a1;
                    }

                    // Process
                    processAction(transaction, cursor, from, to);
                }

                // Log unresolved for the moment
                if (a1.remaining() > 0) {
                    GeyserConnector.getInstance().getLogger().warning("Inventory Items Unresolved: " + a1);
                    transaction.add(new Refresh());
                }
            }
        }));

        Transaction.execute();
    }

    protected void processAction(Transaction transaction, ActionData cursor, ActionData from, ActionData to) {
        // Dropping to the world?
        if (to.action.getSource().getFlag() == InventorySource.Flag.DROP_ITEM) {

            // Is it dropped without a window?
            if (transaction.getSession().getInventoryCache().getOpenInventory() == null
                    && from.action.getSource().getContainerId() == ContainerId.INVENTORY
                    && from.action.getSlot() == transaction.getSession().getInventory().getHeldItemSlot()) {

                // Dropping everything?
                if (from.toCount == 0 && from.currentCount <= to.remaining()) {
                    to.currentCount = from.currentCount;
                    from.currentCount = 0;
                    transaction.add(new Drop(Drop.Type.DROP_STACK_HOTBAR, from.javaSlot));
                } else {
                    while (from.remaining() > 0 && to.remaining() > 0) {
                        to.currentCount++;
                        from.currentCount--;
                        transaction.add(new Drop(Drop.Type.DROP_ITEM_HOTBAR, from.javaSlot));
                    }
                }
            } else {

                // Dropping everything?
                if (from.toCount == 0 && from.currentCount <= to.remaining()) {
                    to.currentCount += from.currentCount;
                    from.currentCount = 0;
                    transaction.add(new Drop(Drop.Type.DROP_STACK, from.javaSlot));
                } else {
                    while (from.remaining() > 0 && to.remaining() > 0) {
                        to.currentCount++;
                        from.currentCount--;
                        transaction.add(new Drop(Drop.Type.DROP_ITEM, from.javaSlot));
                    }
                }
            }
            return;
        }

        // Can we swap the contents of to and from? Only applicable if either is the cursor or the cursor is empty
        if ((cursor.currentCount == 0 || cursor == from || cursor == to)
                && (from.getCurrentItem().equals(to.getToItem())
                && to.getCurrentItem().equals(from.getToItem())
                && from.currentCount == to.toCount
                && !from.getCurrentItem().equals(to.getCurrentItem()))) {

            if (from != cursor && to != cursor) {
                transaction.add(new Click(Click.Type.LEFT, from.javaSlot));
                transaction.add(new Click(Click.Type.LEFT, to.javaSlot));
                if (to.currentCount != 0) {
                    transaction.add(new Click(Click.Type.LEFT, from.javaSlot));
                }
            } else {
                transaction.add(new Click(Click.Type.LEFT, from == cursor ? to.javaSlot : from.javaSlot));
            }

            int currentCount = from.currentCount;
            ItemData currentItem = from.getCurrentItem();

            from.currentCount = to.currentCount;
            from.currentItem = to.getCurrentItem();
            to.currentCount = currentCount;
            to.currentItem = currentItem;
            return;
        }

        // Incompatible Items?
        if (!from.getCurrentItem().equals(to.getCurrentItem())
                && !from.getCurrentItem().equals(ItemData.AIR)
                && !to.getCurrentItem().equals(ItemData.AIR)) {
            return;
        }

        // Can we drop anything from cursor onto to?
        if (cursor != to && to.remaining() > 0 && cursor.currentCount > 0 && cursor.getCurrentItem().equals(to.getToItem())
                && (to.getCurrentItem().equals(ItemData.AIR) || to.getCurrentItem().equals(to.getToItem()))) {

            to.currentItem = cursor.getCurrentItem();
            while (cursor.currentCount > 0 && to.remaining() > 0) {
                transaction.add(new Click(Click.Type.RIGHT, to.javaSlot));
                cursor.currentCount--;
                to.currentCount++;
            }
        }

        // If from is not the cursor and the cursor is empty or is to we can pick up from from and drop onto to
        if (from != cursor && (cursor.currentCount == 0 || to == cursor)) {
            if (from.isResolved()) {
                return;
            }

            // If cursor is to and not empty we will have to use a spot slot if possible
            int spareSlot = -1;
            int spareCount = 0;
            if (to == cursor && cursor.currentCount > 0) {
                spareSlot = findTempSlot(transaction.getInventory(), transaction.getSession().getInventory().getCursor(), new ArrayList<>(), true);
                if (spareSlot == -1) {
                    // Failed, so we abort which will force a refresh if a mismatch occurs
                    return;
                }
                transaction.add(new Click(Click.Type.LEFT, spareSlot));
                spareCount = cursor.currentCount;
                cursor.currentCount = 0;
            }

            // Pick up everything
            transaction.add(new Click(Click.Type.LEFT, from.javaSlot));
            cursor.currentCount += from.currentCount;
            cursor.currentItem = from.getCurrentItem();
            from.currentCount = 0;

            // Drop what we don't need if not an output - NOTE This has the chance of leaking items to the cursor
            // due to the fact bedrock allows arbitrary pickup amounts.
            int leak = 0;
            while (from.remaining() > 0) {
                if (!isOutput(from.action)) {
                    transaction.add(new Click(Click.Type.RIGHT, from.javaSlot));
                    cursor.currentCount--;
                    from.currentCount++;
                } else {
                    leak++;
                    from.toCount--;
                }
            }

            // Drop onto to if not the cursor
            if (to != cursor) {
                to.currentItem = cursor.getCurrentItem();
                while (to.remaining() > 0 && cursor.currentCount > 0) {
                    transaction.add(new Click(Click.Type.RIGHT, to.javaSlot));
                    cursor.currentCount--;
                    to.currentCount++;
                }

                // If we have leaks we try drop everything else onto to
                if (leak > 0) {
                    transaction.add(new Click(Click.Type.LEFT, to.javaSlot));
                    to.toCount += leak;
                    to.currentCount += leak;
                    cursor.currentCount -= leak;
                    transaction.add(new Refresh());
                }
            }

            // Pick up spare if needed
            if (spareSlot != -1) {
                if (cursor.currentCount > 0) {
                    // place first
                    transaction.add(new Click(Click.Type.LEFT, spareSlot));
                }

                transaction.add(new Click(Click.Type.LEFT, spareSlot));
                cursor.currentCount += spareCount;
            }
        } else {
            // From is the cursor, so we can assume to is not
            if (to.isResolved()) {
                return;
            }

            // Can we drop everything onto to?
            if (cursor.toCount == 0 && cursor.remaining() > 0 && cursor.remaining() <= to.remaining()) {
                to.currentCount += cursor.currentCount;
                to.currentItem = cursor.getCurrentItem();
                cursor.currentCount = 0;

                transaction.add(new Click(Click.Type.LEFT, to.javaSlot));
            } else {
                // Drop what we need onto to
                to.currentItem = cursor.getCurrentItem();
                while (cursor.remaining() > 0 && to.remaining() > 0) {
                    cursor.currentCount--;
                    to.currentCount++;

                    transaction.add(new Click(Click.Type.RIGHT, to.javaSlot));
                }
            }
        }

        // Can we drop anything from cursor onto to?
        // @TODO: Is this needed still?
        if (cursor != to && to.remaining() > 0 && cursor.currentCount > 0 && cursor.getCurrentItem().equals(to.getToItem())
                && (to.getCurrentItem().equals(ItemData.AIR) || to.getCurrentItem().equals(to.getToItem()))) {

            GeyserConnector.getInstance().getLogger().warning("If no other errors above then the cursor drop is still needed.");

            to.currentItem = cursor.getCurrentItem();

            while (cursor.currentCount > 0 && to.remaining() > 0) {
                transaction.add(new Click(Click.Type.RIGHT, to.javaSlot));
                cursor.currentCount--;
                to.currentCount++;
            }
        }
    }

    private int findTempSlot(Inventory inventory, ItemStack item, List<Integer> slotBlacklist, boolean emptyOnly) {
        /*try and find a slot that can temporarily store the given item
        only look in the main inventory and hotbar
        only slots that are empty or contain a different type of item are valid*/
        int offset = inventory.getId() == 0 ? 1 : 0; //offhand is not a viable slot (some servers disable it)
        List<ItemStack> itemBlacklist = new ArrayList<>(slotBlacklist.size() + 1);
        itemBlacklist.add(item);
        for (int slot : slotBlacklist) {
            ItemStack blacklistItem = inventory.getItem(slot);
            if (blacklistItem != null)
                itemBlacklist.add(blacklistItem);
        }
        for (int i = inventory.getSize() - (36 + offset); i < inventory.getSize() - offset; i++) {
            ItemStack testItem = inventory.getItem(i);
            boolean acceptable = true;
            if (testItem != null) {
                if (emptyOnly) {
                    continue;
                }
                for (ItemStack blacklistItem : itemBlacklist) {
                    if (InventoryUtils.canStack(testItem, blacklistItem)) {
                        acceptable = false;
                        break;
                    }
                }
            }
            if (acceptable && !slotBlacklist.contains(i))
                return i;
        }
        //could not find a viable temp slot
        return -1;
    }

    // We don't use this
    @Override
    public SlotType getSlotType(int i) {
        return null;
    }

    @ToString
    public static class ActionData {
        @ToString.Exclude
        public final InventoryTranslator translator;
        @ToString.Exclude
        public final InventoryActionData action;
        @ToString.Exclude
        public final ItemData toItem;
        public final int javaSlot;
        @ToString.Exclude
        public ItemData currentItem;
        public int currentCount;
        public int toCount;

        public ActionData(InventoryTranslator translator, @NonNull InventoryActionData action) {
            this.translator = translator;
            this.action = action;

            this.toItem = ItemData.of(
                    action.getToItem().getId(),
                    action.getToItem().getDamage(),
                    0,
                    action.getToItem().getTag(),
                    action.getToItem().getCanPlace(),
                    action.getToItem().getCanBreak(),
                    action.getToItem().getBlockingTicks()
            );
            this.currentItem = ItemData.of(
                    action.getFromItem().getId(),
                    action.getFromItem().getDamage(),
                    0,
                    action.getFromItem().getTag(),
                    action.getFromItem().getCanPlace(),
                    action.getFromItem().getCanBreak(),
                    action.getFromItem().getBlockingTicks()
            );

            this.toCount = action.getToItem().getCount();
            this.currentCount = action.getFromItem().getCount();
            this.javaSlot = translator.bedrockSlotToJava(action);
        }

        public ItemData getCurrentItem() {
            return currentCount == 0 ? ItemData.AIR : currentItem;
        }

        public ItemData getToItem() {
            return toCount == 0 ? ItemData.AIR : toItem;
        }

        public int remaining() {
            return Math.abs(toCount - currentCount);
        }

        public boolean isResolved() {
            return remaining() == 0 && getCurrentItem().equals(getToItem());
        }

    }
}
