import java.util.ArrayList;
import java.util.List;

/**
 * このクラスは、フォームに記入して貰った、出席者リストを登録するクラスである.
 * 登録するデータを atListに入れること.フォーマットは指定しており、
 * 氏名 (空白) 学籍番号 (空白) 学年 (空白) 学部学科 (空白) ランク\n　としている.
 * また、登録者の最後のデータには\0を必ず入れること.でないと、エラーが発生します.
 * @author poka
 *
 */
public class registerAttendList {

	attendList al;		//クラス:attendListのオブジェクト
	String rankStr = "";	//参加者リストのランクをmakePartyが使用できる指定フォーマットに変換した格納先
	makeParty mp;			//クラス:makePartyのオブジェクトで、パーティー生成に使用する.
	int member = 0;			//参加者リストの人数を格納する(現在は使用していない)
	int atListSize;			//名簿の数

	List<attendList> attendList = new ArrayList<>();	//名簿一人一人の詳細データ(attendListのオブジェクトに保存)

	/*ここに参加者のリストをexcelからコピペしてください*/
	/*指定フォーマット:氏名 学籍番号 学年 学部学科 ランク\n */
	/*リストの最後に '\0'をつけること*/
	String atList = "\0";

	/**
	 * デフォルトコンストラクタで、名簿の登録,ランクを指定フォーマットに変換し、makePartyを実行する.
	 * makePartyにint型の引数を追加すると、ランクソート方式でパーティーを生成します.
	 * 引数をrankStrのみにすると、評価関数平均方式でパーティーを生成します.
	 */
	public registerAttendList() {
		atList += "\0";	//必須(消さないで)
		register();		//名簿登録
		makeMP();		//指定ランクに変換し、rankStrに格納.
		/*このオブジェクトの引数にintを追加すると、ランク順に席を決める
		 * int無しでオブジェクト生成すると、バランスパーティー生成が行われる*/
		this.mp = new makeParty(rankStr,0);
		makeSeat(this.attendList);
	}

	/**
	 * attendListに登録されてるデータを指定フォーマットに変換し、文字列として
	 * rankStrにセットする.
	 */
	public void makeMP() {
		for(attendList aL:this.attendList) {
			rankStr += aL.getAbbrRank();
		}
	}

	/**
	 * フィールド:attendListのゲッター
	 * @return	attendList
	 */
	public List<attendList> getAttendList(){
		return this.attendList;
	}

	/**
	 * 引数のattendListを用いて、フィールドattendListのデータ登録者全員に座席番号を振り当てる.
	 * makePartyのデータを使用するので、makePartyのオブジェクトが生成されていないと使用できない.
	 * @param attendList	名簿リスト
	 */
	public void makeSeat(List<attendList> attendList ) {
		List<String> allParty = new ArrayList<>();
		allParty = mp.getMakedAllParty();	//ここで、makePartyで生成されたparty情報をarrayListに登録

		/**
		 * ダミーデータ登録部
		 */
		if(allParty.size() == 20) {			//生成ランクリストのデータが20の場合は、データ0〜9に、ダミーデータ(xx)を登録
			for(int i = 0; i < 10;i++) {
				allParty.add(i, "xx");
			}
		}else if(allParty.size() == 10) {	//生成ランクリストのデータが10の場合は、データ0〜19に、ダミーデータ(xx)を登録
			for(int i = 0; i < 20;i++) {
				allParty.add(i,"xx");
			}
		}

	/**
	 * ここから実際に座席を振り当てる.
	 * ただし、makePartyで弾かれたランクの人に関しては、データ登録が遅い人から順番に座席が振り当てられず、
	 * 座席データは未割当の状態となる.
	 */

	for(int n = 0; n < attendList.size(); n++) {
		if(attendList.get(n).getSeat().equals("未割当")) {
		for(int i = 0; i < allParty.size();i++) {
			if(attendList.get(n).getAbbrRank().equals(allParty.get(i))) {
				allParty.set(i, "xx");
				int p = i + 1;
				Integer seat = Integer.valueOf(p);
				this.attendList.get(n).setSeat(seat.toString());
				break;
			}
		}
		}
	}
	}

	/**
	 * データ登録部
	 */
	public void register() {
		int i=0;
		while(atList.charAt(i) != '\0') {
			int judge = 0;	//0なら名前登録,1なら学籍番号登録,2なら学年登録,3なら学部学科登録を行う.
			String str = "";
			String name = "",id = "",grade = "",faculty = "",rank = "";

		//行の処理、空白文字を読み飛ばして、データ登録
		while(atList.charAt(i) != '\n'){

		if(atList.charAt(i) == ' ' || atList.charAt(i) == '\t') {
			if(judge == 0) name = str;
			else if(judge == 1) id = str;
			else if(judge == 2) grade = str;
			else if(judge == 3) faculty = str;

			str = "";
			while(atList.charAt(i) ==  ' ' || atList.charAt(i) == '\t') {
				i++;
				};
			judge++;
		}else {
			str += atList.charAt(i);
			i++;
		}

		rank = str;
		//最後の人の判定
		if(atList.charAt(i) == '\0') break;

	}
		//実際にオブジェクトを生成し、attenListに追加
		attendList.add(al = new attendList(name,id,grade,faculty,rank,"未割当"));
		i++;
		this.atListSize++;
		member++;
		}

	}

}
