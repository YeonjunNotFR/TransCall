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
    ":feature:home",
    ":feature:home:api",
    ":feature:call",
    ":feature:call:api",
    ":feature:history",
    ":feature:history:api",
    ":feature:splash",
    ":feature:splash:api",
    ":feature:auth",
    ":feature:auth:api",
    ":feature:room",
    ":feature:room:api",
)

include(
    ":data:common",
    ":data:room",
    ":data:history",
    ":data:auth",
    ":data:calling",
    ":data:user",
    ":data:conversation"
)

include(
    ":domain:room",
    ":domain:history",
    ":domain:auth",
    ":domain:calling",
    ":domain:user",
    ":domain:conversation"
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
    ":core:datastore",
    ":core:route",
    ":core:event",
    ":core:permission",
    ":core:share"
)