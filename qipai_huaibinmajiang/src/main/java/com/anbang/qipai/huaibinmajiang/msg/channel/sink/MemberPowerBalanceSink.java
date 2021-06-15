package com.anbang.qipai.huaibinmajiang.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MemberPowerBalanceSink {
	String MEMBERPOWERBALANCE = "memberPowerBalance";

	@Input
	SubscribableChannel memberPowerBalance();
}
