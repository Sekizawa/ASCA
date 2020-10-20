package asca.random;

/**
 * MersenneTwisterにシングルパターンを適用するためのクラス<br>
 * なお、10億回に1度MersenneTwisterを生成し直している
 */
public class WrapMersenneTwister {
	private static WrapMersenneTwister instance = new WrapMersenneTwister();
	private MersenneTwister rand;

	/**
	 * 乱数生成回数
	 */
	private int count = 0;

	/**
	 * コンストラクタ
	 */
	private WrapMersenneTwister() {
		rand = new MersenneTwister();
	}

	/**
	 * インスタンスを返す
	 * @return WrapMersenneTwisterクラスのインスタンス
	 */
	public static WrapMersenneTwister getInstance() {
		return instance;
	}

	public int nextInt() {
		addCount();
		return rand.nextInt();
	}

	public double nextDouble() {
		addCount();
		return rand.nextDouble();
	}

	synchronized public double nextGaussian() {
		addCount();
		return rand.nextGaussian();
	}

	/**
	 * 乱数の生成回数をカウントする<br>
	 * 10億回生成されたらMersenneTwisterを生成し直す
	 */
	private void addCount() {
		count++;

		// 10億回生成で乱数テーブル初期化
		if(count == 1000000000) {
			count = 0;
			rand = new MersenneTwister();
		}
	}
}
