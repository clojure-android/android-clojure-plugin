package org.clojure_android.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

/**
 * Tests that the extensions to the Android project DSL have effect.
 */
class AndroidClojurePluginDSLTest {
    Project createProject() {
        Project p = ProjectBuilder.builder().build()
        p.pluginManager.apply('android')
        p.pluginManager.apply('android-clojure')
        p
    }
    Project project = createProject()

    @Test
    void defaultClojureOptions() {
        def clojureOptions = project.extensions.getByName('android').clojureOptions
        Assert.assertFalse clojureOptions.warnOnReflection
    }

    @Test
    void overrideClojureOptionsWithEmptyBlock() {
        project.android { clojureOptions {} }
        Assert.assertFalse project.android.clojureOptions.warnOnReflection
    }

    @Test
    void overrideClojureOptions() {
        project.android {
            clojureOptions {
                warnOnReflection = true
            }
        }
        Assert.assertTrue project.android.clojureOptions.warnOnReflection
    }

    @Test
    void defaultClojureSourcePaths() {
        Assert.assertEquals(['src/main/clojure'], project.android.sourceSets.main.clojure.directories)
        Assert.assertEquals(['src/androidTest/clojure'], project.android.sourceSets.androidTest.clojure.directories)
        Assert.assertEquals(['src/debug/clojure'], project.android.sourceSets.debug.clojure.directories)
        Assert.assertEquals(['src/release/clojure'], project.android.sourceSets.release.clojure.directories)
        Assert.assertEquals(['src/test/clojure'], project.android.sourceSets.test.clojure.directories)
        Assert.assertEquals(['src/testDebug/clojure'], project.android.sourceSets.testDebug.clojure.directories)
        Assert.assertEquals(['src/testRelease/clojure'], project.android.sourceSets.testRelease.clojure.directories)
    }

    @Test
    void appendSrcDir() {
        project.android {
            sourceSets {
                main {
                    clojure {
                        srcDir 'clj-main'
                    }
                }

                debug {
                    clojure {
                        srcDir 'clj-debug'
                    }
                }
            }
        }
        Assert.assertEquals(['src/main/clojure', 'clj-main'], project.android.sourceSets.main.clojure.directories)
        Assert.assertEquals(['src/androidTest/clojure'], project.android.sourceSets.androidTest.clojure.directories)
        Assert.assertEquals(['src/debug/clojure', 'clj-debug'], project.android.sourceSets.debug.clojure.directories)
        Assert.assertEquals(['src/release/clojure'], project.android.sourceSets.release.clojure.directories)
        Assert.assertEquals(['src/test/clojure'], project.android.sourceSets.test.clojure.directories)
        Assert.assertEquals(['src/testDebug/clojure'], project.android.sourceSets.testDebug.clojure.directories)
        Assert.assertEquals(['src/testRelease/clojure'], project.android.sourceSets.testRelease.clojure.directories)
    }

    @Test
    void appendSrcDirs() {
        project.android {
            sourceSets {
                main {
                    clojure {
                        srcDirs 'clj-main'
                    }
                }

                debug {
                    clojure {
                        srcDirs 'clj-debug', 'clj-debug2'
                    }
                }
            }
        }
        Assert.assertEquals(['src/main/clojure', 'clj-main'], project.android.sourceSets.main.clojure.directories)
        Assert.assertEquals(['src/androidTest/clojure'], project.android.sourceSets.androidTest.clojure.directories)
        Assert.assertEquals(['src/debug/clojure', 'clj-debug', 'clj-debug2'], project.android.sourceSets.debug.clojure.directories)
        Assert.assertEquals(['src/release/clojure'], project.android.sourceSets.release.clojure.directories)
        Assert.assertEquals(['src/test/clojure'], project.android.sourceSets.test.clojure.directories)
        Assert.assertEquals(['src/testDebug/clojure'], project.android.sourceSets.testDebug.clojure.directories)
        Assert.assertEquals(['src/testRelease/clojure'], project.android.sourceSets.testRelease.clojure.directories)
    }

    @Test
    void replaceSrcDirs() {
        project.android {
            sourceSets {
                main {
                    clojure.srcDirs = ['clj-main']
                }

                debug {
                    clojure {
                        srcDirs = ['clj-debug', 'clj-debug2']
                    }
                }
            }
        }
        Assert.assertEquals(['clj-main'], project.android.sourceSets.main.clojure.directories)
        Assert.assertEquals(['src/androidTest/clojure'], project.android.sourceSets.androidTest.clojure.directories)
        Assert.assertEquals(['clj-debug', 'clj-debug2'], project.android.sourceSets.debug.clojure.directories)
        Assert.assertEquals(['src/release/clojure'], project.android.sourceSets.release.clojure.directories)
        Assert.assertEquals(['src/test/clojure'], project.android.sourceSets.test.clojure.directories)
        Assert.assertEquals(['src/testDebug/clojure'], project.android.sourceSets.testDebug.clojure.directories)
        Assert.assertEquals(['src/testRelease/clojure'], project.android.sourceSets.testRelease.clojure.directories)
    }
}
