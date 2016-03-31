# Shortify installation guide

<ul><li><h2>Linux</h2></li>
1.Install <a href="https://docs.docker.com/compose/install/">docker-compose</a> (you need docker-compose v1.6 or greater, be sure to have already installed <a href="https://docs.docker.com/linux/step_one/">docker</a> before on your machine)<br><br>
2. Create a new folder on your  machine and clone this repo with <a href="https://git-scm.com/">git</a>:<br>
<code>git clone https://github.com/GruppoPBDMNG-8/Shortify</code><br><br>
3. Navigate into the "Shortify" folder:<br><code>cd Shortify</code><br><br>
4. Build and run the containers with:<br><code>docker-compose up</code><br>
...It may take a while, so go on and grab a coffee. <img src="https://cdn3.iconfinder.com/data/icons/pidginsmilies/coffee.png"><br><br>
5. Once ready, open your browser and go to <code>http://localhost:8085</code><br>

<h3>Execute JUnit tests</h3>
1. Run containers in detached mode:<br><code>docker-compose up -d</code><br>NOTE: If containers are already running stop them with CTRL+C<br><br>
2. Run this script with docker:<br><code> docker exec -it shortify_server_1 bash test.sh</code><br><br>
3. Results of the tests will be shown in the terminal.<br>

<li><h2>Windows via <a href="https://www.docker.com/products/docker-toolbox">Docker Toolbox</a></h2></li>
1. Install <a href="https://www.docker.com/products/docker-toolbox">Docker Toolbox</a> and follow instruction.<br>
<strong>IMPORTANT:</strong> Be sure to install <a href="https://docs.docker.com/compose/install/">docker-compose</a> and <a href="https://git-scm.com/">git</a> by ticking the checkboxes during the installation.<br><br>
2. Open the Docker Quickstart Terminal, it should be on your desktop.<br><br>
3. Once ready, take note of your virtual machine ip address (<a href="http://s12.postimg.org/52b7kxam5/dockertool.png">shown here</a>).<br><br>
4. Create a new folder and clone this repo with <a href="https://git-scm.com/">git</a>:<br>
<code>git clone https://github.com/GruppoPBDMNG-8/Shortify</code><br><br>
5. Navigate into the "Shortify" folder:<br><code>cd Shortify</code><br><br>
6. Build and run the containers with:<br><code>docker-compose up</code><br>
...It may take a while, so go on and grab a coffee. <img src="https://cdn3.iconfinder.com/data/icons/pidginsmilies/coffee.png"><br><br>
7. Once ready, open your browser and go to <code>http://(your VM ip):8085 (example: http://192.168.99.100:8085)</code><br>

<h3>Execute JUnit tests</h3>
1. Run containers in detached mode:<br><code>docker-compose up -d</code><br>NOTE: If containers are already running stop them with CTRL+C<br><br>
2. Convert test.sh with <a href="http://www.linuxcommand.org/man_pages/dos2unix1.html">dos2unix</a>:<code> docker exec -it shortify_server_1 dos2unix test.sh</code><br><br>
3. Run this script with docker:<br><code> docker exec -it shortify_server_1 bash test.sh</code><br><br>
4. Results of the tests will be shown in the terminal.<br>



