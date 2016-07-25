from gensim import corpora, models, similarities
import os
import sys
path2=sys.argv[5]
resultfilename=sys.argv[2]
Corpusfilename=sys.argv[3]
Corpuswithoutidfilename=sys.argv[4]
filewithid=open (os.path.join(path2,Corpusfilename),'r')
print "------------------startpython--------------------------------"
if(not(os.path.isfile(os.path.join(path2,"corpuslsi.mm")))):
    filewithoutid=open (os.path.join(path2,Corpuswithoutidfilename),'w')
    for line in filewithid:
        s=line.split("\t")
        i=1
        while(i<=len(s)-1):
            filewithoutid.write(s[i])
            filewithoutid.write(" ")
            i=i+1
    filewithid.close()
    filewithoutid.close()
    #creating dictionary from all of bug description
    texts=[[word for word in line.lower().split()]for line in open(os.path.join(path2,Corpuswithoutidfilename))]
    dictionary=corpora.Dictionary(texts)
    dictionary.save(os.path.join(path2,"dictionary.dict"))
    #creating corpus from all of the word in the dictionary
    corpus =[dictionary.doc2bow(text) for text in texts]
    #creating term frequency matrix for dictionary
    tfidf=models.TfidfModel(corpus)
    tfidf.save(os.path.join(path2,"model.tfidf"))
    corpus_tfidf=tfidf[corpus]
    lsi=models.LsiModel(corpus_tfidf,id2word=dictionary,num_topics=300)
    lsi.save(os.path.join(path2,"model.lsi"))
    corpus_lsi=lsi[corpus_tfidf]
    corpora.MmCorpus.serialize(os.path.join(path2,"corpuslsi.mm"),corpus_lsi)
    index = similarities.docsim.MatrixSimilarity(corpus_lsi,chunksize=500)
    index.save(os.path.join(path2,"modelindex.index"))
    if not os.path.exists(os.path.join(path2,"output")):
        os.makedirs(os.path.join(path2,"output"))
else:
    dictionary = corpora.Dictionary.load(os.path.join(path2,"dictionary.dict"))
    tfidf=models.TfidfModel.load(os.path.join(path2,"model.tfidf"))
    lsi = models.LsiModel.load(os.path.join(path2,"model.lsi"))
    corpus_lsi = corpora.MmCorpus(os.path.join(path2,"corpuslsi.mm"))
    index = similarities.MatrixSimilarity.load(os.path.join(path2,"modelindex.index"))
testdesc=sys.argv[1]
cr=dictionary.doc2bow(testdesc.lower().split())
vec_lsi=lsi[tfidf[cr]]
sim= index[vec_lsi]
list= sorted(enumerate(sim), key=lambda item: -item[1])
max=10
top=list[ :max]
file1= open (os.path.join(path2,Corpusfilename),'r')
lines=file1.readlines()
listoffile=[[0 for x in xrange(1)]for x in xrange(max)]
i=0
for x in top:
    d=x[0]
    f=lines[d]
    filename=f.split("\t",2)[0]
    desc=f.split()
    listoffile[i]=filename
    i=i+1
file1.close()
file3= open (os.path.join(path2,"output",resultfilename),'w')
            
for item in listoffile:
    file3.write(item)
    file3.write("\n")
file3.close()
print"---------------------------------------------finishpython-----------------------------------------------"
