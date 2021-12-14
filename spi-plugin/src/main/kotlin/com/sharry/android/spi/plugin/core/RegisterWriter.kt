package com.sharry.android.spi.plugin.core

import com.sharry.android.spi.plugin.base.ClassVisitorFactory
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import com.sharry.android.spi.plugin.util.Logger
import jdk.internal.org.objectweb.asm.Opcodes.ICONST_0
import jdk.internal.org.objectweb.asm.Opcodes.ICONST_1
import org.objectweb.asm.*


class RegisterWriter(api: Int, cv: ClassVisitor?, val extension: ScanSetting) :
    ClassVisitor(api, cv) {

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        var mv = super.visitMethod(access, name, desc, signature, exceptions)
        // generate code into this method
        if (ScanSetting.GENERATE_TO_METHOD_NAME == name) {
            mv = RouteMethodVisitor(Opcodes.ASM5, mv)
        }
        return mv
    }

    internal inner class RouteMethodVisitor(api: Int, mv: MethodVisitor?) : MethodVisitor(api, mv) {

        override fun visitInsn(opcode: Int) {
            // generate code before return
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                extension.injectMap.entries.forEach { entity ->
                    val classPath = entity.key
                    val annotationInfo = entity.value
                    mv.apply {
                        // 填加 serviceClass
                        visitLdcInsn(Type.getType("L${annotationInfo.serviceApiPath};"))
                        // 添加 implClass
                        visitLdcInsn(Type.getType("L$classPath;"))
                        // 添加 delay
                        visitInsn(
                            if (annotationInfo.delay) {
                                ICONST_1
                            } else {
                                ICONST_0
                            }
                        )
                        // singleton
                        visitInsn(
                            if (annotationInfo.singleton) {
                                ICONST_1
                            } else {
                                ICONST_0
                            }
                        )
                        // generate invoke register method into SRouterImpl.loadRouterMap()
                        visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            ScanSetting.GENERATE_TO_CLASS_NAME,
                            ScanSetting.REGISTER_METHOD_NAME,
                            "(Ljava/lang/Class;Ljava/lang/Class;ZZ)V",
                            false
                        )
                        Logger.print("visitIns $classPath")
                    }
                }
            }
            super.visitInsn(opcode)
        }

        override fun visitMaxs(maxStack: Int, maxLocals: Int) {
            super.visitMaxs(maxStack + 4, maxLocals)
        }

    }

    class Factory(private val extension: ScanSetting) : ClassVisitorFactory {

        override fun create(api: Int, cv: ClassVisitor): ClassVisitor {
            return RegisterWriter(api, cv, extension)
        }

    }

}