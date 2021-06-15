package com.anbang.qipai.huaibinmajiang.msg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.huaibinmajiang.msg.channel.source.WisecrackSource;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.CommonMO;

@EnableBinding(WisecrackSource.class)
public class WiseCrackMsgServcie {
	@Autowired
	private WisecrackSource wisecrackSource;

	public void wisecrack(String memberId) {
		CommonMO mo = new CommonMO();
		mo.setMsg("wisecrack");
		Map data = new HashMap();
		data.put("memberId", memberId);
		mo.setData(data);
		wisecrackSource.wisecrack().send(MessageBuilder.withPayload(mo).build());
	}
}
