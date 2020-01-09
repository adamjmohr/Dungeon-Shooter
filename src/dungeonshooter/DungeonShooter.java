package dungeonshooter;

import java.util.List;

import dungeonshooter.animator.Animator;
import dungeonshooter.entity.Entity;
import dungeonshooter.entity.Player;
import dungeonshooter.entity.PlayerInput;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.InputAdapter;

public class DungeonShooter extends Application {

	/**
	 * size of the scene
	 */
	private double width = 700, height = 700;

	/**
	 * {@link BorderPane} is a layout manager that manages all nodes in 5 areas as
	 * below:
	 * 
	 * <pre>
	 * -----------------------
	 * |        top          |
	 * -----------------------
	 * |    |          |     |
	 * |left|  center  |right|
	 * |    |          |     |
	 * -----------------------
	 * |       bottom        |
	 * -----------------------
	 * </pre>
	 * 
	 * this object is passed to {@link Scene} object in
	 * {@link DungeonShooter#start(Stage)} method.
	 */
	private BorderPane root;
	/**
	 * this object represents the canvas (drawing area)
	 */
	private Canvas canvas;

	private CanvasMap board;

	private PlayerInput input;

	private String title = "Dungeon Shooter";

	private Color background = Color.LIGHTGREEN;

	/**
	 * this method is called at the very beginning of the JavaFX application and can
	 * be used to initialize all components in the application. however,
	 * {@link Scene} and {@link Stage} must not be created in this method. this
	 * method does not run JavaFX thread.
	 */
	@Override
	public void init() throws Exception {
		// initialize the canvas object
		canvas = new Canvas();

		InputAdapter adapter = new InputAdapter(canvas);
		input = new PlayerInput(adapter);

		board = new CanvasMap();

		Player player = new Player(width / 2, height / 2, 70, 46);
		player.setInput(input);
		player.setMap(board);

		Animator animator = new Animator();
		animator.setCanvas(board);

		board.setDrawingCanvas(canvas);
		board.setAnimator(animator);
		board.addSampleShapes();
		List<Entity> players = board.players();
		players.add(player);
		// create two ToolBar objects and store createStatusBar() and createOptionsBar()
		// in each
		ToolBar statusBar = createStatusBar();
		ToolBar optionsBar = createOptionsBar();
		// initialize root
		root = new BorderPane();
		// call setTop on it and pass to it the options bar
		root.setTop(optionsBar);
		// call setCenter on it and pass to it board.getCanvas()
		root.setCenter(board.getCanvas());
		// call setBottom on it and pass to it the status bar
		root.setBottom(statusBar);
		// we need to bind the height and width of of canvas to root so if screen is
		// resized board is resized as well.
		// to bind the width get the canvas from board first then call widthProperty on
		// it and then bind root.widthProperty to it
		// board.getCanvas().widthProperty().bind( root.widthProperty());
		// to bind the height it is almost the same process however the height of
		// options and status bar need to be subtracted from
		// root height. subtract can be done root.heightProperty().subtract(
		// statusBar.heightProperty()).
		// you also need to subtract optionsBar.heightProperty as well.
		board.getCanvas().widthProperty().bind(root.widthProperty());
		board.getCanvas().heightProperty()
				.bind(root.heightProperty().subtract(statusBar.heightProperty()).subtract(optionsBar.heightProperty()));
	}

	/**
	 * <p>
	 * this method is called when JavaFX application is started and it is running on
	 * JavaFX thread. this method must at least create {@link Scene} and finish
	 * customizing {@link Stage}. these two objects must be on JavaFX thread when
	 * created.
	 * </p>
	 * <p>
	 * {@link Stage} represents the frame of your application, such as minimize,
	 * maximize and close buttons.<br>
	 * {@link Scene} represents the holder of all your JavaFX {@link Node}s.<br>
	 * {@link Node} is the super class of every javaFX class.
	 * </p>
	 * 
	 * @param primaryStage - primary stage of your application that will be rendered
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// scene holds all JavaFX components that need to be displayed in Stage
		Scene scene = new Scene(root, width, height, background);
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		// when escape key is pressed close the application
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (KeyCode.ESCAPE == event.getCode()) {
				primaryStage.hide();
			}
		});
		// display the JavaFX application
		primaryStage.show();
	}

	/**
	 * this method is called at the very end when the application is about to exit.
	 * this method is used to stop or release any resources used during the
	 * application.
	 */
	@Override
	public void stop() throws Exception {
		board.stop();
	}

	/**
	 * create a {@link ToolBar} that represent the menu bar at the top of the
	 * application.
	 * 
	 * @return customized {@link ToolBar}
	 */
	public ToolBar createOptionsBar() {
		// use the createButton method and create a start button with lambda that calls
		// board.start()
		// use the createButton method and create a stop button with lambda that calls
		// board.stop()
		Button startButton = createButton("Start", e -> board.start());
		Button stopButton = createButton("Stop", e -> board.stop());
		// create 2 Pane object called filler1 and filler2
		// Pane class is a super class of all layout mangers. by itself it has no rules.
		Pane filler1 = new Pane();
		Pane filler2 = new Pane();
		// call the static method setHgrow from Hbox with Filler1, Priority.ALWAYS
		// call the static method setHgrow from Hbox with Filler2, Priority.ALWAYS
		// this will allow the filler to expand and fill the space between nodes
		HBox.setHgrow(filler1, Priority.ALWAYS);
		HBox.setHgrow(filler2, Priority.ALWAYS);
		// create a MenuButton with argument "Options", null and all of created
		// CheckMenuItem.
		// call createCheckMenuItem 6 times and use following names:
		// FPS, Intersects, Lights, Joints, Bounds, Sectors
		// only FPS is selected the rest are false.S
		// as last argument get the equivalent property from CanvasMap
		MenuButton options = new MenuButton("Options", null, createCheckBox("FPS", true, board.drawFPSProperty()),
				createCheckBox("Bounds", false, board.drawBoundsProperty()));
		// create a new ToolBar and as arguments of its constructor pass the create
		// nodes.
		// there should be 8:
		// startButton, stopButton, filler1, rayCount,
		// options, filler2, new Label( "Animators "), animatorsBox
		// return the created ToolBar
		ToolBar optionsBar = new ToolBar(startButton, stopButton, filler1, options, filler2);
		return optionsBar;
	}

	/**
	 * create a {@link ToolBar} that will represent the status bar of the
	 * application.
	 * 
	 * @return customized {@link ToolBar}
	 */
	public ToolBar createStatusBar() {
		// create two Label objects and with value of "(0,0)".
		// one label keeps track of mouse movement and other mouse drag.
		Label mouseCoordLabel = new Label("(0, 0)");
		Label dragCoordLabel = new Label("(0, 0)");
		// call addEventHandler on canvas for MouseEvent.MOUSE_MOVED event and pass a
		// lambda to
		// it that will update the text in one of the labels with the new coordinate of
		// the mouse.
		// the lambda will take one argument of type MouseEvent and two methods
		// getX and getY from MouseEvnet can be used to get position of the mouse
		board.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
			mouseCoordLabel.setText("(" + e.getX() + ", " + e.getY() + ")");
			;
		});
		// call addEventHandler on canvas for MouseEvent.MOUSE_DRAGGED event and pass a
		// lambda to
		// it that will update the text in one of the labels with the new coordinate of
		// the mouse.
		// the lambda will take one argument of type MouseEvent and two methods
		// getX and getY from MouseEvnet can be used to get position of the mouse
		board.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
			dragCoordLabel.setText("(" + e.getX() + ", " + e.getY() + ")");
			;
		});
		// create a new ToolBar and as arguments of its constructor pass the create
		// labels to it.
		// there should be 4 labels: new Label( "Mouse: "), mouseCoordLabel, new Label(
		// "Drag: "), dragCoordLabel
		// return the created ToolBar
		ToolBar statusBar = new ToolBar(new Label("Mouse: "), mouseCoordLabel, new Label("Drag: "), dragCoordLabel);
		return statusBar;
	}

	/**
	 * <p>
	 * create a {@link CheckMenuItem} with given text, initial state and
	 * {@link BooleanProperty} binding. {@link BooleanProperty} is a special class
	 * that can be binded to another {@link BooleanProperty}. this means every time
	 * bindee changes the other {@link BooleanProperty} changes as well. for
	 * example:
	 * </p>
	 * 
	 * <pre>
	 * BooleanProperty b1 = new SimpleBooleanProperty(false);
	 * BooleanProperty b2 = new SimpleBooleanProperty();
	 * b1.bind(b2);
	 * b2.set(true);
	 * System.out.println(b1.get()); // prints true
	 * IntegerProperty i1 = new SimpleIntegerProperty(1);
	 * IntegerProperty i2 = new SimpleIntegerProperty();
	 * i1.bind(i2);
	 * i2.set(100);
	 * System.out.println(i1.get()); // prints 100
	 * </pre>
	 * 
	 * <p>
	 * if p2 changes p1 changes as well. this relationship is NOT bidirectional.
	 * </p>
	 * 
	 * @param text       - String to be displayed
	 * @param isSelected - initial state, true id selected
	 * @param binding    - {@link BooleanProperty} that will be notified if state of
	 *                   this {@link CheckMenuItem} is changed
	 * @return customized {@link CheckMenuItem}
	 */
	public CheckMenuItem createCheckBox(String text, boolean isSelected, BooleanProperty binding) {
		// create a new CheckMenuItem object with given text.
		CheckMenuItem check = new CheckMenuItem(text);
		// call bind on binding and pass to it check.selectedProperty(),
		// this will notify the binding object every time check.selectedProperty() is
		// changed.
		binding.bind(check.selectedProperty());
		// call setSelected and pass to it isSelected.
		check.setSelected(isSelected);
		// return the created CheckMenuItem.
		return check;
	}

	/**
	 * create a {@link Button} with given text and lambda for when it is clicked
	 * 
	 * @param text   - String to be displayed
	 * @param action - lambda for when the button is clicked, the lambda will take
	 *               one argument of type ActionEvent
	 * @return customized {@link Button}
	 */
	public Button createButton(String text, EventHandler<ActionEvent> action) {
		// create a new Button object with given text
		Button button = new Button(text);
		// call setOnAction on created button and give it action
		button.setOnAction(action);
		// return the created button
		return button;
	}

	/**
	 * main starting point of the application
	 * 
	 * @param args - arguments provided through command line, if any
	 */
	public static void main(String[] args) {
		// launch( args); method starts the javaFX application.
		// some IDEs are capable of starting JavaFX without this method.
		launch(args);
	}
}
