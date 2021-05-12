package Insurance_Payment;

/* 결제정보 히스토리(리스트) 객체 클래스 */
public class HistoryObject {

	String history_id;
	String type;
	String unique_id;
	String amount;
	String vat;
	String data;
	String card_num;
	String month_plan;
	String expire;
	String cvc;
	String creater;
	String create_dt;
	String changer;
	String change_dt;

	public HistoryObject(String history_id, String type, String unique_id, String amount, String vat, String data,
			String card_num, String month_plan, String expire, String cvc, String creater, String create_dt,
			String changer, String change_dt) {
		super();
		this.history_id = history_id;
		this.type = type;
		this.unique_id = unique_id;
		this.amount = amount;
		this.vat = vat;
		this.data = data;
		this.card_num = card_num;
		this.month_plan = month_plan;
		this.expire = expire;
		this.cvc = cvc;
		this.creater = creater;
		this.create_dt = create_dt;
		this.changer = changer;
		this.change_dt = change_dt;
	}

	public String getHistory_id() {
		return history_id;
	}

	public void setHistory_id(String history_id) {
		this.history_id = history_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public String getMonth_plan() {
		return month_plan;
	}

	public void setMonth_plan(String month_plan) {
		this.month_plan = month_plan;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

}