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

import com.adr.mmmmm.display.DisplayMode;
import com.adr.mmmmm.display.DisplayMode3;
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
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author adrian
 */
public class FrmMain extends javax.swing.JFrame {
    
    private GamesModel games;
    private ActionListener al;

    private DisplayMode dm;
    private GamesItemInfo jbeforecontent;

    /**
     * Creates new form FrmMain
     */
    public FrmMain() {

        initComponents();
        try {
            this.setIconImage(ImageIO.read(getClass().getResourceAsStream("/com/adr/mmmmm/res/mimamememu.png")));
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
        
        // Display Mode
        /////////////////////////////////////////////////////////
        dm = new DisplayMode3(); // This shoud be parametric
        /////////////////////////////////////////////////////////
       

        jbeforecontent = dm.getGamesItemInfo();
        if (jbeforecontent == null) {
            jbeforecontainer.setVisible(false);
        } else {
            jbeforecontainer.setVisible(true);
            jbeforecontainer.add(jbeforecontent.getComponent(), BorderLayout.CENTER);
        }
        
        jList1.setCellRenderer(dm.getListRenderer());
        jList1.setLayoutOrientation(dm.getListLayoutOrientation());
        jList1.setVisibleRowCount(-1);
        
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
        
        // Read args
        Options options = new Options();
        options.addOption("h", "help", false, java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.help"));
        options.addOption("f", "fullscreen", false, java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.fullscreen"));
        options.addOption("r", "refresh", false, java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.refresh"));
        CommandLine cmd = null;
        try {
            cmd = new BasicParser().parse(options, args);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getLocalizedMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "mimamememu", options );
            return;
        }
        
        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "mimamememu", options );
            return;
        }
            
        // Set fullscreen
        if (cmd.hasOption("f")) {
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
        
        setVisible(true);  
        loadGames(cmd.hasOption("h"));
    }

    private void loadGames(final boolean refresh) {

        showCard("wait");       
        jList1.setModel(new GamesModel());

        (new Thread() {
            @Override
            public void run() {

                games = new GamesModel();
                games.addAll(PlatformList.INSTANCE.getAllGames(refresh));

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

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages"); // NOI18N
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
            GamesItem item = (GamesItem) jList1.getSelectedValue();
            if (jbeforecontent != null) {
                jbeforecontent.renderGamesItem(item);
            }
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
