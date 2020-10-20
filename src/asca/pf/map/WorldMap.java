package asca.pf.map;
/**
 * 画面上に表示するマップ
 */
public class WorldMap {

	//private double world_size;
	private Map map[][];
	private double worldsizex, worldsizey;

	/**
	 * コンストラクタ
	 * @param world_size
	 */
	public WorldMap(double world_size) {
		/*this.world_size = world_size;
		this.map = new Map[(int)world_size][(int)world_size];

		for(int i = 0; i < this.world_size; i++) {
			for(int j = 0; j < this.world_size; j++) {
				map[i][j] = new Map(i, j, false);
			}
		}*/
		this(world_size, world_size);
	}

	/**
	 * コンストラクタ
	 * @param world_size_x
	 * @param world_size_y
	 */
	public WorldMap(double world_size_x, double world_size_y) {
		this.worldsizex = world_size_x;
		this.worldsizey = world_size_y;
		this.map = new Map[(int)worldsizex][(int)worldsizey];

		for(int i = 0;i < this.worldsizex; i++) {
			for(int j = 0; j < this.worldsizey; j++) {
				map[i][j] = new Map(i, j, false);
			}
		}
	}

	/**
	 * カベがあるかないかを返す
	 * @param x
	 * @param y
	 * @return 目印があればtrue なければfalse
	 */
	public boolean getwall(int x,int y) {
		return map[x][y].getwall();
	}

	/**
	 *
	 */
	public double getx(int x,int y) {
		return map[x][y].getx();
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public double gety(int x,int y) {
		return map[x][y].gety();
	}

	/**
	 *
	 * @return
	 */
	public double getworldsizex() {
		return this.worldsizex;
	}

	/**
	 *
	 * @return
	 */
	public double getworldsizey() {
		return this.worldsizey;
	}
}
