package cn.revoist.lifephoton.module.authentication

import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.module.authentication.data.Tools
import cn.revoist.lifephoton.module.authentication.data.entity.UserDataEntity
import cn.revoist.lifephoton.plugin.event.events.AuthenticationEvent
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import net.axay.simplekotlinmail.delivery.send
import net.axay.simplekotlinmail.email.emailBuilder
import net.axay.simplekotlinmail.html.withHTML

/**
 * @author 6hisea
 * @date  2025/3/4 20:28
 * @description: None
 */
suspend fun RoutingCall.isLogin():Boolean{
    val userCookie = sessions.get("user") ?: return false
    val event = AuthenticationEvent(userCookie as UserSession,false).call() as AuthenticationEvent
    return event.truth
}
suspend fun RoutingCall.getUser():UserSession{
    val userCookie = sessions.get("user")
    if (userCookie != null && userCookie is UserSession) {
        return userCookie
    }
    throw RuntimeException("not user")
}
val UserSession.asEntity:UserDataEntity?
    get() = Tools.findUserByToken(accessToken)
suspend fun UserDataEntity.sendEmail(title:String, content:String){
    emailBuilder {
        from("长春市锐沃科技有限公司","no-replay-revoist@qq.com")
        to(email)
        withSubject(title)
        withPlainText(content)
    }.send()
}
suspend fun UserDataEntity.sendEmailNotice(title:String, content:String){
    emailBuilder {
        from("长春市锐沃科技有限公司","no-replay-revoist@qq.com")
        to(email)
        withSubject(title)
        withHTMLText(content)
    }.send()
}

fun analysisTemplate(title: String,username:String,taskId:String,startDate:String,endDate:String,url:String):String{
    return """
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>长春市锐沃科技有限公司-分析结果</title>
    <style type="text/css">
        /* 客户端特定重置 */
        body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }
        table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }
        img { -ms-interpolation-mode: bicubic; border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }
        
        /* 通用样式 */
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            color: #333333;
            line-height: 1.5;
            margin: 0;
            padding: 0;
            background-color: #f7f7f7;
        }
        
        .container {
            width: 100%;
            max-width: 600px;
            margin: 0 auto;
        }
        
        .header {
            background-color: #4a6fa5;
            color: white;
            padding: 30px 20px;
            text-align: center;
        }
        
        .logo {
            max-width: 150px;
            margin-bottom: 15px;
        }
        
        .content {
            background-color: #ffffff;
            padding: 30px 20px;
        }
        
        .button {
            background-color: #4a6fa5;
            color: white !important;
            display: inline-block;
            font-weight: bold;
            padding: 12px 25px;
            text-decoration: none;
            border-radius: 4px;
            margin: 15px 0;
        }
        
        .footer {
            background-color: #f1f1f1;
            color: #999999;
            font-size: 12px;
            padding: 20px;
            text-align: center;
        }
        
        .divider {
            border-top: 1px solid #eaeaea;
            margin: 20px 0;
        }
        
        @media screen and (max-width: 600px) {
            .container {
                width: 100% !important;
            }
            .content, .header, .footer {
                padding-left: 15px !important;
                padding-right: 15px !important;
            }
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">
    <!-- 主容器 -->
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td align="center" valign="top">
                <table class="container" border="0" cellpadding="0" cellspacing="0" width="600">
                    <!-- 页眉 -->
                    <tr>
                        <td class="header">
                            <img src="https://via.placeholder.com/150x50/4a6fa5/ffffff?text=Company+Logo" alt="长春市锐沃科技有限公司" class="logo" />
                            <h1 style="margin: 0; font-size: 24px;">${title}</h1>
                        </td>
                    </tr>
                    
                    <!-- 内容区 -->
                    <tr>
                        <td class="content">
                            <h2 style="margin-top: 0;">尊敬的${username}, 您好!</h2>
                            
                            <p>感谢您使用我们的服务，您使用FUNGA平台的分析结果如下，请仔细阅读以下内容：</p>
                            
                            <div class="divider"></div>
                            
                            <p><strong>任务标识：</strong> ${taskId}</p>
                            <p><strong>开始时间：</strong> ${startDate}</p>
                            <p><strong>完成时间：</strong> ${endDate}</p>
                            
                            <div class="divider"></div>
                            
                            <p>如需查看分析结果，请点击下方按钮前往：</p>
                            
                            <a href="${url}" class="button">查看详情</a>
                            
                            <p>如果您有任何疑问，请随时联系我们的客服团队。</p>
                            
                            <p>祝您使用愉快！</p>
                            
                            <p><strong>长春市锐沃科技有限公司</strong></p>
                        </td>
                    </tr>
                    
                    <!-- 页脚 -->
                    <tr>
                        <td class="footer">
                            <p>© 2024 - 2025 长春市锐沃科技有限公司. 保留所有权利。</p>
                            <p>
                                <a href="#" style="color: #4a6fa5; text-decoration: none;">隐私政策</a> | 
                                <a href="#" style="color: #4a6fa5; text-decoration: none;">服务条款</a> | 
                                <a href="#" style="color: #4a6fa5; text-decoration: none;">联系我们</a>
                            </p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>
"""
}