import java.util.Random;
import java.util.Scanner;

/**
 * 出席登録画面を表示し、入力を促す
 * 入力は以下のものを受け付ける
 *
 * 数字10桁(学籍番号)
 *
 * コマンドリスト（ただし、管理者でないと使用不可能)
 * -/help コマンドリスト表示
 * -/NA   出席していない人を一括表示
 * -/A    出席している人を一括表示
 * -/list 登録されている全リストを表示
 * -/admin 管理者モードに移行
 * -/logout 管理者モードからログアウト
 * -/exit 実行を終了し、出席していない人のみを表示します
 * -/energy エナドリ付与
 *
 * @author pokaz
 *
 */
public class attendanceWeb {

	registerAttendList rAL;		//registerAttendListのオブジェクト
	String imput;	//入力されたデータorコマンド
	boolean admin = false;	//管理者 true それ以外 false
	boolean finish = false;	//プログラム終了 true それ以外 false
	boolean energydone = false;	//エナドリ抽選をした true していない場合false

	/**
	 * デフォルトコンストラクタで、registerAttendListのオブジェクトを生成し、フィールドに格納
	 */
	public attendanceWeb() {
		rAL = new registerAttendList();
	}

	//webアプリ用attendance

	protected String getSeat(String id) {
		for(attendList AL :rAL.getAttendList()) {
			if(AL.getId().equals(id)) {
				AL.setAttended();
				return AL.getSeat();
			}
		}
		return "未登録";
	}

	protected String getName(String id) {
		for(attendList AL :rAL.getAttendList()) {
			if(AL.getId().equals(id)) return AL.getName();
		}
		return "未登録";
	}

	protected String attendedMember() {
		String str = "";

		for(attendList AL :rAL.getAttendList()) {
			if(AL.getAttend()) str += "学籍番号: " + AL.getId() + "     座席番号: " + AL.getSeat() + "<br>";
		}
		if(str.length() == 0) return "出席者はいません";
		else return str;
	}

	protected String notAttendedMember() {
		String str = "";

		for(attendList AL :rAL.getAttendList()) {
			if(!AL.getAttend()) str += "学籍番号: " + AL.getId() + "     座席番号: " + AL.getSeat() + "<br>";
		}
		if(str.length() != 0) return str;
		else return "未出席者はいません";
	}

	protected String sortNotAttendedMember() {
		String str = "";

		for(int i = 1;i <= 30;i++) {
			String seat = Integer.toString(i);
			for(attendList AL :rAL.getAttendList()) {
				if(AL.getSeat().equals(seat)) {
					if(!AL.getAttend()) str += "学籍番号: " + AL.getId() + "     座席番号: " + AL.getSeat() + "<br>";
				}
			}
		}

		if(str.length() != 0) return str;
		return "未出席者はいません";
	}


	/**
	 * フィールドfinishのゲッター
	 * @return	finish(プログラム終了するか否か)
	 */
	public boolean getFinish() {
		return this.finish;
	}

	/**
	 * コマンドを受け付けるメソッドで、コマンド(引数)に応じて、メソッドを呼び出す
	 * @param imput コマンド
	 */
	public void imput(String imput) {
		//学籍番号入力検知
		if(imput.length() == 10) {
			attend(imput);
		}else if(Character.isDigit(imput.charAt(0))) {
			error("不正な学籍番号です");
		}

		// /admin or /logout or /help の検知、この3つはフィールドadminに関わらず実行可能
		else if(imput.equals("/admin 2010370206") || imput.equals("/admin 2010370154") || imput.equals("/admin 2233730017")) {
			System.out.println("管理者モードに移行しました.");
			this.admin = true;
		}else if(imput.equals("/logout")) {
			System.out.println("管理者モードを終了しました.");
			this.admin = false;
		}else if(imput.equals("/help")) {
			help();
		}else if(imput.equals("/NA")) {
			if(admin) NA();
			else System.out.println("あなたは管理者ではありません");
		}else if(imput.equals("/A")){
			if(admin)A();
			else System.out.println("あなたは管理者ではありません");
		}else if(imput.equals("/list")) {
			if(admin)list();
			else System.out.println("あなたは管理者ではありません");
		}else if(imput.equals("/exit")){
			if(admin) exit();
			else System.out.println("あなたは管理者ではありません");
		}else if(imput.equals("/allID")){
			if(admin) allID();
			else System.out.println("あなたは管理者ではありません");
		}else if(imput.equals("/seatList")){
			if(admin) listSeat();
			else System.out.println("あなたは管理者ではありません");
		}else if(imput.equals("/energy")){
			if(admin) energy();
			else System.out.println("あなたは管理者ではありません");
		}else {
			System.out.println("不正な入力です /helpでコマンドリストを表示できます");
		}
	}

	/**
	 * エラーメッセージを表示する(プログラムは終了しない)
	 * @param errorMessage	表示するメッセージ
	 */
	protected void error(String errorMessage) {
		System.out.println(errorMessage);
	}

	/**
	 * helpコマンドで、コマンドリストを表示する
	 */
	protected void help() {
		System.out.println("----------コマンドリスト----------");
		System.out.println("[/A]出席している人を表示");
		System.out.println("[/NA]出席していない人を表示");
		System.out.println("[/list]登録されているリストを表示");
		System.out.println("[/admin 管理者のid]管理者に移行");
		System.out.println("[/logout]管理者からログアウト");
		System.out.println("[/exit]出席している人を一括表示");
		System.out.println("[/allID]登録されているリストの全学籍番号を表示");
		System.out.println("[/remake]入力した学籍番号の人のランクを書き換えてリメイクを実行.");
		System.out.println("[/energy]ランダムで5人に1人、エナドリを付与します.");
		System.out.println("-------------------------------");
	}

	/**
	 * 引数の学籍番号をリストから参照し、その人を出席登録し、座席番号や名前を出力する
	 * @param id	学籍番号
	 */
	protected void attend(String id) {
		boolean found = false;
		for(attendList AL :rAL.getAttendList()) {
			if(AL.getId().equals(id)){
				if(AL.getBlack()) {
					System.out.println("あなたはブラックリストに登録されています.");
					found = true;
				}else {
				if(AL.getAttend()) System.out.println("出席登録済みです.座席番号は【 " + AL.getSeat() + " 】です");
				else{System.out.println("こんにちは " + AL.getName() + "さん! 座席番号は【 " + AL.getSeat() + " 】です");}
				found = true;
				AL.setAttended();
			}
		}
		}
		if(!found) System.out.println("未登録の学籍番号です");
	}



	/**
	 * Not Attendの略.未出席者リストを表示する.
	 */
	protected void NA() {
		System.out.println("---------------未出席リスト---------------");
		for(attendList AL :rAL.getAttendList()) {
			if(!AL.getAttend()) showDate(AL.getName(),AL.getId(),AL.getGrade(),AL.getRank(),AL.getSeat());
		}
		System.out.println("----------------------------------------");
	}

	/**
	 * 登録されてる学籍番号を全て表示する
	 */
	protected void allID() {
		System.out.println("-------学籍番号リスト-------");
		for(attendList AL :rAL.getAttendList()) {
			showList(AL.getId());
		}
		System.out.println("-------------------------");
	}

	/**
	 * Attendの略で、現在出席登録されてるリストを表示する
	 */
	protected void A() {
		System.out.println("---------------出席リスト---------------");
		for(attendList AL :rAL.getAttendList()) {
			if(AL.getAttend()) showList(AL.getName(),AL.getId(),AL.getSeat());
		}
		System.out.println("---------------------------------------");
	}

	/**
	 * 全員の名前、学籍番号、座席番号を表示する
	 */
	protected void list() {
		System.out.println("---------------登録リスト---------------");
		for(attendList AL :rAL.getAttendList()) {
			showList(AL.getName(),AL.getId(),AL.getSeat());
		}
		System.out.println("--------------------------------------");
	}

	/**
	 * ランクと座席を表示する
	 */
	protected void listSeat() {
		System.out.println("---------------座席リスト---------------");
		for(attendList AL :rAL.getAttendList()) {
			showList(AL.getAbbrRank(),AL.getSeat());
		}
		System.out.println("--------------------------------------");
	}

	/**
	 * エナドリを付与する　なお、energyListに登録されている学籍番号の人には
	 * 付与されない
	 */
	protected void energy() {
		if(!energydone) {
		Random rand = new Random();
		new energyList();
		System.out.println("エナドリリスト登録者以外で,ランダムでエナドリ付与します.");

		for(int i = 0; i < rAL.getAttendList().size() / 5;i++) {
			int random = rand.nextInt(rAL.getAttendList().size());


			if(!rAL.getAttendList().get(random).getEnergy() && !rAL.getAttendList().get(random).getEnergyED()) {
				rAL.getAttendList().get(random).setEnergy();
			}else i--;
		}
		}
		this.energydone = true;

		System.out.println("------------当選者を表示------------");
		for(attendList list:rAL.getAttendList()) {
			if(list.getEnergy()) showList(list.getName(),list.getId(),list.getSeat());
		}
		System.out.println("----------------------------------");
	}

	/**
	 * 引数で与えられたデータを表示する
	 * @param name	名前
	 * @param id	学生番号
	 * @param seat	座席番号
	 */
	protected void showList(String name,String id,String seat) {
		System.out.println("名前:" + name + ",学籍番号:" + id + ",座席:" + seat);
	}


	/**
	 * 引数で与えられたデータを表示
	 * @param id	 学籍番号
	 */
	protected void showList(String id) {
		System.out.println("学籍番号:" + id);
	}

	/**
	 * 引数で与えられたデータを表示
	 * @param addrRank	ランク
	 * @param seat		座席
	 */
	protected void showList(String addrRank,String seat) {
		System.out.println("ランク:" + addrRank + ",座席:" + seat);
	}

	/**
	 * 引数で与えられたデータを表示
	 * @param name	名前
	 * @param id	学籍番号
	 * @param grade	学年
	 * @param rank	ランク
	 * @param seat	座席番号
	 */
	protected void showDate(String name,String id,String grade,String rank,String seat) {
		System.out.println("名前: " + name + ",学籍番号: " + id + ",学年: " + grade + ",ランク: " + rank + ",座席: " + seat);
	}

	/*protected void remake() {
		Scanner scanRM = new Scanner(System.in);
		System.out.print("ランクを変更する人の学籍番号を入力: ");
		String id = scanRM.nextLine();
		if(id.length() == 0) remake();
		else {
			for(attendList aL:rAL.getAttendList()) {
				if(aL.getId().equals(id)) {

					System.out.print("変更するランクを入力: ");
					String rank = scanRM.nextLine();
					if(rank.length() == 0) remake();
					else {

					}


				}
			}
		}
	}*/

	/**
	 * プログラムを終了するコマンド
	 */
	protected void exit() {
		Scanner scanYN = new Scanner(System.in);
		System.out.print("本当に終了しますか？ y/n: ");
		String YN = scanYN.nextLine();
		if(YN.equals("y")) {this.finish = true;
		System.out.println("プログラムを終了しました");
		NA();
		}else if(YN.equals("n")) ;
		else {System.out.print("yかnを入力してください"); exit();};
	}

	/**
	 * メインメソッド 引数は不要
	 * @param args	不要
	 */
	public static void main(String[] args) {
		attendanceWeb at = new attendanceWeb();
		String imput;
		Scanner scan = new Scanner(System.in);

		while(!at.getFinish()) {
			System.out.print("学籍番号を入力してください: ");
		imput = scan.nextLine();
		if(imput.length() != 0) {
		at.imput(imput);
		}
		}
		scan.close();
	}

}
