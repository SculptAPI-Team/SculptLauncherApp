cmake_minimum_required(VERSION 3.31.6)

project("app")

add_library(${CMAKE_PROJECT_NAME} SHARED app.cpp)

add_library(sculpt-api SHARED mod/mod_main.cpp)

find_package(game-activity REQUIRED CONFIG)
find_package(shadowhook REQUIRED CONFIG)

target_link_libraries(${CMAKE_PROJECT_NAME} PUBLIC
        game-activity::game-activity_static
        shadowhook::shadowhook
        sculpt-api
        android log
)
