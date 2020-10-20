package asca.pf;

import asca.Setting;
import asca.pf.csv.CSVpf;
import asca.pf.map.Map;
import asca.pf.map.WorldMap;
import asca.random.MersenneTwister;
import asca.random.WrapMersenneTwister;

/**
 *
 */
public class PFilter {
	private Setting setting = Setting.getInstance();

	/**
	 * 粒子の数
	 */
	private int n;

	/**
	 * 粒子
	 */
	private Particle[] particle;

	/**
	 * WrapMersenneTwisterクラスのオブジェクト
	 */
	private WrapMersenneTwister rand = WrapMersenneTwister.getInstance();

	private double pi = Math.PI;
	private WorldMap map;
	private CSVpf csv = new CSVpf(setting.getOutputPath() + "pfoutput.csv");
	private int mode;
	private Singleton s = Singleton.getInstance();

	/**
	 * コンストラクタ
	 * @param n 粒子の数
	 * @param world_size 地図のサイズ
	 */
	public PFilter(int n, WorldMap map) {
		//環境データ取得
		this.n = n;
		this.map = map;
		csv.mapOut(map, n);
	}

	/**
	 * 粒子生成メソッド
	 */
	public void newParticle() {
		this.particle = new Particle[n];
		for(int i = 0; i < n; i++) {
			double xr = rand.nextDouble() * map.getworldsizex();
			double yr = rand.nextDouble() * map.getworldsizey();
			double ar = rand.nextDouble() * (2.0 * Math.PI);
			Particle x = new Particle(xr, yr, ar, this.map/*, landlen, maxlen, plus, minus, estp, estm*/);
			this.particle[i] = x;
		}
	}

	/**
	 * 2016/11/24 粒子生成メソッド。ロボット初期位置既知用テスト
	 * @param x ロボットのx座標
	 * @param y ロボットのy座標
	 * @param a ロボットのangle(rad)
	 */
	public void newParticle(double x, double y, double a) {
		this.particle = new Particle[n];
		for(int i = 0; i < n; i++) {
			Particle p = new Particle(x, y, a, this.map);
			this.particle[i] = p;
		}
	}

	/**
	 * ロボットまたは粒子に移動を反映
	 * @param rob 動かしたいロボット
	 * @param f 動く距離
	 * @param angle 動く角度
	 * @param mode
	 */
	public void move(Robot rob, double f, double angle, int mode) {
		double x = rob.getX();
		double y = rob.getY();
		double ang = rob.getAngle();
		this.mode = mode;

		ang += angle;
		if(mode == 0){
			f = this.adjustForward(rob, f, ang);
		}

		ang = normalAngle(ang);

		x += f * Math.cos(ang);
		y += f * Math.sin(ang);

		//地図から飛び出たら反対側に戻ってくる
		x %= map.getworldsizex();
		y %= map.getworldsizey();

		if(x < 0){
			x = map.getworldsizex() + x;
		}
		if(y < 0){
			y = map.getworldsizey() + y;
		}

		//ロボットの位置と向きを更新する
		rob.setAll(x, y, ang);
	}

	/**
	 * 各目印とロボットor粒子の距離計算
	 * 実際のロボット（超音波センサ）では目印の区別がつかない
	 *
	 * 20190108 rand.setSeed(100)を消すと動作がおかしくなるため、このメソッドのみMersenneTwisterを別に生成する
	 *
	 * @param robot 実際のロボット
	 * @return ロボットと各目印との距離
	 */
	public LenAngle[] calcLenAngle(Robot robot) {
		MersenneTwister rand = new MersenneTwister();
		Map[] landmark = new Map[s.getLandlen()];

		LenAngle[] Z = new LenAngle[s.getLandlen()];

		// 乱数生成の不具合の原因と考えて消したが、消すと動作がおかしくなる by 鈴木
		rand.setSeed(100);

		for(int i = 0; i < landmark.length; i++) {
			double lr = rand.nextDouble() * robot.getMaxlen();
			double ar = rand.nextDouble() * (2.0 * Math.PI);
			Z[i] = new LenAngle(robot.getMaxlen() + lr, ar, false);
		}

		double dist;
		double radian;

		robot.useSensor();
		landmark = robot.getLandmark();
		for(int i = 0; i < landmark.length; i++) {
			dist = Math.sqrt(Math.pow((robot.getX() - landmark[i].getx()), 2) + Math.pow((robot.getY() - landmark[i].gety()), 2));

			// ロボット中心の相対座標を求める
			double ax = 10.0 * Math.cos(robot.getAngle());
			double ay = 10.0 * Math.sin(robot.getAngle());
			double bx = landmark[i].getx() - robot.getX();
			double by = landmark[i].gety() - robot.getY();
			double inner = ax * bx + ay * by;
			double outer = ax * by - ay * bx;

			radian = Math.atan2(outer, inner);
			//radian = Math.atan2(y2 - landmark[i].gety(), x2 - landmark[i].getx());

			radian = normalAngle(radian);
			//ConsoleFrame.getInstance().println(radian);
			if(landmark[i].getwall()){
				Z[i].setLength(dist);
				Z[i].setAngle(radian);
				Z[i].setWall(true);
			}
		}

		return Z;
	}

	/**
	 * 重み付け、尤度計算、リサンプリング
	 * @param L ロボットと各目印との距離
	 * @param forward ロボットが動く距離
	 * @param angle ロボットの向き
	 * @return パーティクルフィルタを反映させ動かした粒子
	 */
	public Particle[] doing(LenAngle[] L, double forward, double angle) {

		//動作
		for(int i = 0; i < n; i++) {
			//各粒子の移動
			move(this.particle[i], forward, angle, this.mode);
		}

		csv.stepOut();

		csv.filterOut(this.particle, "重み付け前");

		//重み付け
		calcWeight(this.particle, L);

		csv.filterOut(this.particle, "重み付け後");

		//粒子のリサンプリング
		resample();

		csv.filterOut(this.particle, "リサンプリング後");

		csv.newLine();

		return this.particle;
	}

	/**
	 * 重み付け
	 * @param particle 粒子
	 * @param Z 実際のロボットと各目印との距離
	 */
	private void calcWeight(Particle[] particle, LenAngle/*double*/[] Z) {
		double weight;
		for(int j = 0; j < n; j++) {
			weight = 1.0;
			LenAngle[] L = this.calcLenAngle(particle[j]);
			for(int i = 0; i < s.getLandlen(); i++){

				/*double angle1 = Math.abs(L[i].getAngle());
				double angle2 = Math.abs(L[i].getAngle() - 2 * pi);
				L[i].setAngle(Math.min(angle1, angle2));*/

				weight *= calcWeight(L[i], Z[i], 5/*？？？*/);//分散は要検討
			}
			this.particle[j].setWeight(weight);
		}

		//重みの正規化
		normalizeWeight();
	}

	/**
	 * 重みの正規化
	 */
	private void normalizeWeight() {
		double sum = 0;
		double[] temp = new double[n];
		for(int i = 0; i < n; i++) {
			sum += this.particle[i].getWeight();
		}
		for(int i = 0; i < n; i++) {
			temp[i] = this.particle[i].getWeight() / sum;
		}

		csv.filterOut(particle, "重みの正規化前");

		for(int i = 0; i < n; i++) {
			this.particle[i].setNweight(temp[i]);
		}
	}

	/**
	 *
	 * @param x 粒子とある目印との距離
	 * @param mu 実際のロボットとある目印との距離
	 * @param sigma σ
	 * @return
	 */
	private double calcWeight(LenAngle x, LenAngle mu, double sigma) {
		double len = 1.0 / (Math.sqrt(2.0 * Math.PI) * sigma) * Math.exp(-Math.pow((x.getLength()) - (mu.getLength()), 2.0) / (2.0 * Math.pow(sigma, 2.0)));
		double angleDif = x.getAngle() - mu.getAngle();
		if(Math.abs(angleDif) > pi) {
			if(angleDif < 0.0) {
				angleDif += 2.0 * pi;
			}
			else {
				angleDif -= 2.0 * pi;
			}
		}
		sigma = 0.5;
		double ang = 1.0 / (Math.sqrt(2.0 * Math.PI) * sigma) * Math.exp(-Math.pow(angleDif, 2.0) / (2.0 * Math.pow(sigma, 2.0)));
		return len * ang;
	}

	/**
	 * リサンプリング
	 */
	private void resample() {
		double[] W = new double[n];
		W[0] = particle[0].getNweight();

		for(int i = 1; i < n; i++) {
			W[i] = W[i - 1] + particle[i].getNweight();
		}

		Particle[] crob = new Particle[n];

		for(int i = 0; i < n; i++) {
			crob[i] = new Particle(particle[i].getX(), particle[i].getY(), particle[i].getAngle(), this.map);
			crob[i].setAll(particle[i].getX(), particle[i].getY(), particle[i].getAngle());
			crob[i].setWeight(particle[i].getWeight());
			crob[i].setNweight(particle[i].getNweight());
		}

		/*
		 * ここだけRandomクラスなのは疑問
		 * WrapMersenneTwisterクラスを使用してもよいと思ったため除去
		 * 挙動がおかしければ直すように
		 * by 鈴木
		 */
		// Random ran = new Random();

		for(int i = 0;i < n; i++) {
			double d;// = Math.random();
			d = rand.nextDouble();
			for(int j = 0; j < n; j++) {
				if(d <= W[j]) {
					/*
					 * ここだけRandomクラスなのは疑問
					 * WrapMersenneTwisterクラスを使用してもよいと思ったため除去
					 * 挙動がおかしければ直すように
					 * by 鈴木
					 */
					/*double x = crob[j].getX() + ran.nextGaussian();
					double y = crob[j].getY() + ran.nextGaussian();
					double a = crob[j].getAngle() + ran.nextGaussian();*/

					double x = crob[j].getX() + rand.nextGaussian();
					double y = crob[j].getY() + rand.nextGaussian();
					double a = crob[j].getAngle() + rand.nextGaussian();

					a = normalAngle(a);

					//地図から飛び出たら反対側に戻ってくる

					x %= map.getworldsizex();
					y %= map.getworldsizey();
					if(x < 0) {
						x = map.getworldsizex() + x;
					}
					if(y < 0){
						y = map.getworldsizey() + y;
					}

					particle[i].setAll(x, y, a);

					break;
				}
			}
		}
	}

	/**
	 * 粒子のデータを取得する
	 * @return 粒子
	 */
	public Particle[] getData() {
		return this.particle;
	}

	/**
	 * 各目印の座標を取得する
	 * @param myrobot
	 * @return 目印
	 */
	public double[][] getLandmark(Robot myrobot) {
		double[][] re = new double[s.getLandlen()][2];
		Map[] landmark = myrobot.getLandmark();
		for(int i = 0; i < landmark.length; i++) {
			re[i][0] = landmark[i].getx();
			re[i][1] = landmark[i].gety();

		}
		return re;
	}

	/**
	 * --------------------------------------------------------------------<br>
	 * 2016/10/14 追加 以下、未完成です<br>
	 * 前方に進める限界値を取得する<br>
	 * ここから得た値をforwardとすれば、壁より先には進まなくなる、はず
	 * 観測して得た値ではなくて、ロボットが実際に動けなくなる値
	 * 現状のこのメソッドだと角度によって壁にめり込むため、ロボットの中心からの最短距離を求めたい
	 * 2.0だとか2.1は地図上でのロボットの大きさを想定した値
	 * 2016/10/20 getForwardMaxメソッド削除
	 * --------------------------------------------------------------------
	 *
	 * 2016/10/20 追加 壁に衝突した場合、進行を止めるメソッド.
	 * lenが変な増え方してたので、BigDecimal使用
	 * 重い 軽くしたい
	 * @param act 動かすロボット
	 * @param forward 前進距離
	 * @param angle 制御命令の角度に基づいて変更した角度（ラジアン）
	 * @return 前進可能距離
	 */
	private double adjustForward(Robot act, double forward, double angle) {
		double len = 0.0;
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double inc = (forward < 0.0) ? -0.1 : 0.1;
		// 前進可能距離の大きさが、前進限界距離の大きさを超えた場合、ループ終了
		while(Math.abs(len) < Math.abs(forward)) {
			double dx = len * cos;
			dx = (act.getX() + dx + map.getworldsizex()) % map.getworldsizex();
			double dy = len * sin;
			dy = (act.getY() + dy + map.getworldsizey()) % map.getworldsizey();
			// 後退してたらsin,cosが逆になるように調整して調べる
			if(this.nearWall(cos * Math.signum(forward), sin * Math.signum(forward), dx, dy)) {
				// 衝突するなら衝突点一歩手前までの前進可能距離を戻す
				return (len == 0.0) ? 0.0 : len - inc;
			}
			// 前進可能距離を更新する
			len += inc;
		}
		return forward;
	}

	/**
	 * 2016/10/20 追加
	 * 壁が至近にあるか調べるメソッド. 見る壁の位置がこの感じでいいのかは不明.
	 * 引数のx, y をdoubleに変更して、robotの幅との和をgetwallの引数にする予定
	 * 地図上で左上方向に動く場合は、上と右に壁があるか調べる.
	 * 地図上で右上方向に動く場合は、上と左に壁があるか調べる.
	 * 地図上で右下方向に動く場合は、下と左に壁があるか調べる.
	 * 地図上で左下方向に動く場合は、下と右に壁があるか調べる.
	 * @param cos 進行方向判別用. 0以上なら右側、0未満なら左側に移動していると見る
	 * @param sin 進行方向判別用. 0以上なら上側、0未満なら下側に移動していると見る
	 * @param x x座標. この座標を中心として近傍の壁を調べる
	 * @param y y座標. 弧の座標を中心として近傍の壁を調べる
	 * @return 壁が存在する場合はtrue,そうでない場合false
	 */
	private boolean nearWall(double cos, double sin, double x, double y) {
		double r = 0.5;
		boolean ret = map.getwall((int)x, (int)y);
		if(Math.signum(cos) >= 0 && Math.signum(sin) >= 0) {
			ret = map.getwall((int)((x + r) % map.getworldsizex()), (int)y) ? true
					: map.getwall((int)x, (int)((y + r) % map.getworldsizey())) ? true
							: ret;
		}
		else if(Math.signum(cos) < 0 && Math.signum(sin) >= 0) {
			ret = map.getwall((int)((x - r + map.getworldsizex()) % map.getworldsizex()), (int)y) ? true
					: map.getwall((int)x, (int)((y + r) % map.getworldsizey())) ? true
							: ret;
		}
		else if(Math.signum(cos) < 0 && Math.signum(sin) < 0) {
			ret = map.getwall((int)((x - r + map.getworldsizex()) % map.getworldsizex()), (int)y) ? true
					: map.getwall((int)x, (int)((y - r + map.getworldsizey()) % map.getworldsizey())) ? true
							: ret;
		}
		else if(Math.signum(cos) >= 0 && Math.signum(sin) < 0) {
			ret = map.getwall((int)((x + r) % map.getworldsizex()), (int)y) ? true
					: map.getwall((int)x, (int)((y - r + map.getworldsizey()) % map.getworldsizey())) ? true
							: ret;
		}
		return ret;
	}

	/**
	 *
	 * @param angle
	 * @return
	 */
	private double normalAngle(double angle) {
		double ang = angle;

		//角度の剰余
		ang %= 2 * pi;

		//角度を正の値にする

		if(ang < 0) {
			ang += 2 * pi;
		}

		return ang;
	}
}

