package dungeonshooter.animator;

import java.util.Iterator;

import dungeonshooter.entity.Bullet;
import dungeonshooter.entity.Entity;
import dungeonshooter.entity.Player;
import dungeonshooter.entity.PolyShape;
import dungeonshooter.entity.property.HitBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Animator extends AbstractAnimator {

	private Color background = Color.ANTIQUEWHITE;

	public void handle(GraphicsContext gc, long now) {
		updateEntities();
		clearAndFill(gc, background);
		drawEntities(gc);
	}

	public void updateEntities() {
		map.updateProjectilesList();

		for (Entity bullet : map.projectiles()) {
			bullet.update();
		}

		for (Entity player : map.players()) {
			player.update();
		}

		for (PolyShape staticShape : map.staticShapes()) {
			staticShape.update();
		}

		if (map.getDrawBounds()) {
			for (Entity bullet : map.projectiles()) {
				bullet.getHitBox().getDrawable().setStroke(Color.RED);
			}
			for (Entity player : map.players()) {
				player.getHitBox().getDrawable().setStroke(Color.RED);
			}
		}
		for (PolyShape staticShape : map.staticShapes()) {
			proccessEntityList(map.projectiles().iterator(), staticShape.getHitBox());
			proccessEntityList(map.players().iterator(), staticShape.getHitBox());
		}
	}

	public void proccessEntityList(Iterator<Entity> iterator, HitBox shapeHitBox) {
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			HitBox bounds = entity.getHitBox();

			if (!map.inMap(bounds)) {
				if (entity instanceof Player)
					((Player) entity).stepBack();
				else if (entity instanceof Bullet)
					iterator.remove();

			} else if (shapeHitBox.intersectBounds(bounds)) {
				if (map.getDrawBounds())
					bounds.getDrawable().setStroke(Color.BLUEVIOLET);

				if (shapeHitBox.intersectFull(bounds)) {
					if (entity instanceof Player)
						((Player) entity).stepBack();
					else if (entity instanceof Bullet)
						iterator.remove();
				}
			}
		}
	}

}
