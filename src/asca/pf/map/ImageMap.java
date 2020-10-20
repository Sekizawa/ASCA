package asca.pf.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * 2016/10/11 作成
 * ImageからMapを生成するMapクラス
 * WorldMapを継承しているため、今のところPFmain.javaをWorldMap map = new ImageMap();とするだけで使用可能
 * ただし、WorldMapにキャストして使う場合、getHeight, getWidthは使用不可
 * そのため、正方形の画像を読み込む必要あり
 * 2016/10/14 修正 高さと幅の関係を修正
 * @author fukagawa
 *
 */
public class ImageMap extends WorldMap {

	private double height, width;
	private Map[][] map;

	/*public ImageMap() {
		this("./image/map05.png");
	}*/

	/**
	 * コンストラクタ
	 * @param filepath
	 */
	public ImageMap(String filepath) {
		super(0);
		File file = new File(filepath);
		try {
			BufferedImage image = ImageIO.read(file);
			height = (double)image.getHeight();
			width = (double)image.getWidth();
			map = new Map[image.getWidth()][image.getHeight()];

			// 幅回だけ1ずつ
			for(int i = 0; i < map.length; i++) {
				// 高さ回だけ1ずつ
				for(int j = 0; j < map[0].length; j++) {
					if(image.getRGB(i, j) == Color.white.getRGB()) {
						map[i][j] = new Map(i, j, false);
					}
					else {
						map[i][j] = new Map(i, j, true);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	public boolean getwall(int x, int y) {
		return map[x][y].getwall();
	}

	/**
	 *
	 */
	public double getx(int x, int y) {
		return map[x][y].getx();
	}

	/**
	 *
	 */
	public double gety(int x, int y) {
		return map[x][y].gety();
	}

	/**
	 *
	 */
	public double getworldsizex() {
		return width;
	}

	/**
	 *
	 */
	public double getworldsizey() {
		return height;
	}
}
