package seedu.address.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class StrategyPanel extends UiPart<Region> {
    private static final String FXML = "StrategyPanel.fxml";

    private static double orgSceneX;
    private static double orgSceneY;
    private static double orgTranslateX;
    private static double orgTranslateY;

    @FXML
    private Circle player1;
    @FXML
    private Circle player2;
    @FXML
    private Circle player3;

    // Credit to http://java-buddy.blogspot.com/2013/07/move-node-to-front.html
    private EventHandler<MouseEvent> pressHandler =
            new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();
                orgTranslateX = ((Circle) (t.getSource())).getTranslateX();
                orgTranslateY = ((Circle) (t.getSource())).getTranslateY();
            }
    };

    // Credit to http://java-buddy.blogspot.com/2013/07/move-node-to-front.html
    private EventHandler<MouseEvent> dragHandler =
            new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                double offsetX = t.getSceneX() - orgSceneX;
                double offsetY = t.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;
                Circle tmp = (Circle) (t.getSource());
                tmp.setTranslateX(newTranslateX);
                tmp.setTranslateY(newTranslateY);
            }
    };

    /**
     * Creates a {@code StrategyPanel} with draggable circles.
     */
    public StrategyPanel() {
        super(FXML);
        initCircle(player1, 50, 100, 100, Color.RED);
        initCircle(player2, 50, 200, 200, Color.LIGHTBLUE);
        initCircle(player3, 30, 200, 200, Color.YELLOW);
    }

    private void initCircle(Circle circle, double rad, double x, double y, Paint color) {
        circle.setRadius(rad);
        circle.setFill(color);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setCursor(Cursor.HAND);
        circle.setOnMousePressed(pressHandler);
        circle.setOnMouseDragged(dragHandler);
    }
}
