package seedu.address.ui;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.strategy.Player;

public class StrategyPanel extends UiPart<Region> {
    private static final String FXML = "StrategyPanel.fxml";
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private static final Map<String, StackPane> table = new HashMap<>();

    private static double orgSceneX;
    private static double orgSceneY;
    private static double orgCenterX;
    private static double orgCenterY;

    @FXML
    private Pane playerView;
    @FXML
    private ImageView strategyImage;
    @FXML
    private AnchorPane strategyAnchorPane;
    @FXML
    private AnchorPane playerAnchorPane;
    @FXML
    private Slider vSlider;
    @FXML
    private Slider hSlider;


    // Credit to http://java-buddy.blogspot.com/2013/07/move-node-to-front.html
    private final EventHandler<MouseEvent> pressHandler =
            new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();
                orgCenterX = ((StackPane) (t.getSource())).getLayoutX();
                orgCenterY = ((StackPane) (t.getSource())).getLayoutY();
                //logger.log(Level.INFO, "orgCenterX: {0}", new Object[]{orgCenterX});
                //logger.log(Level.INFO, "orgCenterY: {0}", new Object[]{orgCenterY});
            }
    };

    // Credit to http://java-buddy.blogspot.com/2013/07/move-node-to-front.html
    private final EventHandler<MouseEvent> dragHandler =
            new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                double offsetX = t.getSceneX() - orgSceneX;
                double offsetY = t.getSceneY() - orgSceneY;
                double newCenterX = orgCenterX + offsetX;
                double newCenterY = orgCenterY + offsetY;
                StackPane tmp = (StackPane) (t.getSource());
                tmp.setLayoutX(newCenterX);
                tmp.setLayoutY(newCenterY);
                //logger.log(Level.INFO, "newCenterX: {0} new CenterY: {1}",
                //        new Object[]{offsetX + orgCenterX, offsetY + orgCenterY});
                //logger.log(Level.INFO, "newTrueX: {0} new TrueY: {1}",
                //        new Object[]{tmp.getCenterX(), tmp.getCenterY()});
            }
    };

    /**
     * Creates a {@code StrategyPanel} with draggable circles.
     */
    public StrategyPanel(ObservableList<Player> playerList) {
        super(FXML);
        initBackgroundImage();
        playerList.addListener((ListChangeListener<Player>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    changeOnAdd(change.getAddedSubList());
                } else if (change.wasRemoved()) {
                    changeOnDelete(change.getRemoved());
                } else if (change.wasReplaced()) {
                    changeOnReplace(change.getRemoved(), change.getAddedSubList());
                }
            }
        });
        // brings slider to the back
        vSlider.toBack();
        hSlider.toBack();
        sliderValueChangeOnWindowResize();
    }

    /**
     * Listens to changes in the size of strategy anchor pane and reflects the
     * value on the slider.
     */
    private void sliderValueChangeOnWindowResize() {
        strategyAnchorPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                hSlider.setMax(Math.round(strategyAnchorPane.getWidth()));
            }
        });
        strategyAnchorPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                vSlider.setMax(Math.round(strategyAnchorPane.getHeight()));
            }
        });
    }

    private void changeOnAdd(List<? extends Player> addedSubList) {
        for (Player player : addedSubList) {
            String playerName = player.getName();
            if (table.containsKey(playerName)) {
                continue;
            }
            StackPane stack = new StackPane();
            initStack(stack, playerName, player.getXCoord(), player.getYCoord(), 50, Color.BLUE);
            playerView.getChildren().add(stack);
            table.put(playerName, stack);
        }
    }

    private void changeOnDelete(List<? extends Player> removeList) {
        for (Player player : removeList) {
            String playerName = player.getName();
            if (table.containsKey(playerName)) {
                playerView.getChildren().remove(table.get(playerName));
                table.remove(playerName);
            }
        }
    }

    private void changeOnReplace(List<? extends Player> removeList, List<? extends Player> addSubList) {
        changeOnDelete(removeList);
        changeOnAdd(addSubList);
    }

    /**
     * Changes the image contained in ImageView.
     * @param file the file reference for the image to be loaded
     */
    public void changeImageBackground(File file) {
        strategyImage.setImage((new Image((file.toURI().toString()))));
    }

    /**
     * Initializes the background image to allow it to resize automatically along with the window.
     */
    private void initBackgroundImage() {
        strategyImage.setPreserveRatio(false); //needs to be marked false to allow image to properly resize with window
        strategyImage.fitWidthProperty().bind(strategyAnchorPane.widthProperty());
        strategyImage.fitHeightProperty().bind(strategyAnchorPane.heightProperty());
        strategyImage.setManaged(false);
        strategyImage.toBack(); //set image to back to avoid covering player icons
    }

    private void initCircle(Circle circle, double rad, double x, double y, Paint color) {
        circle.setRadius(rad);
        circle.setFill(color);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setCursor(Cursor.HAND);
    }

    private void initText(Text text, String value, double x, double y) {
        text.setX(x);
        text.setY(y);
        text.setText(value);
        text.setFont(Font.font("Arial", 18));
        double width = text.prefWidth(-1);
        text.setX(250 - width / 2);
        text.setTextOrigin(VPos.CENTER);
    }

    private void initStack(StackPane stack, String name, double x, double y, double rad, Paint color) {
        Text text = new Text();
        initText(text, name, x, y);
        Circle cr = new Circle();
        initCircle(cr, rad, x, y, color);
        text.xProperty().bind(cr.centerXProperty());
        text.yProperty().bind(cr.centerYProperty());
        text.wrappingWidthProperty().bind(cr.radiusProperty().multiply(2).subtract(10));
        stack.getChildren().addAll(cr, text);
        stack.setTranslateX(x);
        stack.setTranslateY(y);
        stack.setOnMousePressed(pressHandler);
        stack.setOnMouseDragged(dragHandler);
    }

    /**
     * Captures image of strategyAnchorPane and stores it in users local drive.
     */
    //https://stackoverflow.com/questions/38028825/javafx-save-view-of-pane-to-image
    public void captureAndSaveStrategyPanel() {

        FileChooser chooser = new FileChooser();

        //include title name randomization
        chooser.setInitialFileName("title" + ".png");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = chooser.showSaveDialog(null);

        if (file != null) {
            try {
                //parameters
                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);

                //no edits to capture area
                WritableImage image = strategyAnchorPane.snapshot(sp, null);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);

                //Write the snapshot to the chosen file
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
