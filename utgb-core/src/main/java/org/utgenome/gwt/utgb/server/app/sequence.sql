select start, end, sequence 
from (select * from description where description= '$1') as description
join sequence on sequence.description_id = description.id
where start between $2 and $3 
and end > $4 
order by start
