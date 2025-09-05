cmake_minimum_required(VERSION 3.31.6)

# 测试时使用
set(CMAKE_BUILD_TYPE Debug)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -O0 -g")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O0 -g")

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
