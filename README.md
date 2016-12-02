# Compilador de MiniJava

## Modo de compilación

Para compilar el código fuente se recomienza tener JDK 1.8.0 o superior instalado.
Situarse en la carpeta **/src** y escribir

```
javac modulos/Principal.java
```

## Modo de uso

Para utilizar el compilador
Situarse en la carpeta **/src** y escribir

```
java modulos.Principal <Entrada> [Salida]
```

Donde *Entrada* (obligatorio) es la ruta del archivo de entrada (código fuente de MiniJava) y *Salida* es la ruta del archivo de salida. En caso de no especificarse ruta se generará un archivo nuevo con extensión *.ceiasm*


## Corriendo el código intermedio

Para correr el código intermedio ejecutar el siguiente comando

```
java -jar CeIVM2011.jar <Archivo>
```

Donde *Archivo* es el archivo de código intermedio generado en el paso anterior.
