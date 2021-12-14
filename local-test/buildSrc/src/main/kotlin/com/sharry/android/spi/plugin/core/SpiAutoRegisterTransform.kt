package com.sharry.android.spi.plugin.core

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.sharry.android.spi.plugin.base.BaseFileScanTransform
import com.sharry.android.spi.plugin.base.CodeGenerator
import com.sharry.android.spi.plugin.util.Logger
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import org.objectweb.asm.*

/**
 * transform api
 *
 *
 * 1. Scan all classes to find which classes implement the specified interface
 * 2. Generate register code into class file: [ScanSetting.GENERATE_TO_CLASS_FILE_NAME]
 * @author billy.qi email: qiyilike@163.com
 * @since 17/3/21 11:48
 */
internal class SpiAutoRegisterTransform(val project: Project) : BaseFileScanTransform() {

    private var mIsJarFile: Boolean? = null
    private var mInjectClassFile: File? = null
    private var mWriteSetting = ScanSetting()

    /**
     * name of this transform
     * @return
     */
    override fun getName(): String {
        return ScanSetting.PLUGIN_NAME
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * The com.sharry.srouter.plugin will scan all classes in the project
     * @return
     */
    override fun getScopes(): MutableSet<in QualifiedContent.Scope>? {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun onScanJarEntry(file: File, jarFile: JarFile, jarEntry: JarEntry) {
        val entryName = jarEntry.name
        when {
            // 1. 找到代码注入的目标文件
            entryName == ScanSetting.GENERATE_TO_CLASS_FILE_NAME -> {
                mInjectClassFile = file
                mIsJarFile = true
                Logger.print("find contains generate jar file: " + file.absolutePath)
            }
            // 2. 只扫描 .class 文件, 找寻 @ServiceImpl 注解修饰的 class
            entryName.endsWith(".class") -> {
                val inputStream: InputStream = jarFile.getInputStream(jarEntry)
                scanAnnotation(inputStream)
                inputStream.close()
            }
        }
    }

    override fun onScanClass(classFile: File, classFilePath: String) {
        when (classFilePath) {
            // 1. 找到代码注入的目标文件
            ScanSetting.GENERATE_TO_CLASS_FILE_NAME -> {
                mInjectClassFile = classFile
                mIsJarFile = false
                Logger.print("find generate class file: " + classFile.absolutePath)
            }
            // 2. 扫描 @ServiceImpl 注解修饰的 class
            else -> {
                val inputStream = FileInputStream(classFile)
                scanAnnotation(inputStream)
                inputStream.close()
            }
        }
    }

    override fun onProcess() {
        val fileContainsClass = mInjectClassFile ?: return
        val isJarFile = mIsJarFile ?: return
        mWriteSetting.injectMap.entries.forEach { Logger.print("${it.key}, ${it.value}") }
        if (isJarFile) {
            CodeGenerator.insertCodeToJar(
                fileContainsClass,
                ScanSetting.GENERATE_TO_CLASS_FILE_NAME,
                RegisterWriter.Factory(mWriteSetting)
            )
        } else {
            CodeGenerator.insertCodeToClass(
                fileContainsClass,
                RegisterWriter.Factory(mWriteSetting)
            )
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Utils method.
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun scanAnnotation(inputStream: InputStream) {
        val cr = ClassReader(inputStream)
        val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        val finder = AnnotationFinder(Opcodes.ASM5, cw)
        cr.accept(finder, ClassReader.EXPAND_FRAMES)
        // 找到了一个 SPI 标记的注解
        finder.annotationEntry?.let {
            val classPath = cr.className
            mWriteSetting.injectMap[classPath] = it
            Logger.print(
                "find service: api = ${it.serviceApiPath}, impl=${classPath}"
            )
        }
    }

}