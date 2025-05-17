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
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:call:api")
include(":feature:call:impl")
include(":feature:main")
include(":data:room")
include(":domain:room")
include(":core:network")
include(":core:ui")
include(":core:common")
include(":core:model")
include(":feature:history:api")
include(":feature:history:impl")
include(":data:history")
include(":domain:history")
include(":data:common")
