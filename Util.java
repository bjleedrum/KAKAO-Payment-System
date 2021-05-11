package Insurance_Payment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	/* �ڵ�, �޼����� � �ȵǼ� �׳� ���������� �����Ѵ�. ������ �迭�� �ڵ�� ��ȣ(3�ڸ�)�� �����Ѵ�. */
	static String[][] code = {

			{ "�߸� �ԷµǾ����ϴ�", // E 000
					"�ΰ���ġ���� �����ݾ׺��� Ŭ �� �����ϴ�", // E 001
					"�����ݾ��� 100�� ~ 1,000,000,000�� �Դϴ�", // E 002
					"��ұݾ��� �����ܾ׺��� Ů�ϴ�", // E 003
					"��� �ΰ���ġ���� ���� �ΰ���ġ������ Ů�ϴ�", // E 004
					"�����ݾ��� ��ü��� �Ǿ����� �����ִ� �ΰ���ġ���� �����մϴ�" // E 005
			},

			{ "������ ���� ó�� �Ǿ����ϴ�", // S 000
					"�ڷᰡ ���� ��ȸ �Ǿ����ϴ�", // S 001
					"������Ұ� ���� ó�� �Ǿ����ϴ�" // S 002
			},

			{ "��ȸ ����� �����ϴ�", // I 000
			} };

	/* ���̺� ����Ǵ� Ű���� �����Ѵ�. */
	public String getID() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
		Date time = new Date();
		String result = dateFormat.format(time);
		return result;
	}

	/* �ΰ���ġ���� ����Ѵ�. �ݾ��� 11�� ������, �Ҽ��� ���� �ݿø� �Ѵ�. */
	public int calcVAT(double input) {
		double output = Math.round((input / 11) * 10 / 10.0);
		return (int) output;
	}

	/* ī���� ����ϴ� �������̽� ������ �����Ѵ�. */
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

	/* �ڵ�Ÿ�԰� ��ȣ�� �ش��ϴ� �޼����� ���� �ڵ�� �����ϰ� �������� �迭�� �����ߴ� */
	public static String getCodeMsg(String type_str, String no_str) {

		int type_num = 0;
		int no_num = 0;

		if ("E".equals(type_str)) {					// E ����
			type_num = 0;
			no_num = Integer.parseInt(no_str);
		} else if ("S".equals(type_str)) {			// S ����
			type_num = 1;
			no_num = Integer.parseInt(no_str);
		} else if ("I".equals(type_str)) {			// I ����
			type_num = 2;
			no_num = Integer.parseInt(no_str);
		} else {
			return code[0][0];
		}

		return code[type_num][no_num];
	}

}