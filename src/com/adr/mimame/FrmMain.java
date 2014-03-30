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

package com.adr.mimame;

import com.adr.mimame.display.DisplayMode;
import com.adr.mimame.display.DisplayModeList;
import java.awt.BorderLayout;
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
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author adrian
 */
public class FrmMain extends javax.swing.JFrame {
    
    private GamesModel games;
    private ActionListener al;

    private int dmindex;
    private DisplayMode dm;
    private GamesItemInfo jbeforecontent;

    /**
     * Creates new form FrmMain
     */
    public FrmMain() {

        initComponents();
        try {
            this.setIconImage(ImageIO.read(getClass().getResourceAsStream("/com/adr/mimame/res/mimamememu.png")));
        } catch (IOException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        jtitle.setFont(Main.FONT_TITLE.deriveFont(32.0f));

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "nextdisplaymode");
        getRootPane().getActionMap().put("nextdisplaymode", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = dmindex + 1;
                if (index >= DisplayModeList.INSTANCE.sizeDisplayMode()) {
                    index = 0;
                }
                setDisplayMode(index);
            }
        });
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refreshlist");
        getRootPane().getActionMap().put("refreshlist", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGames(true);
            }
        });        
        
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

        showCard("nogames");
    }
    
    public void start(String[] args) {

        // Initialize PlatformList
        PlatformList.INSTANCE.init();

        // Set fullscreen
        if ("fullscreen".equals(PlatformList.INSTANCE.getOption("display.screenmode"))) {
            setUndecorated(true);
            setResizable(false);
            setAlwaysOnTop(true);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(0, 0, d.width, d.height);
            //setBounds(0, 0, 1024, 768);
            jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        } else {
//            jtitle.setVisible(false);
        }
        
        // Set display mode
        setDisplayMode(parseInt(PlatformList.INSTANCE.getOption("display.listmode")));
        
        setVisible(true);  
        loadGames(false);
    }
    
    private void setDisplayMode(int index) {
        
        dmindex = index;
        dm = DisplayModeList.INSTANCE.getDisplayMode(dmindex);     

        jbeforecontent = dm.getGamesItemInfo();
        jbeforecontainer.removeAll();
        if (jbeforecontent == null) {
            jbeforecontainer.setVisible(false);
        } else {
            jbeforecontainer.setVisible(true);
            jbeforecontainer.add(jbeforecontent.getComponent(), BorderLayout.CENTER);
        }
        jbeforecontainer.revalidate();
        
        
        jList1.setCellRenderer(dm.getListRenderer());
        jList1.setLayoutOrientation(dm.getListLayoutOrientation());
        jList1.setVisibleRowCount(-1);    
               
        refreshSelectedItem();
        if (jList1.getSelectedIndex() >= 0) {
            jList1.ensureIndexIsVisible(jList1.getSelectedIndex());
        }
    }

    private void loadGames(final boolean refresh) {

        showCard("wait");       
        jList1.setModel(new GamesModel());

        (new Thread() {
            @Override
            public void run() {

                games = new GamesModel();
                games.addAll(PlatformList.INSTANCE.getAllGames(refresh));
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jList1.setModel(games);
                        // select first
                        if (jList1.getModel().getSize() > 0) {
                            jList1.setSelectedIndex(0);
                            showCard("list");   
                        } else {
                            showCard("nogames");
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
    
    private void refreshSelectedItem() {
        GamesItem item = (GamesItem) jList1.getSelectedValue();
        if (jbeforecontent != null) {
            jbeforecontent.renderGamesItem(item);
        }
    }
    
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
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
        jbeforecontainer = new javax.swing.JPanel();
        jcards = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jWait = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jNoGames = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MIMAMEMEMU");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jtitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtitle.setText("MIMAMEMEMU");
        getContentPane().add(jtitle, java.awt.BorderLayout.PAGE_START);

        jbeforecontainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 0));
        jbeforecontainer.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jbeforecontainer, java.awt.BorderLayout.LINE_START);

        jcards.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jcards.setLayout(new java.awt.CardLayout());

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jcards.add(jScrollPane1, "list");

        jWait.setLayout(new java.awt.GridBagLayout());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/mimame/res/messages"); // NOI18N
        jLabel1.setText(bundle.getString("lbl.Loading")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        jWait.add(jLabel1, gridBagConstraints);

        jProgressBar1.setIndeterminate(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jWait.add(jProgressBar1, gridBagConstraints);

        jcards.add(jWait, "wait");

        jNoGames.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText(bundle.getString("lbl.nogames")); // NOI18N
        jNoGames.add(jLabel2, new java.awt.GridBagConstraints());

        jcards.add(jNoGames, "nogames");

        getContentPane().add(jcards, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(1130, 727));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

        System.exit(0);

    }//GEN-LAST:event_formWindowClosed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        
        if (evt.getValueIsAdjusting() == false) {
            refreshSelectedItem();
        }
        
    }//GEN-LAST:event_jList1ValueChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jNoGames;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jWait;
    private javax.swing.JPanel jbeforecontainer;
    private javax.swing.JPanel jcards;
    private javax.swing.JLabel jtitle;
    // End of variables declaration//GEN-END:variables
}
