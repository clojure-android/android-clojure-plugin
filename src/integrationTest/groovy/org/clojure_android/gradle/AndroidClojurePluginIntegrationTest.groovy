package org.clojure_android.gradle

import org.apache.commons.io.FileUtils
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Main class for integration tests that check whether or not the plug-in does anything.
 */
class AndroidClojurePluginIntegrationTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder()
    private File getProjectDir() { tempFolder.root }

    @Before
    void setUp() {
        FileUtils.copyDirectory(new File(this.class.classLoader.getResource('projects/basic').toURI()), projectDir)
        Properties localProperties = new Properties()
        localProperties.setProperty('sdk.dir', System.getProperty('android.sdk.dir'))
        File localPropertiesFile = new File(projectDir, 'local.properties')
        FileWriter out = new FileWriter(localPropertiesFile)
        try {
           localProperties.store(out, 'auto-generated')
            new File(projectDir, 'build.gradle').withWriter { w ->
                new File(projectDir, 'build.gradle.template').eachLine { line ->
                    w << line
                            .replaceAll('@@clojurePluginVersion@@', System.getProperty('clojure.plugin.version'))
                            .replaceAll('@@androidPluginVersion@@', System.getProperty('android.plugin.version'))
                            .replaceAll('@@testMavenRepo@@', System.getProperty('test.maven.repo'))
                            .replaceAll('@@compileSdkVersion@@', System.getProperty('compile.sdk.version'))
                            .replaceAll('@@buildToolsVersion@@', System.getProperty('build.tools.version'))
                    w << '\n'
                }
            }
        } catch(IOException e) {
            Assert.fail(e.localizedMessage)
        } finally {
            out.close()
        }
    }

    @Test
    void minimalApplication() {
        GradleConnector connector = GradleConnector.newConnector().forProjectDirectory(projectDir)
        ProjectConnection connection = connector.connect()
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream()
            ByteArrayOutputStream errStream = new ByteArrayOutputStream()
            connection.newBuild()
                    .forTasks('clean', 'assemble')
                    .setStandardOutput(outStream)
                    .setStandardError(errStream)
                    .run()
            println new String(outStream.toByteArray(), 'UTF-8')
            println new String(errStream.toByteArray(), 'UTF-8')

            Assert.assertTrue(new File(projectDir, 'build/intermediates/classes/debug/org/clojure_android/gradle/basic/MainActivity.class').exists())
            Assert.assertTrue(new File(projectDir, 'build/intermediates/classes/debug/org/clojure_android/gradle/basic/MainActivity__init.class').exists())
            Assert.assertTrue(new File(projectDir, 'build/intermediates/classes/release/org/clojure_android/gradle/basic/MainActivity.class').exists())
            Assert.assertTrue(new File(projectDir, 'build/intermediates/classes/release/org/clojure_android/gradle/basic/MainActivity__init.class').exists())
        } finally {
            connection.close()
        }
    }
}
