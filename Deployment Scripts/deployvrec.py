#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
#	This is to deploy the web application part of the system
#  deployvrec.py
#  
#  Copyright 2013 El Zede @ CSDL <El Zede @ CSDL@ELZEDECSDL-PC>
#  
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 2 of the License, or
#  (at your option) any later version.
#  
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#  
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software
#  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
#  MA 02110-1301, USA.
#  
#  

import sys
import os
from os import path
import shutil


def recursive_overwrite(src, dest, ignore=None):
    if os.path.isdir(src):
        if not os.path.isdir(dest):
            os.makedirs(dest)
        files = os.listdir(src)
        if ignore is not None:
            ignored = ignore(src, files)
        else:
            ignored = set()
        for f in files:
            if f not in ignored:
                recursive_overwrite(os.path.join(src, f), 
                                    os.path.join(dest, f), 
                                    ignore)
    else:
        shutil.copyfile(src, dest)


def main():
	
	arg_count = len(sys.argv)
	if(arg_count <= 2):
		print "Must provide more than one argument"
		return 0
		
	if(arg_count == 3) :
		source = sys.argv[1]
		if(source.lower() == "clean") :
			print "cleaning the deployment workspace.."
			return 0;
		
	source = sys.argv[1]
	destination = sys.argv[2]
	print "Source: " + source + "\nDestination: " + destination + "\n"
	
	sourcepath = os.path.abspath(source + "/634Code/vrec/webapplication")
	destpath = os.path.abspath(destination)
	print sourcepath

	print [name for name in os.listdir(sourcepath) if os.path.isdir(sourcepath)]
	
	if not os.path.exists(destpath) :
		print "creating dir"
		os.makedirs(destpath)
	
	for filename in [name for name in os.listdir(sourcepath) if os.path.isdir(sourcepath)] :
		src = os.path.join(sourcepath, filename)
		dst = os.path.join(destpath, filename)
		if os.path.isdir(src) :
			recursive_overwrite(src, dst)
		else :
			shutil.copyfile(src, dst)
		
		print "copied " + filename
	
	srcpath = os.path.abspath(source + "/images/movieitem")
	if os.path.exists(srcpath) :
		print "copying item images..."
		dstpath = os.path.abspath(destpath + "/images/movieitem")
		recursive_overwrite(srcpath, dstpath)
		
	print "Deployment complete."
	
	return 0

if __name__ == '__main__':
	main()

