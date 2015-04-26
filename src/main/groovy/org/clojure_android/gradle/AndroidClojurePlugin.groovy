package org.clojure_android.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

/**
 * Gradle plug-in that adds support for Clojure in Android projects.
 */
public class AndroidClojurePlugin implements Plugin<Project> {
    private static final List<String> ANDROID_PLUGIN_IDS = [
            'com.android.application',
            'android',
            'com.android.library',
            'android-library'
    ]

    @Override
    void apply(Project project) {
        Plugin<Project> androidPlugin = ANDROID_PLUGIN_IDS.collect { project.plugins.findPlugin(it) } find { it != null }
        if (!androidPlugin) {
            throw new ProjectConfigurationException("Please apply an Android plugin before applying the 'android-clojure' plugin.", null)
        }

        def androidExtension = project.extensions.getByName 'android'
        androidExtension.extensions.add('clojureOptions', new ClojureCompileOptionsExtension())
        androidExtension.sourceSets.each { sourceSet ->
            sourceSet.extensions.add('clojure', new ClojureSourceDirectorySet(sourceSet.name, project) )
        }
    }
}
