# jutils
Simple and handful utilities missing from the main Java package.

The information below includes changelogs and information about the tools under this package.
Old tools are not well documented and not documented at all because this was only my personal
bunch of tools since then.

Maven Changelog:
> v1.0  Initial release, and it's now public!

# ---------------------------------------------------------- #

SMSModule - simple utility for sending and reading SMS from a GSM modem.

> Installation SMSLib:

> Before using this SMS Utility, necessary libraries and drivers must be downloaded first at
> [SMSLib installation](http://smslib.org/doc/installation/) and must be placed into appropriate location.
> If you're still confused from the installation page of SMSLib, you may use the files in the installer.7z package, it was up-to-date (Sep-23-2014).

	
> RxTX installation is a bit confusing since it was platform-dependent.
> NOTE: You MUST match your architecture. You can't install the i386 version on a 64-bit version of the JDK and vice-versa.
	

> For Windows 32-bit and 64-bit:

>    For a JDK installation:

> * Copy RXTXcomm.jar ---> `<JAVA_HOME>\jre\lib\ext`
> * Copy rxtxSerial.dll ---> `<JAVA_HOME>\jre\bin`
> * Copy rxtxParallel.dll ---> `<JAVA_HOME>\jre\bin`
			

> For Linux 32-bit and 64-bit

>    For a JDK installation on architecture=i386

> * Copy RXTXcomm.jar ---> `<JAVA_HOME>/jre/lib/ext`
> * Copy librxtxSerial.so ---> `<JAVA_HOME>/jre/lib/i386/`
> * Copy librxtxParallel.so ---> `<JAVA_HOME>/jre/lib/i386/`

> NOTE: For a JDK installation on architecture=x86_64, just change the i386 to x86_64 above.

		
> For Mac OSX

>    Copy RXTXcomm.jar and librxtxSereial.jnilib to,
> * `/Library/Java/Extensions` - to make available to all users 
> * `~/Library/Java/Extensions` - to make available to only your user so it is your choice.

> ADDITIONAL INFO:

> RXTXcomm needs to have /var/lock folder in able to save a lock file. This should be done after the steps above.

> - Make a directory “/var/lock”
> - Set new ownership and permission of /var/lock
>    `sudo chgrp uucp /var/lock`
>    `sudo chmod 775 /var/lock`
> - Set all owners of /var/lock to “Read & Write”
		
# ---------------------------------------------------------- #

JavaMail Framework - simple utility for sending Email message.

> This utility supports HTML or Text only format in mail message.

# ---------------------------------------------------------- #

XPersist - utility for handling Eclipselink's `EntityManagerFactory`. 

> This utility reads/updates `persistence.xml` along runtime, and may alter the host for the specified server.

# ---------------------------------------------------------- #

Serial/JSSC - utility for communicating with Serial ports.

> This utility uses Java's native gnu.io package and a third-party library JSSC. 
> I made two different Serial library because I've encountered problem with JSSC on other devices. But performance wise, JSSC is better.
	
# ---------------------------------------------------------- #
