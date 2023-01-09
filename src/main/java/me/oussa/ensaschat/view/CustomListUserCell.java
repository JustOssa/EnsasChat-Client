package me.oussa.ensaschat.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import me.oussa.ensaschat.model.User;

import java.util.Objects;

public class CustomListUserCell extends ListCell<User> {
    private final HBox content;
    private final Text name;
    private final Text status;
    private final String imageLink = "file:src/main/resources/me/oussa/ensaschat/assets/images/user_avatar.png";

    public CustomListUserCell() {
        super();
        name = new Text();
        status = new Text("Offline");
        status.setFill(Paint.valueOf("#A00000"));
        Image image = new Image(imageLink, 30, 0, true, false);
        content = new HBox(new Label("", new ImageView(image)), new VBox(name, status));
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            name.setText(item.getUsername().toUpperCase());
            if (Objects.equals(item.getStatus(), "Online")) {
                status.setText("Online");
                status.setFill(Paint.valueOf("#00A000"));
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
