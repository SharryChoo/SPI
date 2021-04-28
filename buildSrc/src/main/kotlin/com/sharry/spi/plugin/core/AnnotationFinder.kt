package com.sharry.spi.plugin.core

import com.sharry.spi.plugin.util.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class AnnotationFinder(api: Int, cv: ClassVisitor?) : ClassVisitor(api, cv) {

    var annotationEntry: AnnotationEntry? = null
        private set

    override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
        return if (ScanSetting.SERVICE_IMPL_ANNOTATION_DESC == desc) {
            object : AnnotationVisitor(Opcodes.ASM5, super.visitAnnotation(desc, visible)) {
                override fun visit(name: String, value: Any) {
                    val entry = AnnotationEntry()
                    when (name) {
                        ScanSetting.SERVICE_IMPL_ANNOTATION_SINGLETON_FILED_NAME -> {
                            entry.singleton = (value as? Boolean) ?: true
                        }
                        ScanSetting.SERVICE_IMPL_ANNOTATION_API_FILED_NAME -> {
                            entry.serviceApiPath = (value as? Type)?.className ?:""
                        }
                        ScanSetting.SERVICE_IMPL_ANNOTATION_DELAY_FILED_NAME -> {
                            entry.delay = value as? Boolean ?: true
                        }
                    }
                    annotationEntry = entry
                    super.visit(name, value)
                }

            }
        } else {
            super.visitAnnotation(desc, visible)
        }
    }

}