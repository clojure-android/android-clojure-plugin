package org.clojure_android.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Created by dan on 4/27/15.
 */
class ClojureCompileTask extends DefaultTask {
    @Input
    FileCollection classpath

    @Input
    List<String> namespaces

    @OutputDirectory
    File destinationDir

    @TaskAction
    void compile() {
        ClojureCompileOptionsExtension compileOptions = project.android.clojureOptions
        project.javaexec {
            main = 'clojure.lang.Compile'
            systemProperties = [
                    'clojure.compile.path': destinationDir,
                    'clojure.compile.warn-on-reflection': compileOptions.warnOnReflection
            ]
            args = namespaces
            classpath = this.classpath
        }
    }
}
