https://en.wikipedia.org/wiki/Java_APIs_for_Bluetooth  
Plus détaillé :  
http://www.oracle.com/technetwork/articles/javame/index-156193.html  

Selon ces sources Java comporte nativement une API permettant la communication bluetooth.  
![Use case](http://www.oracle.com/ocom/groups/public/@otn/documents/digitalasset/157909.jpg "Use case d'une application bluetooth")  

Selon les sites suivants :
https://developer.android.com/reference/android/bluetooth/BluetoothSocket.html  
https://en.wikipedia.org/wiki/List_of_Bluetooth_protocols  

L'API android supporte trois protocoles (où trois type de socket) Bluetooth :
- SCO : Synchronous Connection-Oriented, Synchrone et bas niveau, utilisé pour la communication vocale (pas de retransmission)
- L2CAP : Transmet des paquets, maintiens un lien logique (connection) entre les appareils. Découpe/recompose les paquets
- RFCOMM : Courrament utilisé, Construit sur L2CAP, s'utilise avec des streams comme TCP.

Dans tout les cas, au niveau API, on utilise des BluetoothServerSocket et BluetoothSocket, celles-ci sont utilisées comme lors d'une connection TCP avec JAVA.
