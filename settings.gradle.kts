gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "TransCall"
include(":app")

include(
    ":feature:main",
    ":feature:home:api",
    ":feature:home:impl",
    ":feature:call:api",
    ":feature:call:impl",
    ":feature:history:api",
    ":feature:history:impl",
)

include(
    ":data:room",
    ":data:history",
    ":data:common",
)

include(
    ":domain:room",
    ":domain:history",
)

include(
    ":core:network",
    ":core:ui",
    ":core:common",
    ":core:model",
)