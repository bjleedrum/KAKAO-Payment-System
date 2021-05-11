package Insurance_Payment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

	/* ���α׷� ���� ���� �Լ� */
	public static void main(String[] args) throws Exception {

		// �Ʒ� ���Ͽ� �׽�Ʈ ���̽��� �����ϰ� �����Ѵ�.
		System.setIn(new FileInputStream("src/test"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		Payment pay = new Payment();

		int T = Integer.parseInt(br.readLine());

		// ù�ٿ� �Է��� �׽�Ʈ ���̽���ŭ ���鼭 �����Ѵ�.
		for (int tc = 1; tc <= T; tc++) {

			String input = br.readLine();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(input);
			JSONObject jsonObj = (JSONObject) obj;

			String type = (String) jsonObj.get("type");

			// ��ɿ� �ش��ϴ� �޽�带 ȣ���ؼ� �����Ѵ�.
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