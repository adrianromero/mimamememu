//    Mimamememu is launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013 Adrián Romero Corchado.
//    https://github.com/adrianromero/mimamememu
//
//    This file is part of Mimamememu
//
//    Mimamememu is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Mimamememu is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Mimamememu.  If not, see <http://www.gnu.org/licenses/>.
package com.adr.mmmmm;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author adrian
 */
public class FrmMain extends javax.swing.JFrame {

    private GamesModel games;
    private ActionListener al;
    
    private int columns;

    /**
     * Creates new form FrmMain
     */
    public FrmMain(String[] args) {

        initComponents();
        try {
            this.setIconImage(ImageIO.read(getClass().getResourceAsStream("/com/adr/mmmmm/res/mimamememu.png")));
        } catch (IOException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Read args
        Options options = new Options();
        options.addOption("f", "fullscreen", false, java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.fullscreen"));
        options.addOption("c", "columns", false, java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.columns"));

        boolean fullscreen = false;
        int columns = 1;
        try {
            CommandLine cmd = new BasicParser().parse(options, args);
            fullscreen = cmd.hasOption("f");
            columns = Integer.parseInt(cmd.getOptionValue("c", "1"));
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Set fullscreen
        if (fullscreen) {
            setUndecorated(true);
            setResizable(false);
            setAlwaysOnTop(true);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(0, 0, d.width, d.height);
            //setBounds(0, 0, 1024, 768);
            jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        } else {
            jtitle.setVisible(false);
        }


        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        jList1.setCellRenderer(new GamesItemRenderer());

        jList1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                GamesItem item = (GamesItem) jList1.getSelectedValue();
                if (item != null && me.getClickCount() == 2) {
                    if (al != null) {
                        al.actionPerformed(new ActionEvent(item, ActionEvent.ACTION_PERFORMED, item.getName()));
                    }
                    me.consume();
                }
            }
        });

        jList1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                GamesItem item = (GamesItem) jList1.getSelectedValue();

                if (item != null && ke.getKeyChar() == '\n') {
                    if (al != null) {
                        al.actionPerformed(new ActionEvent(item, ActionEvent.ACTION_PERFORMED, item.getName()));
                    }
                    ke.consume();
                }
            }
        });

        al = new GamesActionLauncher(this);

        showCard("list");
    }

    public void refreshGames() {

        showCard("wait");

        (new Thread() {
            @Override
            public void run() {

                games = new GamesModel();
                games.addAll(PlatformList.INSTANCE.getAllGames());

                //        games.add(PlatformList.INSTANCE.createGame("005", "005", "MAME", "Sega", "1981", "Maze"));
                //        games.add(PlatformList.INSTANCE.createGame("10yard", "10-Yard Fight (World)", "MAME", "Irem", "1983", "Sports"));
                //        games.add(PlatformList.INSTANCE.createGame("11beat", "Eleven Beat", "MAME", "Hudson", "1998", "Not Classified"));
                //        games.add(PlatformList.INSTANCE.createGame("galaxian", "Galaxian", "MAME", "Namco", "xxxx", "xxxx"));
                //        games.add(PlatformList.INSTANCE.createGame("zelda", "Zelda", "SNES", "Nintendo", "xxxx", "xxxx"));

                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jList1.setModel(games);
                        // select first
                        if (jList1.getModel().getSize() > 0) {
                            jList1.setSelectedIndex(0);
                        }
                        showCard("list");
                        
                        if (columns > 1) {
                            jList1.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
                            jList1.setVisibleRowCount((jList1.getModel().getSize() + 1) / columns);
                        } else {
                            jList1.setLayoutOrientation(javax.swing.JList.VERTICAL);
                        }
                        jList1.requestFocus();
                    }
                });

            }
        }).start();
    }

    private void showCard(String card) {
        final CardLayout cl = (CardLayout) (jcards.getLayout());
        cl.show(jcards, card);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jtitle = new javax.swing.JLabel();
        jcards = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MIMAMEMEMU");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jtitle.setFont(jtitle.getFont().deriveFont(jtitle.getFont().getStyle() | java.awt.Font.BOLD, jtitle.getFont().getSize()+10));
        jtitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtitle.setText("MIMAMEMEMU");
        getContentPane().add(jtitle, java.awt.BorderLayout.PAGE_START);

        jcards.setLayout(new java.awt.CardLayout());

        jScrollPane1.setBackground(javax.swing.UIManager.getDefaults().getColor("List.background"));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jcards.add(jScrollPane1, "list");

        jPanel2.setBackground(javax.swing.UIManager.getDefaults().getColor("List.background"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jProgressBar1.setIndeterminate(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel2.add(jProgressBar1, gridBagConstraints);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages"); // NOI18N
        jLabel1.setText(bundle.getString("lbl.Loading")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel1, gridBagConstraints);

        jcards.add(jPanel2, "wait");

        getContentPane().add(jcards, java.awt.BorderLayout.CENTER);

        jLabel2.setBackground(javax.swing.UIManager.getDefaults().getColor("List.background"));
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2, java.awt.BorderLayout.LINE_START);

        setSize(new java.awt.Dimension(673, 697));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

        System.exit(0);

    }//GEN-LAST:event_formWindowClosed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        
        if (evt.getValueIsAdjusting() == false) {
            GamesItem item = (GamesItem) jList1.getSelectedValue();
            if (item != null) {
                jLabel2.setIcon(new ScaledIcon(item.getSnap(), 480, 640));
            }
        }
    }//GEN-LAST:event_jList1ValueChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jcards;
    private javax.swing.JLabel jtitle;
    // End of variables declaration//GEN-END:variables
}
