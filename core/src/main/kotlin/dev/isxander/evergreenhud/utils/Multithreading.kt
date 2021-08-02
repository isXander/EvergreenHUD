/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.utils

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object Multithreading {

    private val counter = AtomicInteger(0)
    private val SCHEDULED_POOL = ScheduledThreadPoolExecutor(20) { r: Runnable? ->
        Thread(r, "Scheduled EvergreenHUD Thread ${counter.incrementAndGet()}")
    }
    private val POOL = ThreadPoolExecutor(20, 20, 0L, TimeUnit.SECONDS, LinkedBlockingQueue()) { r: Runnable? ->
        Thread(r, "EvergreenHUD Thread ${counter.incrementAndGet()}")
    }

    fun scheduleAsyncAtFixedRate(runnable: Runnable, initialDelay: Long, delayBetweenExecution: Long, unit: TimeUnit): ScheduledFuture<*> =
        SCHEDULED_POOL.scheduleAtFixedRate(runnable, initialDelay, delayBetweenExecution, unit)


    fun scheduleAsync(runnable: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*> =
        SCHEDULED_POOL.schedule(runnable, delay, unit)


    fun runAsync(runnable: Runnable) =
        POOL.execute(runnable)

    fun submit(runnable: Runnable): Future<*> =
        POOL.submit(runnable)

}