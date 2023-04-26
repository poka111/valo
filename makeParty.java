import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class makeParty {

	protected makeTeam mt;			//makeTeamのオブジェクト
	protected int partyPowerA = 0;	//partyAのスコア
	protected int partyPowerB = 0;	//partyBのスコア
	protected int partyPowerC = 0;	//partyCのスコア
	protected makeTeam mtA;			//partyAを使用するmakeTeamのオブジェクト
	protected makeTeam mtB;			//partyBを使用するmakeTeamのオブジェクト
	protected makeTeam mtC;			//partyCを使用するmakeTeamのオブジェクト

	protected int nowRemake = 0;	//現在のパーティーリメイク回数
	int makeNumber = 5;	//パーティー毎のパワー誤差を許容する範囲

	protected final String[] rankList = {"n0","i1","i2","i3","b1","b2","b3","s1","s2","s3","g1","g2","g3","p1","p2","p3","d1","d2","d3","a1","a2","a3","m1","m2","m3","r0"};	//存在するランク(入力規則の定義)

	protected String allmemberStr = "";
	protected List<String> allMember = new ArrayList<String>();		//入力された全データ
	protected List<String> adMember = new ArrayList<String>();		//入力された全データから10名or20名or30名に調整されたデータ
	protected List<String> deletedMember = new ArrayList<String>();	//調整によって削除されたメンバー
	protected List<String> partyA = new ArrayList<String>();		//全データから10名スプリットされたpartyAのList
	protected List<String> partyB = new ArrayList<String>();		//全データから10名スプリットされたpartyBのList
	protected List<String> partyC = new ArrayList<String>();		//全データから10名スプリットされたpartyCのList
	protected List<String> makedAllParty = new ArrayList<>();		//生成されたチーム順のarrayList

	protected String importRankStr;	//importされたランク文字列
	protected String partyAStr = ""; //partyAのランク文字列
	protected String partyBStr = ""; //partyBのランク文字列
	protected String partyCStr = ""; //partyCのランク文字列

	protected int partyAPower = -100;		//パーティーAのスコア 初期値-100
	protected int partyBPower = -100;		//パーティーBのスコア 初期値-100
	protected int partyCPower = -100;		//パーティーCのスコア 初期値-100

	/**
	 * デフォルトコンストラクタで、makeTeamのオブジェクトを生成
	 */
	public makeParty() {
		mt = new makeTeam();	//引数無しオブジェクト生成
	}

	/**
	 *　オーバーロードしているコンストラクタで、attendanceから呼び出す専用のコンストラクタ
	 *	こちらのparty生成規則は、ランクの評価関数の値がほぼ同じになるように生成
	 * @param importRankStr　名簿登録されている人全員のランク文字列
	 */
	public makeParty(String importRankStr) {
		mt = new makeTeam();	//引数無しオブジェクト生成
		this.importRankStr = importRankStr;	//フィールドにセット
		this.allmemberStr = importRankStr;	//フィールドにセット
		makePartyList(importRankStr);	//入力されたパーティーをフィールドに格納&エラーチェック

		deleteTooParty(getAllMember());//データの数を調整(初期データ生成)
		makeParties(getAdMember());	//初期パーティー生成

		makePartyLast(getAdMember());	//パーティー分け開始
		makeDone();						//終了+表示処理

	}

	/**
	 * オーバーライドコンストラクタで、party生成規則を変更する
	 * intは、どんな値でも良い
	 * こちらの生成規則は、ソート生成で、ランクが低い順にparty生成が行われる
	 * @param importRankStr	名簿登録されいる人全員のランク文字列
	 * @param choose		値、なんでもよい
	 */
	public makeParty(String importRankStr,int choose) {
		mt = new makeTeam();	//引数無しオブジェクト生成
		this.importRankStr = importRankStr;	//フィールドにセット
		this.allmemberStr = importRankStr;	//フィールドにセット
		makePartyList(importRankStr);	//入力されたパーティーをフィールドに格納&エラーチェック

		deleteTooParty(getAllMember());//データの数を調整(初期データ生成)
		this.sortAdMember();			//フィールドadMemberをソートする
		makeParties(getAdMember(),choose);	//初期パーティー生成

		if (partyA.size() != 0) {
			System.out.println("--------------------A--------------------");
			showAllRank(getPartyA());
			System.out.println("-----------------------------------------");
			for(String str:partyA) {
				this.partyAStr += str;
			}
			mtA = new makeTeam(this.partyAStr);
		}
		if (partyB.size() != 0) {
			System.out.println("--------------------B--------------------");
			showAllRank(getPartyB());
			System.out.println("-----------------------------------------");
			for(String str:partyB) {
				this.partyBStr += str;
			}
			mtB = new makeTeam(this.partyBStr);
		}
		if (partyC.size() != 0) {
			System.out.println("--------------------C--------------------");
			showAllRank(getPartyC());
			System.out.println("-----------------------------------------");
			System.out.println();
			for(String str:partyC) {
				this.partyCStr += str;
			}
			mtC = new makeTeam(this.partyCStr);
		}
		makeDone();						//終了+表示処理

	}


	/**
	 * フィールド mtA mtB mtCのゲッター
	 * @param ABC	AorBorC
	 * @return	mtA or mtB or mtC
	 */
	public makeTeam getMt(String ABC) {
		if(ABC.equals("A")) return this.mtA;
		else if(ABC.equals("B")) return this.mtB;
		else if(ABC.equals("C")) return this.mtC;
		else return null;
	}

	/**
	 * 引数の文字列から、コンマが存在する場合は削除し、入力規則に則っているかを検査し、エラーがなければ、
	 * フィールドallMemberにセット
	 * @param allMemberStr	ランク文字列
	 */
	protected void makePartyList(String allMemberStr) {
		this.allmemberStr = allMemberStr.replaceAll(",","");	//コンマ区切りを削除してフィールドallMemberStrに格納

		String rank = "";
		for(int i = 0;i < this.allmemberStr.length();i++){
			rank += String.valueOf(allMemberStr.charAt(i));
			if(i % 2 == 1){
				if(Arrays.asList(rankList).contains(rank)){
				this.allMember.add(rank);
				rank = "";
			}else{
				this.error();
				}
			}
		}
	}

	/**
	 *　フィールドallMemberを全員表示
	 */
	protected void showImportRank() {
		for(int i = 0; i < allmemberStr.length();i++) System.out.print("--");
		System.out.println();
		System.out.print("[入力ランク一覧(未ソート)]");
		showAllRank(this.allMember);
		for(int i = 0; i < allmemberStr.length();i++) System.out.print("--");
		System.out.println();
	}

	/**
	 * importRankStrのセッター
	 * @param str	ランク文字列
	 */
	protected void setImportRankStr(String str) {
		this.importRankStr = str;
	}

	/**
	 * フィールドimportRankStrのゲッター
	 * @return	ランク文字列
	 */
	protected String getImportRankStr() {
		return this.importRankStr;
	}

	/**
	 * importListをランクが低い順にソートする
	 * @param importList　ランクList
	 */
	protected void showSortImportRank(List<String> importList) {
			List<String> sortImportList = new ArrayList<>();

			for(int i=0;i < rankList.length;i++){
				for(int n=0;n < importList.size();n++){
					if(rankList[i].equals(importList.get(n))) {
						sortImportList.add(importList.get(n));
						importList.remove(n);
					i--;	//同じデータが存在する可能性があるので、iをデクリメントする
					break;
					}
				}
			}

		this.allMember = sortImportList;
		for(int i = 0; i < allmemberStr.length();i++) System.out.print("--");
		System.out.println();
		System.out.print("[入力ランク一覧(ソート済み)]");
		showAllRank(sortImportList);
		for(int i = 0; i < allmemberStr.length();i++) System.out.print("--");
		System.out.println();
	}


	/**
	入力規則にエラーがある際に、エラーのある場所を↑で表示するメソッド
	 */
	protected void error(){
		System.out.println("---入力規則にエラーがあります---");
		showAllRank(this.allMember);
		System.out.println();
		System.out.println(this.allmemberStr);
		for(int n=0; n < allMember.size(); n++){
			System.out.print("  ");
		}
		System.out.println("↑");
		System.exit(0);
	}

	/**
	 * フィールドmakeAllPartyのゲッター
	 * @return	全パーティーのランクlist
	 */
	protected List<String> getMakedAllParty(){
		return this.makedAllParty;
	}

	/**
	 *　フィールドallMemberのゲッター
	 * @return	全員のランクlist
	 */
	protected List<String> getAllMember(){
		return this.allMember;
	}

	/**
	 * フィールドadMemberのゲッター
	 * @return	10or20or30人に調整した人たちのランクリスト
	 */
	protected List<String> getAdMember(){
		return this.adMember;
	}

	/**
	 * フィールドdeletedMemberのゲッター
	 * @return	人数調整をした際に、弾かれた人のランクリスト
	 */
	protected List<String> getDeleteMember(){
		return this.deletedMember;
	}

	/**
	 * フィールドpartyAのゲッター
	 * @return	partyAのランクリスト
	 */
	protected List<String> getPartyA(){
		return this.partyA;
	}

	/**
	 * フィールドpartyBのゲッター
	 * @return	partyBのランクリスト
	 */
	protected List<String> getPartyB(){
		return this.partyB;
	}

	/**
	 * フィールドpartyCのゲッター
	 * @return	partyCのランクリスト
	 */
	protected List<String> getPartyC(){
		return this.partyC;
	}

	/**
	 * フィールドpartyAPower,Bpower,CPowerのゲッター
	 * @param ABC	A or B or C
	 * @return	partyAPower or partyBPower or partyCPower
	 */
	protected int getPartyPower(String ABC){
		if(ABC == "A") return partyAPower;
		else if(ABC == "B") return partyBPower;
		else if(ABC == "C") return partyCPower;
		else error("getPartyPowerの引数が不正です");
		return -100;
	}

	/**
	 * 引数のエラーメッセージを表示し、プログラムを終了する
	 * @param str	エラーメッセージ
	 */
	protected void error(String str) {
		System.out.println(str);
		System.exit(0);
	}

	/**
	 * 引数で与えられたリストをコンマ区切りで表示する
	 * @param teamList ランクリスト
	 */
	protected void showAllRank(List<String> teamList){
		for(int i = 0;i < teamList.size();i++){
			if(i == 0) System.out.print("【");
			System.out.print(teamList.get(i));
			if(i != teamList.size()-1) System.out.print(",");
		}
		System.out.print("】");
		System.out.println();
	}

	/**
	 * フィールドadMemberをランクが低い順にソートする
	 */
	protected void sortAdMember() {
		if(this.adMember.size() != 0) {
			List<String> strListAD = new ArrayList<>();

			for(int i=0;i < rankList.length;i++){
				for(int n=0;n < adMember.size();n++){
					if(rankList[i].equals(adMember.get(n))) {
					strListAD.add(adMember.get(n));
					adMember.remove(n);
					i--;	//同じデータが存在する可能性があるので、iをデクリメントする
					break;
					}
				}
			}
			this.adMember= strListAD;
		}
	}

	/**
	 * partyA,partyB,partyC,deletedMemberを全てランクが低い順にソート
	 */
	protected void sortRank() {
		if(partyA.size() != 0) {
			List<String> strListA = new ArrayList<>();

			for(int i=0;i < rankList.length;i++){
				for(int n=0;n < partyA.size();n++){
					if(rankList[i].equals(partyA.get(n))) {
					strListA.add(partyA.get(n));
					partyA.remove(n);
					i--;	//同じデータが存在する可能性があるので、iをデクリメントする
					break;
					}
				}
			}
			this.partyA = strListA;
		}

		if(partyB.size() != 0) {
			List<String> strListB = new ArrayList<>();

			for(int i=0;i < rankList.length;i++){
				for(int n=0;n < partyB.size();n++){
					if(rankList[i].equals(partyB.get(n))) {
					strListB.add(partyB.get(n));
					partyB.remove(n);
					i--;	//同じデータが存在する可能性があるので、iをデクリメントする
					break;
					}
				}
			}
			this.partyB = strListB;
		}


		if(partyC.size() != 0) {
			List<String> strListC = new ArrayList<>();

			for(int i=0;i < rankList.length;i++){
				for(int n=0;n < partyC.size();n++){
					if(rankList[i].equals(partyC.get(n))) {
					strListC.add(partyC.get(n));
					partyC.remove(n);
					i--;	//同じデータが存在する可能性があるので、iをデクリメントする
					break;
					}
				}
			}
			this.partyC = strListC;
		}

		if(this.deletedMember.size() != 0) {
			List<String> strListDelete = new ArrayList<>();

			for(int i=0;i < rankList.length;i++){
				for(int n=0;n < deletedMember.size();n++){
					if(rankList[i].equals(deletedMember.get(n))) {
						strListDelete.add(deletedMember.get(n));
						deletedMember.remove(n);
					i--;	//同じデータが存在する可能性があるので、iをデクリメントする
					break;
					}
				}
			}
			this.deletedMember = strListDelete;
		}

	}

	/**
	 * チームのランクリストと、そのスコアを表示
	 * @param teamList	チームのランクリスト
	 * @param power		そのランクのスコア
	 */
	protected void showAllRank(List<String> teamList,int power){
		for(int i = 0;i < teamList.size();i++){
			if(i == 0) System.out.print("【");
			System.out.print(teamList.get(i));
			if(i != teamList.size()-1) System.out.print(",");
		}
		System.out.print("】");
		System.out.println("[ "+ power + " ]");
	}

	/**
	 * データが10 or 20 or 30ではない場合、メンバーを一部削除し、最適なデータ数に調整する
	 * なお、削除された人は、フィールドdeletedMemberに格納される
	 * @param allMember	全員のランクデータ
	 */
	protected void deleteTooParty(List<String> allMember) {
		Random rand = new Random();
		int size = allMember.size();
		int deleteNum = 0;	//削除するデータ数

		if(size < 10) error("入力メンバーが10名未満です");
		//10〜19名の時の処理
		else if(size >= 10 && size < 20) {
			deleteNum = size-10;
		}
		//20〜29名の時の処理
		else if(size >= 20 && size < 30) {
			deleteNum = size-20;
		}
		//30名以上の場合
		else {
			deleteNum = size-30;
		}

		//削除(代入)処理
		for(int i = 0;i < deleteNum;i++) {
			int nowAllMemberSize = allMember.size();
			int r = rand.nextInt(nowAllMemberSize);
			this.deletedMember.add(allMember.get(r));
			allMember.remove(r);
		}
		this.adMember = allMember;
	}

	/**
	 * 調整されたデータから、partyを生成
	 * @param adMember	調整されたランクリスト
	 */
	protected void makeParties(List<String> adMember) {
		int memberNum = adMember.size();
		partyA = new ArrayList<>();
		partyB = new ArrayList<>();
		partyC = new ArrayList<>();

		//adMemberをシャッフル
		Collections.shuffle(adMember);

		if(memberNum == 10) {
			this.partyA = adMember;
		}

		else if(memberNum == 20) {
			for(int i = 0; i < 10;i++) {
				partyA.add(adMember.get(i));
			}

			for(int n = 10; n < 20; n++) {
				partyB.add(adMember.get(n));
			}
		}

		else if(memberNum == 30){
			for(int i = 0; i < 10;i++) {
				partyA.add(adMember.get(i));
			}

			for(int n = 10; n < 20; n++) {
				partyB.add(adMember.get(n));
			}

			for(int m = 20; m < 30; m++) {
				partyC.add(adMember.get(m));
			}
		}


	}

	/**
	 * シャッフルせずに、パーティー生成を実行
	 * @param adMember	ソート済みのデータ
	 * @param choose	この値はなんでも良い
	 */
	protected void makeParties(List<String> adMember,int choose) {

		int memberNum = adMember.size();
		partyA = new ArrayList<>();
		partyB = new ArrayList<>();
		partyC = new ArrayList<>();

		if(memberNum == 10) {
			this.partyA = adMember;
		}

		else if(memberNum == 20) {
			for(int i = 0; i < 10;i++) {
				partyA.add(adMember.get(i));
			}

			for(int n = 10; n < 20; n++) {
				partyB.add(adMember.get(n));
			}
		}

		else if(memberNum == 30){
			for(int i = 0; i < 10;i++) {
				partyA.add(adMember.get(i));
			}

			for(int n = 10; n < 20; n++) {
				partyB.add(adMember.get(n));
			}

			for(int m = 20; m < 30; m++) {
				partyC.add(adMember.get(m));
			}
		}


	}


	/**
	 * 全パーティーのパワーを計算して、フィールドに代入
	 */
	protected void calcPartyPower() {
		if(partyA.size() != 0) {
			this.partyAPower = mt.calcPower(partyA);
		}

		if(partyB.size() != 0) {
			this.partyBPower = mt.calcPower(partyB);
		}

		if(partyC.size() != 0) {
			this.partyCPower = mt.calcPower(partyC);
		}
	}

	/**
	 * 実際に探索を実行し、見つかったら終了する
	 * @param adMember
	 */
	protected void makePartyLast(List<String> adMember) {
		calcPartyPower();		//初期生成されてるパーティーパワーをフィールドに格納
		int max = 1000;	//最大探索回数
		int remakeMax = 30;	//最大チーム生成回数
		int now = 0;	//現在の探索回数
		//30名の場合
		if(partyC.size() != 0) {
			 while((Math.abs(getPartyPower("A") - getPartyPower("B")) >= makeNumber) ||
				   (Math.abs(getPartyPower("B") - getPartyPower("C")) >= makeNumber) ||
				   (Math.abs(getPartyPower("A") - getPartyPower("C")) >= makeNumber)){
					makeParties(adMember);
					calcPartyPower();
					now++;
					if(now == max) break;
			}

			}
			//探索成功

		//20名の場合
		else if(partyB.size() != 0) {
			while(Math.abs(getPartyPower("A") - getPartyPower("B")) >= makeNumber){
				makeParties(adMember);
				calcPartyPower();
				now++;
				if(now == max) break;
			}
		}

		else {
			//何も実行しない
		}

		//探索失敗
		if( now == max) {
			now = 0;
			this.nowRemake++;
			//再生成最適探索に失敗
			if(nowRemake == remakeMax) {
				System.out.print("チーム再生成最適探索に失敗したので,条件緩和を実行,");
				makeNumber += 5;
				this.nowRemake = 0;
			}
			//最適探索に失敗
			System.out.println("探索が失敗したので,再度チーム生成及び探索を開始します.");
			deleteTooParty(this.allMember);	//adMember再生成
			makeParties(this.adMember);		//adMemberを10人パーティーに区切る
			makePartyLast(this.adMember);	//再帰
		}
		calcPartyPower();
		sortRank();

		if (partyA.size() != 0) {
			System.out.println("--------------------A--------------------");
			showAllRank(getPartyA(),getPartyPower("A"));
			System.out.println("-----------------------------------------");
			for(String str:partyA) {
				this.partyAStr += str;
			}
			mtA = new makeTeam(this.partyAStr);
		}
		if (partyB.size() != 0) {
			System.out.println("--------------------B--------------------");
			showAllRank(getPartyB(),getPartyPower("B"));
			System.out.println("-----------------------------------------");
			for(String str:partyB) {
				this.partyBStr += str;
			}
			mtB = new makeTeam(this.partyBStr);
		}
		if (partyC.size() != 0) {
			System.out.println("--------------------C--------------------");
			showAllRank(getPartyC(),getPartyPower("C"));
			System.out.println("-----------------------------------------");
			System.out.println();
			for(String str:partyC) {
				this.partyCStr += str;
			}
			mtC = new makeTeam(this.partyCStr);
		}
	}

	/**
	 * partyを出力
	 */
	protected void makeDone() {
		if(mtA != null) {
			System.out.println("--------------------A--------------------");
			mtA.showTeamPower();
			System.out.println("-----------------------------------------");
			System.out.println();

			for(String str:mtA.getTeamA()) {
				this.makedAllParty.add(str);
			}
			for(String str:mtA.getTeamB()) {
				this.makedAllParty.add(str);
			}
		}

		if(mtB != null) {
			System.out.println("--------------------B--------------------");
			mtB.showTeamPower();
			System.out.println("-----------------------------------------");
			System.out.println();

			for(String str:mtB.getTeamA()) {
				this.makedAllParty.add(str);
			}
			for(String str:mtB.getTeamB()) {
				this.makedAllParty.add(str);
			}
		}

		if(mtC != null) {
			System.out.println("--------------------C--------------------");
			mtC.showTeamPower();
			System.out.println("-----------------------------------------");
			System.out.println();

			for(String str:mtC.getTeamA()) {
				this.makedAllParty.add(str);
			}
			for(String str:mtC.getTeamB()) {
				this.makedAllParty.add(str);
			}
		}

		if(this.deletedMember.size() != 0) {
			System.out.println("---------------弾かれたランク--------------");
			showAllRank(this.deletedMember);
			System.out.println("-----------------------------------------");
		}
	}


	/**
	 * このクラス単体で使用する際のmainメソッド
	 * testRankの中に、ランク文字列を入力し、実行すること
	 * @param args なんでも良い
	 */
	public static void main(String[] args) {
		//-------------------------------------------入力----------------------------------------------------------------------
		String testRank = "n0b3b2b2b1p1p1p1d1s3s3s3s2s1s1s1g2g2m2a2";
		//--------------------------------------------------------------------------------------------------------------------

		new makeParty(testRank);		//このクラスのオブジェクトを生成


	}

}
