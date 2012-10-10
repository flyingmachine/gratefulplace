#!/bin/bash

middleman build
rm -Rf ../../public/*
cp -R build/* ../../public
mv ../../public/*.html ../templates
