/*
 * JTechDigApp.java
 */

package jtechdig;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class JTechDigApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(view = new JTechDigView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of JTechDigApp
     */
    public static JTechDigApp getApplication() {
        return Application.getInstance(JTechDigApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(JTechDigApp.class, args);
    }
    private JTechDigView view;

    /**
     * @return the view
     */
    public JTechDigView getView() {
        return view;
    }
}
