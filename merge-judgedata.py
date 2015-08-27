#!/usr/bin/python
#coding=utf-8
from os.path import join
import os, sys

problem = 'k'
name = 'knapsackcollection'
dryrun = True

indir = join('nwerc2014testdata', name)
outdir = join('nwerc2014', 'testdata')

def counttests(tests):
	count = 0
	for base in tests:
		test = tests[base]
		intest = open(join(indir, test['in']))
		for line in intest:
#			n = int(line)
			(n,s,t) = list(map(int, line.split(' ')))
			count += 1
			next(intest)
#			(a,b,c) = line.split(' ')
#			a = int(a)
#			b = int(b)
#			c = int(c)
#			if len(line) < 1:
#				raise Exception('parsing failed, expected a nonempty line')
#			next(intest)
#			for i in range(0, n):
#				next(intest)
#			for i in range(0, v):
#				next(intest)
	return count

def gettests():
	tests = {}
	for file in os.listdir(indir):
		# Find the testcase name
		base = None
		if file.endswith('.in'):
			base = file[:-3]
			type = 'in'
		elif file.endswith('.ans'):
			base = file[:-4]
			type = 'out'
		else:
			continue
		if base == None:
			continue
		# Get the testcase
		if base not in tests:
			test = tests[base] = {}
		else:
			test = tests[base]
		# Store the file
		test[type] = file
	return tests

def writefiles(count, tests):
	testCasesIn = join(outdir, problem + '.in')
	testCasesOut = join(outdir, problem + '.out')
	infile = open(testCasesIn, 'w')
	outfile = open(testCasesOut, 'w')

	infile.write(str(count) + '\n')
	for base in tests:
		test = tests[base]
		intest = open(join(indir, test['in']))
		outtest = open(join(indir, test['out']))
		for line in intest.readlines():
			infile.write(line)
		for line in outtest.readlines():
			outfile.write(line)

	infile.close()
	outfile.close()


#tests = gettests()
#count = counttests(tests)
#if dryrun:
#	print(count)
#else:
#	writefiles(count, tests)













