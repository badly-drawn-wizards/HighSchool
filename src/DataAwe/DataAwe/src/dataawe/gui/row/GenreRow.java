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
package dataawe.gui.row;

import dataawe.entity.Genre;

/**
 *
 * @author Reuben Steenekamp
 */
public class GenreRow {
    public static final int NAME_COLUMN = 0;
    public static final int DESCRIPTION_COLUMN = 1;
    public static final ItemRow<Genre> INSTANCE = new ItemRow<Genre>() {

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumn(Genre item, int col) {
            switch (col) {
                case NAME_COLUMN:
                    return item.getName();
                case DESCRIPTION_COLUMN:
                    return item.getDescription();
                default:
                    return null;
            }
        }

        @Override
        public String getHeader(int col) {
            switch (col) {
                case NAME_COLUMN:
                    return "Name";
                case DESCRIPTION_COLUMN:
                    return "Description";
                default:
                    return null;
            }
        }
    };
}
