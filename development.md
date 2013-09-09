## Development notes

### Requirement

 * Java7 (JDK) 


### Developing UTGB with GWT super-dev mode

 - GWT Compile (required only first time)
    ./sbt gwt-compile

 - Launch web container
    ./sbt "~;container:start; container:reload /"

 - Launch GWT code server (super dev mode)
    ./sbt gwt-superdev

  if you need to debug webUI remotely (e.g. using 192.168.xxx.xxx address), launch gwt-superdev with -Dgwt.expose option

    ./sbt gwt-superdev -Dgwt.expose 

 - Visit http://localhost:9876/ and copy the bookmarklet ("Dev Mode On") appeared in the page to your bookmark bar.

 - Open http://localhost:8080/gwt, then click "Dev Mode On".


 If you need to use 192.xx.xx.xx address to access GWT pages, run gwt-superdev mode with -Dgwt.expose=true option

    ./sbt gwt-superdev -Dgwt.expose=true

 You also need to copy bookmarklet from http://192.xx.xx.xx:9876 since it becomes different when using http://localhost:9876


* Install utgb command

    $ make install

