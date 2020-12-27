# CRD_Datastore



Features of Datastore:-
Key - Value Pair DataStore : Used hashMap to implelment this... Best Datastructure for these types of usecases
Thread-Safe
Take Less Memory
Support any os with working Java Compiler




# This datastore supports the following operations :-

It can be initialized using an optional file path. 

If one is not provided, it will reliably create itself in a reasonable location on the laptop (which is current user Directory in this case).

A new key-value pair can be added to the data store using the Create operation. 

The key is always a string - capped at 32chars. The value is always a JSON object - capped at 16KB.

If Create is invoked for an existing key, an appropriate error must be returned.

A Read operation on a key can be performed by providing the key, and receiving the
value in response, as a JSON object.

A Delete operation can be performed by providing the key.

Every key supports setting a Time-To-Live property when it is created. This property is optional. If provided, it will be evaluated as an integer defining the number of seconds the key must be retained in the data store. Once the Time-To-Live for a key has expired, the key will no longer be available for Read or Delete operations.

Appropriate error responses must always be returned to a client if it uses the data store in unexpected ways or breaches any limits.


# How to Run:-
Put the above files in 1 directory and run Main_File.java

Actually Main File.java is the simulation of how it should be used

In Practical casses the Library End points are exposed and it should be used.

