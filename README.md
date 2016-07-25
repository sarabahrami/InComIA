# InComIA
InComIA: An approch for Impact analysis of change reuqest on source code.
The purpose of this project is to find the relevant source code files to an incoming change request.
There are two main inputs: Source code repository and bug description. 
When a new bug is submitted to the bug tracking repository, InComIA takes that bug as input and then update the source code repository to the revision which bug was reported (by using SvnCheckout.java) then InComIA converts the source code files to XML files (by using SrcML.java)  and then creates a corpus for each unique source code file inside the repository (by using XmlParser.java). The output of this step is a corpus of all the keywords for each unique file inside the repository.
A machine learning technique is used (K- NN and LSI)  to find the textual similarity between bug description and created corpus (by using PythonRun.java and machinelearningwithlsi.py).
InComIA recommends top K relevant source code files based on their textual similarity score to bug description.

Requirments:
Java Lucene, Srcml, Python, and Gensim 
