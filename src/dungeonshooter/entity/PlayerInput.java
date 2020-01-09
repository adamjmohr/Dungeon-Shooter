package dungeonshooter.entity;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import utility.InputAdapter;

public class PlayerInput {

	private double x, y;

	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;

	private boolean leftClick = false;
	private boolean rightClick = false;
	private boolean middleClick = false;

	private boolean space = false;
	private boolean shift = false;

	private InputAdapter adapter;

	public PlayerInput(InputAdapter adapter) {
		this.adapter = adapter;
		this.adapter.forceFocusWhenMouseEnters();
		this.adapter.registerMouseMovment(this::moved, this::dragged);
		this.adapter.registerMouseClick(this::mousePressed, this::mouseReleased);
		this.adapter.registerKey(this::keyPressed, this::keyReleased);
	}

	public boolean hasMoved() {
		return left || right || up || down;
	}

	public int leftOrRight() {
		if (!left && !right)
			return 0;
		else if (right)
			return 1;
		else
			return -1;
	}

	public int upOrDown() {
		if (!up && !down)
			return 0;
		else if (down)
			return 1;
		else
			return -1;
	}

	public boolean leftClicked() {
		return leftClick;
	}

	public boolean rightClicked() {
		return rightClick;
	}

	public boolean middleClicked() {
		return middleClick;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	protected void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();

		leftClick = e.isPrimaryButtonDown();
		middleClick = e.isMiddleButtonDown();
		rightClick = e.isSecondaryButtonDown();
	}

	protected void mouseReleased(MouseEvent e) {
		leftClick = false;
		middleClick = false;
		rightClick = false;
	}

	public void changeKeyStatus(KeyCode key, boolean isPressed) {
		switch (key) {
		case W:
			up = isPressed;
			break;
		case A:
			left = isPressed;
			break;
		case S:
			down = isPressed;
			break;
		case D:
			right = isPressed;
			break;
		case SHIFT:
			shift = isPressed;
			break;
		case SPACE:
			space = isPressed;
			break;
		default:
			break;
		}
	}

	protected void keyPressed(KeyEvent key) {
		changeKeyStatus(key.getCode(), true);
	}

	protected void keyReleased(KeyEvent key) {
		changeKeyStatus(key.getCode(), false);
	}

	public boolean isSpace() {
		return space;
	}

	public boolean isShift() {
		return shift;
	}

	protected void moved(MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
	}

	protected void dragged(MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
	}

}
