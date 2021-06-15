package com.anbang.qipai.huaibinmajiang.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.huaibinmajiang.msg.channel.source.HuaibinMajiangResultSource;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.CommonMO;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.MajiangHistoricalPanResult;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.MajiangHistoricalJuResult;

@EnableBinding(HuaibinMajiangResultSource.class)
public class HuaibinMajiangResultMsgService {

	@Autowired
	private HuaibinMajiangResultSource huaibinMajiangResultSource;

	public void recordJuResult(MajiangHistoricalJuResult juResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("shanxiMajiang ju result");
		mo.setData(juResult);
		huaibinMajiangResultSource.huaibinMajiangResult().send(MessageBuilder.withPayload(mo).build());
	}

	public void recordPanResult(MajiangHistoricalPanResult panResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("shanxiMajiang pan result");
		mo.setData(panResult);
		huaibinMajiangResultSource.huaibinMajiangResult().send(MessageBuilder.withPayload(mo).build());
	}
}
