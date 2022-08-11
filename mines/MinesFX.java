package mines;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MinesFX extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox hbox;
		Controller controller;
		stage.setTitle("The Amazing Mines Sweeper");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Screen.fxml"));
			hbox = loader.load();
			controller = loader.getController();
			GridPane grindPane = new GridPane();
			controller.setHbox(hbox);

			TextField textFieldWidth = controller.getTextFieldWidth();
			TextField textFieldHeight = controller.getTextFieldHeight();
			TextField textFieldMines = controller.getTextFieldMines();
			textFieldWidth.setText("10");
			textFieldHeight.setText("10");
			textFieldMines.setText("10");
			controller.setStage(stage);
			BackgroundSize backgroundSize = new BackgroundSize(160, 220, true, true, true, false);
			BackgroundImage image = new BackgroundImage(new Image("mines/leftBackground.png"),
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
			hbox.setBackground(new Background(image));
			hbox.getChildren().add(grindPane);
			controller.initGame();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(hbox);
		stage.setScene(scene);
		stage.show();
	}

}
