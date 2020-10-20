package asca.pf.csv;

import asca.pf.Particle;
import asca.pf.map.WorldMap;

/**
 *
 */
public class CSVpf extends CSV {

	private int step = 0;

	/**
	 * コンストラクタ<br>
	 * ファイルパスを読み込みcsvファイルを作成する
	 * @param filename ファイルパス
	 */
	public CSVpf(String filename) {
		super(filename);
	}

	/**
	 *
	 * @param map
	 * @param n
	 */
	public void mapOut(WorldMap map, int n) {
		post();

		pw.println("地図の大きさ");
		pw.println("横" + map.getworldsizex());
		pw.println("縦" + map.getworldsizey());

		pw.println();
		pw.println("粒子の個数" + "," + n);
		pw.println();

		pw.println("マップ");

		for(int x = 0; x <= map.getworldsizex(); x++) {
			pw.print(x + ",");
		}
		pw.println();

		for(int y = 0; y < map.getworldsizey(); y++) {
			pw.print((y + 1) + ",");
			for(int x = 0; x < map.getworldsizex(); x++) {
				if(map.getwall(x, y)) {
					pw.print("*" + ",");
				}
				else {
					pw.print("_" + ",");
				}
			}
			pw.println();
		}

		pw.println();
		close();

	}

	/**
	 *
	 * @param particle
	 * @param message
	 */
	public void filterOut(Particle[] particle, String message) {
		post();
		pw.println(message);
		pw.print("x座標" + ",");
		for(int i = 0; i < particle.length; i++) {
			pw.print(particle[i].getX() + ",");
		}
		pw.println();

		pw.print("y座標" + ",");
		for(int i = 0; i < particle.length; i++) {
			pw.print(particle[i].getY() + ",");
		}
		pw.println();

		pw.print("角度" + ",");
		for(int i = 0; i < particle.length; i++) {
			pw.print(particle[i].getAngle() + ",");
		}
		pw.println();

		pw.print("weight" + ",");
		for(int i = 0; i < particle.length; i++) {
			pw.print(particle[i].getWeight() + ",");
		}
		pw.println();

		pw.print("Nweight" + ",");
		for(int i = 0; i < particle.length; i++) {
			pw.print(particle[i].getNweight() + ",");
		}
		pw.println();
		pw.println();
		close();
	}

	/**
	 *
	 */
	public void stepOut() {
		post();
		step++;
		pw.println("ステップ" + step);
		close();
	}
}
