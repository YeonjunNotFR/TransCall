package com.youhajun.feature.call.model

enum class CallingPipAction(val request: Int, val type: String) {
    MIC_ENABLE(1, "extra_mic_enable"),
    MIC_DISABLE(2, "extra_mic_disable"),
    CAMERA_ENABLE(3, "extra_camera_enable"),
    CAMERA_DISABLE(4, "extra_camera_disable"),
    LEAVE_CALL(5, "extra_leave_call");

    companion object {
        const val BROADCAST = "action_calling_pip_broadcast"
        const val CONTROL_TYPE = "call_control_type"

        fun fromType(type: String?): CallingPipAction {
            return entries.firstOrNull { it.type == type } ?: LEAVE_CALL
        }
    }
}