package cn.aiyuedu.bs.wap.util;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.*;
import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.wap.dto.RechargeOrder;
import com.duoqu.commons.utils.DigestUtils;

public class SignUtils {

	private static final String DEFAULT_CHARSET = "utf-8";

	public static String sign(String content, RechWay rechWay) {
		if (rechWay.val() == RechWay.zfb.val()) {
			return RSA.sign(content, Constants.RSA_PRIVATE, DEFAULT_CHARSET);
		} else if (rechWay.val() == RechWay.zfk.val()) {
			return DigestUtils.md5ToHex(content).toUpperCase();
		}

		return null;
	}

	public static void main(String[] args) {
		RechargeOrder a = new RechargeOrder();
		a.setNotifyUrl("http://m.psread.cn/recharge/notify.do?uid=" + IdUtil.uuid());
		a.setReturnUrl("http://m.psread.cn/recharge/return.do?uid=" + IdUtil.uuid());
		a.setSubject("test");
		a.setTotalFee(Double.valueOf("30"));

		String sign = RSA.sign(a.getSignString(), Constants.RSA_PRIVATE, "utf-8");
		System.out.println("sign=" + sign);
		System.out.println(RSA.verify(a.getSignString(), sign, Constants.RSA_PUBLIC, "utf-8"));
	}
}
