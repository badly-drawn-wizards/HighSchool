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

import dataawe.gui.form.ItemEditor;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;
import java.util.Observer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Reuben Steenekamp
 * @param <T>
 */
public class ItemListEditor<T> extends javax.swing.JPanel {
    private Frame parent;
    private Observable itemListObservable;
    private Observer itemListObserver;
    private Supplier<ItemEditor<T>> itemEditorSupplier;
    private ItemEditor<T> itemEditor = null;
    private Consumer<T> createFunc, updateFunc, deleteFunc;
    
    private List<T> items;
    private String query = "";
    private T selected = null;
    

    /**
     * Creates new form ListEditor
     * @param parent
     * @param itemListObservable
     * @param listSupplier
     * @param itemEditorSupplier
     * @param createFunc
     * @param updateFunc
     * @param deleteFunc
     */
    public ItemListEditor(Frame parent, Observable itemListObservable, Supplier<List<T>> listSupplier, Supplier<ItemEditor<T>> itemEditorSupplier, Consumer<T> createFunc, Consumer<T> updateFunc, Consumer<T> deleteFunc) {
        initComponents();
        
        this.parent = parent;
        this.createFunc = createFunc;
        this.updateFunc = updateFunc;
        this.deleteFunc = deleteFunc;
        
        itemListObserver = (obs, val) -> {
            this.items = listSupplier.get();
            refreshList();
        };
        this.itemListObservable = itemListObservable;
        itemListObservable.addObserver(itemListObserver);
        itemListObserver.update(null, null);
        
        this.itemEditorSupplier = itemEditorSupplier;
        this.itemEditor = itemEditorSupplier.get();
        if(itemEditor != null && itemEditor.getComponent() != null) {
            itemEditor.setEnabled(false);
            detailPane.add(itemEditor.getComponent(), BorderLayout.CENTER);
        }
        
        refreshSelected();
    }
    
    private void refreshList() {
            DefaultListModel<T> listModel = new DefaultListModel<>();
            for(T item : items) {
                String name = item.toString();
                if(!name.toLowerCase().contains(query.toLowerCase())) continue;
                listModel.addElement(item);
            }
            itemList.setModel(listModel);
    }
    
    private void refreshSelected() {
        int selectedIdx = itemList.getSelectedIndex();
        int rows = itemList.getModel().getSize();
        prevButton.setEnabled(selectedIdx > 0);
        nextButton.setEnabled(selectedIdx != -1 && selectedIdx < rows-1);
        firstButton.setEnabled(rows > 0 && selectedIdx != 0);
        lastButton.setEnabled(rows > 0 && selectedIdx != rows-1);
        updateButton.setEnabled(selected!=null);
        deleteButton.setEnabled(selected!=null);
        if(selected!=null) 
            itemEditor.setItem(selected);
        else
            itemEditor.reset();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchLabel = new javax.swing.JLabel();
        splitPane = new javax.swing.JSplitPane();
        listPane = new javax.swing.JPanel();
        itemListScrollPane = new javax.swing.JScrollPane();
        itemList = new javax.swing.JList<T>();
        searchFieldPane = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        prevButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        firstButton = new javax.swing.JButton();
        lastButton = new javax.swing.JButton();
        itemPane = new javax.swing.JPanel();
        detailPane = new javax.swing.JPanel();
        actionPane = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        searchLabel.setText("Search:");

        setPreferredSize(new java.awt.Dimension(600, 400));

        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(3);
        splitPane.setResizeWeight(0.3);

        listPane.setMinimumSize(new java.awt.Dimension(200, 0));
        listPane.setName(""); // NOI18N
        listPane.setPreferredSize(new java.awt.Dimension(0, 0));

        itemList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                itemListValueChanged(evt);
            }
        });
        itemListScrollPane.setViewportView(itemList);

        searchFieldPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout searchFieldPaneLayout = new javax.swing.GroupLayout(searchFieldPane);
        searchFieldPane.setLayout(searchFieldPaneLayout);
        searchFieldPaneLayout.setHorizontalGroup(
            searchFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchFieldPaneLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(searchField)
                .addGap(0, 0, 0))
        );
        searchFieldPaneLayout.setVerticalGroup(
            searchFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        buttonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Navigation"));

        prevButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        prevButton.setText("<");
        prevButton.setToolTipText("Previous");
        prevButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
        prevButton.setMaximumSize(new java.awt.Dimension(19, 23));
        prevButton.setMinimumSize(new java.awt.Dimension(0, 0));
        prevButton.setPreferredSize(new java.awt.Dimension(19, 23));
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        nextButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        nextButton.setText(">");
        nextButton.setToolTipText("Next");
        nextButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
        nextButton.setMaximumSize(new java.awt.Dimension(19, 23));
        nextButton.setMinimumSize(new java.awt.Dimension(0, 0));
        nextButton.setPreferredSize(new java.awt.Dimension(19, 23));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        firstButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        firstButton.setText("<<");
        firstButton.setToolTipText("First");
        firstButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
        firstButton.setMaximumSize(new java.awt.Dimension(19, 23));
        firstButton.setMinimumSize(new java.awt.Dimension(0, 0));
        firstButton.setPreferredSize(new java.awt.Dimension(19, 23));
        firstButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstButtonActionPerformed(evt);
            }
        });

        lastButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lastButton.setText(">>");
        lastButton.setToolTipText("Last");
        lastButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
        lastButton.setMaximumSize(new java.awt.Dimension(19, 23));
        lastButton.setMinimumSize(new java.awt.Dimension(0, 0));
        lastButton.setPreferredSize(new java.awt.Dimension(19, 23));
        lastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addComponent(firstButton, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prevButton, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastButton, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prevButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout listPaneLayout = new javax.swing.GroupLayout(listPane);
        listPane.setLayout(listPaneLayout);
        listPaneLayout.setHorizontalGroup(
            listPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(searchFieldPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(itemListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        listPaneLayout.setVerticalGroup(
            listPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listPaneLayout.createSequentialGroup()
                .addComponent(itemListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchFieldPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        splitPane.setLeftComponent(listPane);
        listPane.getAccessibleContext().setAccessibleName("");
        listPane.getAccessibleContext().setAccessibleDescription("");

        itemPane.setPreferredSize(new java.awt.Dimension(0, 0));

        detailPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Detail"));
        detailPane.setLayout(new java.awt.BorderLayout());

        actionPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Action"));

        createButton.setText("Create");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout actionPaneLayout = new javax.swing.GroupLayout(actionPane);
        actionPane.setLayout(actionPaneLayout);
        actionPaneLayout.setHorizontalGroup(
            actionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPaneLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(createButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        actionPaneLayout.setVerticalGroup(
            actionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPaneLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(actionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(createButton)
                    .addComponent(updateButton)
                    .addComponent(deleteButton)))
        );

        javax.swing.GroupLayout itemPaneLayout = new javax.swing.GroupLayout(itemPane);
        itemPane.setLayout(itemPaneLayout);
        itemPaneLayout.setHorizontalGroup(
            itemPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(detailPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(actionPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        itemPaneLayout.setVerticalGroup(
            itemPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemPaneLayout.createSequentialGroup()
                .addComponent(detailPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actionPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        splitPane.setRightComponent(itemPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void itemListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_itemListValueChanged
        selected = itemList.getSelectedValue();
        refreshSelected();
    }//GEN-LAST:event_itemListValueChanged

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        ItemEditorDialog<T> dialog = new ItemEditorDialog<>(parent, itemEditorSupplier.get(),createFunc);
        dialog.setTitle("Create");
        dialog.setVisible(true);
    }//GEN-LAST:event_createButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        ItemEditor<T> editor = itemEditorSupplier.get();
        editor.setItem(selected);
        ItemEditorDialog<T> dialog = new ItemEditorDialog<>(parent, editor,updateFunc);
        dialog.setTitle("Update");
        dialog.setVisible(true);
    }//GEN-LAST:event_updateButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        deleteFunc.accept(selected);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        query = searchField.getText();
        refreshList();
    }//GEN-LAST:event_searchFieldKeyReleased

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        itemList.getSelectionModel().setSelectionInterval(itemList.getSelectedIndex()-1,itemList.getSelectedIndex()-1);
    }//GEN-LAST:event_prevButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        itemList.getSelectionModel().setSelectionInterval(itemList.getSelectedIndex()+1,itemList.getSelectedIndex()+1);
    }//GEN-LAST:event_nextButtonActionPerformed

    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstButtonActionPerformed
        itemList.getSelectionModel().setSelectionInterval(0,0);
    }//GEN-LAST:event_firstButtonActionPerformed

    private void lastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastButtonActionPerformed
        itemList.getSelectionModel().setSelectionInterval(itemList.getModel().getSize()-1,itemList.getModel().getSize()-1);
    }//GEN-LAST:event_lastButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPane;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton createButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel detailPane;
    private javax.swing.JButton firstButton;
    private javax.swing.JList<T> itemList;
    private javax.swing.JScrollPane itemListScrollPane;
    private javax.swing.JPanel itemPane;
    private javax.swing.JButton lastButton;
    private javax.swing.JPanel listPane;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel searchFieldPane;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables

    
}