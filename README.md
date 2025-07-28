### Introduction
This package contains both a REST implementation of 
the dynamoDB get() and put() APIs and a GRPC implementation.

### Testing
I have tested the REST APIs by deploying to 3 kubernetes pods, that
replicate over a HttpConnection amongst themselves. Actually,
there is a leader node and the other 3 replicate from it in this design

