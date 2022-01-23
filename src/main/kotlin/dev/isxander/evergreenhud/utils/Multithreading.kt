/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
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

fun scheduleAsyncAtFixedRate(initialDelay: Long, delayBetweenExecution: Long, unit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit): ScheduledFuture<*> =
    SCHEDULED_POOL.scheduleAtFixedRate(action, initialDelay, delayBetweenExecution, unit)


fun scheduleAsync(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit): ScheduledFuture<*> =
    SCHEDULED_POOL.schedule(action, delay, unit)


fun runAsync(runnable: () -> Unit) =
    POOL.execute(runnable)

fun submit(runnable: Runnable): Future<*> =
    POOL.submit(runnable)
