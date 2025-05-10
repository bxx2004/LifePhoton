def maxInd(typeGraph):
	maxX = 0
	maxY = 0
	for node in typeGraph:
		if typeGraph.nodes[node]["x"] > maxX:
			maxX = typeGraph.nodes[node]["x"]
		if typeGraph.nodes[node]["y"] > maxY:
			maxY = typeGraph.nodes[node]["y"]
	return maxX, maxY

#待查
def matingScore(typeGraph, mnName, x, y, matingResDict, osResDict):
	score = 0
	message = ""
	for key in typeGraph.nodes:
		keyX = typeGraph.nodes[key]["x"]
		keyY = typeGraph.nodes[key]["y"]
		
		key = key.split("_")[0]
		mnName = mnName.split("_")[0]
		#print(key, mnName)
		if key not in matingResDict:
			message += "missing "+key+" @ "+mnName+" mating information; "
			continue
		else:
			if mnName not in matingResDict[key]:
				message += "missing "+key+" @ "+mnName+" mating information; "
				continue
			
		if keyX != x and keyY != y:
			if matingResDict[key][mnName] == "+":
				score += 1
			else:
				score += -1
				message += "contradictory "+key+" @ "+mnName+" mating information; "
		
		if keyX == x and keyY != y:
			if matingResDict[key][mnName] == "-":
				if key in osResDict:
					if mnName in osResDict[key]:
						if osResDict[key][mnName][0] == "same" and osResDict[key][mnName][1] == "diff":
							score += 1
						else:
							score += -1
							message += "contradictory "+key+" @ "+mnName+" OS exp information; "
					else:
                                                score += 1
				else:
					score += 1
			else:
				score += -1
				message += "contradictory "+key+" @ "+mnName+" mating information; "
		
		if keyX != x and keyY == y:
			if matingResDict[key][mnName] == "-":
				if key in osResDict:
					if mnName in osResDict[key]:
						if osResDict[key][mnName][0] == "diff" and osResDict[key][mnName][1] == "same":
							score += 1
						else:
							score += -1
							message += "contradictory "+key+" @ "+mnName+" OS exp information; "
					else:
						score += 1
				else:
					score += 1
			else:
				score += -1
				message += "contradictory "+key+" @ "+mnName+" mating information"
		
		if keyX == x and keyY == y:
			if matingResDict[key][mnName] == "-":
				if key in osResDict:
					if mnName in osResDict[key]:
						if osResDict[key][mnName][0] == "same" and osResDict[key][mnName][1] == "same":
							score += 1
						else:   
							score += -1
							message += "contradictory "+key+" @ "+mnName+" OS exp information; "
					else:
						score += 1
				else:
					score += 1
			else:
				score += -1
				message += "contradictory "+key+" @ "+mnName+" mating information; "
		
		#print(message)
		#print(mnName, key, matingResDict[key][mnName])
	return score, message	

def matingScoreForTwoNodes(typeList1, typeList2, matingRes):
	x1 = typeList1[0]
	y1 = typeList1[1]
	x2 = typeList2[0]
	y2 = typeList2[1]	

	score = 0

	if x1 != x2 and y1 != y2:
		if matingRes == "+":
			score = 1
		else:
			score = -1

	if x1 == x2 and y1 != y2:
		if matingRes == "-":
			score = 1
		else:
			score = -1

	if x1 != x2 and y1 == y2:
		if matingRes == "-":
			score = 1
		else:
			score = -1

	if x1 == x2 and y1 == y2:
		if matingRes == "-":
			score = 1
		else:
			score = -1

	return score

def matingScoreForAll(matingResDict, typeDict):
	score = 0
	message = ""
	for nm1 in matingResDict:
		for nm2 in matingResDict[nm1]:
			scoreTmp = matingScoreForTwoNodes(typeDict[nm1], typeDict[nm2], matingResDict[nm1][nm2])
			score += scoreTmp
			if scoreTmp != 1:
				message += "contradictory "+nm1+" @ "+nm2+" mating information; "
	return score, message

def maxFromMatrix(scoreDict):
	maxX = 0
	maxY = 0
	maxScore = 0
	message = ""

	maxScoreList = [[maxX, maxY, maxScore, message]]
	for x in scoreDict.keys():
		for y in scoreDict[x]:
			if maxScore <= scoreDict[x][y][0]:
				if maxScore < scoreDict[x][y][0]:	
					maxScoreList = [[x, y, scoreDict[x][y][0], scoreDict[x][y][1]]]
					maxScore = scoreDict[x][y][0]
				else:
					maxScoreList.append([x, y, maxScore, message])
	return maxScoreList

def addNode(typeGraph, node):
	out_degree_list = list(typeGraph.out_degree())
	nodeLastList = [node for node, degree in out_degree_list if degree == 0]
	#print(nodeLastList)
	for nodeLast in nodeLastList:
		if nodeLast != node and nodeLast.split("_")[0] != node.split("_")[0]:
			typeGraph.add_edge(nodeLast, node)
	return typeGraph