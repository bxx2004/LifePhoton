package cn.revoist.lifephoton.tools

import java.util.*
import java.util.function.Consumer

/**
 * @author 6hisea
 * @date  2025/1/8 20:08
 * @description: None
 */
fun submit(delay: Int = -1, period: Int = -1, consumer: Consumer<TimerTask>) {
    val timer = Timer()
    if (delay == -1 && period == -1) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                consumer.accept(this)
            }
        }, 0)
        return
    }
    if (delay == -1) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                consumer.accept(this)
            }
        }, 0, period.toLong())
    }
    if (period == -1) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                consumer.accept(this)
            }
        }, delay.toLong())
    }
    if (period != -1 && delay != -1) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                consumer.accept(this)
            }
        }, delay.toLong(), period.toLong())
    }
}