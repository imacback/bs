package cn.aiyuedu.bs.common.util;

import java.util.UUID;

/**
 * Description:
 * 
 * @author yz.wu
 */
public class IdUtil {

	public static String uuid() {
		UUID uuid = UUID.randomUUID();
		long mostSigBits = uuid.getMostSignificantBits();
		long leastSigBits = uuid.getLeastSignificantBits();
		StringBuilder sb = new StringBuilder();
		return sb.append(digits(mostSigBits >> 32, 8))
				.append(digits(mostSigBits >> 16, 4))
				.append(digits(mostSigBits, 4))
				.append(digits(leastSigBits >> 48, 4))
				.append(digits(leastSigBits, 12)).toString();
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	public static void main(String[] args) {
		System.out.println(uuid());
		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}
}
