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
<h5>To install Shortify on Windows, you could create a Linux VM and then follow the instruction for Linux. After the installation use the browser inside the VM. (You could use <a href = "https://www.virtualbox.org/">VirtualBox</a> and an <a href="http://www.ubuntu.com/download/desktop"> Ubuntu</a> image).</h5>
</ul>


