package Insurance_Payment;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Encryption {

	/* 암호화 */
	public String doEncrypt(String input) {
		Encoder encode = Base64.getEncoder();
		byte encodeData[] = null;
		try {
			encodeData = encode.encode(input.getBytes());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new String(encodeData);
	}

	/* 복호화 */
	public String doDecrypt(String input) {
		Decoder decode = Base64.getDecoder();
		byte decodeData[] = null;
		try {
			decodeData = decode.decode(input);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new String(decodeData);
	}

}