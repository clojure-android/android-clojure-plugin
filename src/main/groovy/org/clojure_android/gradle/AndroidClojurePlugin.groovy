package org.clojure_android.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.Task
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.plugins.ExtensionContainer

import javax.inject.Inject

/**
 * Gradle plug-in that adds support for Clojure in Android projects.
 */
public class AndroidClojurePlugin implements Plugin<Project> {
    private static final List<String> ANDROID_PLUGIN_IDS = [
            'com.android.application',
            'com.android.library'
    ]

    private final FileResolver fileResolver

    @Inject
    public AndroidClojurePlugin(FileResolver fileResolver) {
        this.fileResolver = fileResolver
    }

    @Override
    void apply(Project project) {
        project.logger.debug('Applying Clojure plugin…')

        Plugin<Project> androidPlugin = ANDROID_PLUGIN_IDS.collect { project.plugins.findPlugin(it) } find { it != null }
        if (!androidPlugin) {
            throw new ProjectConfigurationException("Please apply an Android plugin before applying the 'android-clojure' plugin.", null)
        }

        def androidExtension = project.extensions.getByName 'android'
        // add Clojure compilation options to the main Android DSL
        androidExtension.extensions.add('clojureOptions', new ClojureCompileOptionsExtension())

        // function used to add Clojure source directories to any source sets
        def extendSourceSets = {
            androidExtension.sourceSets.each {
                String name = it.name
                ExtensionContainer extensions = it.extensions
                if (extensions.findByName('clojure') == null) {
                    project.logger.debug("Adding clojure extension to source set ${name}")
                    it.extensions.add('clojure', new ClojureSourceDirectorySet(it.name, fileResolver))
                }
            }
        }
        // do all of the default source sets
        extendSourceSets()
        // when a new build type or product flavour is added, add the corresponding Clojure sources
        androidExtension.buildTypes.whenObjectAdded(extendSourceSets)
        androidExtension.productFlavors.whenObjectAdded(extendSourceSets)

        // Android plugin builds things up lazily, so we need to do our final work at the very end
        project.afterEvaluate {
            // last chance to add Clojure sources
            extendSourceSets()

            boolean isLibrary = androidPlugin.class.name.endsWith('LibraryPlugin')
            def variants = androidExtension.testVariants +
                    (isLibrary ? androidExtension.libraryVariants : androidExtension.applicationVariants)
            variants.each { variant ->
                project.logger.debug("Creating Clojure compile task for varinat ${variant.name}")
                String variantName = variant.name.capitalize()
                Task javaCompileTask = variant.javaCompile
                ClojureCompileTask clojureCompileTask = project.tasks.create("compile${variantName}Clojure", ClojureCompileTask)
//                clojureCompileTask.classpath = []
                clojureCompileTask.destinationDir = javaCompileTask.destinationDir
                clojureCompileTask.dependsOn(javaCompileTask)
                def sourceSets = variant.variantData.variantConfiguration.sortedSourceProviders
                clojureCompileTask.namespaces = sourceSets.inject([]) { namespaces, sourceSet ->
                    namespaces + sourceSet.clojure.namespaces
                }
                clojureCompileTask.classpath = sourceSets.inject(javaCompileTask.classpath) { fileCollection, sourceSet ->
                    fileCollection + sourceSet.clojure.classpath
                } + project.files(androidExtension.bootClasspath)

                project.tasks.findByName("compile${variantName}Sources").dependsOn clojureCompileTask
            }
        }
    }
}
