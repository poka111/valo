import java.util.ArrayList;

/**
 * このクラスは、交流会に無断欠席した人をblackListに保存するクラスである
 * 登録者はフィールドblackListStrに"氏名,学籍番号,氏名,学籍番号,‥"
 * @author poka
 *
 */
public class blackList {

	//---------------blackList本体-------------------------------
	String blackListStr = "矢野遼太郎,2011550132";
	//-----------------------------------------------------------
	ArrayList<String> blackListName;	//blackList登録者の名前のarrayList
	ArrayList<String> blackListID;		//blackList登録者の学籍番号のarryaList

	/**
	 * コンストラクタで、blackList本体をarrayListに登録する
	 */
	public blackList() {
		this.blackListName = new ArrayList<>();
		this.blackListID = new ArrayList<>();

		int judge = 0;
		String str = "";

		for(int i = 0; i < blackListStr.length(); i++) {

			if(this.blackListStr.charAt(i) == ',') {
				if(judge == 0) {	//名前登録

					blackListName.add(str);
					judge++;
					str = "";

				}else if(judge == 1) {	//学籍番号登録

					blackListID.add(str);
					judge--;
					str = "";
				}
			}else {
				str += blackListStr.charAt(i);
			}
		}
			blackListID.add(str);
		}

	/**
	 * フィールドblackListNameのゲッター
	 * @return	登録者の名前
	 */
	public ArrayList<String> getBlackListName() {
		return this.blackListName;
	}

	/**
	 * フィールドblackListIDのゲッター
	 * @return	登録者の学籍番号
	 */
	public ArrayList<String> getBlackListID(){
		return this.blackListID;
	}

	/**
	 * 登録者のデータを表示
	 */
	public void showBlackList() {
		System.out.println("----------ブラックリスト----------");
		for(int i = 0; i < blackListName.size();i++) {
			System.out.println("名前:" + blackListName.get(i) + ",学籍番号:" + blackListID.get(i));
		}
		System.out.println("--------------------------------");
	}

	/**
	 * テスト用メイン
	 * @param args
	 */
		public static void main(String[] args) {
			blackList bl = new blackList();
			bl.showBlackList();
		}
	}
