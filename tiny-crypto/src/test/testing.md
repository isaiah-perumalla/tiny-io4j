# Setup socat for testing
run socat TLS listener 
1. Generate cert/keys
```agsl
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 365 -nodes -subj '/CN=localhost' 

```
2. Run socat listener
```agsl
socat openssl-listen:2000,reuseaddr,cert=cert.pem,key=key.pem,verify=0,fork EXEC:cat

```
3. enforce tls 1.3 add the following 
```agsl
socat openssl-listen:2000,reuseaddr,cert=cert.pem,key=key.pem,verify=0,openssl-min-proto-version=TLS1.3,fork EXEC:cat

```