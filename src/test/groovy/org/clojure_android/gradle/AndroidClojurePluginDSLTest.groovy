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

    @Test
    void withProductFlavors() {
        project.android {
            productFlavors {
                cherry {}
                lime {}
            }
            sourceSets {
                cherry.clojure.srcDirs = ['src/cherry-clojure']
            }
        }

        Assert.assertEquals(['src/androidTest/clojure'], project.android.sourceSets.androidTest.clojure.directories)
        Assert.assertEquals(['src/androidTestCherry/clojure'], project.android.sourceSets.androidTestCherry.clojure.directories)
        Assert.assertEquals(['src/androidTestLime/clojure'], project.android.sourceSets.androidTestLime.clojure.directories)
        Assert.assertEquals(['src/cherry-clojure'], project.android.sourceSets.cherry.clojure.directories)
        Assert.assertEquals(['src/debug/clojure'], project.android.sourceSets.debug.clojure.directories)
        Assert.assertEquals(['src/lime/clojure'], project.android.sourceSets.lime.clojure.directories)
        Assert.assertEquals(['src/main/clojure'], project.android.sourceSets.main.clojure.directories)
        Assert.assertEquals(['src/release/clojure'], project.android.sourceSets.release.clojure.directories)
        Assert.assertEquals(['src/test/clojure'], project.android.sourceSets.test.clojure.directories)
        Assert.assertEquals(['src/testCherry/clojure'], project.android.sourceSets.testCherry.clojure.directories)
        Assert.assertEquals(['src/testDebug/clojure'], project.android.sourceSets.testDebug.clojure.directories)
        Assert.assertEquals(['src/testLime/clojure'], project.android.sourceSets.testLime.clojure.directories)
        Assert.assertEquals(['src/testRelease/clojure'], project.android.sourceSets.testRelease.clojure.directories)
    }

    @Test
    void withBuildTypes() {
        project.android {
            buildTypes {
                buggy {}
                encrypted {}
            }
            sourceSets {
                testEncrypted.clojure.srcDirs = ['src/clj-test-enc']
            }
        }

        Assert.assertEquals(['src/androidTest/clojure'], project.android.sourceSets.androidTest.clojure.directories)
        Assert.assertEquals(['src/buggy/clojure'], project.android.sourceSets.buggy.clojure.directories)
        Assert.assertEquals(['src/debug/clojure'], project.android.sourceSets.debug.clojure.directories)
        Assert.assertEquals(['src/encrypted/clojure'], project.android.sourceSets.encrypted.clojure.directories)
        Assert.assertEquals(['src/main/clojure'], project.android.sourceSets.main.clojure.directories)
        Assert.assertEquals(['src/release/clojure'], project.android.sourceSets.release.clojure.directories)
        Assert.assertEquals(['src/test/clojure'], project.android.sourceSets.test.clojure.directories)
        Assert.assertEquals(['src/testBuggy/clojure'], project.android.sourceSets.testBuggy.clojure.directories)
        Assert.assertEquals(['src/testDebug/clojure'], project.android.sourceSets.testDebug.clojure.directories)
        Assert.assertEquals(['src/clj-test-enc'], project.android.sourceSets.testEncrypted.clojure.directories)
        Assert.assertEquals(['src/testRelease/clojure'], project.android.sourceSets.testRelease.clojure.directories)
    }
}
