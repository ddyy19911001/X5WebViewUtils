X5WebViewUtils

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

     allprojects {
	    repositories {
	  	...
	  	maven { url 'https://jitpack.io' }
	   }
    }
Step 2. Add the dependency

      dependencies {
        implementation 'com.github.ddyy19911001:X5WebViewUtils:1.0.2'
      }
Step 3. init x5
  in Application,init the X5
    
      X5WebView.init();
      
      at Activity or Fragment
       use X5.onresume、X5.onpause、X5.ondestroy
 
Step 4.Add .so and ndk
   add .so jniLibs
   
      ndk {
            abiFilters "armeabi", "armeabi-v7a", "x86", "mips"
        }
