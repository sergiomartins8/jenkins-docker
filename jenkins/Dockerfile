FROM jenkins/jenkins:lts

USER root

# install docker
RUN apt-get update \
      && apt-get install -y sudo apt-transport-https software-properties-common \
      && curl -fsSL https://download.docker.com/linux/debian/gpg | sudo apt-key add - \
      && add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"\
      && apt-get update -y \
      && apt-get install docker-ce -y \
      && rm -rf /var/lib/apt/lists/*

# install git and remove download archive files
RUN apt-get install -y git && \
apt-get clean

# deactivate default random admon password
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
COPY security.groovy /usr/share/jenkins/ref/init.groovy.d/security.groovy

# install plugins
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt

RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers