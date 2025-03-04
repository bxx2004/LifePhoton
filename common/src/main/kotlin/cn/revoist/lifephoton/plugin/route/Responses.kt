package cn.revoist.lifephoton.plugin.route



/**
 * @author 6hisea
 * @date  2025/1/8 12:54
 * @description: None
 */

interface Response

data class PayloadResponse<T>(
    val status: Boolean,
    val message:String,
    val payload:T? = null
): Response{
    val code = ResponseCode.OK
}

data class ErrorResponse(
    val status: Boolean = false,
    val message:String
) : Response{
    val code = ResponseCode.ERROR
}

data class MessageResponse(
    val status: Boolean,
    val message:String,
) : Response{
    val code = ResponseCode.OK
}


data class PagingPayloadResponse<T>(
    val status: Boolean,
    val message:String,
    val previous:Int,
    val next:Int,
    val allPages:Int,
    val session:String,
    val payload:T? = null
) : Response{
    val code = ResponseCode.PAGE
}