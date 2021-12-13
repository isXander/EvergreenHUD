/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

private val counter = AtomicInteger(0)
val SCHEDULED_POOL = ScheduledThreadPoolExecutor(20) { r: Runnable? ->
    Thread(r, "Scheduled EvergreenHUD Thread ${counter.incrementAndGet()}")
}
val POOL = ThreadPoolExecutor(20, 20, 0L, TimeUnit.SECONDS, LinkedBlockingQueue()) { r: Runnable? ->
    Thread(r, "EvergreenHUD Thread ${counter.incrementAndGet()}")
}

fun scheduleAsyncAtFixedRate(runnable: Runnable, initialDelay: Long, delayBetweenExecution: Long, unit: TimeUnit): ScheduledFuture<*> =
    SCHEDULED_POOL.scheduleAtFixedRate(runnable, initialDelay, delayBetweenExecution, unit)


fun scheduleAsync(runnable: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*> =
    SCHEDULED_POOL.schedule(runnable, delay, unit)


fun runAsync(runnable: () -> Unit) =
    POOL.execute(runnable)

fun submit(runnable: Runnable): Future<*> =
    POOL.submit(runnable)
