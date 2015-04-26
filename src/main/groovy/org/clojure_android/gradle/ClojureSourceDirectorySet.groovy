package org.clojure_android.gradle

import org.gradle.api.Project

/**
 * Source directory set for Clojure sources.   Mimics the interface of an Android source directory set.
 */
class ClojureSourceDirectorySet {
    String name
    List<Object> directories
    private Project resolver

    ClojureSourceDirectorySet(String name, Project project) {
        this.name = name
        this.directories = ['src/' + name + '/clojure']
        this.resolver = project
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
}
