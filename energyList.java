import java.util.ArrayList;

public class energyList {


	String energyID = "2010370206,2010370154,2222222222";
	ArrayList<String> energyListID;

	public energyList() {
		this.energyListID = new ArrayList<>();

		String ID = "";
		int IDSize = 0;

		for(int i = 0; i < this.energyID.length(); i++) {

			if(this.energyID.charAt(i) == ',') {
					if(IDSize != 10) {
						System.out.println("energyIDに登録エラーが発生してます");
					}else {
					this.energyListID.add(ID);
					ID = "";
					IDSize = 0;
					}
			}else {
				IDSize++;
				ID += energyID.charAt(i);
			}
		}
			energyListID.add(ID);
		}

	public ArrayList<String> getEnergyListID() {
		return this.energyListID;
	}

	public void showEnergyList() {
		System.out.println("----------エナジーリスト----------");
		for(int i = 0; i < this.energyListID.size();i++) {
			System.out.println("学籍番号:" + this.energyListID.get(i));
		}
		System.out.println("--------------------------------");
	}

		public static void main(String[] args) {
			energyList eL = new energyList();
			eL.showEnergyList();
		}
	}
