My KentKart
=================================

Welcome to My KentKart!

My KentKart is an Android application for KentKart. It's purpose is to see information of a KentKart easily. My KentKart requires a device with Android 4.0.3 and newer to work.

Disclaimer
--------------
KentKart is a company developing an automatic fare collection system for public transportation. They operate in Turkey and many other countries.

Website: http://www.kentkart.com

My KentKart, however, is in no way related to the company and is for **personal use only**! Please **DO NOT** use it for any other purpose. I **DO NOT** take any responsibility in this case.

Download
--------------
You can download My KentKart from [**Releases**](https://github.com/mehmetakiftutuncu/MyKentKart/releases) section.

Install
--------------
1. Transfer **.apk** file to your device's storage.
2. Enable **Unknown Sources** in device settings so that you can install applications that are not from Google Play Store.
3. Locate the **.apk** file with a file manager and open it.
4. Go through the installation process.

How to Use
--------------
My KentKart is pretty easy to use. Just follow these steps;

1. Add a new KentKart and give it a name.
2. Enter 11 digit KentKart number.
3. Select region in which your KentKart is active.
4. You'll see information of your KentKart.

Now your KentKart is saved. All you need to do is select your card from the list after saving it once with steps above.

If your device has NFC, things get even cooler. After enabling NFC in device settings follow these steps;

1. Touch your KentKart to your device.
2. It will be recognized and you will need to go through steps above for once.

After saving your KentKart using NFC, you can just touch your KentKart and you will directly see KentKart information. You don't even have to open My KentKart application for this to work.

Here's a demonstration video:
<a href="http://www.youtube.com/watch?feature=player_embedded&v=GN_uFy-u7r4" target="_blank"><img src="http://img.youtube.com/vi/GN_uFy-u7r4/0.jpg" alt="Demonstration video" width="640" height="480" border="10" /></a>

Information My KentKart Provides
--------------
You can see following information using My KentKart:

* Amount of current balance of the card
* Date and time of last use of the card
* Amount of last use of the card
* Date and time of last reload to the card
* Amount of last reload to the card

Where My KentKart Works?
--------------
My KentKart is for Turkey and it works in regions that KentKart is used which are following:

* Adana
* Alanya
* Antakya
* Bandırma
* Burdur
* Çanakkale
* Edirne
* Ereğli
* İnegöl
* İzmir
* Kocaeli
* Manisa
* Mersin
* Muğla
* Niğde
* Sivas
 
Permissions
--------------
My KentKart requires following permissions:

Permission | Details
---------- | -------
[android.permission.INTERNET](http://developer.android.com/reference/android/Manifest.permission.html#INTERNET) | This permission is needed to connect to the internet. My KentKart gets the information by sending a request to KentKart website.
[android.permission.ACCESS_NETWORK_STATE](http://developer.android.com/reference/android/Manifest.permission.html#ACCESS_NETWORK_STATE) | This permission is required to check if the device is connected to the internet.
[android.permission.NFC](http://developer.android.com/reference/android/Manifest.permission.html#NFC) | This permission is required to use NFC on devices that support it.
[android.permission.WRITE_EXTERNAL_STORAGE](http://developer.android.com/reference/android/Manifest.permission.html#WRITE_EXTERNAL_STORAGE) | This permission is required to save your KentKarts to device's storage.

License
--------------
My KentKart is licenses under Apache License Version 2.0.

```
Copyright (C) 2015 Mehmet Akif Tütüncü

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
