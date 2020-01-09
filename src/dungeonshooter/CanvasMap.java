package dungeonshooter;

import java.util.ArrayList;
import java.util.List;

import dungeonshooter.animator.Animator;
import dungeonshooter.entity.Bullet;
import dungeonshooter.entity.Entity;
import dungeonshooter.entity.PolyShape;
import dungeonshooter.entity.property.HitBox;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

/**
 * this class represents the drawing area. it is backed by {@link Canvas} class.
 * this class itself does not handle any of the drawing. this task is
 * accomplished by the {@link AnimationTimer}.
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 13, 2019
 */
public class CanvasMap {

	private Canvas map;

	private BooleanProperty drawBounds, drawFPS;

	private List<Entity> players, projectiles, buffer;

	private PolyShape border;

	private Animator animator;

	private List<PolyShape> staticShapes;

	/**
	 * create a constructor and initialize all class variables.
	 */
	public CanvasMap() {
		map = new Canvas();

		drawBounds = new SimpleBooleanProperty();
		drawFPS = new SimpleBooleanProperty();

		players = new ArrayList<>(1);
		staticShapes = new ArrayList<>(50);
		projectiles = new ArrayList<>(500);
		buffer = new ArrayList<>(500);

		border = new PolyShape();
		border.getDrawable().setFill(new ImagePattern(new Image("file:assets/floor/pavin.png"), 0, 0, 256, 256, false));
	}

	public BooleanProperty drawBoundsProperty() {
		return drawBounds;
	}

	public boolean getDrawBounds() {
		return drawBounds.get();
	}

	public BooleanProperty drawFPSProperty() {
		return drawFPS;
	}

	public boolean getDrawFPS() {
		return drawFPS.get();
	}

	public CanvasMap setDrawingCanvas(Canvas map) {
		if (map == null)
			throw new NullPointerException();

		this.map = map;
		this.map.widthProperty()
				.addListener((observable, oldValue, newValue) -> border.setPoints(0, 0, w(), 0, w(), h(), 0, h()));
		this.map.heightProperty()
				.addListener((observable, oldValue, newValue) -> border.setPoints(0, 0, w(), 0, w(), h(), 0, h()));

		return this;
	}

	/**
	 * create a method called setAnimator. set an {@link AbstractAnimator}. if an
	 * animator exists {@link CanvasMap#stop()} it and call
	 * {@link CanvasMap#removeMouseEvents()}. then set the new animator and call
	 * {@link CanvasMap#start()} and {@link CanvasMap#registerMouseEvents()}.
	 * 
	 * @param newAnimator - new {@link AbstractAnimator} object
	 * @return the current instance of this object
	 */
	public CanvasMap setAnimator(Animator newAnimator) {
		animator = newAnimator;
		return this;
	}

	/**
	 * <p>
	 * register the given {@link EventType} and {@link EventHandler}
	 * </p>
	 * 
	 * @param event   - an event such as {@link MouseEvent#MOUSE_MOVED}.
	 * @param handler - a lambda to be used when registered event is triggered.
	 */
	public <E extends Event> void addEventHandler(EventType<E> event, EventHandler<E> handler) {
		map.addEventHandler(event, handler);
	}

	/**
	 * <p>
	 * remove the given {@link EventType} registered with {@link EventHandler}
	 * </p>
	 * 
	 * @param event   - an event such as {@link MouseEvent#MOUSE_MOVED}.
	 * @param handler - a lambda to be used when registered event is triggered.
	 */
	public <E extends Event> void removeEventHandler(EventType<E> event, EventHandler<E> handler) {
		map.removeEventHandler(event, handler);
	}

	/**
	 * create a method called start. start the animator.
	 * {@link AnimationTimer#start()}
	 */
	public void start() {
		animator.start();
	}

	/**
	 * create a method called stop. stop the animator. {@link AnimationTimer#stop()}
	 */
	public void stop() {
		animator.stop();
	}

	/**
	 * create a method called getCanvas. get the JavaFX {@link Canvas} node
	 * 
	 * @return {@link Canvas} node
	 */
	public Canvas getCanvas() {
		return map;
	}

	/**
	 * create a method called gc. get the {@link GraphicsContext} of {@link Canvas}
	 * that allows direct drawing.
	 * 
	 * @return {@link GraphicsContext} of {@link Canvas}
	 */
	public GraphicsContext gc() {
		return map.getGraphicsContext2D();
	}

	/**
	 * create a method called h. get the height of the map,
	 * {@link Canvas#getHeight()}
	 * 
	 * @return height of canvas
	 */
	public double h() {
		return map.getHeight();
	}

	/**
	 * create a method called w. get the width of the map, {@link Canvas#getWidth()}
	 * 
	 * @return width of canvas
	 */
	public double w() {
		return map.getWidth();
	}

	public List<PolyShape> staticShapes() {
		return staticShapes;
	}

	public List<Entity> players() {
		return players;
	}

	public List<Entity> projectiles() {
		return projectiles;
	}

	public CanvasMap addSampleShapes() {
		PolyShape shape1 = new PolyShape();
		shape1.setPoints(650, 110, 500, 450, 690, 200);
		shape1.getDrawable().setFill(Color.TEAL).setStroke(Color.BLACK).setWidth(5);
		staticShapes.add(shape1);

		PolyShape shape2 = new PolyShape();
		shape2.setPoints(30, 40, 400, 60, 170, 140, 100, 350);
		shape2.getDrawable().setFill(Color.PALEGREEN).setStroke(Color.BLACK).setWidth(3);
		staticShapes.add(shape2);

		PolyShape shape3 = new PolyShape();
		shape3.setPoints(25, 600, 130, 500, 395, 535, 600, 750);
		shape3.getDrawable().setFill(Color.TOMATO).setStroke(Color.BLACK).setWidth(4);
		staticShapes.add(shape3);

		return this;
	}

	public void fireBullet(Bullet bullet) {
		buffer.add(bullet);
	}

	public void updateProjectilesList() {
		projectiles.addAll(buffer);
		buffer.clear();
	}

	public PolyShape getMapShape() {
		return border;
	}

	public boolean inMap(HitBox hitbox) {
		return border.getHitBox().containsBounds(hitbox);
	}
}
