package com.sharry.spi.plugin.core

import org.objectweb.asm.*

class AnnotationFinder(api: Int, cv: ClassVisitor?) : ClassVisitor(api, cv) {

    var annotationEntry: AnnotationEntry? = null
        private set

    override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
        return if (ScanSetting.SERVICE_IMPL_ANNOTATION_NAME == desc) {
            object : AnnotationVisitor(Opcodes.ASM5, super.visitAnnotation(desc, visible)) {
                override fun visit(name: String, value: Any) {
                    val entry = AnnotationEntry()
                    if (ScanSetting.SERVICE_IMPL_ANNOTATION_SINGLETON_FILED_NAME == name) {
                        entry.singleton = value as Boolean
                    }
                    if (ScanSetting.SERVICE_IMPL_ANNOTATION_API_FILED_NAME == name) {
                        entry.serviceApiPath = (value as Type).className.replace(
                            '.',
                            '/'
                        )
                    }
                    if (ScanSetting.SERVICE_IMPL_ANNOTATION_DELAY_FILED_NAME == name) {
                        entry.delay = value as Boolean
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