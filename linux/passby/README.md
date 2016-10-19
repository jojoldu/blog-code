# 망 분리된 상태에서 build 파일 주고 받기

gradle로 build시 war.json에
[jq 다운로드 페이지](https://stedolan.github.io/jq/download/)
[jq 직접 다운로드](https://github.com/stedolan/jq/releases/download/jq-1.5/jq-1.5.tar.gz)

[sshpass 설치 가이드](https://gist.github.com/arunoda/7790979)
[sshpass 다운로드](https://sourceforge.net/projects/sshpass/)

**jq 설치**

```
./configure && make && sudo make install

sudo chmod a+x ./jq

sudo cp ./jq /usr/bin

```

**sshpass 설치**

```
tar -xvf sshpass-1.06

cd sshpass-1.06

./configure

sudo make install
```


**passby.sh**

```
#!/bin/bash
############
# description : Pass the war file from Public server to Mgmt server
############

# echo color list
# Black        0;30     Dark Gray     1;30
# Red          0;31     Light Red     1;31
# Green        0;32     Light Green   1;32
# Brown/Orange 0;33     Yellow        1;33
# Blue         0;34     Light Blue    1;34
# Purple       0;35     Light Purple  1;35
# Cyan         0;36     Light Cyan    1;36
# Light Gray   0;37     White         1;37

RED="\033[1;31m"
RESET="\033[0m"

function color_echo {
	# $RED + $1 + $RESET
	echo -e "$RED$1$RESET"
}

PUBLIC_SERVER_HOST=공용서버 IP
PUBLIC_SERVER_PORT=공용서버 Port
PUBLIC_SERVER_USER=공용서버 접속계정
PUBLIC_SERVER_PASSWORD=공용서버 접속비밀번호
PUBLIC_SERVER_DIR=공용서버의 war.json과 war파일이 위치한 디렉토리

WAR_NAME_JSON=war.json
LOCAL_DIR=공용서버의 war파일을 저장할 로컬PC 디렉토리

MGMT_SERVER_HOST=중앙서버 IP
MGMT_SERVER_PORT=중앙서버 Port
MGMT_SERVER_USER=중앙서버 접속계정
MGMT_SERVER_PASSWORD=중앙서버 접속비밀번호
MGMT_SERVER_DIR=중앙서버에 저장할 디렉토리

function get_war_name {
	color_echo "============== get WAR File Name =============="

	sshpass -p $PUBLIC_SERVER_PASSWORD sftp -oport=$PUBLIC_SERVER_PORT $PUBLIC_SERVER_USER@$PUBLIC_SERVER_HOST << EOF

	lcd $LOCAL_DIR

	cd $PUBLIC_SERVER_DIR

	get $WAR_NAME_JSON

	quit

EOF

	WAR_FILE_NAME=$(cat $LOCAL_DIR/$WAR_NAME_JSON | jq '.name')
	color_echo "============== WAR File Name =============="
	echo $WAR_FILE_NAME
}

function get_war_file {
	color_echo "============== get WAR File =============="

	sshpass -p $PUBLIC_SERVER_PASSWORD sftp -oport=$PUBLIC_SERVER_PORT $PUBLIC_SERVER_USER@$PUBLIC_SERVER_HOST << EOF

	lcd $LOCAL_DIR

	cd $PUBLIC_SERVER_DIR

	get $WAR_FILE_NAME

	quit

EOF
}

function put_war_file {
	color_echo "============== put WAR File =============="

	sshpass -p $MGMT_SERVER_PASSWORD sftp -oport=$MGMT_SERVER_PORT $MGMT_SERVER_USER@$MGMT_SERVER_HOST << EOF

	lcd $LOCAL_DIR

	cd $MGMT_SERVER_DIR

	put $WAR_FILE_NAME

	quit

EOF

}

color_echo "============== Start!! =============="

# call functions

# get war name
get_war_name

# get war file
get_war_file

# put war file
put_war_file

color_echo "============== The End =============="

```
