package ru.lodrean.findyourcourse

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class UploadTask : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun upload() {
        val api = TelegramApi(HttpClient(OkHttp))
        val token = System.getenv("TG_TOKEN")
        val chatId = System.getenv("TG_CHAT_ID")

        runBlocking {
            apkDir
                .get()
                .asFile
                .listFiles()
                .filter {
                    it.name.endsWith(".apk")
                }.forEach { apkFile ->
                    api.uploadFile(apkFile, token, chatId)
                }
        }
    }
}
