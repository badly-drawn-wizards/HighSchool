package dataawe.gui;


import dataawe.entity.Anime;
import dataawe.entity.Genre;
import dataawe.gui.form.AnimeEditor;
import dataawe.gui.form.GenreEditor;
import dataawe.gui.row.AnimeRow;
import dataawe.gui.row.GenreRow;
import java.awt.BorderLayout;
import java.util.List;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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

/**
 *
 * @author Reuben Steenekamp
 */
public class MainFrame extends javax.swing.JFrame {
    EntityManager em;
    Observable animeObservable, genreObservable;
    Supplier<List<Anime>> animeListSupplier;
    Supplier<List<Genre>> genreListSupplier;
    
    ItemListEditor<Anime> animeListEditor;
    ItemListEditor<Genre> genreListEditor;
    ItemTable<Anime> animeTable;
    ItemTable<Genre> genreTable;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        em = Persistence.createEntityManagerFactory("DataAwePU").createEntityManager();

        animeObservable = new Observable();
        genreObservable = new Observable();
        animeListSupplier = () -> {
            return em.createNamedQuery("Anime.findAll", Anime.class).getResultList();
        };
        genreListSupplier = () -> {
            return em.createNamedQuery("Genre.findAll", Genre.class).getResultList();
        };
        
        animeTable = new ItemTable(animeObservable, animeListSupplier, AnimeRow.INSTANCE);
        animeListEditor = new ItemListEditor<>(this, animeObservable, animeListSupplier,
                () -> {
                    return new AnimeEditor(genreObservable, genreListSupplier);
                },
                (anime) -> {
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    em.persist(anime);
                    if (transaction.isActive()) {
                        transaction.commit();
                    }
                    animeObservable.setChanged();
                    animeObservable.notifyObservers();
                },
                (anime) -> {
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    em.merge(anime);
                    if (transaction.isActive()) {
                        transaction.commit();
                    }
                    animeObservable.setChanged();
                    animeObservable.notifyObservers();
                },
                (anime) -> {
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    em.remove(anime);
                    if (transaction.isActive()) {
                        transaction.commit();
                    }
                    animeObservable.setChanged();
                    animeObservable.notifyObservers();
                });

       genreTable = new ItemTable(genreObservable, genreListSupplier, GenreRow.INSTANCE);
       genreListEditor = new ItemListEditor<>(this, genreObservable, genreListSupplier,
                () -> {
                    return new GenreEditor();
                },
                (genre) -> {
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    em.persist(genre);
                    if (transaction.isActive()) {
                        transaction.commit();
                    }
                    genreObservable.setChanged();
                    genreObservable.notifyObservers();
                },
                (genre) -> {
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    em.persist(genre);
                    if (transaction.isActive()) {
                        transaction.commit();
                    }
                    genreObservable.setChanged();
                    genreObservable.notifyObservers();
                },
                (genre) -> {
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    em.remove(genre);
                    if (transaction.isActive()) {
                        transaction.commit();
                    }
                    genreObservable.setChanged();
                    genreObservable.notifyObservers();
                });
        animeTableTab.add(animeTable, BorderLayout.CENTER);
        animeEditorTab.add(animeListEditor, BorderLayout.CENTER);
        genreTableTab.add(genreTable, BorderLayout.CENTER);
        genreEditorTab.add(genreListEditor, BorderLayout.CENTER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        animeTableTab = new javax.swing.JPanel();
        animeEditorTab = new javax.swing.JPanel();
        genreTableTab = new javax.swing.JPanel();
        genreEditorTab = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(0, 0));
        setResizable(false);

        animeTableTab.setLayout(new java.awt.BorderLayout());
        tabbedPane.addTab("Anime (Table)", animeTableTab);

        animeEditorTab.setLayout(new java.awt.BorderLayout());
        tabbedPane.addTab("Anime (Editor)", animeEditorTab);

        genreTableTab.setLayout(new java.awt.BorderLayout());
        tabbedPane.addTab("Genre (Table)", genreTableTab);

        genreEditorTab.setLayout(new java.awt.BorderLayout());
        tabbedPane.addTab("Genre (Editor)", genreEditorTab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel animeEditorTab;
    private javax.swing.JPanel animeTableTab;
    private javax.swing.JPanel genreEditorTab;
    private javax.swing.JPanel genreTableTab;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
