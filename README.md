# kerberos-timer
Fill in connection information in `src/test/java/com/test/KerberosTimerTest.java`.

Provide Kerberos configuration for your domain in `src/test/resources/krb5.conf`.

Run in your favorite IDE or with

    mvn -Dtest=KerberosTimerTest#testSelect test

This will produce two text files containing thread dumps before and after connecting to the database. The second thread dump contains an additional thread like the following

    "Thread-1" #14 daemon prio=5 os_prio=0 tid=0x000000001e41c800 nid=0x3700 runnable [0x000000001f72f000]
       java.lang.Thread.State: RUNNABLE
	    at sun.net.dns.ResolverConfigurationImpl.notifyAddrChange0(Native Method)
	    at sun.net.dns.ResolverConfigurationImpl$AddressChangeListener.run(ResolverConfigurationImpl.java:144)

This thread cannot be stopped and creates a memory leak in web applications during undeploy/redeploy.
