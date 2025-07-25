package cn.revoist.lifephoton.module.funga.ai.chat.tools

import dev.langchain4j.agent.tool.P
import dev.langchain4j.agent.tool.Tool
import kotlinx.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

/**
 * @author 6hisea
 * @date  2025/5/26 20:48
 * @description: None
 */
object AINetworkTools {
    @Tool("根据给定关键词，联网在线搜索相关论文的网址")
    fun searchPaper(@P("给定关键词")input: String):String{
        return "没有查到"
    }
    @Tool("阅读在线网页的能力")
    fun readWeb(@P("网址") url: String): String {
        return try {
            // 创建URL对象
            val urlObj = URL(url)

            // 打开连接
            val connection = urlObj.openConnection() as HttpURLConnection

            // 设置请求参数
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000  // 10秒超时
            connection.readTimeout = 10000
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")

            // 检查响应码
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return "无法获取网页内容，HTTP错误码: ${connection.responseCode}"
            }

            // 读取网页内容
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))

            val content = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                content.append(line).append("\n")
            }

            // 关闭资源
            reader.close()
            inputStream.close()
            connection.disconnect()

            // 返回网页内容（可以在这里添加HTML解析逻辑，如使用Jsoup等库提取正文）
            content.toString()
        } catch (e: MalformedURLException) {
            "无效的URL: ${e.message}"
        } catch (e: IOException) {
            "网络错误: ${e.message}"
        } catch (e: Exception) {
            "发生未知错误: ${e.message}"
        }
    }
}