package Insurance_Payment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

	/* 프로그램 실행 메인 함수 */
	public static void main(String[] args) throws Exception {

		// 아래 파일에 테스트 케이스를 저장하고 실행한다.
		System.setIn(new FileInputStream("src/test"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		Payment pay = new Payment();

		int T = Integer.parseInt(br.readLine());

		// 첫줄에 입력한 테스트 케이스만큼 돌면서 수행한다.
		for (int tc = 1; tc <= T; tc++) {

			String input = br.readLine();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(input);
			JSONObject jsonObj = (JSONObject) obj;

			String type = (String) jsonObj.get("type");

			// 명령에 해당하는 메써드를 호출해서 수행한다.
			if ("PAYMENT".equals(type)) {
				pay.doPayment(input);
			} else if ("CANCEL".equals(type)) {
				pay.cancelPayment(input);
			} else if ("INQUIRY".equals(type)) {
				pay.getPaymentInfo(input);
			} else {
				System.out.println("E000 : " + Util.getCodeMsg("E", "000"));
			}

		} // tc

	} // main

} // class