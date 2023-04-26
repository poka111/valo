/**
 * このクラスは、名簿に登録された人のデータを保持するクラスで、他にも
 * @author poka
 *
 */
public class attendList {

	blackList bL;
	energyList eL;

	String name;	//名前
	String id;		//学籍番号
	String grade;	//学年
	String faculty;	//学部学科
	String rank;	//ランク
	String abbrRank;//指定書式の省略されたランク
	String seat;	//座席
	boolean attend;	//true ->　出席 false -> 欠席
	boolean energy;	//エナドリが付与されたかどうかを判定
	boolean energyED;	//過去にエナドリが付与されたかどうかを判定
	boolean black;	//ブラックリストに登録されてるかどうかを判定
	protected final String[] rankList = {"n0","i1","i2","i3","b1","b2","b3","s1","s2","s3","g1","g2","g3","p1","p2","p3","d1","d2","d3","a1","a2","a3","m1","m2","m3","r0"};


	/**
	 * コンストラクタで、引数をフィールドにセットする.
	 * また、rankを指定書式にしたものを、フィールドabbrRankに格納する.
	 * @param name	名前
	 * @param id	学籍番号
	 * @param grade	学年
	 * @param faculty	学部学科
	 * @param rank	ランク
	 * @param seat	座席番号
	 */
	public attendList(String name,String id,String grade,String faculty,String rank,String seat) {

		this.bL = new blackList();
		this.eL = new energyList();

		this.name = name;
		this.id = id;
		this.makeGrade(grade);
		this.faculty = faculty;
		this.rank = rank;
		this.makeRank(rank);
		this.seat = seat;
		this.attend = false;
		this.energy = false;
		this.black = false;

		for(String energyID:eL.getEnergyListID()) {
			if(id.equals(energyID)) this.energyED = true;
		}

		for(String blackID:bL.getBlackListID()) {
			if(blackID.equals(id)) {
				System.out.println("ブラックリストに登録されてる人の名簿登録を確認しました.以下のデータをコメントアウトして下さい.");
				System.out.println("-----------------------------------");
				System.out.println("名前:" + name + ",学籍番号:" + id);
				System.out.println("-----------------------------------");
				this.black = true;
			}
		}
	}

	/**
	 * フィールドenergyのセッターで、trueをセット
	 */
	public void setEnergy() {
		this.energy = true;
	}

	/**
	 * フィールドseatのセッター
	 * @param seat	座席番号
	 */
	public void setSeat(String seat) {
		this.seat = seat;
	}

	/**
	 * フィールドrankのセッター　また、指定フォーマットに変換し、フィールドabbrRankにも入れる
	 * @param rank	ランク
	 */
	public void setRank(String rank) {
		this.rank = rank;
		makeRank(rank);
	}

	/**
	 * フィールド attendのセッターでtrueをセットする
	 */
	public void setAttended() {
		this.attend = true;
	}

	/**
	 * フィールドblackのゲッター
	 * @return	blackリスト登録者か否か
	 */
	public boolean getBlack() {
		return this.black;
	}

	/**
	 * フィールドenergyEDのゲッター
	 * @return	エナドリリスト登録者か否か
	 */
	public boolean getEnergyED() {
		return this.energyED;
	}

	/**
	 * フィールドenergyのゲッター
	 * @return	エナドリを付与されているか否か
	 */
	public boolean getEnergy() {
		return this.energy;
	}

	/**
	 * フィールドnameのゲッター
	 * @return	名前
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * フィールドidのゲッター
	 * @return	学籍番号
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * フィールドgradeのゲッター
	 * @return	学年
	 */
	public String getGrade() {
		return this.grade;
	}

	/**
	 * フィールドfacultyのゲッター
	 * @return	学部学科
	 */
	public String getFaculty() {
		return this.faculty;
	}

	/**
	 * フィールドrankのゲッター
	 * @return	ランク
	 */
	public String getRank() {
		return this.rank;
	}

	/**
	 * フィールドabbrRankのゲッター
	 * @return	指定書式のランク
	 */
	public String getAbbrRank() {
		return this.abbrRank;
	}

	/**
	 * フィールドseatのゲッター
	 * @return	座席番号
	 */
	public String getSeat() {
		return this.seat;
	}

	/**
	 * フィールドattendのゲッター
	 * @return	出席しているか否か
	 */
	public boolean getAttend() {
		return this.attend;
	}
	/**
	 * 入力されたランクを指定の書式に変更し、返します.
	 * @param rank	指定書式のランク(例:ゴールド1 g1,イモータル2 m2)
	 */
	public void makeRank(String rank) {			//指定書式のランク
		if(rank.equals("ランクなし")) this.abbrRank = rankList[0];		//n0
		else if(rank.equals("アイアン1")) this.abbrRank = rankList[1];		//i1
		else if(rank.equals("アイアン2")) this.abbrRank = rankList[2];		//i2
		else if(rank.equals("アイアン3")) this.abbrRank = rankList[3];		//i3
		else if(rank.equals("ブロンズ1")) this.abbrRank = rankList[4];		//b1
		else if(rank.equals("ブロンズ2")) this.abbrRank = rankList[5];		//b2
		else if(rank.equals("ブロンズ3")) this.abbrRank = rankList[6];		//b3
		else if(rank.equals("シルバー1")) this.abbrRank = rankList[7];		//s1
		else if(rank.equals("シルバー2")) this.abbrRank = rankList[8];		//s2
		else if(rank.equals("シルバー3")) this.abbrRank = rankList[9];		//s3
		else if(rank.equals("ゴールド1")) this.abbrRank = rankList[10];		//g1
		else if(rank.equals("ゴールド2")) this.abbrRank = rankList[11];		//g2
		else if(rank.equals("ゴールド3")) this.abbrRank = rankList[12];		//g3
		else if(rank.equals("プラチナ1")) this.abbrRank = rankList[13];		//p1
		else if(rank.equals("プラチナ2")) this.abbrRank = rankList[14];		//p2
		else if(rank.equals("プラチナ3")) this.abbrRank = rankList[15];		//p3
		else if(rank.equals("ダイヤ1")) this.abbrRank = rankList[16];			//d1
		else if(rank.equals("ダイヤ2")) this.abbrRank = rankList[17];			//d2
		else if(rank.equals("ダイヤ3")) this.abbrRank = rankList[18];			//d3
		else if(rank.equals("アセンダント1")) this.abbrRank = rankList[19];	//a1
		else if(rank.equals("アセンダント2")) this.abbrRank = rankList[20];	//a2
		else if(rank.equals("アセンダント3")) this.abbrRank = rankList[21];	//a3
		else if(rank.equals("イモータル1")) this.abbrRank = rankList[22];		//m1
		else if(rank.equals("イモータル2")) this.abbrRank = rankList[23];		//m2
		else if(rank.equals("イモータル3")) this.abbrRank = rankList[24];		//m3
		else if(rank.equals("レディアント")) this.abbrRank = rankList[25];		//r0
		else this.abbrRank = "未登録";
	}

	/**
	 * 引数で与えられた学年を1,2,3,4のいずれかに変換し、フィールドgradeにセットします
	 * @param grade	学年
	 */
	protected void makeGrade(String grade) {
		if(grade.equals("1年") || grade.equals("１年")) this.grade = "1";
		else if(grade.equals("2年") || grade.equals("２年")) this.grade = "2";
		else if(grade.equals("3年") || grade.equals("３年")) this.grade = "3";
		else if(grade.equals("4年") || grade.equals("４年")) this.grade = "4";
		else this.grade = "未登録";
	}

}
