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

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public abstract class BaseAction implements Comparable<BaseAction>, Comparator<BaseAction> {
    // These handle tie breaking with equal priority actions to ensure they are executing FIFO
    final static AtomicInteger SEQUENCE = new AtomicInteger();
    final private int seq = SEQUENCE.getAndIncrement();

    @Setter
    protected Transaction transaction;

    public abstract void execute();

    public int getWeight() {
        return 0;
    }

    @Override
    public int compare(BaseAction left, BaseAction right) {
        int ret = left.getWeight() - right.getWeight();
        if (ret == 0) {
            return left.getSeq() - right.getSeq();
        }
        return ret;
    }

    @Override
    public int compareTo(BaseAction other) {
        int ret = getWeight() - other.getWeight();
        if (ret == 0) {
            return getSeq() - other.getSeq();
        }
        return ret;
    }
}
