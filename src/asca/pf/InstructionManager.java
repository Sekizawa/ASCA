package asca.pf;

import java.util.LinkedList;
import java.util.Queue;

import asca.ConsoleFrame;

/**
 *
 */
public class InstructionManager {
	private Queue<Instruction> queue;
	private Queue<Instruction> priority;

	/**
	 * InstructionManagerのサブクラス
	 */
	public class Instruction {
		private double forward;
		private double angleRad;

		/**
		 * コンストラクタ
		 * @param forward
		 * @param angleRad
		 */
		private Instruction(double forward, double angleRad) {
			this.forward = forward;
			this.angleRad = angleRad;
		}

		/**
		 *
		 * @return
		 */
		public double getForward() {
			return forward;
		}

		/**
		 *
		 * @return
		 */
		public double getAngle() {
			return angleRad;
		}
	}

	/**
	 * コンストラクタ
	 */
	public InstructionManager() {
		queue = new LinkedList<Instruction>();
		priority = new LinkedList<Instruction>();
	}

	/**
	 * 処理していない命令があるか調べる
	 * @return 処理していない命令が存在する場合true、そうでない場合false
	 */
	public boolean hasInstruction() {
		return !queue.isEmpty();
	}

	/**
	 * 次に処理する命令を取得する
	 * @return 処理する命令を返す、命令が存在しない場合null
	 */
	public Instruction getInstruction() {
		return queue.poll();
	}

	/**
	 * 命令を追加する
	 * @param forward 前進距離
	 * @param angleRad ラジアン
	 */
	public void addInstruction(double forward, double angleRad) {
		queue.add(new Instruction(forward, angleRad));
	}

	/**
	 * 条件が満たされた場合に処理する命令をセットする
	 * @param forward 前進距離
	 * @param angleRad ラジアン
	 */
	public void setPriorityInstruction(double forward, double angleRad) {
		removePriorityInstructionAll();
		priority.add(new Instruction(forward, angleRad));
		for(Instruction elm : priority) {
			ConsoleFrame.getInstance().println("forward -> " + elm.forward);
			ConsoleFrame.getInstance().println("angle   -> " + elm.angleRad);
		}
	}

	/**
	 * 条件が満たされた場合に処理する命令列をセットする
	 * @param forwards 前進距離のセット
	 * @param anglesRad ラジアンのセット
	 */
	public boolean setPriorityInstruction(double[] forwards, double[] anglesRad) {
		if(forwards == null || anglesRad == null) {
			return false;
		}
		if(forwards.length != anglesRad.length) {
			return false;
		}
		removePriorityInstructionAll();
		for(int i = 0; i < forwards.length; i++) {
			priority.add(new Instruction(forwards[i], anglesRad[i]));
		}
		return true;
	}

	/**
	 * 優先的に処理すべき命令のリストを空にする
	 */
	public void removePriorityInstructionAll() {
		while(!priority.isEmpty()) {
			priority.poll();
		}
	}

	/**
	 * 優先的に処理すべき命令を次の処理として読み込む
	 */
	public void doPriorityInstruction() {
		Queue<Instruction> tmp = new LinkedList<Instruction>();
		while(!queue.isEmpty()) {
			tmp.add(queue.poll());
		}
		for(Instruction elm : priority) {
			queue.add(elm);
		}

		for(Instruction elm : queue) {
			ConsoleFrame.getInstance().println("forward -> " + elm.forward);
			ConsoleFrame.getInstance().println("angle   -> " + elm.angleRad);
		}
	}
}
