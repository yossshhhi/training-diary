package kz.yossshhhi;

import kz.yossshhhi.configuration.Configuration;

/**
 * The entry point of the application.
 */
public class Application {

    /**
     * The main method of the application. It initializes the configuration and starts the application.
     *
     * @param args The command-line arguments passed to the application (not used in this implementation).
     */
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.appController().run();
    }
}
