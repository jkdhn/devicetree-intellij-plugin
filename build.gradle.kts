import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

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
    val generatePreLexer by registering(GenerateLexerTask::class) {
        source.set("src/main/grammars/PreLexer.flex")
        targetDir.set("src/main/gen/me/jkdhn/devicetree/lexer")
        targetClass.set("_PreLexer")
        purgeOldFiles.set(true)
    }

    generateLexer {
        source.set("src/main/grammars/DtsLexer.flex")
        targetDir.set("src/main/gen/me/jkdhn/devicetree/lexer")
        targetClass.set("_DtsLexer")
        purgeOldFiles.set(true)
    }

    fun GenerateParserTask.setup() {
        source.set("src/main/grammars/DtsParser.bnf")
        targetRoot.set("src/main/gen")
        pathToParser.set("me/jkdhn/devicetree/parser/DtsParser.java")
        pathToPsiRoot.set("me/jkdhn/devicetree/psi")
        purgeOldFiles.set(true)
    }

    register("generateInitialParser", GenerateParserTask::class) {
        setup()
        // generateParser requires compileKotlin, but compileKotlin requires the generated parser
        // bootstrap:
        //   * generateInitialParser (doesn't need compileKotlin)
        //   * generateLexer
        //   * generatePreLexer
        //   * generateParser (includes compileKotlin)
    }

    generateParser {
        setup()
        classpath.from(compileKotlin)
    }

    runIde {
        maxHeapSize = "4G"
    }

    patchPluginXml {
        untilBuild.set("221.*")
    }

    buildSearchableOptions {
        enabled = false
    }
}

sourceSets {
    main {
        java {
            srcDir("src/main/gen")
        }
    }
}
