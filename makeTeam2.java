import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * このクラスは、コンマ区切り、もしくは連続入力で与えられた様々なランクから、適切な2チームに分け、最後にソートして出力するクラスである。
 * 探索回数は初期値として5000回としており、5000回適切なチームが見つからない場合は、条件を緩和(1チーム当たりの目指すべき理想のランク合計値をデクリメント)し、
 * 再度、5000回探索を行う。それでも見つからなければ、再度緩和し、見つかるまで繰り返す。
 * 最後に、条件緩和した回数をスコア差レベルとして同時に出力する。
 * つまり、スコア差レベルが高ければ高いほど、AチームとBチームを比べると、Bチームの方がチームスコアが高くなり、格差が生じることとなる。
 */
public class makeTeam2{

	protected int sum = 0;	//与えられた全員のランクの合計値
	protected int teamPower = 0;	//与えられたランクの合計値の半分の値(1チーム当たりの目指すべき理想のランク合計値)
	protected int e = 0;			//探索失敗した際に、探索緩和するレベル
	protected String makeTeam = "";	//引数で与えられたチームリストからコンマを削除したものをここに格納
	protected final String[] rankList = {"n0","i1","i2","i3","b1","b2","b3","s1","s2","s3","g1","g2","g3","p1","p2","p3","d1","d2","d3","a1","a2","a3","m1","m2","m3","r0"};	//存在するランク(入力規則の定義)
	protected Map<String, Integer> ranks;
	protected List<String> teamList = new ArrayList<>();	//与えられた全員のリスト
	protected List<String> teamA = new ArrayList<>();	//チームAのリスト
	protected List<String> teamB = new ArrayList<>();	//チームBのリスト

	public makeTeam2() {
		ranks = new HashMap<>();
		int power = 0;
		for (String rank: rankList)
			ranks.put(rank, power++);
	}

	/**
		String型で与えられたteamのランクのコンマを取り除き、2文字毎に切り分け、フィールドのrankList
		に一致するものがあるかを判定し、あればフィールドのteamListにaddする。
		見つからなければ、入力規則エラーとして、error()メソッドを呼び出し、プログラムを終了する。
	 */
	protected void makeTeamList(String team){
		String[] teamRanks = team.split(",");
		for (String teamRank: teamRanks) {
			if (ranks.containsKey(teamRank))
				teamList.add(teamRank);
			else
				error();
		}
	}

	/**
		入力規則にエラーがある際に、エラーのある場所を↑で表示するメソッド
	 */
	protected void error(){
		System.out.println("---入力規則にエラーがあります---");
		showTeamRank(teamList);
		System.out.println();
		System.out.println(this.makeTeam);
		for(int n=0; n < teamList.size(); n++){
			System.out.print("  ");
		}
		System.out.println("↑");
		System.exit(0);
	}

	/**
		フィールド:teamListのゲッター
	 */
	protected List<String> getTeamList(){
		return this.teamList;
	}

	/**
		フィールド:teamAのゲッター
	 */
	protected List<String> getTeamA(){
		return this.teamA;
	}

	/**
		フィールド:teamBのゲッター
	 */
	protected List<String> getTeamB(){
		return this.teamB;
	}

	/**
		引き数で与えられたListをランク別にコンマでスプリットし、見やすい形に変更して出力する
	 */
	protected void showTeamRank(List<String> teamList){
		System.out.println(teamList);
	}

	/**
		引数で与えられたteamの前半分のスコアをランクの評価関数に基づいて計算し、その合計値を返す
	 */
	protected int calcHalfPower(List<String> team){
		int sum = 0;
		for(int i = 0; i < team.size()/2; i++){
			sum += ranks.get(team.get(i));
		}
		return sum;
	}

	/**
		引数で与えられたteamのスコアをランクの評価関数に基づいて計算し、その合計値を返す
		評価関数は、ランクが低い順から,0,1,2,3,4・・・25となっている。
	 */
	protected int calcPower(List<String> team){
		int sum = 0;
		for(String rank: team)
			sum += ranks.get(rank);
		return sum;
	}

	/**
	 * 最適なチーム探索を開始する。
	 * 与えられたチームをランダムに並び替え、前半のデータがteamPowerに等しくなれば、
	 * 探索を終了する。
	 * @param teamList	全員のチームリスト
	 */
	protected void makeTeamRandom(List<String> teamList){
		Random rand = new Random();
		int teamAPower = calcHalfPower(teamList);
		int teamBPower = calcPower(teamList) - teamAPower;
		int size = teamList.size()/2;
		double temperature  = 10;
		for (int i = 0; i < calcPower(teamList) && teamAPower == teamBPower; i++) {
			int candidateA = rand.nextInt() % size;
			int candidateB = rand.nextInt() % size  + size;
			int diff = ranks.get(teamList.get(candidateA)) - ranks.get(teamList.get(candidateA));
			if ((teamAPower - teamBPower > 0 && diff > 0) ||
				(teamAPower - teamBPower < 0 && diff < 0) ||
				(rand.nextDouble() < Math.exp(diff/temperature))) {
					String rA = teamList.get(candidateA);
					teamList.set(candidateA, teamList.get(candidateB));
					teamList.set(candidateB, rA);
				}
			temperature -= 0.002;
		}
	}

	/**
	 * フィールド:teamListに保存されているデータを前半と後半で2つのデータに分割し、
	 *	前半をteamA,後半をteamBに格納するメソッド
	 * 	格納後、sortTeamRank()メソッドにて、両チームともランクの低い順からソートを実行
	 */
	protected void makeTeamLast(){
		int half = teamList.size()/2;
		for(int i = 0;i < this.teamList.size(); i++){
			if(i < half) teamA.add(teamList.get(i));
			else teamB.add(teamList.get(i));
		}
		sortTeamRank();
	}


	/**
		両チームのスコアと、スコア差レベルを表示するメソッド
	 */
	protected void showTeamPower(){
		System.out.print("チームAのスコアは" + calcPower(teamA) + "です.");
		showTeamRank(teamA);
		System.out.print("チームBのスコアは" + calcPower(teamB) + "です.");
		showTeamRank(teamB);
		System.out.println("両チームのスコア差レベルは【" + e + "】です");
	}

	/**
	 *	チームAを、ランクを低い順にソートし、フィールドにセットするメソッド
	 */
	protected void sortTeamRank(){
		List<String> strListA = new ArrayList<>();
		List<String> strListB = new ArrayList<>();

		for(int i=0;i < rankList.length;i++){
			for(int n=0;n < teamA.size();n++){
				if(rankList[i].equals(teamA.get(n))) {
				strListA.add(teamA.get(n));
				teamA.remove(n);
				i--;	//同じデータが存在する可能性があるので、iをデクリメントする
				break;
				}
			}
		}

		for(int i=0;i < rankList.length;i++){
			for(int n=0;n < teamB.size();n++){
				if(rankList[i].equals(teamB.get(n))) {
				strListB.add(teamB.get(n));
				teamB.remove(n);
				i--;	//同じデータが存在する可能性があるので、iをデクリメントする
				break;
				}
			}
		}
		teamA = strListA;
		teamB = strListB;
	}

	/**
		入力規則を表示するメソッド
	 */
	protected void printRule(){
		System.out.println("----------入力表----------");
		System.out.println("ランク無し　| n0");
		System.out.println("アイアン　　| i1,i2,i3");
		System.out.println("ブロンズ　　| b1,b2,b3");
		System.out.println("シルバー　　| s1,s2,s3");
		System.out.println("ゴールド　　| g1,g2,g3");
		System.out.println("プラチナ　　| p1,p2,p3");
		System.out.println("ダイヤ　　　| d1,d2,d3");
		System.out.println("アセンダント| a1,a2,a3");
		System.out.println("イモータル　| m1,m2,m3");
		System.out.println("レディアント| r0");
		System.out.println("--------------------------");
	}

	/**
	 * sumのセッターで、同時にteamPowerも計算し、セットする。
	 * @param sum	チーム全体のランク評価値
	 */
	protected void setSum(int sum){
		this.sum = sum;
		this.teamPower = sum/2;
	}

		public static void main(String[] args){
			makeTeam make = new makeTeam();
			//入力規則の生成
			make.printRule();
			String team = "";
			Scanner scan = new Scanner(System.in);
			System.out.println("　　　　　　　　　　　　 *                  *        *");
			System.out.print("ランク入力をしてください:");
			team = scan.next();
			//入力されたランクから、入力規則に則っているかを確認し、チームリストを生成する。
			make.makeTeamList(team);

			//人数制限を超えている場合はエラーを表示
			if(make.teamList.size() < 6 || make.teamList.size() > 10){
			System.out.println("人数が不正です");
			System.exit(0);
			}

			//入力されたランク全部の評価値と、その半分の値を、フィールドsumとteamPowerにセットする。
			make.setSum(make.calcPower(make.getTeamList()));
			//チーム生成を開始する
			make.makeTeamRandom(make.getTeamList());
			//前半と後半で別れたチームを、フィールドのteamAとteamBに分割し、格納後、ソートを行う。
			make.makeTeamLast();
			//生成された両チームの評価値と、スコア差レベルを表示する
			make.showTeamPower();
			//
			scan.close();
		}

}
