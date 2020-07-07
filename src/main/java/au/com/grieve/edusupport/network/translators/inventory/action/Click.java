/*
 * EduSupport - Minecraft Educational Support for Geyser
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

package au.com.grieve.edusupport.network.translators.inventory.action;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.ShiftClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.geysermc.connector.inventory.PlayerInventory;
import org.geysermc.connector.utils.InventoryUtils;

/**
 * Send a Left, Right or Shift+Click to the Downstream Server
 */
@Getter
@ToString
public class Click extends ConfirmAction {

    private final Type clickType;
    private final int javaSlot;

    @Setter
    private boolean refresh;

    public Click(Type clickType, int javaSlot, boolean refresh) {
        this.clickType = clickType;
        this.javaSlot = javaSlot;
        this.refresh = refresh;
    }

    public Click(Type clickType, int javaSlot) {
        this(clickType, javaSlot, false);
    }

    @Override
    public void execute() {
        super.execute();
        ItemStack clickedItem = transaction.getInventory().getItem(javaSlot);
        PlayerInventory playerInventory = transaction.getSession().getInventory();
        final ItemStack cursorItem = playerInventory.getCursor();

        ClientWindowActionPacket clickPacket;

        switch (clickType) {
            case LEFT:
                clickPacket = new ClientWindowActionPacket(transaction.getInventory().getId(),
                        id, javaSlot, refresh ? InventoryUtils.REFRESH_ITEM : clickedItem,
                        WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);

                if (!InventoryUtils.canStack(cursorItem, clickedItem)) {
                    playerInventory.setCursor(clickedItem);
                    transaction.getInventory().setItem(javaSlot, cursorItem);
                } else {
                    playerInventory.setCursor(null);
                    transaction.getInventory().setItem(javaSlot, new ItemStack(clickedItem.getId(),
                            clickedItem.getAmount() + cursorItem.getAmount(), clickedItem.getNbt()));
                }
                transaction.getSession().sendDownstreamPacket(clickPacket);
                break;

            case RIGHT:
                clickPacket = new ClientWindowActionPacket(transaction.getInventory().getId(),
                        id, javaSlot, refresh ? InventoryUtils.REFRESH_ITEM : clickedItem,
                        WindowAction.CLICK_ITEM, ClickItemParam.RIGHT_CLICK);

                if (cursorItem == null && clickedItem != null) {
                    ItemStack halfItem = new ItemStack(clickedItem.getId(),
                            clickedItem.getAmount() / 2, clickedItem.getNbt());
                    transaction.getInventory().setItem(javaSlot, halfItem);
                    playerInventory.setCursor(new ItemStack(clickedItem.getId(),
                            clickedItem.getAmount() - halfItem.getAmount(), clickedItem.getNbt()));
                } else if (cursorItem != null && clickedItem == null) {
                    playerInventory.setCursor(new ItemStack(cursorItem.getId(),
                            cursorItem.getAmount() - 1, cursorItem.getNbt()));
                    transaction.getInventory().setItem(javaSlot, new ItemStack(cursorItem.getId(),
                            1, cursorItem.getNbt()));
                } else if (InventoryUtils.canStack(cursorItem, clickedItem)) {
                    playerInventory.setCursor(new ItemStack(cursorItem.getId(),
                            cursorItem.getAmount() - 1, cursorItem.getNbt()));
                    transaction.getInventory().setItem(javaSlot, new ItemStack(clickedItem.getId(),
                            clickedItem.getAmount() + 1, clickedItem.getNbt()));
                }
                transaction.getSession().sendDownstreamPacket(clickPacket);
                break;

            case SHIFT_CLICK:
                clickedItem = transaction.getInventory().getItem(javaSlot);

                ClientWindowActionPacket shiftClickPacket = new ClientWindowActionPacket(
                        transaction.getInventory().getId(),
                        id,
                        javaSlot, clickedItem,
                        WindowAction.SHIFT_CLICK_ITEM,
                        ShiftClickItemParam.LEFT_CLICK
                );
                transaction.getSession().sendDownstreamPacket(shiftClickPacket);
                break;
        }
    }

    public enum Type {
        LEFT,
        RIGHT,
        SHIFT_CLICK
    }
}
