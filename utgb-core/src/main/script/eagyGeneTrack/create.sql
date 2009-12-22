create table gene(
  id integer primary key not null autoincrement,
  target string not null,
  begin integer not null,
  end integer not null,
  name string,
  url string,
  strand string,
);


create table exon(
  id integer primary key not null autoincrement,
  gene_id integer not null,
  begin integer not null,
  end integer not null
);


create table track(
  id integer primary key not null autoincrement,
  species string,
  revision string,
  name string not null,
  comment string,
  description_url string,
  color string,
  species_url string,
  created_date string,
);

