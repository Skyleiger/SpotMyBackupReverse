module spotmybackupreverse {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.apache.logging.log4j.core;

    opens de.dwienzek.spotmybackupreverse to javafx.fxml;
    opens de.dwienzek.spotmybackupreverse.controller to javafx.fxml;
    opens de.dwienzek.spotmybackupreverse.api to javafx.fxml;

    exports de.dwienzek.spotmybackupreverse to javafx.graphics;
    exports de.dwienzek.spotmybackupreverse.api;
}