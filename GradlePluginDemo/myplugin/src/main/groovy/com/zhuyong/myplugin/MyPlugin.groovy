package com.zhuyong.myplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("=================================")
        println("======这是我的自定义Gradle插件======")
        println("=================================")

        //AppExtension对应build.gradle中android{...}
        def android = project.extensions.getByType(AppExtension.class)
        //注册一个Transform
        def classTransform = new MyTransform(project)
        android.registerTransform(classTransform)

        // 通过Extension的方式传递将要被注入的自定义代码
        def extension = project.extensions.create("InjectCodeToClass", InjectCodeExtension)
        project.afterEvaluate {
            classTransform.className = extension.className
            classTransform.packageName = extension.packageName
            classTransform.methodName = extension.methodName
            classTransform.injectCode = extension.injectCode
        }
    }
}
