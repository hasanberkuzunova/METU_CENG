#include "Graph.h"
#include "GraphExceptions.h"

#include <iostream>
#include <iomanip>
#include <queue>
#include <fstream>
#include <sstream>
#include <cstdlib>

// Literally do nothing here
// default constructors of the std::vector is enough
Graph::Graph()
{}

Graph::Graph(const std::string& filePath)
{
    // ============================= //
    // This function is implemented  //
    // Do not edit this function !   //
    // ============================= //
    // Tokens
    std::string tokens[3];

    std::ifstream mapFile(filePath.c_str());
    // Read line by line
    std::string line;
    while (std::getline(mapFile, line))
    {
        // Empty Line Skip
        if(line.empty()) continue;
        // Comment Skip
        if(line[0] == '#') continue;

        // Tokenize the line
        int i = 0;
        std::istringstream stream(line);
        while(stream >> tokens[i]) i++;

        // Single token (Meaning it is a vertex)
        if(i == 1)
        {
            InsertVertex(tokens[0]);
        }
        // Exactly three tokens (Meaning it is an edge)
        else if(i == 3)
        {
            int weight = std::atoi(tokens[0].c_str());
            if(!ConnectVertices(tokens[1], tokens[2], weight))
            {
                std::cerr << "Duplicate edge on "
                          << tokens[0] << "-"
                          << tokens[1] << std::endl;
            }
        }
        else std::cerr << "Token Size Mismatch" << std::endl;
    }
}

void Graph::InsertVertex(const std::string& vertexName)
{
    // TODO
    int i=0;
    GraphVertex V;
    V.name=vertexName;
    V.edgeCount=0;
    for(;i<vertexList.size();i++){
        if(vertexList[i].name==vertexName) throw DuplicateVertexNameException();
    }
    vertexList.push_back(V);
}

bool Graph::ConnectVertices(const std::string& fromVertexName,
                            const std::string& toVertexName,
                            int weight)
{
    // TODO
    int i=0,count=0,j=0,m=0;
    for(;i<vertexList.size();i++){
        if(vertexList[i].name==fromVertexName){
           count++; 
        } 
    }
     for(i=0;i<vertexList.size();i++){
        if(vertexList[i].name==toVertexName ){
           count++;
        } 
    }
    if(count!=2){
        throw VertexNotFoundException();
    } 
    
    if(toVertexName==fromVertexName){
        return 0;  
    } 
    
    for(i=0;i<vertexList.size();i++){
        if(vertexList[i].name==fromVertexName){
            if(vertexList[i].edgeCount>=MAX_EDGE_PER_VERTEX) {
                throw TooManyEdgeOnVertexExecption();
            }
            else{
                break;
            } 
        }
    }
     for(j=0;j<vertexList.size();j++){
        if(vertexList[j].name==toVertexName){
            if(vertexList[j].edgeCount>=MAX_EDGE_PER_VERTEX){
                throw TooManyEdgeOnVertexExecption();
            } 
            else {
                break;
            }
        }
    }

     for(;m<edgeList.size();m++){
         if( ( edgeList[m].vertexId0==i && edgeList[m].vertexId1==j) ||(edgeList[m].vertexId0==i && edgeList[m].vertexId1==j) ){
             return 0;
         } 
     }
     vertexList[j].edgeIds[vertexList[j].edgeCount++]=edgeList.size();
     vertexList[i].edgeIds[vertexList[i].edgeCount++]=edgeList.size();

    GraphEdge connected={weight,0,i,j};
    edgeList.push_back(connected);
    return 1;
}

bool Graph::ShortestPath(std::vector<int>& orderedVertexIdList,
                         const std::string& from,
                         const std::string& to) const
{
    // TODO
}

int Graph::MultipleShortPaths(std::vector<std::vector<int> >& orderedVertexIdList,
                              const std::string& from,
                              const std::string& to,
                              int numberOfShortestPaths)
{
    // TODO
}

void Graph::MaskEdges(const std::vector<StringPair>& vertexNames)
{
    // TODO
    int i=0,j=0,m=0,k=0;
    for(k=0;k<vertexNames.size();k++){
        StringPair pair=vertexNames[k];
        for(i=0;i<vertexList.size();i++){
            if(pair.s0==vertexList[i].name) {
                break;
            }
        }
        for(j=0;j<vertexList.size();j++){
            if(pair.s1==vertexList[j].name){
                break;
            } 
        }
        
        if(i>=vertexList.size() || j>=vertexList.size()) {
            throw VertexNotFoundException();
        }
        
        for(m=0;m<edgeList.size();m++){
            if( ( edgeList[m].vertexId0==i && edgeList[m].vertexId1==j) ||(edgeList[m].vertexId0==j && edgeList[m].vertexId1==i) ) edgeList[m].masked=1;
        }
    }
}

void Graph::UnMaskEdges(const std::vector<StringPair>& vertexNames)
{
    // TODO
   int i=0,j=0,m=0,k=0;
    for(k=0;k<vertexNames.size();k++){
        StringPair pair=vertexNames[k];
        for(i=0;i<vertexList.size();i++){
            if(pair.s0==vertexList[i].name) {
                break;
            }
        }
        for(j=0;j<vertexList.size();j++){
            if(pair.s1==vertexList[j].name){
                break;
            } 
        }
        
        if(i>=vertexList.size() || j>=vertexList.size()) {
            throw VertexNotFoundException();
        }
        
        for(m=0;m<edgeList.size();m++){
            if( ( edgeList[m].vertexId0==i && edgeList[m].vertexId1==j) ||(edgeList[m].vertexId0==j && edgeList[m].vertexId1==i) ) edgeList[m].masked=0;
        }
    }
}

void Graph::UnMaskAllEdges()
{
    // TODO
    int i=0;
    for(;i<edgeList.size();i++){
        edgeList[i].masked=0;
    }
}

void Graph::MaskVertexEdges(const std::string& name)
{
    // TODO
  int i,j;
    for(i=0;i<vertexList.size();i++){
        if(vertexList[i].name==name) {
            break;
        }
    }
    if(i==vertexList.size()){
        throw VertexNotFoundException();
    } 
    for(j=0;j<edgeList.size();j++){
        if(edgeList[j].vertexId1==i || edgeList[j].vertexId0==i){
            edgeList[j].masked=1;
        } 
    }
}

void Graph::UnMaskVertexEdges(const std::string& name)
{
    // TODO
    int i,j;
    for(i=0;i<vertexList.size();i++){
        if(vertexList[i].name==name) {
            break;
        }
    }
    if(i==vertexList.size()){
        throw VertexNotFoundException();
    } 
    for(j=0;j<edgeList.size();j++){
        if(edgeList[j].vertexId1==i || edgeList[j].vertexId0==i){
            edgeList[j].masked=0;
        } 
    }
}

void Graph::ModifyEdge(const std::string& vName0,
                       const std::string& vName1,
                       float newWeight)
{
    // TODO
    int i,j,m;
    for(i=0;i<vertexList.size();i++){
        if(vertexList[i].name==vName0){
             break;
        }  
    }
     for(j=0;j<vertexList.size();j++){
        if(vertexList[j].name==vName1){
            break;
        }  
     }
     
     if(i>=vertexList.size() ||  j>=vertexList.size()){
         throw VertexNotFoundException();
     } 
     
     for(m=0;m<edgeList.size();m++){
            if( ( edgeList[m].vertexId0==i && edgeList[m].vertexId1==j) ||(edgeList[m].vertexId0==j && edgeList[m].vertexId1==i) ){
                edgeList[m].weight=int(newWeight);
            } 
        }
}

void Graph::ModifyEdge(int vId0, int vId1,
                       float newWeight)
{
    // TODO
     int m;
     for(m=0;m<edgeList.size();m++){
            if( ( edgeList[m].vertexId0==vId0 && edgeList[m].vertexId1==vId1) || (edgeList[m].vertexId0==vId1 && edgeList[m].vertexId1==vId0) ){
                edgeList[m].weight=int(newWeight);
            }
     } 
        
}

void Graph::PrintAll() const
{
    // ============================= //
    // This function is implemented  //
    // Do not edit this function !   //
    // ============================= //
    for(size_t i = 0; i < vertexList.size(); i++)
    {
        const GraphVertex& v = vertexList[i];
        std::cout << v.name << "\n";
        for(int j = 0; j < v.edgeCount; j++)
        {
            int edgeId = v.edgeIds[j];
            const GraphEdge& edge = edgeList[edgeId];
            // Skip printing this edge if it is masked
            if(edge.masked)
                continue;

            // List the all vertex names and weight
            std::cout << "-" << std::setfill('-')
                             << std::setw(2) << edge.weight
                             << "-> ";
            int neigVertexId = (static_cast<int>(i) == edge.vertexId0)
                                 ? edge.vertexId1
                                 : edge.vertexId0;
            std::cout << vertexList[neigVertexId].name << "\n";
        }
    }
    // Reset fill value because it "sticks" to the std out
    std::cout << std::setfill(' ');
    std::cout.flush();
}

void Graph::PrintPath(const std::vector<int>& orderedVertexIdList,
                      bool sameLine) const
{
    // ============================= //
    // This function is implemented  //
    // Do not edit this file !       //
    // ============================= //
    for(size_t i = 0; i < orderedVertexIdList.size(); i++)
    {
        int vertexId = orderedVertexIdList[i];
        if(vertexId >= static_cast<int>(vertexList.size()))
            throw VertexNotFoundException();

        const GraphVertex& vertex = vertexList[vertexId];
        std::cout << vertex.name;
        if(!sameLine) std::cout << "\n";
        // Only find and print the weight if next is available
        if(i == orderedVertexIdList.size() - 1) break;
        int nextVertexId = orderedVertexIdList[i + 1];
        if(nextVertexId >= static_cast<int>(vertexList.size()))
            throw VertexNotFoundException();

        // Find the edge between these two vertices
        int edgeId = INVALID_INDEX;
        if(vertexId     < static_cast<int>(vertexList.size()) &&
           nextVertexId < static_cast<int>(vertexList.size()))
        {
            // Check all of the edges of vertex
            // and try to find
            const GraphVertex& fromVert = vertexList[vertexId];
            for(int i = 0; i < fromVert.edgeCount; i++)
            {
                int eId = fromVert.edgeIds[i];
                // Since the graph is not directional
                // check the both ends
                if((edgeList[eId].vertexId0 == vertexId &&
                    edgeList[eId].vertexId1 == nextVertexId)
                ||
                   (edgeList[eId].vertexId0 == nextVertexId &&
                    edgeList[eId].vertexId1 == vertexId))
                {
                    edgeId = eId;
                    break;
                }
            }
        }
        if(edgeId != INVALID_INDEX)
        {
            const GraphEdge& edge = edgeList[edgeId];
            std::cout << "-" << std::setfill('-')
                      << std::setw(2)
                      << edge.weight << "->";
        }
        else
        {
            std::cout << "-##-> ";
        }
    }
    // Print endline on the last vertex if same line is set
    if(sameLine) std::cout << "\n";
    // Reset fill value because it "sticks" to the std out
    std::cout << std::setfill(' ');
    std::cout.flush();
}

int Graph::TotalVertexCount() const
{
    // TODO
    return vertexList.size();
}

int Graph::TotalEdgeCount() const
{
    // TODO
    return edgeList.size();
}

std::string Graph::VertexName(int vertexId) const
{
    // TODO
    if(vertexId>vertexList.size()) return "";
    return vertexList[vertexId].name;
}

int Graph::TotalWeightInBetween(std::vector<int>& orderedVertexIdList)
{
    // TODO
int i=0,j=0,total=0;
for(;i<orderedVertexIdList.size();i++){
    if(orderedVertexIdList[i]>=vertexList.size()){
        throw VertexNotFoundException();
    }
}
for(i=0;i<edgeList.size();i++){
    for(j=0;j<orderedVertexIdList.size();j++){
        if((edgeList[i].vertexId0==orderedVertexIdList[j] && edgeList[i].vertexId1==orderedVertexIdList[j+1]) || (edgeList[i].vertexId1==orderedVertexIdList[j] && edgeList[i].vertexId0==orderedVertexIdList[j+1]) ){
            total+=edgeList[i].weight;
        }
    }
    if(total==0) {
        return -1;
        }
    else{
        return total; 
        }
    }

}