package mines;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Controller {
	private Mines field;
	private Buttons[][] boardButtons;
	private int width, height, numOfMines;
	private HBox hbox;
	private Stage stage;
	private Label popUpLabel;

	@FXML
	private TextField textFieldWidth;

	@FXML
	private TextField textFieldHeight;

	@FXML
	private TextField textFieldMines;

	@FXML
	void pressReset(ActionEvent event) {
		initGame();
	}

	// init game with a new grid
	public void initGame() {
		GridPane gridPane = new GridPane();
		List<ColumnConstraints> column = new ArrayList<>();
		List<RowConstraints> row = new ArrayList<>();
		getValuesFromUser();
		// check input

		field = new Mines(height, width, numOfMines);
		boardButtons = new Buttons[height][width];

		initSizeboardButtons(column, row);
		startGame(gridPane);

		gridPane.getColumnConstraints().addAll(column);
		gridPane.getRowConstraints().addAll(row);

		hbox.getChildren().remove(hbox.getChildren().size() - 1);
		hbox.getChildren().add(gridPane);
		hbox.autosize();
		stage.sizeToScene();
	}

	private void getValuesFromUser() {
		width = Integer.valueOf(textFieldWidth.getText());
		height = Integer.valueOf(textFieldHeight.getText());
		numOfMines = Integer.valueOf(textFieldMines.getText());
	}

	private void initSizeboardButtons(List<ColumnConstraints> column, List<RowConstraints> row) {
		for (int i = 0; i < width; i++)
			column.add(new ColumnConstraints(35));
		for (int i = 0; i < height; i++)
			row.add(new RowConstraints(35));
	}

	private void startGame(GridPane gridPane) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				boardButtons[i][j] = new Buttons(i, j);
				boardButtons[i][j].setText(field.get(i, j));
				boardButtons[i][j].setStyle(
						"-fx-background-color: #15758f; -fx-border-color: #34253d; -fx-border-radiuss: 3; -fx-text-fill: #ffffff");

				boardButtons[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.PRIMARY) { // left click - will open the block that the
																		// user wants.
							boolean isReset = field.open(((Buttons) event.getSource()).getX(),
									((Buttons) event.getSource()).getY());
							updateboardButtons();
							if (!isReset) {
								field.setShowAll(true);
								updateboardButtons();
								popUpWindow(true, false);
							}
							if (field.isDone()) {
								popUpWindow(false, true);
							}
						}
						if (event.getButton() == MouseButton.SECONDARY) { // put flag when right mouse clicked
							boolean exist = true;
							int x = ((Buttons) event.getSource()).getX();
							int y = ((Buttons) event.getSource()).getY();
							field.toggleFlag(x, y);
							if (exist) {
								updateboardButtons();
							}
							exist = !exist;

						}
					}
				});
				boardButtons[i][j].setMaxWidth(Double.MAX_VALUE);
				boardButtons[i][j].setMaxHeight(Double.MAX_VALUE);
				gridPane.add(boardButtons[i][j], j, i);
			}
		}
	}

	private void updateboardButtons() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				boardButtons[i][j].setText(field.get(i, j));
				// if user pressed the button change background to white
				if (!boardButtons[i][j].getText().equals(".") && !boardButtons[i][j].getText().equals(" ")
						&& !boardButtons[i][j].getText().equals("X")) {
					boardButtons[i][j].setGraphic(null);
					boardButtons[i][j].setStyle("-fx-background-color:white");

				}
				// if user press mine change image to mine
				if (boardButtons[i][j].getText().equals("X")) {
					boardButtons[i][j].setText("");
					boardButtons[i][j].setStyle("-fx-background-color:red");
					Image xImage = new Image("mines/mine.png");
					ImageView view = new ImageView(xImage);
					view.setFitHeight(15);
					view.setPreserveRatio(true);
					boardButtons[i][j].setGraphic(view);
				}
				// if user tries to add flag then add the flag image
				if (boardButtons[i][j].getText().equals("F")) {
					boardButtons[i][j].setText("");
					boardButtons[i][j].setStyle("-fx-background-color: white");
					Image flagImage = new Image("mines/flag.png");
					ImageView view = new ImageView(flagImage);
					view.setFitHeight(25);
					view.setPreserveRatio(true);
					boardButtons[i][j].setGraphic(view);
				}
				// remove the flag if user right clicked and there is already a flag
				if (boardButtons[i][j].getText().equals(".")) {
					boardButtons[i][j].setGraphic(null);
					boardButtons[i][j].setStyle(
							"-fx-background-color: #15758f; -fx-border-color: #34253d; -fx-border-radiuss: 3; -fx-text-fill: #ffffff");
				}
			}
	}

	public void setHbox(HBox hbox) {
		this.hbox = hbox;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void popUpWindow(boolean isOver, boolean isDone) {
		Scene popUp = new Scene(initHBox(isOver, isDone), 400, 200);
		Stage popUpStage = new Stage();
		popUpStage.setScene(popUp);
		popUpStage.show();
	}

	private HBox initHBox(boolean isOver, boolean isDone) {
		HBox hbox = new HBox();
		// user losing
		if (isOver) {
			popUpLabel = new Label("YOU LOST!");
			Image lostImage = new Image("mines/loser.png");
			ImageView view = new ImageView(lostImage);
			view.setFitHeight(150);
			view.setPreserveRatio(true);
			popUpLabel.setGraphic(view);
		}
		// user winning
		else if (isDone) {
			popUpLabel = new Label("YOU WON!");
			Image winImage = new Image("mines/winner.png");
			ImageView view = new ImageView(winImage);
			view.setFitHeight(150);
			view.setPreserveRatio(true);
			popUpLabel.setGraphic(view);
		}
		popUpLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		hbox.setPadding(new Insets(30, 20, 20, 80));
		hbox.getChildren().addAll(popUpLabel);
		return hbox;
	}

	public TextField getTextFieldWidth() {
		return textFieldWidth;
	}

	public TextField getTextFieldHeight() {
		return textFieldHeight;
	}

	public TextField getTextFieldMines() {
		return textFieldMines;
	}

}