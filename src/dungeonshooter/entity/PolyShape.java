package dungeonshooter.entity;

import dungeonshooter.entity.property.HitBox;
import dungeonshooter.entity.property.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class PolyShape implements Entity {

	private int pointCount;
	private double[][] points;
	private double minX, minY, maxX, maxY;
	private Sprite sprite;
	private HitBox hitbox;

	public PolyShape() {
		hitbox = new HitBox() {
			protected boolean hasIntersectFull() {
				return true;
			}

			protected double[][] getPoints() {
				return points;
			}
		};
		sprite = new Sprite() {
			{
				setFill(new ImagePattern(new Image("file:assets/concrete/dsc_1621.png")));
			}

			public void draw(GraphicsContext gc) {
				gc.setLineWidth(getWidth());
				if (getStroke() != null) {
					gc.setStroke(getStroke());
					gc.strokePolygon(points[0], points[1], pointCount);
				}
				if (getFill() != null) {
					gc.setFill(getFill());
					gc.fillPolygon(points[0], points[1], pointCount);
				}
			}
		};
	}

	public PolyShape randomize(double centerX, double centerY, double size, int minPoints, int maxPoints) {
		return null;
	}

	public PolyShape setPoints(double... nums) {
		minX = nums[0];
		minY = nums[1];

		maxX = nums[0];
		maxY = nums[1];

		pointCount = nums.length / 2;

		points = new double[2][pointCount];
		
		int pointIndex = 0;
		for (int i = 0; i < nums.length; i = i + 2) {
			points[0][pointIndex] = nums[i];
			points[1][pointIndex] = nums[i + 1];
			pointIndex++;
			updateMinMax(nums[i], nums[i + 1]);
		}
		hitbox.setBounds(minX, minY, maxX - minX, maxY - minY);

		return this;
	}

	private void updateMinMax(double x, double y) {
		if (x < minX)
			minX = x;
		else if (x > maxX)
			maxX = x;

		if (y < minY)
			minY = y;
		else if (y > maxY)
			maxY = y;
	}

	public int pointCount() {
		return pointCount;
	}

	public double[][] points() {
		return points;
	}

	public double pX(int index) {
		return points[0][index];
	}

	public double pY(int index) {
		return points[1][index];
	}

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public Sprite getDrawable() {
		return sprite;
	}

	@Override
	public void update() {

	}

	@Override
	public boolean hasHitbox() {
		return true;
	}

	@Override
	public HitBox getHitBox() {
		return hitbox;
	}

}
