package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.BaseTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore
public class ClientTabServiceTest extends BaseTest {
	@Autowired
	ClientTabService clientTabService;
	@Test
	public void updateStatus() {
		clientTabService.release();
	}
}
