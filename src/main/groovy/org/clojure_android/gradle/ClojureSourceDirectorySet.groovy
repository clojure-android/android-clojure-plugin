package org.clojure_android.gradle

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.FileResolver

/**
 * Source directory set for Clojure sources.   Mimics the interface of an Android source directory set.
 */
class ClojureSourceDirectorySet {
    String name
    List<Object> directories
    private FileResolver fileResolver

    ClojureSourceDirectorySet(String name, FileResolver fileResolver) {
        this.name = name
        this.directories = ['src/' + name + '/clojure']
        this.fileResolver = fileResolver
    }

    void srcDir(Object dir) {
        directories.add(dir)
    }

    void srcDirs(Object... dirs) {
        directories.addAll(dirs)
    }

    void setSrcDirs(Iterable<Object> dirs) {
        directories.clear()
        dirs.each { directories.add(it) }
    }

    String toString() {
        directories.toString()
    }

    List<String> getNamespaces() {
        classpath.inject([]) { namespaces, srcDir ->
            namespaces + namespacesIn(srcDir)
        }
    }

    FileCollection getClasspath() {
        fileResolver.resolveFiles(directories)
    }

    private List<String> namespacesIn(File srcDir) {
        URI srcDirURI = srcDir.toURI()
        fileResolver.resolveFilesAsTree(srcDir).matching { include '**/*.clj' }.collect { file ->
            srcDirURI.relativize(file.toURI()).toString().replaceAll('\\.clj$', '').replaceAll('/', '.').replaceAll('_','-')
        }
    }
}
