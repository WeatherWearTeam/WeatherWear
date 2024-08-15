#!/bin/bash
# 애플리케이션 설치 후 추가 작업

# 필요에 따라 설정 파일 등을 복사
LOGDIR="/home/ubuntu/spring/WeatherWear/logs"
LOGFILE="$LOGDIR/startup.log"

mkdir -p $LOGDIR
chown ubuntu:ubuntu $LOGDIR