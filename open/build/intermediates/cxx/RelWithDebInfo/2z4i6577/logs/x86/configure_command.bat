@echo off
"D:\\android SDK\\AndroidSDK\\cmake\\3.18.1\\bin\\cmake.exe" ^
  "-HD:\\Animaland\\open\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=26" ^
  "-DANDROID_PLATFORM=android-26" ^
  "-DANDROID_ABI=x86" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86" ^
  "-DANDROID_NDK=D:\\android SDK\\AndroidSDK\\ndk\\23.1.7779620" ^
  "-DCMAKE_ANDROID_NDK=D:\\android SDK\\AndroidSDK\\ndk\\23.1.7779620" ^
  "-DCMAKE_TOOLCHAIN_FILE=D:\\android SDK\\AndroidSDK\\ndk\\23.1.7779620\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=D:\\android SDK\\AndroidSDK\\cmake\\3.18.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=D:\\Animaland\\open\\build\\intermediates\\cxx\\RelWithDebInfo\\2z4i6577\\obj\\x86" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=D:\\Animaland\\open\\build\\intermediates\\cxx\\RelWithDebInfo\\2z4i6577\\obj\\x86" ^
  "-DCMAKE_BUILD_TYPE=RelWithDebInfo" ^
  "-BD:\\Animaland\\open\\.cxx\\RelWithDebInfo\\2z4i6577\\x86" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
