#!/bin/bash

middleman build
cp -R build/* ../../public
