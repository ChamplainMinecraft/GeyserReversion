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

package au.com.grieve.geyser.reversion.translators.inventory.action;

import lombok.Getter;
import lombok.ToString;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.InventoryTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * A transaction is created when changes are made to the Inventory. This will store changes sent to us from
 * downstream and playback a series of actions.
 */

@Getter
@ToString(onlyExplicitlyIncluded = true)
public class Transaction {
    public static final List<Transaction> TRANSACTIONS = new ArrayList<>();
    public static Transaction CURRENT_TRANSACTION = null;
    private static boolean running = false;
    @ToString.Include
    private final PriorityQueue<BaseAction> actions = new PriorityQueue<>();
    private final GeyserSession session;
    private final InventoryTranslator translator;
    private final Inventory inventory;
    @ToString.Include
    private BaseAction currentAction = null;

    private Transaction(GeyserSession session, InventoryTranslator translator, Inventory inventory) {
        this.session = session;
        this.translator = translator;
        this.inventory = inventory;
    }

    public static Transaction of(GeyserSession session, InventoryTranslator translator, Inventory inventory) {
        Transaction ret = new Transaction(session, translator, inventory);
        TRANSACTIONS.add(ret);

        return ret;
    }

    /**
     * Start Execution of Transactions if not already started
     */
    public static void execute() {
        if (running || TRANSACTIONS.isEmpty()) {
            return;
        }

        running = true;

        nextTransaction();
    }

    public static void nextTransaction() {
        if (TRANSACTIONS.isEmpty()) {
            CURRENT_TRANSACTION = null;
            running = false;
            return;
        }

        CURRENT_TRANSACTION = TRANSACTIONS.remove(0);
        CURRENT_TRANSACTION.start();
    }

    public static void cancel() {
        running = false;
        TRANSACTIONS.clear();
        CURRENT_TRANSACTION = null;
    }

    public void add(BaseAction action) {
        action.setTransaction(this);
        actions.add(action);
    }

    /**
     * Start Transactions
     */
    void start() {
        if (actions.isEmpty()) {
            nextTransaction();
            return;
        }
        next();
    }

    /**
     * Execute the next action
     */
    public void next() {
        if (actions.isEmpty()) {
            currentAction = null;
            nextTransaction();
            return;
        }

        currentAction = actions.remove();
        currentAction.execute();
    }

}
