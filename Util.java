package Insurance_Payment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	/* 코드, 메세지가 몇개 안되서 그냥 전역변수로 세팅한다. 이차원 배열로 코드와 번호(3자리)로 구분한다. */
	static String[][] code = {

			{ "잘못 입력되었습니다", // E 000
					"부가가치세는 결제금액보다 클 수 없습니다", // E 001
					"결제금액은 100원 ~ 1,000,000,000원 입니다", // E 002
					"취소금액이 현재잔액보다 큽니다", // E 003
					"취소 부가가치세가 현재 부가가치세보다 큽니다", // E 004
					"결제금액이 전체취소 되었지만 남아있는 부가가치세가 존재합니다" // E 005
			},

			{ "결제가 정상 처리 되었습니다", // S 000
					"자료가 정상 조회 되었습니다", // S 001
					"결제취소가 정상 처리 되었습니다" // S 002
			},

			{ "조회 결과가 없습니다", // I 000
			} };

	/* 테이블에 저장되는 키값을 생성한다. */
	public String getID() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
		Date time = new Date();
		String result = dateFormat.format(time);
		return result;
	}

	/* 부가가치세를 계산한다. 금액을 11로 나누고, 소수점 이하 반올림 한다. */
	public int calcVAT(double input) {
		double output = Math.round((input / 11) * 10 / 10.0);
		return (int) output;
	}

	/* 카드사와 통신하는 인터페이스 전문을 생성한다. */
	public String makeCardData(String type, String id, String origin_id, String card_num, int month_plan, String expire,
			String cvc, int amount, int vat) throws Exception {

		String result = "";
		String temp = "";

		temp = String.format("%-10s", type);
		temp += String.format("%20s", id);
		temp += String.format("%-20s", card_num);
		temp += String.format("%02d", month_plan);
		temp += String.format("%4s", expire);
		temp += String.format("%3s", cvc);
		temp += String.format("%10d", amount);
		temp += String.format("%010d", vat);
		temp += String.format("%20s", origin_id);

		Encryption encry = new Encryption();
		String encry_data = encry.doEncrypt(card_num + "|" + expire + "|" + cvc);
		temp += String.format("%-300s", encry_data);

		temp += String.format("%47s", "");

		int temp_length = temp.length();
		result = String.format("%4d", temp_length);

		result += temp;

		return result;
	}

	/* 코드타입과 번호에 해당하는 메세지를 리턴 코드는 간략하게 전역변수 배열로 설정했다 */
	public static String getCodeMsg(String type_str, String no_str) {

		int type_num = 0;
		int no_num = 0;

		if ("E".equals(type_str)) {					// E 에러
			type_num = 0;
			no_num = Integer.parseInt(no_str);
		} else if ("S".equals(type_str)) {			// S 성공
			type_num = 1;
			no_num = Integer.parseInt(no_str);
		} else if ("I".equals(type_str)) {			// I 정보
			type_num = 2;
			no_num = Integer.parseInt(no_str);
		} else {
			return code[0][0];
		}

		return code[type_num][no_num];
	}

}