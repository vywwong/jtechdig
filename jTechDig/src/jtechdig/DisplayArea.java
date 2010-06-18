/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jtechdig;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kipe
 */
public class DisplayArea extends JLabel {

    private boolean captureCoordinates = false;
    private boolean originSet = false;
    private int clickCount = 0;
    // Screen coordinates of corners
    private double x1,  y1,  x2,  y2;
    // Real coordinates of corners
    private double H1 = 0.0,  B1 = 0.0,  H2 = 0.0,  B2 = 0.0;
    // Coordinate point
    private double x,  y,  H,  B;
    private double panX1,  panY1,  panX2,  panY2;
    // scales linear (xLin=true) on logarithmic (xLin=false)
    private boolean xLin = true,  yLin = true;
    private DecimalFormatSymbols dts = new DecimalFormatSymbols(new Locale("en_US"));
    private DecimalFormat FourDigits = new DecimalFormat("######.####", dts);
    private DecimalFormat TwoDigits = new DecimalFormat("######.##", dts);
    private ImageDisplay imageDisplay;
    private int width,  height;
    private jCoordinatesDialog dlg;

    public DisplayArea() {
        width = 600;
        height = 400;
        MyListener myListener = new MyListener();
        addMouseListener(myListener);
        addMouseMotionListener(myListener);
        addMouseWheelListener(myListener);
    }

    public DisplayArea(int w, int h) {
        width = w;
        height = h;
        MyListener myListener = new MyListener();
        addMouseListener(myListener);
        addMouseMotionListener(myListener);
        addMouseWheelListener(myListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Coordinate system reference points
        if (Math.abs(H1) > 0.0 && Math.abs(B1) > 0.0) {
            g.setColor(Color.RED);
            imageDisplay.calculateOriginalCoordinates(x1, y1);
            int xxmo = (int) Math.round(imageDisplay.getXMo()) - 5;
            int yymo = (int) Math.round(imageDisplay.getYMo()) - 5;
            if ((xxmo > imageDisplay.getVGap()) && (yymo > imageDisplay.getHGap()) &&
                    (xxmo < (600 - imageDisplay.getVGap())) &&
                    (yymo < (400 - imageDisplay.getHGap()))) {
                g.fillOval(xxmo,yymo, 10, 10);
            }
        }
        if (Math.abs(H2) > 0.0 && Math.abs(B2) > 0.0) {
            g.setColor(Color.RED);
            imageDisplay.calculateOriginalCoordinates(x2, y2);
            int xxmo = (int) Math.round(imageDisplay.getXMo()) - 5;
            int yymo = (int) Math.round(imageDisplay.getYMo()) - 5;
            if ((xxmo > imageDisplay.getVGap()) && (yymo > imageDisplay.getHGap()) &&
                    (xxmo < (600 - imageDisplay.getVGap())) &&
                    (yymo < (400 - imageDisplay.getHGap()))) {
                g.fillOval(xxmo, yymo, 10, 10);
            }
        }
//        // Captured points
//        if (JTechDigApp.getApplication().getView().isTableExist()) {
        int rc = JTechDigApp.getApplication().getView().getJTableCapturedData().getRowCount();
        if (rc > 1) {
            for (int ii = 0; ii < rc - 1; ii++) {
                double HH = Double.valueOf(JTechDigApp.getApplication().getView().getJTableCapturedData().getValueAt(ii, 0).toString());
                double BB = Double.valueOf(JTechDigApp.getApplication().getView().getJTableCapturedData().getValueAt(ii, 1).toString());
                double xd, yd;
                if (xLin) {
                    xd = (HH - H1) / (H2 - H1) * (x2 - x1) + x1;
                } else {
                    xd = (Math.log10(HH) - Math.log10(H1)) / (Math.log10(H2) - Math.log10(H1)) * (x2 - x1) + x1;
                }
                if (yLin) {
                    yd = (BB - B1) / (B2 - B1) * (y2 - y1) + y1;
                } else {
                    yd = (Math.log10(BB) - Math.log10(B1)) / (Math.log10(B2) - Math.log10(B1)) * (y2 - y1) + y1;
                }
                imageDisplay.calculateOriginalCoordinates(xd, yd);
                g.setColor(Color.GREEN);
                int xxmo = (int) Math.round(imageDisplay.getXMo()) - 5;
                int yymo = (int) Math.round(imageDisplay.getYMo()) - 5;
                if ((xxmo > imageDisplay.getVGap()) && (yymo > imageDisplay.getHGap()) &&
                        (xxmo < (600 - imageDisplay.getVGap())) &&
                        (yymo < (400 - imageDisplay.getHGap()))) {
                    g.fillOval(xxmo, yymo, 10, 10);
                }
            }
        }
//        }
    }

    /**
     * @return the xLin
     */
    public boolean isXLin() {
        return xLin;
    }

    /**
     * @param xLin the xLin to set
     */
    public void setXLin(boolean xLin) {
        this.xLin = xLin;
    }

    /**
     * @return the yLin
     */
    public boolean isYLin() {
        return yLin;
    }

    /**
     * @param yLin the yLin to set
     */
    public void setYLin(boolean yLin) {
        this.yLin = yLin;
    }

    public void showPicture(BufferedImage bi) {
        imageDisplay = new ImageDisplay(bi, width, height);
        setIcon(new ImageIcon(imageDisplay.displayImage()));
    }

    /**
     * @return the captureCoordinates
     */
    public boolean isCaptureCoordinates() {
        return captureCoordinates;
    }

    /**
     * @param captureCoordinates the captureCoordinates to set
     */
    public void setCaptureCoordinates(boolean captureCoordinates) {
        this.captureCoordinates = captureCoordinates;
    }

    /**
     * @return the clickCount
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * @param clickCount the clickCount to set
     */
    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    private class MyListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
                // LEFT click = capture coords or set origin
                if (captureCoordinates && (clickCount < 2)) {
                    // Setting origin
                    clickCount++;
                    jCoordinatesDialog dlg = new jCoordinatesDialog(null, true);

//                    dlg.setLocation((int) (getFrame().getLocation().getX() + evt.getX()),
//                            (int) (getFrame().getLocation().getY() + evt.getY()));
                    dlg.setVisible(true);
                    if (clickCount == 1) {
                        imageDisplay.calculateDisplayCoordinates(evt.getX(), evt.getY());
                        x1 = imageDisplay.getXDisplay();
                        y1 = imageDisplay.getYDisplay();
                        H1 = dlg.getXx();
                        B1 = dlg.getYy();
                        JTechDigApp.getApplication().getView().getJLogWindow().append("Coordinate assignment(" + clickCount + "): " +
                                "(" + TwoDigits.format(x1) + ";" + TwoDigits.format(y1) + ") -> " +
                                "(" + OwnFormat(H1) + ";" + OwnFormat(B1) + ")...\n");
//                        repaint();
                    } else {
                        imageDisplay.calculateDisplayCoordinates(evt.getX(), evt.getY());
                        x2 = imageDisplay.getXDisplay();
                        y2 = imageDisplay.getYDisplay();
                        H2 = dlg.getXx();
                        B2 = dlg.getYy();
                        JTechDigApp.getApplication().getView().getJLogWindow().append("Coordinate assignment(" + clickCount + "): (" + TwoDigits.format(x2) + ";" + TwoDigits.format(y2) + ") -> " +
                                "(" + OwnFormat(H2) + ";" + OwnFormat(B2) + ")...\n");
                        JTechDigApp.getApplication().getView().getJLogWindow().append("Hint: The coordinate system is defined...\n");
                        JTechDigApp.getApplication().getView().getJLogWindow().append("Hint: Now you can start capturing points by mouse click ...\n");
//                    jLogWindow.append("Both 'x' and 'y' scales are linear ...\n");
                        originSet = true;
//                        repaint();
                    }

                } else if (captureCoordinates) {
                    // Capturing data points
                    imageDisplay.calculateDisplayCoordinates(evt.getX(), evt.getY());
                    x = imageDisplay.getXDisplay();
                    y = imageDisplay.getYDisplay();
                    if (xLin) {
                        H = H1 + (x - x1) / (x2 - x1) * (H2 - H1);
                    } else {
                        H = Math.pow(10, Math.log10(H1) +
                                (x - x1) / (x2 - x1) * (Math.log10(H2) - Math.log10(H1)));
                    }

                    if (yLin) {
                        B = B1 + (y - y1) / (y2 - y1) * (B2 - B1);
                    } else {
                        B = Math.pow(10, Math.log10(B1) +
                                (y - y1) / (y2 - y1) * (Math.log10(B2) - Math.log10(B1)));
                    }
                    JTechDigApp.getApplication().getView().getJLogWindow().append("Screen coordinates: (" + FourDigits.format(x) + ";" + FourDigits.format(y) + ") -> " +
                            "Real coordinates: (" + OwnFormat(H) + ";" + OwnFormat(B) + ")\n");
                    JTechDigApp.getApplication().getView().getJTableCapturedData().getModel().setValueAt(OwnFormat(H), JTechDigApp.getApplication().getView().getJTableCapturedData().getRowCount() - 1, 0);
                    JTechDigApp.getApplication().getView().getJTableCapturedData().getModel().setValueAt(OwnFormat(B),
                            JTechDigApp.getApplication().getView().getJTableCapturedData().getRowCount() - 1, 1);
                    ((DefaultTableModel) JTechDigApp.getApplication().getView().getJTableCapturedData().getModel()).insertRow(JTechDigApp.getApplication().getView().getJTableCapturedData().getRowCount(), new Object[]{"", ""});

                }
            } else if (evt.getButton() == MouseEvent.BUTTON3) {
                // RIGHT click = pan
                panX1 = evt.getX();
                panY1 = evt.getY();
            }
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            // pan to
            panX2 = evt.getX();
            panY2 = evt.getY();
            imageDisplay.pan(panX1 - panX2, panY1 - panY2);
            setIcon(new ImageIcon(imageDisplay.displayImage()));
            panX1 = panX2;
            panY1 = panY2;
        }

        @Override
        public void mousePressed(MouseEvent evt) {
            if (getIcon() != null) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    // RIGHT click = pan
                    panX1 = evt.getX();
                    panY1 = evt.getY();
                }
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent evt) {
            if (getIcon() != null) {
                int notches = evt.getWheelRotation();
                if (notches < 0) {
                    imageDisplay.zoom(evt.getX(), evt.getY(), 1);
                    setIcon(new ImageIcon(imageDisplay.displayImage()));
                } else {
                    // Zoom out
                    imageDisplay.zoom(evt.getX(), evt.getY(), -1);
                    setIcon(new ImageIcon(imageDisplay.displayImage()));
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent evt) {
            if (getIcon() != null) {
                imageDisplay.calculateDisplayCoordinates(evt.getX(), evt.getY());
                x = imageDisplay.getXDisplay();
                y = imageDisplay.getYDisplay();
                if (originSet) {
                    if (xLin) {
                        H = H1 + (x - x1) / (x2 - x1) * (H2 - H1);
                    } else {
                        H = Math.pow(10, Math.log10(H1) +
                                (x - x1) / (x2 - x1) * (Math.log10(H2) - Math.log10(H1)));
                    }
                    if (yLin) {
                        B = B1 + (y - y1) / (y2 - y1) * (B2 - B1);
                    } else {
                        B = Math.pow(10, Math.log10(B1) +
                                (y - y1) / (y2 - y1) * (Math.log10(B2) - Math.log10(B1)));
                    }
                    JTechDigApp.getApplication().getView().getJCoordinatesLabel().setText(
                            "Pixels: (" + Math.round(imageDisplay.getXDisplay()) + ";" + Math.round(imageDisplay.getYDisplay()) +
                            ") -> Coordinates: (" + OwnFormat(H) + ";" + OwnFormat(B) + ")");
                } else {
                    JTechDigApp.getApplication().getView().getJCoordinatesLabel().setText(
                            "Pixels: (" + Math.round(imageDisplay.getXDisplay()) + ";" + Math.round(imageDisplay.getYDisplay()) + ")");
                }
            }

        }
    }

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
}
