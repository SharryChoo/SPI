package com.sharry.android.spi.plugin.base

import org.objectweb.asm.ClassVisitor

interface ClassVisitorFactory {

    fun create(api: Int, cv: ClassVisitor): ClassVisitor

}