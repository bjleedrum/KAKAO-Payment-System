package Insurance_Payment;

/* 결제정보 마스터 객체 클래스 */
public class MasterObject {

	String unique_id;
	int first_amount;
	int first_vat;
	int current_amount;
	int current_vat;
	String creater;
	String create_dt;
	String changer;
	String change_dt;

	public MasterObject(String unique_id, int first_amount, int first_vat, int current_amount,
			int current_vat, String creater, String create_dt, String changer, String change_dt) {
		super();
		this.unique_id = unique_id;
		this.first_amount = first_amount;
		this.first_vat = first_vat;
		this.current_amount = current_amount;
		this.current_vat = current_vat;
		this.creater = creater;
		this.create_dt = create_dt;
		this.changer = changer;
		this.change_dt = change_dt;
	}

	public int getFirst_amount() {
		return first_amount;
	}

	public void setFirst_amount(int first_amount) {
		this.first_amount = first_amount;
	}

	public int getFirst_vat() {
		return first_vat;
	}

	public void setFirst_vat(int first_vat) {
		this.first_vat = first_vat;
	}

	public int getCurrent_amount() {
		return current_amount;
	}

	public void setCurrent_amount(int current_amount) {
		this.current_amount = current_amount;
	}

	public int getCurrent_vat() {
		return current_vat;
	}

	public void setCurrent_vat(int current_vat) {
		this.current_vat = current_vat;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(String create_dt) {
		this.create_dt = create_dt;
	}

	public String getChanger() {
		return changer;
	}

	public void setChanger(String changer) {
		this.changer = changer;
	}

	public String getChange_dt() {
		return change_dt;
	}

	public void setChange_dt(String change_dt) {
		this.change_dt = change_dt;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

}