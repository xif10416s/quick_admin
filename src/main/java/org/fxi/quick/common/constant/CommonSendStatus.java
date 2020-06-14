package org.fxi.quick.common.constant;

/**
 * 	系统通告 - 发布状态
 * @Author LeeShaoQing
 *
 */
public interface CommonSendStatus {

	public static final Short UNPUBLISHED_STATUS_0 = 0;	//未发布

	public static final Short PUBLISHED_STATUS_1 = 1;		//已发布

	public static final Short REVOKE_STATUS_2 = 2;			//撤销



	/**流程催办——系统通知消息模板*/
	public static final String TZMB_BPM_CUIBAN = "bpm_cuiban";
	/**标准模板—系统消息通知*/
	public static final String TZMB_SYS_TS_NOTE = "sys_ts_note";
	/**流程超时提醒——系统通知消息模板*/
	public static final String TZMB_BPM_CHAOSHI_TIP = "bpm_chaoshi_tip";
}
