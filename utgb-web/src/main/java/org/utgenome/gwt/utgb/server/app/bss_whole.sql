select rowid, target, targetStart as start, targetEnd as end, strand
from alignment  
where queryID = '$1'  