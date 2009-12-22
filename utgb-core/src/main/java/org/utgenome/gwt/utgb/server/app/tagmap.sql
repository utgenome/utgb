select * from tagmap
where scaffold = '$1'
and start between $2 and $3
