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
    ":feature:splash:api",
    ":feature:splash:impl",
    ":feature:auth:api",
    ":feature:auth:impl",
)

include(
    ":data:common",
    ":data:room",
    ":data:history",
    ":data:auth",
    ":data:calling",
    ":data:user",
    ":data:conversation",
)

include(
    ":domain:room",
    ":domain:history",
    ":domain:auth",
    ":domain:calling",
    ":domain:user",
    ":domain:conversation",
)

include(
    ":core:network",
    ":core:ui",
    ":core:common",
    ":core:model",
    ":core:database",
    ":core:webRtc",
    ":core:notification",
    ":core:design",
    ":core:stt",
    ":core:datastore",
    ":core:route",
    ":core:event",
)