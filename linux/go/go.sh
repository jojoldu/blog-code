#!/bin/bash

RED="\033[1;31m"
RESET="\033[0m"

function color_echo {
        # $RED + $1 + $RESET
        echo -e "$RED$1$RESET"
}

USER=jojoldu
PASSWORD=비밀번호
HOST=원격서버 주소
PORT=원격서버 포트

function connect_server {
	color_echo "connet server"
	sshpass -p $PASSWORD ssh -t -p$PORT $USER@$HOST "su - root"
}


connect_server
