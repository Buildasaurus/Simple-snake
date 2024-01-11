module com.snake {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires org.reflections;

    opens com.snake to javafx.fxml;
    opens com.snake.Model to com.google.gson;
    exports com.snake;
}
