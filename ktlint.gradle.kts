val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:1.4.1")
}

tasks.register<JavaExec>("ktlintCheck") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args("src/**/*.kt", "!src/**/*Test.kt")
}

tasks.register<JavaExec>("applyToIDEAProject") {
    group = "verification"
    description = "Apply to IDEA Project."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args("--android", "--apply-to-idea-project")
}

tasks.named("check") {
    dependsOn(tasks.named("ktlintCheck"))
}

tasks.register<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args("-F", "src/**/*.kt")
}
