package Insurance_Payment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Payment {

	/* H2 DB 접속 정보 */
	static final String DB_DRIVER = "org.h2.Driver";
	static final String DB_CONNECTION = "jdbc:h2:~/test"; // database name
	static final String DB_USER = "sa"; // user id
	static final String DB_PASSWORD = "1234"; // passward

	/* DB 커넥션 */
	public static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}

	/* 결제 */
	public void doPayment(String input) throws SQLException {

		Connection connection = getDBConnection();
		Statement stmt = null;

		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();

			Util util = new Util();
			String id = util.getID();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(input);
			JSONObject jsonObj = (JSONObject) obj;

			// 인풋 Json Format
			String type = jsonObj.get("type") + "";
			String card_num = jsonObj.get("card_num") + "";
			int month_plan = Integer.parseInt(jsonObj.get("month_plan") + "");
			String expire = jsonObj.get("expire") + "";
			String cvc = jsonObj.get("cvc") + "";
			int amount = Integer.parseInt(jsonObj.get("amount") + "");

			int vat = 0;

			// 입력에 vat가 없으면 자동계산, 있으면 그대로 세팅한다. 0입력해도 그대로 세팅한다.
			if (jsonObj.get("vat") == null) {
				double dbAmt = Double.parseDouble(jsonObj.get("amount") + "");
				vat = util.calcVAT(dbAmt);
			} else {
				vat = Integer.parseInt(jsonObj.get("vat") + "");
			}

			// check : 부가가치세는 결제금액보다 클 수 없습니다
			if (amount < vat) {
				System.out.println("Error : " + "E001 " + Util.getCodeMsg("E", "001"));
				return;
			}

			// check : 결제금액은 100원 ~ 1,000,000,000원 입니다
			if (amount < 100 || amount > 1000000000) {
				System.out.println("Error : " + "E002 " + Util.getCodeMsg("E", "002"));
				return;
			}

			// 인터페이스 전문 생성
			String data = util.makeCardData(type, id, "", card_num, month_plan, expire, cvc, amount, vat);

			// Master 정보 인서트
			String sql = String.format("INSERT INTO PAYMENT_MASTER VALUES ('%s', %s, %s, %s, %s, '%s', %s, '%s', %s)",
					id, amount, vat, amount, vat, "RYAN", "CURRENT_TIMESTAMP()", "RYAN", "CURRENT_TIMESTAMP()");

			stmt.executeUpdate(sql);

			// History 정보 인서트
			String sql2 = String.format(
					"INSERT INTO PAYMENT_HISTORY VALUES ('%s', '%s', '%s', %s, %s, '%s', '%s', %s, '%s', %s)", id, type,
					id, amount, vat, data, "RYAN", "CURRENT_TIMESTAMP()", "RYAN", "CURRENT_TIMESTAMP()");

			stmt.executeUpdate(sql2);

			stmt.close();
			connection.commit();

			// 출력도 Json Format
			JSONObject info = new JSONObject();
			info.put("id", id);
			info.put("data", data);

			System.out.println("==================================================================");
			System.out.println("Request");
			System.out.println(input);
			System.out.println("Response : " + "S000 " + Util.getCodeMsg("S", "000"));
			System.out.println(info.toJSONString());
			System.out.println("==================================================================");

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

	}

	/* 결제 취소 */
	public void cancelPayment(String input) throws SQLException {
		Connection connection = getDBConnection();
		Statement stmt = null;

		JSONObject info = new JSONObject();

		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();

			Util util = new Util();
			String id = util.getID();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(input);
			JSONObject jsonObj = (JSONObject) obj;

			String origin_id = jsonObj.get("id") + "";

			// Master 조회
			MasterObject mo = getPaymentMasterInfo(origin_id);

			if (mo != null) {
				String type = jsonObj.get("type") + "";
				int amount = Integer.parseInt(jsonObj.get("amount") + "");

				int vat = 0;

				// 입력에 vat가 없으면 자동계산, 있으면 그대로 세팅한다. 0입력해도 그대로 세팅한다.
				if (jsonObj.get("vat") == null) {
					double dbAmt = Double.parseDouble(jsonObj.get("amount") + "");
					vat = util.calcVAT(dbAmt);
				} else {
					vat = Integer.parseInt(jsonObj.get("vat") + "");
				}

				// 예외처리 : 결제금액을 다 털었을때 자동계산 부가가치세가 잔액 부가가치세가 더 크면 잔액 부가가치세에 딱 맞춘다. 0 0 만들기 위해서
				if ((mo.current_amount - amount) == 0 && jsonObj.get("vat") == null && vat > mo.current_vat) {
					vat = mo.current_vat;
				}

				// 현재 잔액 계산
				int currentAmt = mo.current_amount - amount;
				int currentVat = mo.current_vat - vat;

				// check
				if (mo.current_amount < amount) {
					// check : 취소금액이 현재잔액보다 큽니다
					System.out.println("Error : " + "E003 " + Util.getCodeMsg("E", "003"));
					return;
				} else if (currentAmt >= 0 && mo.current_vat < vat) {
					// check : 취소 부가가치세가 현재 부가가치세보다 큽니다
					System.out.println("Error : " + "E004 " + Util.getCodeMsg("E", "004"));
					return;
				} else if (currentAmt == 0 && currentVat > 0) {
					// check : 결제금액이 전체취소 되었지만 남아있는 부가가치세가 존재합니다
					System.out.println("Error : " + "E005 " + Util.getCodeMsg("E", "005"));
					return;
				}

				// History 조회
				ArrayList<HistoryObject> hoList = getPaymentHistoryInfo(origin_id);

				String card_num = hoList.get(0).card_num;
				String expire = hoList.get(0).expire;
				String cvc = hoList.get(0).cvc;

				// 인터페이스 전문 생성
				String data = util.makeCardData(type, id, origin_id, card_num, 0, expire, cvc, amount, vat);

				// Master 정보는 업데이트 한다. 원장 개념
				String sql2 = String.format(
						"UPDATE PAYMENT_MASTER SET CURRENT_AMOUNT = %s, CURRENT_VAT = %s, CHANGER = '%s', CHANGE_DT = %s WHERE UNIQUE_ID = '%s'",
						currentAmt, currentVat, "MUZI", "CURRENT_TIMESTAMP()", origin_id);

				stmt.executeUpdate(sql2);

				// History 정보는 인서트 한다. 이력 개념. 취소할때마다 한줄씩 생긴다
				String sql3 = String.format(
						"INSERT INTO PAYMENT_HISTORY VALUES ('%s', '%s', '%s', %s, %s, '%s', '%s', %s, '%s', %s)", id,
						type, origin_id, amount, vat, data, "MUZI", "CURRENT_TIMESTAMP()", "MUZI",
						"CURRENT_TIMESTAMP()");

				stmt.executeUpdate(sql3);

				stmt.close();
				connection.commit();

				info.put("id", id);
				info.put("data", data);

				System.out.println("==================================================================");
				System.out.println("Request");
				System.out.println(input);
				System.out.println("Response : " + "S002 " + Util.getCodeMsg("S", "002"));
				System.out.println(info.toJSONString());
				System.out.println("==================================================================");
			}

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/* 조회. 이 안에서 마스터, 히스토리 둘다 조회한다. */
	public void getPaymentInfo(String input) throws ParseException, SQLException {

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(input);
		JSONObject jsonObj = (JSONObject) obj;

		JSONObject info = new JSONObject();
		JSONObject historyInfo = new JSONObject();

		String id = (String) jsonObj.get("id");

		// 마스터 정보를 조회한다
		MasterObject mo = getPaymentMasterInfo(id);

		if (mo == null) {

			// 마스터 정보가 없음
			System.out.println("==================================================================");
			System.out.println("Request : " + id);
			System.out.println("Response : " + "I000 " + Util.getCodeMsg("I", "000"));
			System.out.println("==================================================================");
		} else {

			// 히스토리 정보를 조회한다
			ArrayList<HistoryObject> hoList = getPaymentHistoryInfo(id);

			if (hoList == null) {
				// 히스토리 정보가 없음
				System.out.println("==================================================================");
				System.out.println("Request : " + id);
				System.out.println("Response : " + "I000 " + Util.getCodeMsg("I", "000"));
				System.out.println("==================================================================");
			} else {
				// 정상 조회됨
				info.put("id", id);
				info.put("card_num", hoList.get(0).card_num);
				info.put("expire", hoList.get(0).expire);
				info.put("cvc", hoList.get(0).cvc);

				// 취소형태 표시
				if (mo.current_amount == 0) {
					info.put("cancel_type", "전체취소"); // 현재 잔액이 0 이면 전체 취소 된거다
				} else if (hoList.size() == 1) {
					info.put("cancel_type", "취소없음"); // 히스토리가 한개라는건 결제만 했다는 뜻이다
				} else if (hoList.size() > 1)
					info.put("cancel_type", "부분취소"); // 히스토리가 여러개라는건 부분 취소를 했다는 뜻이다
			}

			info.put("first_amount", mo.first_amount);
			info.put("first_vat", mo.first_vat);
			info.put("current_amount", mo.current_amount);
			info.put("current_vat", mo.current_vat);

			System.out.println("==================================================================");
			System.out.println("Request : " + id);
			System.out.println("Response : " + "S000 " + Util.getCodeMsg("S", "001"));
			System.out.println(info);
			System.out.println("Optional : 결제/취소 이력"); // 옵션정보로 결재 이력을 시간 순서대로 보여준다
			for (int i = 0; i < hoList.size(); i++) {
				historyInfo.put("type", hoList.get(i).type);
				historyInfo.put("change_dt", hoList.get(i).change_dt.substring(0, 19));
				historyInfo.put("changer", hoList.get(i).changer);
				historyInfo.put("amount", hoList.get(i).amount);
				historyInfo.put("vat", hoList.get(i).vat);
				System.out.println(historyInfo);
			}
			System.out.println("==================================================================");
		}
	}

	/* 결제정보 마스터 정보 조회 */
	public MasterObject getPaymentMasterInfo(String id) throws SQLException {

		Connection connection = getDBConnection();
		Statement stmt = null;

		MasterObject mo = null;

		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();

			// 키값으로 조회. 단건임
			String sql = String.format("SELECT * FROM PAYMENT_MASTER WHERE UNIQUE_ID = '%s'", id);
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				// master object
				mo = new MasterObject(rs.getString("unique_id"), Integer.parseInt(rs.getString("first_amount")),
						Integer.parseInt(rs.getString("first_vat")), Integer.parseInt(rs.getString("current_amount")),
						Integer.parseInt(rs.getString("current_vat")), rs.getString("creater"),
						rs.getString("create_dt"), rs.getString("changer"), rs.getString("change_dt"));
			}

			stmt.close();
			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return mo;

	}

	/* 결제정보 히스토리 정보 조회 */
	public ArrayList getPaymentHistoryInfo(String id) throws SQLException {

		Connection connection = getDBConnection();
		Statement stmt = null;

		ArrayList<HistoryObject> hoList = new ArrayList();

		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();

			// 히스토리 정보 시간순서대로 조회. 결재와 취소가 이력으로 쌓인다
			String sql = String.format("SELECT * FROM PAYMENT_HISTORY WHERE UNIQUE_ID = '%s' ORDER BY CHANGE_DT ASC",
					id);
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String data = rs.getString("data");
				String card_data = data.substring(103, 403).trim();
				String month_plan = data.substring(54, 56).trim();

				Encryption encry = new Encryption();
				String decry_data = encry.doDecrypt(card_data);
				String[] decry = decry_data.split("\\|");

				hoList.add(new HistoryObject(rs.getString("history_id"), rs.getString("type"),
						rs.getString("unique_id"), rs.getString("amount"), rs.getString("vat"), rs.getString("data"),
						decry[0], month_plan, decry[1], decry[2], rs.getString("creater"), rs.getString("create_dt"),
						rs.getString("changer"), rs.getString("change_dt")));
			}

			stmt.close();
			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return hoList;

	}

} // class