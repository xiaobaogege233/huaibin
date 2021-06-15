package com.anbang.qipai.huaibinmajiang.web.vo;

import lombok.Data;

/**
 * 一般的view obj
 * 
 * @author neo
 *
 */
@Data
public class CommonVO {

	private boolean success = true;

	private String msg;

	private Object data;


}
