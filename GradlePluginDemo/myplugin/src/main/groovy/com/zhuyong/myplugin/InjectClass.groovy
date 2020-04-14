package com.zhuyong.myplugin

import javassist.*
import org.gradle.api.Project

class InjectClass {

    //初始化类池,以单例模式获取
    private final static ClassPool pool = ClassPool.getDefault()

    static void inject(String path, Project project, String className,
                       String packageName, String methodName, String injectCode)
            throws NotFoundException, CannotCompileException {
        println("filePath: " + path)
        //将当前路径加入类池,不然找不到这个类
        pool.appendClassPath(path)
        //为了能找到android相关的所有类，添加project.android.bootClasspath 加入android.jar，
        pool.appendClassPath(project.android.bootClasspath[0].toString())

        File dir = new File(path)
        //判断如果是文件夹，则遍历文件夹
        if (dir.isDirectory()) {
            //开始遍历
            dir.eachFileRecurse { File file ->
                if (file.getName().equals(className + ".class")) {
                    //获取到要修改的class文件
                    CtClass ctClass = pool.getCtClass(packageName + "." + className)
                    if (null != ctClass) {
                        println "正在操作的路径 = " + file.getAbsolutePath()
                        //判断一个类是否已被冻结,如果被冻结，则进行解冻,使其可以被修改
                        if (ctClass.isFrozen()) ctClass.defrost()
                        //获取到方法
                        CtMethod ctMethod = ctClass.getDeclaredMethod(methodName)
                        println "要插入的代码 = " + injectCode

                        ctMethod.insertBefore(injectCode)//在方法开始注入代码
//                        ctMethod.insertAfter(injectCode)//在方法结尾注入代码
//                        ctMethod.insertAt(18, injectCode)//在class文件的某一行插入代码，前提是class包含行号信息

                        ctClass.writeFile(path)//根据CtClass生成.class文件；
                        /**
                         * 将该class从ClassPool中删除
                         *
                         * ClassPool 会在内存中维护所有被它创建过的 CtClass，当 CtClass 数量过多时，会占用大量的内存，
                         * API中给出的解决方案是 有意识的调用CtClass的detach()方法以释放内存。
                         */
                        ctClass.detach()
                    }
                }
            }
        }
    }
}