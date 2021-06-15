package com.anbang.qipai.huaibinmajiang.msg.channel.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface HuaibinMajiangGameSource {
	@Output
	MessageChannel huaibinMajiangGame();
}
