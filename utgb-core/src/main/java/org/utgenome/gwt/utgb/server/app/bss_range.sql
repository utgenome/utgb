select rowid, queryID as name, target,
targetStart as start, targetEnd as end, strand,
queryLength, similarity, queryCoverage, evalue, bitScore, 
targetSequence, querySequence, alignment
from alignment  
where name = '$1'  