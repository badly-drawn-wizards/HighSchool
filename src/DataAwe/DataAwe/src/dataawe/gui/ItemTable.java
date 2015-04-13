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

package dataawe.gui;

import dataawe.gui.row.ItemRow;
import java.util.List;
import java.util.Observer;
import java.util.function.Supplier;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Reuben Steenekamp
 */
public class ItemTable<T> extends javax.swing.JPanel {
    private Observable itemListObservable;
    private Supplier<List<T>> itemListSupplier;
    private ItemRow<T> itemRow;
    
    private Observer itemListObserver;
    private AbstractTableModel tableModel;
    private TableColumnModel columnModel;
    
    private List<T> items;

    /**
     * Creates new form ItemTable
     * @param itemListObservable
     * @param itemListSupplier
     * @param itemRow
     */
    public ItemTable(Observable itemListObservable, Supplier<List<T>> itemListSupplier, ItemRow<T> itemRow) {
        initComponents();
        this.itemListSupplier = itemListSupplier;
        this.itemListObservable = itemListObservable;
        this.itemRow = itemRow;
        this.tableModel = new AbstractTableModel() {
            @Override
            public String getColumnName(int col) {
                return itemRow.getHeader(col);
            }
            
            @Override
            public int getRowCount() {
                return items == null ? 0 : items.size();
            }
            @Override
            public int getColumnCount() {
                return itemRow.getColumnCount();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return items == null ? null : itemRow.getColumn(items.get(rowIndex), columnIndex);
            }
        };
        table.setModel(tableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.itemListObserver = (obs, val) -> {
            this.items = itemListSupplier.get();
            tableModel.fireTableDataChanged();
        };
        itemListObservable.addObserver(itemListObserver);
        itemListObserver.update(null, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableSrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableSrollPane.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableSrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(tableSrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableSrollPane;
    // End of variables declaration//GEN-END:variables

}