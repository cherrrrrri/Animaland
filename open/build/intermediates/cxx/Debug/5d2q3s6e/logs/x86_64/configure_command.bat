@echo off
"D:\\android SDK\\AndroidSDK\\cmake\\3.18.1\\bin\\cmake.exe" ^
  "-HD:\\Animaland\\open\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=26" ^
  "-DANDROID_PLATFORM=android-26" ^
  "-DANDROID_ABI=x86_64" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86_64" ^
  "-DANDROID_NDK=D:\\android SDK\\AndroidSDK\\ndk\\23.1.7779620" ^
  "-DCMAKE_ANDROID_NDK=D:\\android SDK\\AndroidSDK\\ndk\\23.1.7779620" ^
  "-DCMAKE_TOOLCHAIN_FILE=D:\\android SDK\\AndroidSDK\\ndk\\23.1.7779620\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=D:\\android SDK\\AndroidSDK\\cmake\\3.18.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=D:\\Animaland\\open\\build\\intermediates\\cxx\\Debug\\5d2q3s6e\\obj\\x86_64" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=D:\\Animaland\\open\\build\\intermediates\\cxx\\Debug\\5d2q3s6e\\obj\\x86_64" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BD:\\Animaland\\open\\.cxx\\Debug\\5d2q3s6e\\x86_64" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
