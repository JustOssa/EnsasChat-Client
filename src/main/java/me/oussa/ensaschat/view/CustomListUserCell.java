package me.oussa.ensaschat.view;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import me.oussa.ensaschat.model.User;

import java.util.Objects;

public class CustomListUserCell extends ListCell<User> {
    private final HBox content;
    private final Text name;
    private final Text username;
    private final Circle profileImage;
    private final String[] images = {
            "file:src/main/resources/me/oussa/ensaschat/assets/images/avatar.png",
            "file:src/main/resources/me/oussa/ensaschat/assets/images/avatar2.png",
            "file:src/main/resources/me/oussa/ensaschat/assets/images/avatar3.png"
    };

    public CustomListUserCell() {
        super();
        name = new Text();
        name.setStyle("-fx-font-weight: bold");
        username = new Text();
        username.setFill(Paint.valueOf("#898989"));
        Image image = new Image("file:src/main/resources/me/oussa/ensaschat/assets/images/user_avatar.png", 35, 0, true, true);
        profileImage = new Circle(17, new ImagePattern(image));
        profileImage.setStroke(Paint.valueOf("#dc3545"));
        profileImage.setStrokeWidth(2);
        content = new HBox(profileImage, new VBox(name, username));
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(8);
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            name.setText(item.getName());
            username.setText("@" + item.getUsername());
            String imageLink = images[Objects.hash(item.getUsername()) % images.length];
            profileImage.setFill(new ImagePattern(new Image(imageLink, 37, 0, true, true)));
            if (Objects.equals(item.getStatus(), "Online")) {
                profileImage.setStroke(Paint.valueOf("#28a745"));
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
