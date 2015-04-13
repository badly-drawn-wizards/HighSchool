/* 
 * Copyright (C) 2014 Reuben Steenekamp
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
package dataawe.gui.form;

import java.awt.Component;

/**
 * Interface to get and set an item on a {@link Component}
 * @author Reuben Steenekamp
 * @param <T> the type of item to edit
 */
public interface ItemEditor<T> {
    public void reset();
    public void setItem(T item);
    public T getItem();
    public Component getComponent();
    public void setEnabled(boolean enabled);
}
