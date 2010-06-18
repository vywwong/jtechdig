/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jtechdig;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author kipe
 */
public class ImageDisplay {

    public ImageDisplay(BufferedImage img, double wFrame, double hFrame) {
        image = img;
        init(wFrame, hFrame);
    }

    public void init(double wFrame, double hFrame) {
        setWo(image.getWidth());
        setHo(image.getHeight());
        setWf(wFrame);
        setHf(hFrame);
        if (wo / ho > wFrame / hFrame) {
            setWr(wf);
            setHr(ho / wo * wFrame);
        } else {
            setWr(wo / ho * hFrame);
            setHr(hf);
        }
        setVGap((wf - wr) / 2);
        setHGap((hf - hr) / 2);
        setXZo(0);
        setYZo(0);
        setWZo(wo);
        setHZo(ho);
        setWZr(wr);
        setHZr(hr);
        setZc(0);
    }

    public BufferedImage displayImage() {
        return toBufferedImage(image.getSubimage(Math.round((float) xZo),
                Math.round((float) yZo), Math.round((float) wZo), Math.round((float) hZo)).getScaledInstance(Math.round((float) wr), Math.round((float) hr), Image.SCALE_AREA_AVERAGING));
    }

    public void pan(double dx, double dy) {
        if (zc > 0) {
            setXZf(xZf + dx); // * (double) wZo / (double) wr);
            setYZf(yZf + dy); // * (double) hZo / (double) hr);
            setXZr(xZf - vGap);
            setYZr(yZf - hGap);
            setXZo(xZo + dx * wo / wr / Math.pow(k, zc));
            setYZo(yZo + dy * ho / hr / Math.pow(k, zc));
//            setXZo((int) Math.round((float)xZo + (float)dx/Math.pow(k, zc)));
//            setYZo((int) Math.round((float)yZo + (float)dy/Math.pow(k, zc)));
//            setXZo(Math.round((float) xZr * (float) wZr / (float) wZo));
//            setYZo(Math.round((float) yZr * (float) hZr / (float) hZo));
        }
    }

    public void zoom(int xcf, int ycf, int dir) {
        if ((wZo / k > 5 && hZo / k > 5) || (dir <= 0)) { // 5 pixels min.
            // zoom count
            setZc(zc + dir);
            // Main frame with gaps
            setXMf(xcf);
            setYMf(ycf);
            setWZf((wr / Math.pow(k, zc)));
            setHZf((hr / Math.pow(k, zc)));
            setXZf((xMr * (1 - 1 / Math.pow(k, zc))) + vGap);
            setYZf((yMr * (1 - 1 / Math.pow(k, zc))) + hGap);
            // Real coordinates without gaps
            setXMr(xMf - vGap);
            setYMr(yMf - hGap);
            setWZr((wr / Math.pow(k, zc)));
            setHZr((hr / Math.pow(k, zc)));
            setXZr(xZf - vGap);
            setYZr(yZf - hGap);
            // Coordinates in the original picture
            setXMo(xMr * wo / wr);
            setYMo(yMr * ho / hr);
            setWZo((wo / Math.pow(k, zc)));
            setHZo((ho / Math.pow(k, zc)));
            setXZo((xMo * (1 - 1 / Math.pow(k, zc))));
            setYZo((yMo * (1 - 1 / Math.pow(k, zc))));
        }
    }

    public void calculateDisplayCoordinates(int xcf, int ycf) {
//            zoom(xcf,ycf,0);
        setXDisplay(xZo + ((xcf - vGap) * wo / wr) / Math.pow(k, zc));
        setYDisplay(yZo + ((ycf - hGap) * ho / hr) / Math.pow(k, zc));
    }

    public void calculateOriginalCoordinates(double xd, double yd) {
        // inverse of the above
        setXMo((xd -xZo)* Math.pow(k, zc)*wr/wo+vGap);
        setYMo((yd -yZo)* Math.pow(k, zc)*hr/ho+hGap);
    }

    // This method returns a buffered image with the contents of an image
    private BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = false; //hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
            }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }
    // Basic data
    // original image, never changed/transformed
    private BufferedImage image;
    // The original size image scaled to frame
    private double wo,  ho;
    // The real size image scaled to frame
    private double wr,  hr;
    // Frame size on main window
    private double wf,  hf;
    // Data for displaying image in original/frame/real coordinates
    // left top coordinates and width and height
    private double xZo,  yZo,  wZo,  hZo;
    private double xZf,  yZf,  wZf,  hZf;
    private double xZr,  yZr,  wZr,  hZr;
    // mouse pointer location for zoomming in all three reference frame
    private double xMo,  yMo;
    private double xMf,  yMf;
    private double xMr,  yMr;
    // magnifying factor  for zoomming
    private double k = 1.3;
    // vertical and horizontal gaps below, upper or left right side
    private double vGap,  hGap;
    // zoom count
    private int zc = 0;
    private double xDisplay,  yDisplay;

    public double getXMo() {
        return xMo;
    }

    public double getYMo() {
        return yMo;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @param wr the wr to set
     */
    public void setWr(double wr) {
        if (wr > 0) {
            this.wr = wr;
        }
    }

    /**
     * @param hr the hr to set
     */
    public void setHr(double hr) {
        if (hr > 0) {
            this.hr = hr;
        }
    }

    /**
     * @param wf the wf to set
     */
    public void setWf(double wf) {
        if (wf > 0) {
            this.wf = wf;
        }
    }

    /**
     * @param hf the hf to set
     */
    public void setHf(double hf) {
        if (hf > 0) {
            this.hf = hf;
        }
    }

    /**
     * @param xZo the xZo to set
     */
    public void setXZo(double xZo) {
        if (xZo >= 0 && xZo <= wo) {
            this.xZo = xZo;
        }
    }

    /**
     * @param yZo the yZo to set
     */
    public void setYZo(double yZo) {
        if (yZo >= 0 && yZo <= ho) {
            this.yZo = yZo;
        }
    }

    /**
     * @param wZo the wZo to set
     */
    public void setWZo(double wZo) {
        if (wZo >= 0 && wZo <= wo) {
            this.wZo = wZo;
        }
    }

    /**
     * @param hZo the hZo to set
     */
    public void setHZo(double hZo) {
        if (hZo >= 0 && hZo <= ho) {
            this.hZo = hZo;
        }
    }

    /**
     * @param xZf the xZf to set
     */
    public void setXZf(double xZf) {
        if (xZf >= 0 && xZf < wf) {
            this.xZf = xZf;
        }
    }

    /**
     * @param yZf the yZf to set
     */
    public void setYZf(double yZf) {
        if (yZf >= 0 && yZf < hf) {
            this.yZf = yZf;
        }
    }

    /**
     * @param wZf the wZf to set
     */
    public void setWZf(double wZf) {
        if (wZf >= 0 && wZf < wf) {
            this.wZf = wZf;
        }
    }

    /**
     * @param hZf the hZf to set
     */
    public void setHZf(double hZf) {
        if (hZf >= 0 && hZf < hf) {
            this.hZf = hZf;
        }
    }

    /**
     * @param xZr the xZr to set
     */
    public void setXZr(double xZr) {
        if (xZr >= 0 && xZr < wr) {
            this.xZr = xZr;
        }
    }

    /**
     * @param yZr the yZr to set
     */
    public void setYZr(double yZr) {
        if (yZr >= 0 && yZr < hr) {
            this.yZr = yZr;
        }
    }

    /**
     * @param wZr the wZr to set
     */
    public void setWZr(double wZr) {
        if (wZr >= 0 && wZr < wr) {
            this.wZr = wZr;
        }
    }

    /**
     * @param hZr the hZr to set
     */
    public void setHZr(double hZr) {
        if (hZr >= 0 && hZr < hr) {
            this.hZr = hZr;
        }
    }

    /**
     * @param xMo the xMo to set
     */
    public void setXMo(double xMo) {
        if (xMo >= 0 && xMo <= wo) {
            this.xMo = xMo;
        }
    }

    /**
     * @param yMo the yMo to set
     */
    public void setYMo(double yMo) {
        if (yMo >= 0 && yMo <= ho) {
            this.yMo = yMo;
        }
    }

    /**
     * @param xMf the xMf to set
     */
    public void setXMf(double xMf) {
        if (xMf >= 0 && xMf <= wf) {
            this.xMf = xMf;
        }
    }

    /**
     * @param yMf the yMf to set
     */
    public void setYMf(double yMf) {
        if (yMf >= 0 && yMf <= hf) {
            this.yMf = yMf;
        }
    }

    /**
     * @param xMr the xMr to set
     */
    public void setXMr(double xMr) {
        if (xMr >= 0 && xMr <= wr) {
            this.xMr = xMr;
        }
    }

    /**
     * @param yMr the yMr to set
     */
    public void setYMr(double yMr) {
        if (yMr >= 0 && yMr <= hr) {
            this.yMr = yMr;
        }
    }

    /**
     * @param k the k to set
     */
    public void setK(double k) {
        if (k > 1) {
            this.k = k;
        }
    }

    /**
     * @param vGap the vGap to set
     */
    public void setVGap(double vGap) {
        if (vGap >= 0) {
            this.vGap = vGap;
        }
    }

    /**
     * @param hGap the hGap to set
     */
    public void setHGap(double hGap) {
        if (hGap >= 0) {
            this.hGap = hGap;
        }
    }

    /**
     * @param zc the zc to set
     */
    public void setZc(int zc) {
        if (zc >= 0) {
            this.zc = zc;
        }
    }

    /**
     * @param wo the wo to set
     */
    public void setWo(double wo) {
        if (wo > 0) {
            this.wo = wo;
        }
    }

    /**
     * @param ho the ho to set
     */
    public void setHo(double ho) {
        if (ho > 0) {
            this.ho = ho;
        }
    }

    /**
     * @return the xDisplay
     */
    public double getXDisplay() {
        return xDisplay;
    }

    /**
     * @param xDisplay the xDisplay to set
     */
    public void setXDisplay(double xDisplay) {
        if (xDisplay >= 0 && xDisplay <= wo) {
            this.xDisplay = xDisplay;
        }
    }

    /**
     * @return the yDisplay
     */
    public double getYDisplay() {
        return yDisplay;
    }

    /**
     * @param yDisplay the yDisplay to set
     */
    public void setYDisplay(double yDisplay) {
        if (yDisplay >= 0 && yDisplay <= ho) {
            this.yDisplay = yDisplay;
        }
    }

    /**
     * @return the wo
     */
    public double getWzo() {
        return wZo;
    }

    /**
     * @return the ho
     */
    public double getHzo() {
        return hZo;
    }

    /**
     * @return the xZo
     */
    public double getXZo() {
        return xZo;
    }

    /**
     * @return the yZo
     */
    public double getYZo() {
        return yZo;
    }

    /**
     * @return the vGap
     */
    public double getVGap() {
        return vGap;
    }

    /**
     * @return the hGap
     */
    public double getHGap() {
        return hGap;
    }
}
