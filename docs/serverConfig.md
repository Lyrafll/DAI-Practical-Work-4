# Drink Manager - Server configuration

To have the same configuration as this project you need to install : `Ubuntu 22.04.3 LTS`

```cmd
# Update the list of packages
sudo apt update

# Upgrade the packages
sudo apt upgrade

# Install apache2-utils
sudo apt install apache2-util

# Install JDK-17
sudo apt install openjdk-17-jdk

# Add Docker's official GPG key
sudo apt-get install ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

# Add the repository to Apt sources:
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update

# Install Docker
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Verify installation
sudo docker run hello-world

# Manage Docker as a non-root user
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
docker run hello-world

# Configure Docker to start on boot with systemd
sudo systemctl enable docker.service
sudo systemctl enable containerd.service

# Open the HTTP protocol on the virtual machine
sudo ufw allow http

# Open the HTTPS protocol on the virtual machine
sudo ufw allow https

# Restart the server
sudo reboot

# Clone repository on the virtual machine
git clone https://github.com/Lyrafll/DAI-Practical-Work-4.git

# Navigate into Traefik directory
cd DAI-Practical-Work-4/traefik-secure

# Modify environement variable
vi .env

# Create the secrets directory
mkdir secrets

# Create the auth-users.txt file with admin user
htpasswd -c secrets/auth-users.txt admin

# Start Traefik (reverse proxy)
docker compose up -d

# Navigate into Drink-Manager directorie
cd ../DAI-PW4

# Build the jar localy
./mvnw package

# Build docker
docker build -t dai-pw4 .

# Modify environement variable
vi .env

# Start Drink-Manager (API)
docker compose up -d
```
