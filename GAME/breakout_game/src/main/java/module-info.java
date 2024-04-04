module com.example.breakout_game {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.breakout_game to javafx.fxml;
    exports com.example.breakout_game;
}