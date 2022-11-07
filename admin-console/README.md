# PES_ECOnnect (Admin console)

## Team Members
| Name | GitHub username | Taiga username |
| --- | --- | --- |
| Pol Rivero | PolRivero | polrivero |
| Pau Antonio | pauantonio | pauantonio |
| Marc César | marc-cesar | marc-cesar |
| David Jiménez | DavidFIB | DavidFIB |
| Thiago Mulero | thiagobulls05 | thiagobulls05 |
| Pol Burkardt | pol9061 | pol9061 |
| Silvia Andreu | silviandreu | silviandreu |


## Instructions
**Run tests:**
```
chmod +x mvnw
./mvnw -q test
```
**Build JAR:**
```
chmod +x mvnw
./mvnw -q compile assembly:single
```
**Run JAR:**
```
java -jar target/admin-console-1.0.jar
```
