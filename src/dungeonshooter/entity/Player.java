package dungeonshooter.entity;

import dungeonshooter.CanvasMap;
import dungeonshooter.entity.property.HitBox;
import dungeonshooter.entity.property.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.geometry.Point2D;
import utility.Point;

public class Player implements Entity {

	private Rotate rotationPlayer;

	private double angle;

	private double playerFrame = 0;

	private double muzzleFrame = 0;

	private Point pos;

	private Point dimension;

	private Point prev;

	private Sprite sprite;

	private HitBox hitbox;

	private PlayerInput input;

	private CanvasMap map;

	public Player(double x, double y, double w, double h) {
		rotationPlayer = new Rotate();

		pos = new Point((x - w / 2), (y - 5 / 2));

		prev = new Point(pos);

		dimension = new Point(w, h);

		sprite = new Sprite() {
			// player and muzzle each have 20 and 16 set of images than can be loaded
			private final Image[] PLAYER = new Image[20];
			private final Image[] MUZZLE = new Image[16];
			{
				// load the images
				for (int i = 0; i < PLAYER.length; i++) {
					PLAYER[i] = new Image("file:assets\\rifle\\idle\\survivor-idle_rifle_" + i + ".png");
				}
				for (int i = 0; i < MUZZLE.length; i++) {
					MUZZLE[i] = new Image("file:assets\\muzzle_flashs\\m_" + i + ".png");
				}
			}

			public void draw(GraphicsContext gc) {
				gc.save();
				// rotate gc for drawing
				gc.setTransform(rotationPlayer.getMxx(), rotationPlayer.getMyx(), rotationPlayer.getMxy(),
						rotationPlayer.getMyy(), rotationPlayer.getTx(), rotationPlayer.getTy());
				// if left click display fire animation
				if (input.leftClicked()) {
					gc.drawImage(MUZZLE[(int) muzzleFrame], getRifleMuzzleX() - 8, getRifleMuzzleY() - 25, 50, 50); // this
					muzzleFrame += .5;
				}
				// draw player image
				gc.drawImage(PLAYER[(int) playerFrame], pos.x(), pos.y(), dimension.x(), dimension.y());
				gc.restore();
				// this number is how fast the next frame of player animation will be drawn. The
				// higher the faster.
				playerFrame += 0.25;
				// reset frame counts if reach the max frame
				if (playerFrame >= PLAYER.length) {
					playerFrame = 0;
				}
				if (muzzleFrame >= MUZZLE.length || !input.leftClicked()) {
					muzzleFrame = 0;
				}
			}
		};
		double size = h * .74;
		hitbox = new HitBox().setBounds(pos.x() + dimension.x() * .303 - size / 2,
				pos.y() + dimension.y() * .58 - size / 2, size, size);
	}

	public Player setMap(CanvasMap map) {
		this.map = map;
		return this;
	}

	public double getPlayerCenterX() {
		return pos.x() + dimension.x() * .303;
	}

	public double getPlayerCenterY() {
		return pos.y() + dimension.y() * .58;
	}

	public double getRifleMuzzleX() {
		return pos.x() + dimension.x() * .93;
	}

	public double getRifleMuzzleY() {
		return pos.y() + dimension.y() * .73;
	}

	public void calculateAngles() {
		angle = Math.toDegrees(Math.atan2(input.y() - getPlayerCenterY(), input.x() - getPlayerCenterX()));
		rotationPlayer.setAngle(angle);
		rotationPlayer.setPivotX(getPlayerCenterX());
		rotationPlayer.setPivotY(getPlayerCenterY());
	}

	public void stepBack() {
		hitbox.undoTranslate();
		pos.move(prev);
	}

	@Override
	public void update() {
		calculateAngles();
		// base movement speed
		int speed = 1;

		// sprint
		if (input.isSpace())
			speed *= 2;

		// crouch prone
		if (input.isShift())
			speed = 0;

		// update after movement has taken place
		if (input.hasMoved()) {
			prev.move(pos);
			pos.translate(input.leftOrRight(), input.upOrDown() * speed);
			hitbox.translate(input.leftOrRight(), input.upOrDown() * speed);
		}

		// normal fire bullet
		if (input.leftClicked()) {
			Point2D muzzle = rotationPlayer.transform(getRifleMuzzleX(), getRifleMuzzleY());
			map.fireBullet(new Bullet(this.angle, muzzle.getX(), muzzle.getY()));
		}

		// triple fire
		if (input.middleClicked()) {
			Point2D muzzle = rotationPlayer.transform(getRifleMuzzleX(), getRifleMuzzleY());
			map.fireBullet(new Bullet(this.angle + 40, muzzle.getX(), muzzle.getY()));
			map.fireBullet(new Bullet(this.angle, muzzle.getX(), muzzle.getY()));
			map.fireBullet(new Bullet(this.angle - 40, muzzle.getX(), muzzle.getY()));
		}

		// double fire
		if (input.rightClicked()) {
			Point2D muzzle = rotationPlayer.transform(getRifleMuzzleX(), getRifleMuzzleY());
			map.fireBullet(new Bullet(this.angle + 40, muzzle.getX(), muzzle.getY()));
			map.fireBullet(new Bullet(this.angle - 40, muzzle.getX(), muzzle.getY()));
		}
	}

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public boolean hasHitbox() {
		return true;
	}

	@Override
	public HitBox getHitBox() {
		return hitbox;
	}

	@Override
	public Sprite getDrawable() {
		return sprite;
	}

	public void setInput(PlayerInput input) {
		this.input = input;
	}

}
