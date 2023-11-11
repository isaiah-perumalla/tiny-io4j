# use Jextract to get native lib bindings 
```agsl
$JEXTRACT @libsodium.conf   crypto.h  --output src/main/java/
```