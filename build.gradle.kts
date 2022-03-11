import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "1.4.0"
    id("org.jetbrains.grammarkit") version "2021.2.1"
    kotlin("jvm") version "1.6.10"
}

group = "me.jkdhn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

intellij {
    version.set("2021.3.1")
    type.set("CL")
}

tasks {
    generateLexer {
        source.set("src/main/grammars/DtsLexer.flex")
        targetDir.set("src/main/gen/me/jkdhn/devicetree/lexer")
        targetClass.set("DtsLexer")
        purgeOldFiles.set(true)
    }

    generateParser {
        source.set("src/main/grammars/DtsParser.bnf")
        targetRoot.set("src/main/gen")
        pathToParser.set("me/jkdhn/devicetree/parser/DtsParser.java")
        pathToPsiRoot.set("me/jkdhn/devicetree/psi")
        purgeOldFiles.set(true)
    }

    withType<KotlinCompile> {
        dependsOn(generateLexer, generateParser)
    }

    runIde {
        maxHeapSize = "4G"
    }

    patchPluginXml {
        untilBuild.set("221.*")
    }
}

sourceSets {
    main {
        java {
            srcDir("src/main/gen")
        }
    }
}
