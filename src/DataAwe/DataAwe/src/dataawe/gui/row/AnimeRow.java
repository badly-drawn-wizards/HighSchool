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

import dataawe.entity.Anime;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 *
 * @author Reuben Steenekamp
 */
public class AnimeRow {
    public static final int TITLE_COLUMN = 0;
    public static final int AIRED_COLUMN = 1;
    public static final int GENRE_COLUMN = 2;
    public static final int DUBBED_COLUMN = 3;
    public static final int RATING_COLUMN = 4;
    public static final ItemRow<Anime> INSTANCE = new ItemRow<Anime>() {

        private Format dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumn(Anime item, int col) {
            switch (col) {
                case TITLE_COLUMN:
                    return item.getTitle();
                case AIRED_COLUMN:
                    return dateFormat.format(item.getAired());
                case DUBBED_COLUMN:
                    return item.getDubbed() ? "Yes" : "No";
                case RATING_COLUMN:
                    return ""+item.getRating();
                case GENRE_COLUMN:
                    return item.getGenre().toString();
                default:
                    return null;
            }
        }

        @Override
        public String getHeader(int col) {
            switch (col) {
                case TITLE_COLUMN:
                    return "Title";
                case AIRED_COLUMN:
                    return "Date Aired";
                case DUBBED_COLUMN:
                    return "Dubbed";
                case RATING_COLUMN:
                    return "Rating";
                case GENRE_COLUMN:
                    return "Genre";
                default:
                    return null;
            }
        }
    };
}
