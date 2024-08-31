package io.github.itsflicker.tools.module.resourcepack

import java.io.File

interface ResourcePackUploader {

    fun upload(file: File): Boolean

}