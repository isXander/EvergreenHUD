package dev.isxander.evergreenhud.utils

import com.uchuhimo.konf.*
import com.uchuhimo.konf.source.Writer
import java.io.File

fun <T> Config.setOrAdd(name: String, value: T) {
    try {
        this[name] = value
    } catch (ignored: NoSuchItemException) {
        this.addItem(OptionalItem(Spec.dummy, name, value))
    }
}
fun <T> Config.getOrAdd(name: String, value: T): T {
    return try {
        this[name]
    } catch (ignored: NoSuchItemException) {
        this.addItem(OptionalItem(Spec.dummy, name, value))
        this[name]
    }
}
fun Writer.toFileMkdirs(file: File) {
    file.parentFile.mkdirs()
    this.toFile(file)
}