package org.clojure_android.gradle

import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

/**
 * Basic test suite for invoking the Clojure/Android plugin.
 */
public class AndroidClojurePluginInvocationTest {
    @Test(expected = PluginApplicationException)
    public void pluginRequiresAndroid() {
        Project project = ProjectBuilder.builder().build()

        project.pluginManager.apply 'org.clojure-android.android-clojure-plugin'
        Assert.fail 'android-clojure plugin should not have applied'
    }

    @Test
    public void pluginWorksWithComAndroidApplication() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.android.application'
        project.pluginManager.apply 'org.clojure-android.android-clojure-plugin'
    }

    @Test
    public void pluginWorksWithAndroid() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'android'
        project.pluginManager.apply 'org.clojure-android.android-clojure-plugin'
    }

    @Test
    public void pluginWorksWithComAndroidLibrary() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.android.library'
        project.pluginManager.apply 'org.clojure-android.android-clojure-plugin'
    }

    @Test
    public void pluginWorksWithAndroid_Library() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'android-library'
        project.pluginManager.apply 'org.clojure-android.android-clojure-plugin'
    }

    @Test
    public void pluginWorksAsAndroidClojure() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'android'
        project.pluginManager.apply 'android-clojure'
    }
}
