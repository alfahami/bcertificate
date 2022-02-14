# Quick intro

**BCertificate** is a Hyperledger blockchain full application that automates the issuance, dispatch, and management of university's digital credentials (diplomas, skills, transcripts, degree certifications, etc).

A blockchain network that let schools, colleges and universities add their credentials in the blockchain which could help against fraud papers.

The network is build using [Hyperledger Fabric V2.X.X](https://www.hyperledger.org/use/fabric)

### Configuration and running
**BCertificate** is built on top of Hyperledger Linux foundation, thus it's a must to install and configure Hyperledger first.

#### Hyperledger installation and configuration

*Prerequisites*:
  - Install and configure Docker (tons of tutorial on the web, )
  - Install curl (`sudo apt update && sudo apt upgrade` then `sudo apt install curl` on linux machine)

*Hyperledger*:
  - Run this single command to download and install the samples, binaries, and docker image of the latest production release of HL:

  - `curl -sSL https://bit.ly/2ysbOFE | bash -s`
  - Find more details [Official HL documentation](https://hyperledger-fabric.readthedocs.io/en/release-2.2/install.html)
  - Run the test-network as described in [using the test-network tutoriel](https://hyperledger-fabric.readthedocs.io/en/release-2.2/test_network.html) to make sure that everything is set up.

### BCertificate, installation and running
Now how to get start our network and get our web app running?

First and foremost, download or clone this repo.
Copy the *certificate/* folder and paste it to */fabric-samples/*(this folder was downloaded when setting up HL) 


