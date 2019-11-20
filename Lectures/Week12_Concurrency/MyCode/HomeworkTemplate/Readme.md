# Problems with / observations about the code

* int is a *small* data type to use for this operation. It is quite easy to
overflow the *total* variable. Try to use N = 100000.
* Ask yourself - what is the largest N we can accomodate in this example?
* Ask yourself - is there perhaps a better primitive to use?
* Note the use of the *synchronized* keyword in method signature of the 
*update()* method.
