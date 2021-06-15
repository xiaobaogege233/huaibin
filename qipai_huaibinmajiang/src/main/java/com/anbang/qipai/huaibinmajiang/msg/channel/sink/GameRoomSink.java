package com.anbang.qipai.huaibinmajiang.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface GameRoomSink {

	String HONGZHONGMAJIANGMAJIANGGAMEROOM = "hongzhongMajiangGameRoom";

	@Input
	SubscribableChannel hongzhongMajiangGameRoom();
}
