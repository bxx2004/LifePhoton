import os
import sys
import networkx as nx
import itertools as it
import matplotlib.pyplot as plt
import json
from tools import *

if len(sys.argv) != 3:
	print("Function: imputate mating-types from mating-result")
	print("Usage: python", sys.argv[0], "mating_result_matrix_file os_result_file")
	print("inFile: mating_result_matrix_file is the matrix of mating result")
	print("      the first row and column is the smonokaryon id, and column number should be identical for all rows")
	print("      the strain name is <strain id>-<monokaryon id>")
	print("inFile: os_result_file is the table result for OS experiment")
	print("      the file must have a head line")
	print("      each line has 4 columns with <stain1> <stain2> <x> <y>")
	print("	     <x> and <y> must be diff or same")
	print("Note: strain name should NOT contain # _ or blank")
	sys.exit()

####
matingResFile=sys.argv[1]
osResFile=sys.argv[2]
#outFile=sys.argv[1]+".output.txt"

####load mating result table
monokaryonsMatingNumber = 0
matingResDict = {}
matingNegResList = []

lineNum = 0
fIn = open(matingResFile)
for line in fIn:
	tmp = line.replace('\n', '').split()
	lineNum += 1
	if lineNum == 1:
		mnNameList = tmp
		for mnName in mnNameList[1:]:
			matingResDict[mnName] = {}
	else:
		mnName1 = tmp[0]
		if mnName1 not in matingResDict:
			matingResDict[mnName1] = {}

		for ind in range(1, len(tmp)):
			mnName2 = mnNameList[ind]
			
			if mnName2 not in matingResDict:
				matingResDict[mnName2] = {}
	
			if tmp[ind] in ["+", "-"]:
				matingResDict[mnName1][mnName2] = tmp[ind]
				matingResDict[mnName2][mnName1] = tmp[ind]
				monokaryonsMatingNumber += 1	
				if tmp[ind] == "-":
					matingNegResList.append(mnName1+"#"+mnName2)
fIn.close()

####load proposed mating result
#fIn = open("mating_result.propose.dat")
#for line in fIn:
#	tmp = line.replace('\n', '').split()
#	mnName1 = tmp[0]
#	mnName2 = tmp[1]
#	res = tmp[2]
#	if res in ["+", "-"]:
#		matingResDict[mnName1][mnName2] = res
#		matingResDict[mnName2][mnName1] = res	
#fIn.close()

####load OWE-SOJ result
osResDict = {}
osResList = []
fIn = open(osResFile)
lineNum = 0
for line in fIn:
	lineNum += 1
	tmp = line.replace('\n', '').split()
	if lineNum > 0:
		if len(tmp) == 4 and tmp[2] in ["same", "diff"] and tmp[3] in ["same", "diff"]:
			strain1 = tmp[0]
			strain2 = tmp[1]
			x = tmp[2]
			y = tmp[3]
			osResDict[strain1] = {}
			osResDict[strain1][strain2] = [x, y]
			osResDict[strain2] = {}
			osResDict[strain2][strain1] = [x, y]
			osResList.append(strain1+"#"+strain2)
			osResList.append(strain2+"#"+strain1)
fIn.close()

result = {}

###add monokaryons mating result from dikaryon
mnList = list(matingResDict.keys())
for ind_i in range(0, len(mnList)-1):
	mnName1 = mnList[ind_i]
	for ind_j in range(ind_i+1, len(mnList)):
		mnName2 = mnList[ind_j]
		if mnName1 not in matingResDict:
			matingResDict[mnName1] = {}
		else:
			if mnName2 not in matingResDict[mnName1]:
				matingResDict[mnName1][mnName2] = ""

		if mnName2 not in matingResDict:
                        matingResDict[mnName2] = {}
		else:
			if mnName1 not in matingResDict[mnName2]:
                                matingResDict[mnName2][mnName1] = ""
		
		if mnName1 != mnName2 and mnName1.split("-")[0] == mnName2.split("-")[0]:
			matingResDict[mnName1][mnName2] = "+"
			matingResDict[mnName2][mnName1] = "+"
			monokaryonsMatingNumber += 1

###collect stain paris without mating result
missedMatingList = []
for ind_i in range(0, len(mnList)-1):
	mnName1 = mnList[ind_i]
	for ind_j in range(ind_i+1, len(mnList)):
		mnName2 = mnList[ind_j]
		if matingResDict[mnName2][mnName1] not in ["+", "-"] or matingResDict[mnName1][mnName2] not in ["+", "-"]:
			missedMatingList.append(mnName1+"#"+mnName2)

###collect stain paris without owe-soj result
missedOSList = []
for elem in matingNegResList:
	if elem not in osResList:
		missedOSList.append(elem)

###summary mating result matrix
#print("###########")
#print("summary mating result")
#print("the number of monokaryons: ", len(mnList))
result["number_of_monokaryons"] = len(mnList)
#print("the number of mating info: ", monokaryonsMatingNumber)
result["number_of_mating_info"] = monokaryonsMatingNumber
#print("missed mating information: ", len(missedMatingList))
result["number_of_missed_mating_info"] = len(missedMatingList)
result["missed_mating_list"] = missedMatingList
for elem in missedMatingList:
	print(elem)

result["number_of_OS_exp_info"] = len(osResList)
#print("the number of OS exp info: ", len(osResList))
result["number_of_missed_OS_exp_info"] = len(missedOSList)
result["missed_OS_exp_list"] = missedOSList
#print("missed OS exp: ", len(missedOSList))
for elme in missedOSList:
	print(elem)

###setting the initial monokaryons
begList = ["GS0002-2", "GS0002-190"]

###define monokaryons mating-type graph
typeGraph = nx.DiGraph()
unidentifiedList = []

###load predefined monokaryons mating type information
for ind in range(0, len(begList)):
	mnName = begList[ind]
	typeGraph.add_node(mnName)
	typeGraph.nodes[mnName]["x"] = ind+1
	typeGraph.nodes[mnName]["y"] = ind+1
	if ind > 0:
		typeGraph = addNode(typeGraph, mnName)

####select all-mating monokaryons
result["pre-assign_mating_type"] = []
for ind in range(0, len(mnList)):
	mnName = mnList[ind]
	#print("#####")
	#print(mnName)
	if mnName in typeGraph.nodes():
		result["pre-assign_mating_type"].append(
			{
				"mn_name": mnName,
				"x": typeGraph.nodes[mnName]["x"],
				"y": typeGraph.nodes[mnName]["y"]
			}
		)
		#print("Pre-assign mating type of ", typeGraph.nodes[mnName]["x"], " and ", typeGraph.nodes[mnName]["y"], " for ", mnName)
	else:
		maxX, maxY = maxInd(typeGraph)
		scoreDict = {}
		for x in range(1, maxX+2):
			scoreDict[x] = {}
			for y in range(1, maxY+2):
				score, message = matingScore(typeGraph, mnName, x, y, matingResDict, osResDict)
				scoreDict[x][y] = [score, message]
				#print(x, y, score)
		#print(scoreDict)
		maxScoreList = maxFromMatrix(scoreDict)
		
		if len(maxScoreList) == 1:
			#print(maxScoreList)
			maxX = maxScoreList[0][0]
			maxY = maxScoreList[0][1]
			maxScore = maxScoreList[0][2]
			message = maxScoreList[0][3]
			if maxScore == len(typeGraph.nodes):
				typeGraph.add_node(mnName)
				typeGraph.nodes[mnName]["x"] = maxX
				typeGraph.nodes[mnName]["y"] = maxY
				typeGraph.nodes[mnName]["message"] = message
				typeGraph = addNode(typeGraph, mnName)
			else:
				print("not identify: ", mnName)
				print("bacause: ", message)
				unidentifiedList.append(mnName)

		elif len(maxScoreList) > 1:
			print("not identify: ", mnName)
			print("posible: ", message)
			#print(maxScoreList)
			for elem in maxScoreList:
				print(elem[0], elem[1])
			unidentifiedList.append(mnName)

###print all-mating monokaryons
#print(typeGraph)
#nx.draw(typeGraph,with_labels=True)
#plt.show()
result["mn_graph"] = {
	"nodes":[],
	"lines": []
}
for x in typeGraph.nodes:
	result["mn_graph"]["nodes"].append({
		"id": x,
		"text": x,
		"data": {
			"x": typeGraph.nodes[x]["x"],
			"y": typeGraph.nodes[x]["y"]
		}
	})
result["mn_graph"]["rootId"] = result["mn_graph"]["nodes"][0]
for x in typeGraph.edges:
	result["mn_graph"]["lines"].append({
		"from": x[0],
		"to": x[1],
		"lineShape": 1
	})
###print non-all-mating monokaryons
#print(unidentifiedList)

###include all possible mating-type informations for non-all-mating monokaryons
for node in unidentifiedList:
	maxX, maxY = maxInd(typeGraph)
	#print("########")
	#print(node)
	scoreDict = {}
	for x in range(1, maxX+2):
		scoreDict[x] = {}
		for y in range(1, maxY+2):
			score, message = matingScore(typeGraph, node, x, y, matingResDict, osResDict)
			scoreDict[x][y] = [score, message]
	
	maxScoreList = maxFromMatrix(scoreDict)
	#print(maxScoreList)

	out_degree_list = list(typeGraph.out_degree())
	nodeLastList = [node for node, degree in out_degree_list if degree == 0]
	#print(nodeLastList)        

	for ind in range(0, len(maxScoreList)):
		nodeName = node+"_"+str(ind)
		typeGraph.add_node(nodeName)
		typeGraph.nodes[nodeName]["x"] = maxScoreList[ind][0]
		typeGraph.nodes[nodeName]["y"] = maxScoreList[ind][1]
		typeGraph.nodes[nodeName]["score"] = maxScoreList[ind][2]
		typeGraph.nodes[nodeName]["message"] = maxScoreList[ind][3]
		#typeGraph = addNode(typeGraph, nodeName)
		for nodeLast in nodeLastList:
			#print(nodeLast)
			typeGraph.add_edge(nodeLast, nodeName)
result["un_graph"] = {
	"nodes":[],
	"lines": []
}
for x in typeGraph.nodes:
	result["un_graph"]["nodes"].append({
		"id": x,
		"text": x,
		"data": {
			"x": typeGraph.nodes[x]["x"],
			"y": typeGraph.nodes[x]["y"],
			"score": typeGraph.nodes[x]["score"] if "score" in typeGraph.nodes[x] else -1
		}
	})
result["un_graph"]["rootId"] = result["un_graph"]["nodes"][0]
for x in typeGraph.edges:
	result["un_graph"]["lines"].append({
		"from": x[0],
		"to": x[1],
		"lineShape": 1
	})
#print(typeGraph)
#nx.draw(typeGraph, with_labels=True)
#fig = plt.gcf()
#fig.set_size_inches(18.5, 10.5)
#fig.savefig('1.png', dpi=100)
#plt.show()

###screen all possible mating-type combination and print all-right mating-type combinations 
in_degree_list = list(typeGraph.in_degree())
nodeBegList = [node for node, degree in in_degree_list if degree == 0]
out_degree_list = list(typeGraph.out_degree())
nodeEndList = [node for node, degree in out_degree_list if degree == 0]

totalScore = sum([len(matingResDict[x]) for x in matingResDict.keys() ])
#fOut = open(outFile, 'w')
numberTotal = 0
numberFit = 0
result["data"] = []
for nodeBeg in nodeBegList:
	for nodeEnd in nodeEndList:
		for path in nx.all_simple_paths(typeGraph, source=nodeBeg, target=nodeEnd):
			typeDict = {}
			for node in path:
				#print(node, typeGraph.nodes[node]["x"], typeGraph.nodes[node]["y"])
				typeDict[node.split("_")[0]] = [typeGraph.nodes[node]["x"], typeGraph.nodes[node]["y"]]
			score, message = matingScoreForAll(matingResDict, typeDict)	
			if score == totalScore:
				#print("#########", totalScore, score, file=fOut)
				result["score"] = score
				result["total_score"] = totalScore
				for node in path:
					result["data"].append({
						"id": node,
						"x": typeGraph.nodes[node]["x"],
						"y": typeGraph.nodes[node]["y"]
					})
					#print(node, typeGraph.nodes[node]["x"], typeGraph.nodes[node]["y"], file=fOut)
				numberFit += 1
			numberTotal += 1
			#print(path, totalScore, score, message, file=fOut)
#fOut.close()
result["total_number_of_mating-type_combinations"] = numberTotal
result["total_number_of_all-right_mating-type_combinations"] = numberFit
infoFile=sys.argv[1]+".output.json"
infoOut = open(infoFile, 'w')
infoOut.write(json.dumps(result,indent=4))
infoOut.close()
print("processor is finished.")
#print("total number of mating-type combinations:", numberTotal)
#print("number of all-right mating-type combinations:", numberFit)
#print("all combinations were listed in", outFile)
