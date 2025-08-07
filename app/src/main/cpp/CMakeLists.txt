# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

cmake_minimum_required(VERSION 3.22.1)

project("sculptlauncher")

# Creates your game shared library. The name must be the same as the
# one used for loading in your Kotlin/Java or AndroidManifest.txt files.
add_library(sculptlauncher SHARED main.cpp)
add_library(nmod-core SHARED nmod.cpp)

# Searches for a package provided by the game activity dependency
find_package(game-activity REQUIRED CONFIG)

# Configure libraries CMake uses to link your target library.
target_link_libraries(sculptlauncher PUBLIC
        # The game activity
        game-activity::game-activity_static
        android
        log
        nmod-core
)