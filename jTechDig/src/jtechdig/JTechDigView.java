/*
 * JTechDigView.java
 */
package jtechdig;

import java.awt.Cursor;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

/**
 * The application's main frame.
 */
public class JTechDigView extends FrameView {

    public JTechDigView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        jLogWindow.append("jTechDig has been started ...\n");
        jLogWindow.append("Hint: Go to File/Open menu to open pictures ...\n");
        jLogWindow.append("Hint: or open example figure ...\n");
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = JTechDigApp.getApplication().getMainFrame();
            aboutBox = new JTechDigAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        JTechDigApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jCoordinatesLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLogWindow = new javax.swing.JTextArea();
        jSaveLogButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableCapturedData = new javax.swing.JTable();
        jSaveDataButton = new javax.swing.JButton();
        jClearTableDataButton = new javax.swing.JButton();
        displayArea = new jtechdig.DisplayArea(600,400);
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jOpenMenu = new javax.swing.JMenuItem();
        jOpenExampleMenu = new javax.swing.JMenuItem();
        jOriginMenu = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenuLog = new javax.swing.JMenu();
        jLogXCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        jLogYCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                mainPanelComponentResized(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jtechdig.JTechDigApp.class).getContext().getResourceMap(JTechDigView.class);
        jCoordinatesLabel.setText(resourceMap.getString("jCoordinatesLabel.text")); // NOI18N
        jCoordinatesLabel.setToolTipText(resourceMap.getString("jCoordinatesLabel.toolTipText")); // NOI18N
        jCoordinatesLabel.setName("jCoordinatesLabel"); // NOI18N
        jCoordinatesLabel.setPreferredSize(new java.awt.Dimension(65, 10));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jLogWindow.setColumns(20);
        jLogWindow.setEditable(false);
        jLogWindow.setFont(resourceMap.getFont("jLogWindow.font")); // NOI18N
        jLogWindow.setLineWrap(true);
        jLogWindow.setRows(5);
        jLogWindow.setName("jLogWindow"); // NOI18N
        jScrollPane1.setViewportView(jLogWindow);

        jSaveLogButton.setText(resourceMap.getString("jSaveLogButton.text")); // NOI18N
        jSaveLogButton.setToolTipText(resourceMap.getString("jSaveLogButton.toolTipText")); // NOI18N
        jSaveLogButton.setName("jSaveLogButton"); // NOI18N
        jSaveLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveLogButtonActionPerformed(evt);
            }
        });

        jSeparator2.setName("jSeparator2"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tableExist = false;
        jTableCapturedData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "x", "f(x)"
            }
        ));
        jTableCapturedData.setToolTipText(resourceMap.getString("jTableCapturedData.toolTipText")); // NOI18N
        jTableCapturedData.setName("jTableCapturedData"); // NOI18N
        tableExist = true;
        jScrollPane2.setViewportView(jTableCapturedData);

        jSaveDataButton.setText(resourceMap.getString("jSaveDataButton.text")); // NOI18N
        jSaveDataButton.setToolTipText(resourceMap.getString("jSaveDataButton.toolTipText")); // NOI18N
        jSaveDataButton.setName("jSaveDataButton"); // NOI18N
        jSaveDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveDataButtonActionPerformed(evt);
            }
        });

        jClearTableDataButton.setText(resourceMap.getString("jClearTableDataButton.text")); // NOI18N
        jClearTableDataButton.setToolTipText(resourceMap.getString("jClearTableDataButton.toolTipText")); // NOI18N
        jClearTableDataButton.setName("jClearTableDataButton"); // NOI18N
        jClearTableDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearTableDataButtonActionPerformed(evt);
            }
        });

        displayArea.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        displayArea.setText(resourceMap.getString("displayArea.text")); // NOI18N
        displayArea.setName("displayArea"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSaveLogButton)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(displayArea, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jSaveDataButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jClearTableDataButton))))
                    .addComponent(jCoordinatesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)
                    .addComponent(displayArea, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSaveLogButton))
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSaveDataButton)
                        .addComponent(jClearTableDataButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCoordinatesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N
        fileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });

        jOpenMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jOpenMenu.setText(resourceMap.getString("jOpenMenu.text")); // NOI18N
        jOpenMenu.setName("jOpenMenu"); // NOI18N
        jOpenMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOpenMenuActionPerformed(evt);
            }
        });
        fileMenu.add(jOpenMenu);

        jOpenExampleMenu.setText(resourceMap.getString("jOpenExampleMenu.text")); // NOI18N
        jOpenExampleMenu.setName("jOpenExampleMenu"); // NOI18N
        jOpenExampleMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOpenExampleMenuActionPerformed(evt);
            }
        });
        fileMenu.add(jOpenExampleMenu);

        jOriginMenu.setText(resourceMap.getString("jOriginMenu.text")); // NOI18N
        jOriginMenu.setName("jOriginMenu"); // NOI18N
        jOriginMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOriginMenuActionPerformed(evt);
            }
        });
        fileMenu.add(jOriginMenu);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(jtechdig.JTechDigApp.class).getContext().getActionMap(JTechDigView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenuLog.setText(resourceMap.getString("jMenuLog.text")); // NOI18N
        jMenuLog.setName("jMenuLog"); // NOI18N

        jLogXCheckBoxMenuItem.setText(resourceMap.getString("jLogXCheckBoxMenuItem.text")); // NOI18N
        jLogXCheckBoxMenuItem.setName("jLogXCheckBoxMenuItem"); // NOI18N
        jLogXCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLogXCheckBoxMenuItemActionPerformed(evt);
            }
        });
        jMenuLog.add(jLogXCheckBoxMenuItem);

        jLogYCheckBoxMenuItem.setText(resourceMap.getString("jLogYCheckBoxMenuItem.text")); // NOI18N
        jLogYCheckBoxMenuItem.setName("jLogYCheckBoxMenuItem"); // NOI18N
        jLogYCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLogYCheckBoxMenuItemActionPerformed(evt);
            }
        });
        jMenuLog.add(jLogYCheckBoxMenuItem);

        menuBar.add(jMenuLog);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(500, 0));

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setEnabled(false);
        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(statusMessageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 806, Short.MAX_VALUE)
                        .addComponent(statusAnimationLabel))
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel))
                .addGap(31, 31, 31))
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jOpenMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOpenMenuActionPerformed
        // TODO add your handling code here:
        String filePath = "";
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select image file ...");
            chooser.setFileFilter(new FileFilter() {

                public String getDescription() {
                    return "GIF file (*.gif)";
                }

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().toLowerCase().endsWith(".gif")) {
                        return true;
                    }
                    return false;
                }
            });
            chooser.addChoosableFileFilter(new FileFilter() {

                public String getDescription() {
                    return "JPG files (*.jpg)";
                }

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().toLowerCase().endsWith(".jpg")) {
                        return true;
                    }
                    return false;
                }
            });
            chooser.addChoosableFileFilter(new FileFilter() {

                public String getDescription() {
                    return "BMP files (*.bmp)";
                }

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().toLowerCase().endsWith(".bmp")) {
                        return true;
                    }
                    return false;
                }
            });
            chooser.addChoosableFileFilter(new FileFilter() {

                public String getDescription() {
                    return "PNG files (*.png)";
                }

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().toLowerCase().endsWith(".png")) {
                        return true;
                    }
                    return false;
                }
            });
            int returnVal = chooser.showOpenDialog(null);//showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filePath = chooser.getSelectedFile().getAbsolutePath();
                File file = new File(filePath);
                displayArea.showPicture(ImageIO.read(file));
//                jLabelPicture.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                jLogWindow.append("File '" + chooser.getSelectedFile().getName() + "' successfully opened ...\n");
                jLogWindow.append("Hint: Go to File/Set Origin menu to define origin ...\n");
                displayArea.setToolTipText("");
            }
        } catch (IOException ex) {
        }

    }//GEN-LAST:event_jOpenMenuActionPerformed

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileMenuActionPerformed

    private void jOriginMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOriginMenuActionPerformed
        // TODO add your handling code here:
        if (displayArea.getIcon() == null) {
            jLogWindow.append("No figure is loaded ...\n");
        } else {
            jLogWindow.append("Defining the coordinate system: Select two distinct points where the real coordinates are known ...\n");
            jLogWindow.append("Hint: Zoom by mouse wheel ...\n");
            jLogWindow.append("Hint: Pan by right click and move ...\n");
            displayArea.setCaptureCoordinates(true);
            displayArea.setClickCount(0);
            displayArea.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }
    }//GEN-LAST:event_jOriginMenuActionPerformed

    private String OwnFormat(double in) {
        DecimalFormatSymbols loc = new DecimalFormatSymbols(new Locale("en_US"));
        DecimalFormat NonSciFormat = new DecimalFormat("#####.####", loc);
        DecimalFormat SciFormat = new DecimalFormat("0.#E0###", loc);

        if (Math.abs(in) > 1e-3 && Math.abs(in) < 1e4) {
            return NonSciFormat.format(in);
        } else {
            return SciFormat.format(in);
        }
    }
    private void jOpenExampleMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOpenExampleMenuActionPerformed
        // TODO add your handling code here:
        if (displayArea.getIcon() == null) {
            try {
                displayArea.showPicture(ImageIO.read(getClass().getResource("resources/hysteresis.png")));
                jLogWindow.append("Hint: Go to File/Set Origin menu to define origin ...\n");
                displayArea.setToolTipText("");

            } catch (IOException ex) {
                jLogWindow.append("Something went wrong ...\n");
            }
        }
    }//GEN-LAST:event_jOpenExampleMenuActionPerformed

    private void mainPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_mainPanelComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_mainPanelComponentResized

    private void jSaveLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveLogButtonActionPerformed
        // TODO add your handling code here:
        String pngFilePath = "";
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save log to text file ...");
            chooser.setFileFilter(new FileFilter() {

                public String getDescription() {
                    return "Text file (*.txt)";
                }

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().toLowerCase().endsWith(".txt")) {
                        return true;
                    }
                    return false;
                }
            });
            chooser.setAcceptAllFileFilterUsed(false);
            int returnVal = chooser.showSaveDialog(null);
//            chooser.getFileFilter().getDescription()
//            String[] list = ImageIO.getWriterFileSuffixes();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String ft = "txt";
                pngFilePath = chooser.getSelectedFile().getAbsolutePath();
                if (chooser.getFileFilter().getDescription().contains("txt")) {
                    ft = "txt";
                    if (!pngFilePath.endsWith(".txt")) {
                        pngFilePath += ".txt";
                    }
                }
                BufferedWriter out = new BufferedWriter(new FileWriter(pngFilePath));
                out.write(jLogWindow.getText());
                out.close();
            }
        } catch (IOException eccc) {
        }

    }//GEN-LAST:event_jSaveLogButtonActionPerformed

    private void jSaveDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveDataButtonActionPerformed
        // TODO add your handling code here:
        String pngFilePath = "";
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save data to text file ...");
            chooser.setFileFilter(new FileFilter() {

                public String getDescription() {
                    return "Text file (*.txt)";
                }

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().toLowerCase().endsWith(".txt")) {
                        return true;
                    }
                    return false;
                }
            });
            chooser.setAcceptAllFileFilterUsed(false);
            int returnVal = chooser.showSaveDialog(null);
//            chooser.getFileFilter().getDescription()
//            String[] list = ImageIO.getWriterFileSuffixes();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String ft = "txt";
                pngFilePath = chooser.getSelectedFile().getAbsolutePath();
                if (chooser.getFileFilter().getDescription().contains("txt")) {
                    ft = "txt";
                    if (!pngFilePath.endsWith(".txt")) {
                        pngFilePath += ".txt";
                    }
                }
                BufferedWriter out = new BufferedWriter(new FileWriter(pngFilePath));
                for (int ii = 0; ii < jTableCapturedData.getRowCount(); ii++) {
                    if (!jTableCapturedData.getModel().getValueAt(ii, 0).toString().isEmpty() &&
                            !jTableCapturedData.getModel().getValueAt(ii, 1).toString().isEmpty()) {
                        out.write(jTableCapturedData.getModel().getValueAt(ii, 0) + ";" + jTableCapturedData.getModel().getValueAt(ii, 1) + "\n");
                    }
                }
                out.close();
            }
        } catch (IOException eccc) {
        }
    }//GEN-LAST:event_jSaveDataButtonActionPerformed

    private void jClearTableDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearTableDataButtonActionPerformed
        // TODO add your handling code here:
        jTableCapturedData.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null}
                },
                new String[]{
                    "x", "f(x)"
                }));
    }//GEN-LAST:event_jClearTableDataButtonActionPerformed

    private void jLogXCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLogXCheckBoxMenuItemActionPerformed
        // TODO add your handling code here:
        if (jLogXCheckBoxMenuItem.getState()) {
            displayArea.setXLin(false);
            jLogWindow.append("'x' scale has been set to logarithmic ...\n");
        } else {
            displayArea.setXLin(true);
            jLogWindow.append("'x' scale has been set to linear ...\n");
        }
    }//GEN-LAST:event_jLogXCheckBoxMenuItemActionPerformed

    private void jLogYCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLogYCheckBoxMenuItemActionPerformed
        // TODO add your handling code here:
        if (jLogYCheckBoxMenuItem.getState()) {
            displayArea.setYLin(false);
            jLogWindow.append("'y' scale has been set to logarithmic ...\n");
        } else {
            displayArea.setYLin(true);
            jLogWindow.append("'y' scale has been set to linear ...\n");
        }

    }//GEN-LAST:event_jLogYCheckBoxMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private jtechdig.DisplayArea displayArea;
    private javax.swing.JButton jClearTableDataButton;
    private javax.swing.JLabel jCoordinatesLabel;
    private javax.swing.JTextArea jLogWindow;
    private javax.swing.JCheckBoxMenuItem jLogXCheckBoxMenuItem;
    private javax.swing.JCheckBoxMenuItem jLogYCheckBoxMenuItem;
    private javax.swing.JMenu jMenuLog;
    private javax.swing.JMenuItem jOpenExampleMenu;
    private javax.swing.JMenuItem jOpenMenu;
    private javax.swing.JMenuItem jOriginMenu;
    private javax.swing.JButton jSaveDataButton;
    private javax.swing.JButton jSaveLogButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTableCapturedData;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private boolean tableExist = false;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
//    private BufferedImage techImage;
    private DecimalFormatSymbols dts = new DecimalFormatSymbols(new Locale("en_US"));
    private DecimalFormat FourDigits = new DecimalFormat("######.####", dts);
    private DecimalFormat TwoDigits = new DecimalFormat("######.##", dts);

    /**
     * @return the jLogWindow
     */
    public javax.swing.JTextArea getJLogWindow() {
        return jLogWindow;
    }

    /**
     * @param jLogWindow the jLogWindow to set
     */
    public void setJLogWindow(javax.swing.JTextArea jLogWindow) {
        this.jLogWindow = jLogWindow;
    }

    /**
     * @return the jCoordinatesLabel
     */
    public javax.swing.JLabel getJCoordinatesLabel() {
        return jCoordinatesLabel;
    }

    /**
     * @param jCoordinatesLabel the jCoordinatesLabel to set
     */
    public void setJCoordinatesLabel(javax.swing.JLabel jCoordinatesLabel) {
        this.jCoordinatesLabel = jCoordinatesLabel;
    }

    /**
     * @return the jTableCapturedData
     */
    public javax.swing.JTable getJTableCapturedData() {
        return jTableCapturedData;
    }

    /**
     * @param jTableCapturedData the jTableCapturedData to set
     */
    public void setJTableCapturedData(javax.swing.JTable jTableCapturedData) {
        this.jTableCapturedData = jTableCapturedData;
    }

    /**
     * @return the tableExist
     */
    public boolean isTableExist() {
        return tableExist;
    }


}
