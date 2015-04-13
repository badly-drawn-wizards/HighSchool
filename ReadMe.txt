      ___           ___           ___           ___           ___           ___           ___           ___           ___           ___           ___     
     /\  \         /\  \         /\__\         /\  \         |\__\         /\  \         /\__\         /\  \         /\  \         /\  \         /\  \    
    /::\  \       /::\  \       /::|  |       /::\  \        |:|  |       /::\  \       /:/  /        /::\  \       /::\  \       /::\  \       /::\  \   
   /:/\:\  \     /:/\:\  \     /:|:|  |      /:/\:\  \       |:|  |      /:/\ \  \     /:/__/        /:/\:\  \     /:/\:\  \     /:/\:\  \     /:/\:\  \  
  /::\~\:\  \   /::\~\:\  \   /:/|:|  |__   /:/  \:\  \      |:|__|__   _\:\~\ \  \   /::\  \ ___   /::\~\:\  \   /:/  \:\__\   /::\~\:\  \   /::\~\:\  \ 
 /:/\:\ \:\__\ /:/\:\ \:\__\ /:/ |:| /\__\ /:/__/ \:\__\     /::::\__\ /\ \:\ \ \__\ /:/\:\  /\__\ /:/\:\ \:\__\ /:/__/ \:|__| /:/\:\ \:\__\ /:/\:\ \:\__\
 \/__\:\ \/__/ \/__\:\/:/  / \/__|:|/:/  / \:\  \  \/__/    /:/~~/~    \:\ \:\ \/__/ \/__\:\/:/  / \/__\:\/:/  / \:\  \ /:/  / \:\~\:\ \/__/ \/_|::\/:/  /
      \:\__\        \::/  /      |:/:/  /   \:\  \         /:/  /       \:\ \:\__\        \::/  /       \::/  /   \:\  /:/  /   \:\ \:\__\      |:|::/  / 
       \/__/        /:/  /       |::/  /     \:\  \        \/__/         \:\/:/  /        /:/  /        /:/  /     \:\/:/  /     \:\ \/__/      |:|\/__/  
                   /:/  /        /:/  /       \:\__\                      \::/  /        /:/  /        /:/  /       \::/__/       \:\__\        |:|  |    
                   \/__/         \/__/         \/__/                       \/__/         \/__/         \/__/         ~~            \/__/         \|__|

------------------------------------------------------------------  Reuben  Steenekamp  ------------------------------------------------------------------

---- Requirements ----

Hardware and Software requirements
 Server
  Hardware
   1GB RAM
   5GB available disk space
  Software
   Java Runtime Environment 8
 Client
  Hardware
   1GB RAM
   500MB available disk space
   Graphics card compatible with OpenGL 2.0 or later
  Software
   Java Runtime Environment 8

---- Information ----
- FancyShader consists of two executables
	- FancyShaderClient
	- FancyShaderServer
- By default RunMe will have FancyShaderClient connect to localhost, this can be changed in client_args.cfg
- FancyShaderClient will not proceed if it cannot connect to FancyShaderClient

---- Instructions ----
--- Windows ---
Run RunMe.bat

--- Other platforms ---
If you are on another platform that can run Java, then you can run the binaries manually.
In a shell with "bin" as the working directory on the server machine run the following:
java -jar FancyShaderServer

Then, in another shell with "bin" as the working directory on the client machine run the following:
java -jar FancyShaderClient --host <server-ip> --port 1099

If you wish to run the server on the machine as the client then you can use the loopback ip 127.0.0.1 as the server-ip